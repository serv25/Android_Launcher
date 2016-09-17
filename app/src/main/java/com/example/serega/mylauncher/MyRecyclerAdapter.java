package com.example.serega.mylauncher;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView appView;
        public ImageView appIcon;

        public ViewHolder(View v) {
            super(v);
            appView = (TextView) v.findViewById(R.id.app_veiw);
            appIcon = (ImageView)v.findViewById(R.id.app_icon);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String label = getApps().get(position).getName().toString();
                    PackageManager manager = v.getContext().getPackageManager();
                    try {
                        Intent intent = manager.getLaunchIntentForPackage(label);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        v.getContext().startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    System.out.println("LOOOOOOOOOOOOOOOOOOOOOOONG=" + position);
                    return true;
                }
            });
        }
    }

    private static ArrayList<AppInfo> apps;

    public static ArrayList<AppInfo> getApps() {
        return apps;
    }

    public MyRecyclerAdapter(ArrayList<AppInfo> apps) {
        this.apps = apps;
    }

    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyRecyclerAdapter.ViewHolder holder, int position) {
        holder.appView.setText(apps.get(position).getLabel());
        holder.appIcon.setImageDrawable(apps.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }
}
