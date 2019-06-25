package com.mailerdaemon.app.Notices;

import android.content.Context;

import android.net.Uri;

import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.mailerdaemon.app.R;

import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<NoticeRecyclerViewAdapter.Holder> {
  private List<DocumentSnapshot> noticeModels=new ArrayList<>();
  private Context context;
  private FragmentManager fragment;

  public NoticeRecyclerViewAdapter(Context context, FragmentManager fragment){
    this.context=context;
    this.fragment=fragment;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view=LayoutInflater.from(context).inflate(R.layout.rv_notices,viewGroup,false);
    return new Holder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int i) {
    NoticeModel model=noticeModels.get(i).toObject(NoticeModel.class);
    holder.heading.setText(model.getHeading());
    holder.detail.setText(model.getDetails());
    String s=model.getPhoto();
    holder.options.setOnClickListener(v -> getBottomSheet(noticeModels.get(i).getId()));
    if(s!=null)
    { holder.imageView.setImageURI(Uri.parse(s));
      holder.date_time.setText(model.getDate());
      holder.imageView.setOnClickListener(v -> openImage(s));
    }else {
      holder.imageView.setVisibility(View.GONE);
      holder.date_time.setVisibility(View.GONE);
      holder.time2.setVisibility(View.VISIBLE);
      holder.time2.setText(model.getDate());
    }
  }

  @Override
  public int getItemCount() {
    return noticeModels.size();
  }

  public void setData(List<DocumentSnapshot> noticeModels) {
    this.noticeModels=noticeModels;
  }

  public class Holder extends RecyclerView.ViewHolder {
    TextView heading;
    TextView detail;
    SimpleDraweeView imageView;
    TextView date_time;
    TextView time2;
    View options;

    public Holder(@NonNull View itemView) {
      super(itemView);
      options=itemView.findViewById(R.id.notice_options);
      time2=itemView.findViewById(R.id.time2);
      heading =itemView.findViewById(R.id.notice_head);
      detail=itemView.findViewById(R.id.notice_detail);
      imageView=itemView.findViewById(R.id.notice_photo);
      date_time=itemView.findViewById(R.id.time);
    }
  }

  private void getBottomSheet(String id) {
    Bundle bundle=new Bundle();
    bundle.putString("id",id);
    OptionsFragment optionsFragment=new OptionsFragment();
    optionsFragment.setArguments(bundle);
    optionsFragment.show(fragment,null);

  }

  private void openImage(String s) {
    new ImageViewer.Builder(context,Arrays.asList(s)).show();
  }

}
