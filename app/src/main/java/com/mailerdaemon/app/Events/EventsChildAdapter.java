package com.mailerdaemon.app.Events;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mailerdaemon.app.Notices.NoticeModel;
import com.mailerdaemon.app.R;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Utils.DialogOptions;

public class EventsChildAdapter extends RecyclerView.Adapter<EventsChildAdapter.Holder> {
  private List<NoticeModel> noticeModels=new ArrayList<>();
  private DialogOptions options;
  private String path;
  private int size;
  private List<String> photos=new ArrayList<>();
  private ImageViewer viewer;

  public EventsChildAdapter(DialogOptions options,Context context){
    this.viewer= new ImageViewer.Builder(context, photos).show();
    this.options=options;
  }

  @NonNull
  @Override
  public EventsChildAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_event_post,viewGroup,false);
    return new Holder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull EventsChildAdapter.Holder holder, int i) {
    NoticeModel noticeModel=noticeModels.get(size-1-i);
    holder.heading.setText(noticeModel.getHeading());
    holder.detail.setText(noticeModel.getDetails());
    String s=noticeModel.getPhoto();
    holder.options.setOnClickListener(v -> options.showOptions(noticeModel,path));
    if(s!=null)
    { holder.imageView.setImageURI(Uri.parse(s));
      holder.date_time.setText(noticeModel.getDate());
      holder.imageView.setOnClickListener(v ->{
        photos.clear();
        photos.add(s);
        viewer.show();
      });
    }else {
      holder.imageView.setVisibility(View.GONE);
      holder.date_time.setVisibility(View.GONE);
      holder.time2.setVisibility(View.VISIBLE);
      holder.time2.setText(noticeModel.getDate());
    }
  }

  @Override
  public int getItemCount() {
    return noticeModels.size();
  }

  public void setData(List<NoticeModel> noticeModels, String path) {
    if(noticeModels!=null)
    {this.noticeModels=(noticeModels);
    this.path=path;
    this.size=noticeModels.size();}
  }

  public static class Holder extends RecyclerView.ViewHolder {
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

  private void openImage(String s,Context context) {

  }

}
