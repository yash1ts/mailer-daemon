package com.mailerdaemon.app.Placement;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mailerdaemon.app.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlacementRecyclerViewAdapter extends RecyclerView.Adapter<PlacementRecyclerViewAdapter.Holder> {
    private List<DocumentSnapshot> models=new ArrayList<>();
    private Context context;

    PlacementRecyclerViewAdapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_placement,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        PlacementModel model=models.get(i).toObject(PlacementModel.class);
        DateFormat dateFormat=new SimpleDateFormat("hh:mm aaa  dd.MM.yy", Locale.ENGLISH);
        assert model != null;
        holder.detail.setText(model.getData());
        holder.date_time.setText(dateFormat.format(model.getDate()));
        String path=models.get(i).getReference().getPath();
        holder.card.setOnLongClickListener(v -> {
            Bundle bundle=new Bundle();
            bundle.putString("path", path);
            bundle.putString("data",model.getData());
            PlacementOptionsFragment optionsFragment=new PlacementOptionsFragment();
            optionsFragment.setArguments(bundle);
            AppCompatActivity manager=(AppCompatActivity) context;
            optionsFragment.show(manager.getSupportFragmentManager(), null);
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public List<DocumentSnapshot> getData(){
        return models;
    }

    public void setData(List<DocumentSnapshot> noticeModels) {
        this.models=noticeModels;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.data)
        TextView detail;
        @BindView(R.id.date)
        TextView date_time;
        @BindView(R.id.placement_card)
        MaterialCardView card;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
