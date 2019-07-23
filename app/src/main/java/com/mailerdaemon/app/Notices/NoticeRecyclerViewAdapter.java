package com.mailerdaemon.app.Notices;


import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.mailerdaemon.app.R;

import com.stfalcon.frescoimageviewer.ImageViewer;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Utils.DialogOptions;

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<NoticeRecyclerViewAdapter.Holder> {
  private List<DocumentSnapshot> noticeModels=new ArrayList<>();
  private DialogOptions options;
  private static int px;
  int lastPosition=-1;
  PrettyTime p;
  DateFormat format;
  static CircularProgressDrawable drawable;
  ImageViewer.Builder imageViewer;
  List<String> photo=new ArrayList<>();

  public NoticeRecyclerViewAdapter(int px , DialogOptions options, Context context){
    this.options=options;
    this.px=px;
    this.p = new PrettyTime();
    this.format=new SimpleDateFormat();
    this.drawable=new CircularProgressDrawable(context);
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
    holder.heading.setText(model.getHeading());
    holder.detail.setText(model.getDetails());
    String s=model.getPhoto();
    holder.options.setOnClickListener(v -> options.showOptions(model,noticeModels.get(i).getReference().getPath()));
    if(s!=null)
    { holder.imageView.setImageURI(Uri.parse(s));

      try {
        holder.date_time.setText(p.format(format.parse(model.getDate())));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      holder.imageView.setOnClickListener(v ->{
        photo.clear();
        photo.add(s);
        imageViewer.show();
      });
    }else {
      holder.imageView.setVisibility(View.GONE);
      holder.date_time.setVisibility(View.GONE);
      holder.time2.setVisibility(View.VISIBLE);
      try {
        holder.time2.setText(p.format(format.parse(model.getDate())));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    setAnimation(holder.container,i);
  }
  private void setAnimation(View viewToAnimate, int position) {
    // If the bound view wasn't previously displayed on screen, it's animated
    if (position > lastPosition) {
      Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.push_left_in);
      viewToAnimate.startAnimation(animation);
      lastPosition = position;
    }
  }

  @Override
  public int getItemCount() {
    return noticeModels.size();
  }

  public void setData(List<DocumentSnapshot> noticeModels) {
    this.noticeModels=noticeModels;
  }

  public static class Holder extends RecyclerView.ViewHolder {
    TextView heading;
    TextView detail;
    SimpleDraweeView imageView;
    TextView date_time;
    TextView time2;
    View options;
    CardView container;

    public Holder(@NonNull View itemView) {
      super(itemView);
      options=itemView.findViewById(R.id.notice_options);
      time2=itemView.findViewById(R.id.time2);
      heading =itemView.findViewById(R.id.notice_head);
      detail=itemView.findViewById(R.id.notice_detail);
      imageView=itemView.findViewById(R.id.notice_photo);

      drawable.setCenterRadius(px);
      imageView.getHierarchy().setProgressBarImage(drawable);
      date_time=itemView.findViewById(R.id.time);
      container=itemView.findViewById(R.id.container);
    }
  }

}
