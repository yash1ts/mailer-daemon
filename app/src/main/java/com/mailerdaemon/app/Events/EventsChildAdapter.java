package com.mailerdaemon.app.Events;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
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

public class EventsChildAdapter extends RecyclerView.Adapter<EventsChildAdapter.Holder> {
  private List<NoticeModel> noticeModels=new ArrayList<>();
  private FragmentManager fragment;
  private String path;
  private int size;

  public EventsChildAdapter(FragmentManager fragment){
    this.fragment=fragment;
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
    holder.options.setOnClickListener(v -> getBottomSheet(noticeModel,path));
    if(s!=null)
    { holder.imageView.setImageURI(Uri.parse(s));
      holder.date_time.setText(noticeModel.getDate());
      holder.imageView.setOnClickListener(v -> openImage(s,holder.imageView.getContext()));
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

  private void getBottomSheet(NoticeModel model,String path) {
    Bundle bundle=new Bundle();
    bundle.putParcelable("model",model);
    bundle.putString("id",path);
    OptionsEventFragment optionsFragment=new OptionsEventFragment();
    optionsFragment.setArguments(bundle);
    optionsFragment.show(fragment,null);

  }

  private void openImage(String s,Context context) {
    new ImageViewer.Builder(context, Arrays.asList(s)).show();
  }

}
