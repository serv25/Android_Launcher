package com.example.serega.mylauncher;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<AppInfo> apps;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private static int currentAmountOfApps = 0;
    private static final int MAX_AMOUNT_OF_APPS = 12;

    public static int getCurrentAmountOfApps() {
        return currentAmountOfApps;
    }

    public static void setCurrentAmountOfApps(int currentAmountOfApps) {
        MainActivity.currentAmountOfApps = currentAmountOfApps;
    }

    public static int getMaxAmountOfApps() {
        return MAX_AMOUNT_OF_APPS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        ImageButton btnCall = (ImageButton) findViewById(R.id.btnCall);
        ImageButton btnApps = (ImageButton) findViewById(R.id.btnApps);
        ImageButton btnSMS = (ImageButton) findViewById(R.id.btnSMS);

        initializeAppsList();
        apps = getDesktopApps();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecyclerAdapter(apps, this);
        recyclerView.setAdapter(adapter);

        btnCall.setOnClickListener(onClickListener);
        btnApps.setOnClickListener(onClickListener);
        btnSMS.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCall:
                    startActivity(new Intent(Intent.ACTION_DIAL));
                    break;
                case R.id.btnApps:
                    startActivity(new Intent(MainActivity.this, AppsActivity.class));
                    break;
                case R.id.btnSMS:
                    Intent intentSMS = new Intent(Intent.ACTION_VIEW);
                    intentSMS.setType("vnd.android-dir/mms-sms");
                    startActivity(intentSMS);
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        apps = getDesktopApps();
        adapter = new MyRecyclerAdapter(apps, MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<AppInfo> getDesktopApps() {
        ArrayList<AppInfo> tempList = AppsActivity.getApps();
        ArrayList<AppInfo> desktopList = new ArrayList<>();
        for (AppInfo app : tempList) {
            if (app.isOnDesktop()) desktopList.add(app);
            if (tempList.size() == MAX_AMOUNT_OF_APPS) break;
        }
        return desktopList;
    }

    private void initializeAppsList() {
        PackageManager manager = getPackageManager();
        ArrayList<AppInfo> allApps = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableApps = manager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : availableApps) {
            AppInfo app = new AppInfo(
                    resolveInfo.loadLabel(manager),
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.activityInfo.loadIcon(manager));
            allApps.add(app);
        }
        AppsActivity.setApps(allApps);
    }
}
