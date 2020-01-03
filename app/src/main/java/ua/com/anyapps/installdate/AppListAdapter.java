package ua.com.anyapps.installdate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    List<OneAppInfo> objects;
    private static final String TAG = "debapp";

    AppListAdapter(Context context, List<OneAppInfo> apps) {
        this.context = context;
        this.objects = apps;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(Context context, List<OneAppInfo> apps){
        this.context = context;
        this.objects = apps;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // используем созданные, но не используемые view
        View v = view;
        if (v == null) {
            v = lInflater.inflate(R.layout.app_list_item, viewGroup, false);
        }

        OneAppInfo aInfo = getProduct(i);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        if(aInfo.applicationLabel==null){
            Log.d(TAG, "++++++++++++++++++++++");
        }//Log.d(TAG, "AD: " + aInfo.applicationLabel);

        //TextView tvAppLabel = v.findViewById(R.id.tvAppLabel);
        //tvAppLabel.setText(aInfo.applicationLabel);
        ((TextView) v.findViewById(R.id.tvAppLabel)).setText((i+1)+". "+aInfo.applicationLabel);
        ((TextView) v.findViewById(R.id.tvAppName)).setText(aInfo.appName);
        ((ImageView) v.findViewById(R.id.ivAppIcon)).setImageDrawable(aInfo.appIcon);

        ((TextView) v.findViewById(R.id.tvInstallDateText)).setText(context.getResources().getString(R.string.install_date_text));
        ((TextView) v.findViewById(R.id.tvLastUpdateDateText)).setText(context.getResources().getString(R.string.last_update_date_text));
        ((TextView) v.findViewById(R.id.tvAppNameText)).setText(context.getResources().getString(R.string.app_name_text));

        ((TextView) v.findViewById(R.id.tvInstallDate)).setText(String.valueOf(Functions.convertTime(aInfo.installDate)));
        ((TextView) v.findViewById(R.id.tvLastUpdateDate)).setText(String.valueOf(Functions.convertTime(aInfo.lastUpdateDate)));
        ((TextView) v.findViewById(R.id.tvAppName)).setText(aInfo.appName);

        //((TextView) view.findViewById(R.id.tvPrice)).setText(p.price + "");
        //((ImageView) view.findViewById(R.id.ivImage)).setImageResource(p.image);

        //CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
        // присваиваем чекбоксу обработчик
        //cbBuy.setOnCheckedChangeListener(myCheckChangeList);
        // пишем позицию
        //cbBuy.setTag(position);
        // заполняем данными из товаров: в корзине или нет
        //cbBuy.setChecked(p.box);
        return v;
    }

    OneAppInfo getProduct(int position) {
        return ((OneAppInfo) getItem(position));
    }
}

