package com.mailerdaemon.app.Clubs;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mailerdaemon.app.R;

import java.util.ArrayList;
import java.util.List;

import Utils.DialogOptions;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.Holder> {
    private List<ClubIconModel> iconModel=new ArrayList<>();
    private CircularProgressDrawable drawable;
    private DialogOptions options;

    ClubAdapter(Context context, DialogOptions options){
    this.options=options;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_club,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ClubIconModel model= iconModel.get(position);
        //holder.icon.getHierarchy().setProgressBarImage(drawable);
        if (model.getUrl()==null){
            holder.icon.setImageResource(R.drawable.ic_account_circle_black_24dp);
        }else{//holder.icon.setBackground(null);
    holder.icon.setImageURI(Uri.parse(model.getUrl()));}
        holder.icon.setOnClickListener(v-> options.showDialog(model.getTag()));
    }

    @Override
    public int getItemCount() {
        return iconModel.size();
    }

    public void setData(List<ClubIconModel> iconModel) {
        this.iconModel=iconModel;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        SimpleDraweeView icon;

        public Holder(@NonNull View itemView) {
            super(itemView);
            icon=itemView.findViewById(R.id.club_icon);
        }
    }
}
