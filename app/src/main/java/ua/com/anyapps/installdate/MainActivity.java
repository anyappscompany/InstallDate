package ua.com.anyapps.installdate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    View rootView;

    int sorting = 0;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        lvApps = (ListView) findViewById(R.id.lvApps);
        svSearch = (SearchView) findViewById(R.id.svSearch);
        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);
        rootView = findViewById(R.id.layRoot);

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sorting = sharedPreferences.getInt("sorting", 0);


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_name_ascending:
                Log.d(TAG, "action_sort_name_ascending");
                sorting = 1;
                editor.putInt("sorting", 1);
                editor.commit();

                sortingApps(apps);
                appListAdapter.setList(context, apps);
                appListAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_sort_name_descending:
                Log.d(TAG, "action_sort_name_descending");
                sorting = 2;
                editor.putInt("sorting", 2);
                editor.commit();

                sortingApps(apps);
                appListAdapter.setList(context, apps);
                appListAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_sort_app_name_ascending:
                Log.d(TAG, "action_sort_app_name_ascending");
                sorting = 3;
                editor.putInt("sorting", 3);
                editor.commit();

                sortingApps(apps);
                appListAdapter.setList(context, apps);
                appListAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_sort_app_name_descending:
                Log.d(TAG, "action_sort_app_name_descending");
                sorting = 4;
                editor.putInt("sorting", 4);
                editor.commit();

                sortingApps(apps);
                appListAdapter.setList(context, apps);
                appListAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_sort_inst_date_ascending:
                Log.d(TAG, "action_sort_inst_date_ascending");
                sorting = 5;
                editor.putInt("sorting", 5);
                editor.commit();

                sortingApps(apps);
                appListAdapter.setList(context, apps);
                appListAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_sort_inst_date_descending:
                Log.d(TAG, "action_sort_inst_date_descending");
                sorting = 6;
                editor.putInt("sorting", 6);
                editor.commit();

                sortingApps(apps);
                appListAdapter.setList(context, apps);
                appListAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_sort_update_date_ascending:
                Log.d(TAG, "action_sort_update_date_ascending");
                sorting = 7;
                editor.putInt("sorting", 7);
                editor.commit();

                sortingApps(apps);
                appListAdapter.setList(context, apps);
                appListAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_sort_update_date_descending:
                Log.d(TAG, "action_sort_update_date_descending");
                sorting = 8;
                editor.putInt("sorting", 8);
                editor.commit();

                sortingApps(apps);
                appListAdapter.setList(context, apps);
                appListAdapter.notifyDataSetChanged();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
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
                        Log.d(TAG, "Installed "  + installed);
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

                sortingApps(apps);


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

    private void sortingApps(List<OneAppInfo> tApps){
        svSearch.setQuery("", false);
        //svSearch.clearFocus();
        //rootView.requestFocus();
        Collections.sort(tApps, new Comparator() {
            @Override
            public int compare(Object sOne, Object sTwo) {
                //comparision for primitive int uses compareTo of the wrapper Integer
                        /*return(new String(((OneAppInfo)sOne).applicationLabel))
                                .compareTo(((OneAppInfo)sTwo).applicationLabel);*/
                switch(sorting){
                    case 0:
                    case 1:
                        return(new String(((OneAppInfo)sOne).getApplicationLabel()))
                                .compareToIgnoreCase(((OneAppInfo)sTwo).getApplicationLabel());
                    case 2:
                        return(new String(((OneAppInfo)sTwo).getApplicationLabel()))
                                .compareToIgnoreCase(((OneAppInfo)sOne).getApplicationLabel());
                    case 3:
                        return(new String(((OneAppInfo)sOne).getAppName()))
                                .compareToIgnoreCase(((OneAppInfo)sTwo).getAppName());
                    case 4:
                        return(new String(((OneAppInfo)sTwo).getAppName()))
                                .compareToIgnoreCase(((OneAppInfo)sOne).getAppName());
                    case 5:
                        return(new String(String.valueOf(((OneAppInfo)sOne).getInstallDate())))
                                .compareTo(String.valueOf(((OneAppInfo)sTwo).getInstallDate()));
                    case 6:
                        return(new String(String.valueOf(((OneAppInfo)sTwo).getInstallDate())))
                                .compareTo(String.valueOf(((OneAppInfo)sOne).getInstallDate()));
                    case 7:
                        return(new String(String.valueOf(((OneAppInfo)sOne).getLastUpdateDate())))
                                .compareTo(String.valueOf(((OneAppInfo)sTwo).getLastUpdateDate()));
                    case 8:
                        return(new String(String.valueOf(((OneAppInfo)sTwo).getLastUpdateDate())))
                                .compareTo(String.valueOf(((OneAppInfo)sOne).getLastUpdateDate()));
                    default:
                        return(new String(((OneAppInfo)sOne).applicationLabel))
                                .compareToIgnoreCase(((OneAppInfo)sTwo).applicationLabel);
                }
            }
        });
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


