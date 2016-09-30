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

    private static ArrayList<AppInfo> allApps;
    private static Context context;

    public MyRecyclerAdapter(ArrayList<AppInfo> allApps, Context context) {
        MyRecyclerAdapter.allApps = allApps;
        MyRecyclerAdapter.context = context;
    }

    public static ArrayList<AppInfo> getAllApps() {
        return allApps;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyRecyclerAdapter.ViewHolder holder, int position) {
        holder.appName.setText(allApps.get(position).getLabel());
        holder.appIcon.setImageDrawable(allApps.get(position).getIcon());

        if (context instanceof MainActivity) {
            holder.appCheckBox.setVisibility(View.GONE);
            holder.appIcon.setPadding(0, 20, 0, 0);
        } else {
            holder.appCheckBox.setChecked(allApps.get(position).isOnDesktop());
        }
    }

    @Override
    public int getItemCount() {
        return allApps.size();
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

            v.setOnClickListener(onItemClick);
            v.setOnLongClickListener(onItemLongClick);
            appCheckBox.setOnClickListener(onCheckBoxClick);
        }

        private View.OnClickListener onItemClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchApp(view);
            }
        };

        private View.OnLongClickListener onItemLongClick = new View.OnLongClickListener() {
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
                aboutApp.setOnClickListener(onItemSubMenuClick);
                uninstall.setOnClickListener(onItemSubMenuClick);
                return true;
            }
        };

        private View.OnClickListener onItemSubMenuClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                String name = getAllApps().get(position).getName().toString();
                switch (view.getId()) {
                    case R.id.uninstall:
                        uninstallApp(view, position, name);
                        break;
                    case R.id.about_app:
                        aboutApp(view, name);
                        break;
                }
                popupWindow.dismiss();
            }
        };

        private View.OnClickListener onCheckBoxClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                DesktopSettingManager dsm = new DesktopSettingManager(getContext());

                if (appCheckBox.isChecked()) {
                    if (dsm.isSaved(getAllApps().get(position))) {
                        getAllApps().get(position).setOnDesktop(true);
                    } else {
                        appCheckBox.setChecked(false);
                        Toast.makeText(view.getContext(), "There is no space on desktop!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if(dsm.isRemoved(getAllApps().get(position))){
                        getAllApps().get(position).setOnDesktop(false);
                    }else{
                        getAllApps().get(position).setOnDesktop(true);
                        Toast.makeText(view.getContext(), "Error!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        private void launchApp(View view) {
            int position = getAdapterPosition();
            String name = getAllApps().get(position).getName().toString();
            PackageManager manager = view.getContext().getPackageManager();
            try {
                Intent intent = manager.getLaunchIntentForPackage(name);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                view.getContext().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void uninstallApp(View view, int position, String name) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + name));
            try {
                if (getAllApps().get(position).isOnDesktop()) {
                    ////////////////////
//                    int count = MainActivity.getCurrentAmountOfApps() - 1;
//                    MainActivity.setCurrentAmountOfApps(count);
                    ///////////////////////
                }
                view.getContext().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void aboutApp(View view, String name) {
            Intent i = new Intent();
            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", name, null);
            i.setData(uri);
            try {
                view.getContext().startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
