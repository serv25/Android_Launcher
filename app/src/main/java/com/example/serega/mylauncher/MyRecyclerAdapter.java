package com.example.serega.mylauncher;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private static ArrayList<AppInfo> apps;
    private Context context;

    public static ArrayList<AppInfo> getApps() {
        return apps;
    }

    public MyRecyclerAdapter(ArrayList<AppInfo> apps, Context context) {
        this.apps = apps;
        this.context = context;
    }

    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyRecyclerAdapter.ViewHolder holder, int position) {
        holder.appName.setText(apps.get(position).getLabel());
        holder.appIcon.setImageDrawable(apps.get(position).getIcon());
        if (context instanceof MainActivity) holder.appCheckBox.setVisibility(View.GONE);
        else holder.appCheckBox.setChecked(apps.get(position).isOnDesktop());
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView appName;
        public ImageView appIcon;
        public CheckBox appCheckBox;
        private PopupWindow popupWindow;

        public ViewHolder(View v) {
            super(v);
            appName = (TextView) v.findViewById(R.id.app_view);
            appIcon = (ImageView) v.findViewById(R.id.app_icon);
            appCheckBox = (CheckBox) v.findViewById(R.id.on_desktop);
            appCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    int count = MainActivity.getCurrentAmountOfApps();
                    if (appCheckBox.isChecked()) {
                        if (count < MainActivity.getMaxAmountOfApps()) {
                            getApps().get(position).setOnDesktop(true);
                            count++;
                            MainActivity.setCurrentAmountOfApps(count);
                        } else {
                            appCheckBox.setChecked(false);
                            Toast.makeText(view.getContext(), "There is no space on desktop!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        getApps().get(position).setOnDesktop(false);
                        count--;
                        MainActivity.setCurrentAmountOfApps(count);
                    }
                }
            });
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String name = getApps().get(position).getName().toString();
                    PackageManager manager = v.getContext().getPackageManager();
                    try {
                        Intent intent = manager.getLaunchIntentForPackage(name);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        v.getContext().startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    View inflatedView = view.inflate(view.getContext(), R.layout.popup_window, null);
                    popupWindow = new PopupWindow(
                            inflatedView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.showAsDropDown(view, 0, 0);
                    TextView uninstall = (TextView) inflatedView.findViewById(R.id.uninstall);
                    TextView aboutApp = (TextView) inflatedView.findViewById(R.id.about_app);
                    aboutApp.setOnClickListener(onClickListener);
                    uninstall.setOnClickListener(onClickListener);
                    return true;
                }
            });
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                String name = getApps().get(position).getName().toString();
                switch (view.getId()) {
                    case R.id.uninstall:
                        Intent intent = new Intent(Intent.ACTION_DELETE);
                        intent.setData(Uri.parse("package:" + name));
                        try {
                            view.getContext().startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.about_app:
                        Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", name, null);
                        i.setData(uri);
                        try {
                            view.getContext().startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                popupWindow.dismiss();
            }
        };
    }
}
