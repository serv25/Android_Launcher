package com.example.serega.mylauncher;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<AppInfo> apps;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton btnCall;
    private Button btnApps;
    private ImageButton btnSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(MainActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);

        apps = allApps();
        adapter = new MyRecyclerAdapter(apps,this);
        recyclerView.setAdapter(adapter);

        btnCall = (ImageButton) findViewById(R.id.btnCall);
        btnApps = (Button) findViewById(R.id.btnApps);
        btnSMS = (ImageButton) findViewById(R.id.btnSMS);

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
        apps = allApps();
        adapter = new MyRecyclerAdapter(apps,MainActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<AppInfo> allApps() {
        ArrayList<AppInfo> tempList = AppsActivity.getApps();
        ArrayList<AppInfo> desktopList = new ArrayList<>();
        for (AppInfo app: tempList) {
            if(app.isOnDesktop())desktopList.add(app);
        }
        return desktopList;
    }
}
