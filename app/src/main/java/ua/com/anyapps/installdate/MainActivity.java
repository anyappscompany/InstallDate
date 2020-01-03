package ua.com.anyapps.installdate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "debapp";
    List<OneAppInfo> apps = new ArrayList<>();
    List<OneAppInfo> dynamicListApps = new ArrayList<>();
    AppListAdapter appListAdapter;
    private Context context;
    ListView lvApps;
    SearchView svSearch;
    ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        lvApps = (ListView) findViewById(R.id.lvApps);
        svSearch = (SearchView) findViewById(R.id.svSearch);
        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);

        appListAdapter = new AppListAdapter(context, dynamicListApps);

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                dynamicListApps.clear();

                for (int i = 0; i < apps.size(); i++) {
                    if (Pattern.compile(Pattern.quote(newText), Pattern.CASE_INSENSITIVE).matcher(apps.get(i).applicationLabel).find() || Pattern.compile(Pattern.quote(newText), Pattern.CASE_INSENSITIVE).matcher(apps.get(i).appName).find()) {
                        dynamicListApps.add(apps.get(i));
                    }
                }


                appListAdapter.setList(context, dynamicListApps);
                appListAdapter.notifyDataSetChanged();

                return false;
            }
        });

        showLoader();
    }

    @Override
    protected void onResume() {
        super.onResume();

        showLoader();

        Runnable runnable = new Runnable() {
            public void run() {

                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                PackageManager pm = getPackageManager();
                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

                for (ApplicationInfo packageInfo : packages) {
                    long installed = 0;
                    long lastModified = 0;
                    String applicationLabel = "(unknown)";
                    String packName = "";
                    Drawable ico = null;
                    String appFile;
                    try {
                        installed = pm.getPackageInfo(packageInfo.packageName, 0).firstInstallTime;
                        ico = getPackageManager().getApplicationIcon(packageInfo.packageName);

                        ApplicationInfo appInfo;

                        try{
                            appInfo = pm.getApplicationInfo(packageInfo.packageName, 0);
                            if(appInfo!=null){
                                appFile = appInfo.sourceDir;
                                lastModified = new File(appFile).lastModified();
                                applicationLabel = String.valueOf(pm.getApplicationLabel(appInfo));
                            }
                        }catch (Exception ex){
                            Log.d(TAG, ex.getMessage());
                        }

                        apps.add(new OneAppInfo(packageInfo.packageName, applicationLabel, installed, lastModified, ico));

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                Collections.sort(apps, new Comparator() {
                    @Override
                    public int compare(Object softDrinkOne, Object softDrinkTwo) {
                        //comparision for primitive int uses compareTo of the wrapper Integer
                        return(new String(((OneAppInfo)softDrinkOne).applicationLabel))
                                .compareTo(((OneAppInfo)softDrinkTwo).applicationLabel);
                    }
                });

                if(svSearch.getQuery().toString().length()>0) {
                    dynamicListApps.clear();
                    for (int i = 0; i < apps.size(); i++) {
                        if (Pattern.compile(Pattern.quote(svSearch.getQuery().toString()), Pattern.CASE_INSENSITIVE).matcher(apps.get(i).applicationLabel).find() || Pattern.compile(Pattern.quote(svSearch.getQuery().toString()), Pattern.CASE_INSENSITIVE).matcher(apps.get(i).appName).find()) {
                            dynamicListApps.add(apps.get(i));
                        }
                    }
                    appListAdapter.setList(context, dynamicListApps);
                }else{
                    appListAdapter.setList(context, apps);
                }



                //appListAdapter = new AppListAdapter(context, apps);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lvApps.setAdapter(appListAdapter);
                        showList();
                    }
                });
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void showLoader(){
        pbLoading.setVisibility(View.VISIBLE);
        lvApps.setVisibility(View.GONE);
    }

    private void showList(){
        pbLoading.setVisibility(View.GONE);
        lvApps.setVisibility(View.VISIBLE);
    }
}

