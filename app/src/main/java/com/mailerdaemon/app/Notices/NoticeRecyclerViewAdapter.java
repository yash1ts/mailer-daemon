package com.mailerdaemon.app.Notices;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mailerdaemon.app.R;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

import Utils.DialogOptions;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<NoticeRecyclerViewAdapter.Holder> {
  private List<DocumentSnapshot> noticeModels=new ArrayList<>();
  private DialogOptions options;
  private ImageViewer.Builder imageViewer;
  private List<String> photo=new ArrayList<>();

  NoticeRecyclerViewAdapter(DialogOptions options, Context context){
    this.options=options;
    this.imageViewer=new ImageViewer.Builder( context,photo);
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_notices,viewGroup,false);

    return new Holder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int i) {
    NoticeModel model=noticeModels.get(i).toObject(NoticeModel.class);
    assert model != null;
    holder.heading.setText(model.getHeading());
    String detail=model.getDetails();
    if(detail.length()>200){
        detail=detail.substring(0,200)+"...";
        holder.detail.setOnClickListener(v-> holder.detail.setText(model.getDetails()));
    }
    holder.detail.setText(detail);
    String s=model.getPhoto();
    holder.options.setOnClickListener(v -> options.showOptions(model,noticeModels.get(i).getReference().getPath()));
    if(s!=null)
    { holder.imageView.setImageURI(Uri.parse(s));
    holder.date_time.setText(model.getDate());

      holder.imageView.setOnClickListener(v ->{
        photo.clear();
        photo.add(s);
        imageViewer.show();
      });
    }else {
      holder.imageView.setVisibility(View.GONE);
      holder.date_time.setVisibility(View.GONE);
      holder.time2.setVisibility(View.VISIBLE);
      holder.time2.setText(model.getDate());
    }
    //setAnimation(holder.container,i);
  }

  @Override
  public int getItemCount() {
    return noticeModels.size();
  }

  public void setData(List<DocumentSnapshot> noticeModels) {
    this.noticeModels=noticeModels;
  }

  public static class Holder extends RecyclerView.ViewHolder {
    @BindView(R.id.notice_head)
    TextView heading;
    @BindView(R.id.notice_detail)
    TextView detail;
    @BindView(R.id.notice_photo)
    SimpleDraweeView imageView;
    @BindView(R.id.time)
    TextView date_time;
    @BindView(R.id.time2)
    TextView time2;
    @BindView(R.id.notice_options)
    View options;
    @BindView(R.id.container)
    CardView container;

    public Holder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this,itemView);
    }
  }

}
