package com.example.serega.mylauncher;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class AppsActivity extends Activity {

    private PackageManager manager;
    private ArrayList<AppInfo> apps;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button btnList;
    private Button btnGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_activity);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(AppsActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);

        apps = allApps();
        adapter = new MyRecyclerAdapter(apps);
        recyclerView.setAdapter(adapter);

        btnList = (Button) findViewById(R.id.btn_list);
        btnGrid = (Button) findViewById(R.id.btn_grid);
        btnList.setOnClickListener(onClickListener);
        btnGrid.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_list:
                    layoutManager = new LinearLayoutManager(AppsActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    break;
                case R.id.btn_grid:
                    layoutManager = new GridLayoutManager(AppsActivity.this, 3);
                    recyclerView.setLayoutManager(layoutManager);
                    break;
            }
        }
    };

    private ArrayList<AppInfo> allApps() {
        manager = getPackageManager();
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
        return allApps;
    }
}
