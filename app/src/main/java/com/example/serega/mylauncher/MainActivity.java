package com.example.serega.mylauncher;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnCall = (ImageButton) findViewById(R.id.btnCall);
        Button btnApps = (Button) findViewById(R.id.btnApps);
        ImageButton btnSMS = (ImageButton) findViewById(R.id.btnSMS);

        btnCall.setOnClickListener(onClickListener);
        btnApps.setOnClickListener(onClickListener);
        btnSMS.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCall:
                    Intent intentCall = new Intent(Intent.ACTION_DIAL);
                    startActivity(intentCall);
                    break;
                case R.id.btnApps:
                    Intent intentApps = new Intent(MainActivity.this, AppsActivity.class);
                    startActivity(intentApps);
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
}
