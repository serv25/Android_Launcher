package com.example.serega.mylauncher;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class AppsActivity extends Activity {

    private PackageManager manager;
    private static ArrayList<AppInfo> apps = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button btnList;
    private Button btnGrid;
    private EditText search;

    public static ArrayList<AppInfo> getApps() {
        return apps;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_activity);

        search = (EditText) findViewById(R.id.search);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(AppsActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);

        apps = getAllApps();
        adapter = new MyRecyclerAdapter(apps, this);
        recyclerView.setAdapter(adapter);

        btnList = (Button) findViewById(R.id.btn_list);
        btnGrid = (Button) findViewById(R.id.btn_grid);
        btnList.setOnClickListener(onClickListener);
        btnGrid.setOnClickListener(onClickListener);

        addTextListener();
    }

    public void addTextListener() {
        search.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                query = query.toString().toLowerCase();
                final ArrayList<AppInfo> filteredList = new ArrayList<>();

                for (int i = 0; i < apps.size(); i++) {
                    final String text = apps.get(i).getLabel().toString().toLowerCase();
                    if (text.contains(query)) filteredList.add(apps.get(i));
                }

                adapter = new MyRecyclerAdapter(filteredList, AppsActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        apps = getAllApps();
        adapter = new MyRecyclerAdapter(apps, AppsActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<AppInfo> getAllApps() {
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

        for (int i = 0; i < apps.size(); i++) {
            String name = apps.get(i).getLabel().toString();
            for (int k = 0; k < allApps.size(); k++) {
                if (name.equals(allApps.get(k).getLabel().toString())) {
                    allApps.get(k).setOnDesktop(apps.get(i).isOnDesktop());
                    break;
                }
            }
        }
        return allApps;
    }

}
