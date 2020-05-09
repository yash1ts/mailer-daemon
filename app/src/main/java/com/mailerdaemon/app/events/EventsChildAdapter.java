package com.mailerdaemon.app.events;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.utils.DialogOptions;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventsChildAdapter extends RecyclerView.Adapter<EventsChildAdapter.Holder> {
  private List<PostModel> postModels =new ArrayList<>();
  private DialogOptions options;
  private String path;
  private List<String> photos=new ArrayList<>();
  private Context context;

  EventsChildAdapter(DialogOptions options, Context context){
    this.context=context;
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
    PostModel postModel = postModels.get(i);
    DateFormat dateFormat=new SimpleDateFormat("hh:mm aaa  dd.MM.yy", Locale.ENGLISH);
    holder.heading.setText(postModel.getHeading());
    String detail= postModel.getDetails();
    if(detail.length()>200){
      detail=detail.substring(0,200)+"...";
      holder.detail.setOnClickListener(v-> holder.detail.setText(postModel.getDetails()));
    }
    holder.detail.setText(detail);
    String s= postModel.getPhoto();
    holder.options.setOnClickListener(v -> options.showOptions(postModel,path));
    if(s!=null)
    {  holder.imageView.setVisibility(View.VISIBLE);
        holder.date_time.setVisibility(View.VISIBLE);
        holder.time2.setVisibility(View.GONE);
        holder.imageView.setImageURI(Uri.parse(s));
      holder.date_time.setText(dateFormat.format(postModel.getDate()));
      holder.imageView.setOnClickListener(v ->{
        photos.clear();
        photos.add(s);
          new ImageViewer.Builder(context, photos).show();
      });
    }else {
      holder.imageView.setVisibility(View.GONE);
      holder.date_time.setVisibility(View.GONE);
      holder.time2.setVisibility(View.VISIBLE);
      holder.time2.setText(dateFormat.format(postModel.getDate()));
    }
  }

  @Override
  public int getItemCount() {
    return postModels.size();
  }

  public void setData(List<PostModel> postModels, String path) {
    if(postModels !=null)
    {this.postModels =(postModels);
    this.path=path; }
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

}
