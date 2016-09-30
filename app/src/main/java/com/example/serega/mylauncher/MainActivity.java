package com.example.serega.mylauncher;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<AppInfo> desktopApps;
    private GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid = (GridLayout) findViewById(R.id.grid_layout);
        ImageButton btnCall = (ImageButton) findViewById(R.id.btnCall);
        ImageButton btnApps = (ImageButton) findViewById(R.id.btnApps);
        ImageButton btnSMS = (ImageButton) findViewById(R.id.btnSMS);

        desktopApps = getDesktopApps();
        initializeAllAppsList();
        showDesktopApps();

        btnCall.setOnClickListener(onClickListener);
        btnApps.setOnClickListener(onClickListener);
        btnSMS.setOnClickListener(onClickListener);
    }

    private void showDesktopApps() {
        grid.removeAllViews();
        for (AppInfo app : desktopApps) {
            ImageView cell = new ImageView(this);
            cell.setImageDrawable(app.getIcon());
            grid.addView(cell);
        }
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
        desktopApps = getDesktopApps();
        showDesktopApps();
    }

    private ArrayList<AppInfo> getDesktopApps() {
        DesktopSettingManager dsm = new DesktopSettingManager(MainActivity.this);
        return dsm.getDesktopApps();
    }

    private void initializeAllAppsList() {
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
            for (AppInfo desktopApp : desktopApps) {
                if (desktopApp.getName().equals(app.getName()) && desktopApp.getLabel().equals(app.getLabel())) {
                    app.setOnDesktop(true);
                }
            }
        }
        AppsActivity.setApps(allApps);
    }
}
