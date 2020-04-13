package com.mailerdaemon.app.LostAndFound;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.GsonBuilder;
import com.mailerdaemon.app.R;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LNFAdapter extends RecyclerView.Adapter<LNFAdapter.Holder> {
  private List<DocumentSnapshot> noticeModels=new ArrayList<>();
  private ImageViewer.Builder imageViewer;
  private List<String> photo=new ArrayList<>();
  int red;
  private Context context;

  LNFAdapter( Context context){
    this.imageViewer=new ImageViewer.Builder( context,photo);
    this.context=context;
  }

  @NonNull
  @Override
  public LNFAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_notices,viewGroup,false);
    red=viewGroup.getResources().getColor(R.color.red);
    return new LNFAdapter.Holder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull LNFAdapter.Holder holder, int i) {
    LostAndFoundModel model=noticeModels.get(i).toObject(LostAndFoundModel.class);
      DateFormat dateFormat=new SimpleDateFormat("hh:mm aaa  dd.MM.yy", Locale.ENGLISH);
    assert model != null;
    holder.heading.setText(model.getHeading());
    if(!model.getVerified())
      holder.heading.setTextColor(red);
    String detail=model.getDetails();
    if(detail.length()>200){
      detail=detail.substring(0,200)+"...";
      holder.detail.setOnClickListener(v-> holder.detail.setText(model.getDetails()));
    }
    holder.detail.setText(detail);
    String s=model.getPhoto();
    holder.options.setOnClickListener(v -> showOptions(model,noticeModels.get(i).getReference().getPath()));
    if(s!=null)
    { holder.imageView.setVisibility(View.VISIBLE);
        holder.date_time.setVisibility(View.VISIBLE);
        holder.time2.setVisibility(View.GONE);
        holder.imageView.setImageURI(Uri.parse(s));
      holder.date_time.setText(dateFormat.format(model.getDate()));

      holder.imageView.setOnClickListener(v ->{
        photo.clear();
        photo.add(s);
        imageViewer.show();
      });
    }else {
      holder.imageView.setVisibility(View.GONE);
      holder.date_time.setVisibility(View.GONE);
      holder.time2.setVisibility(View.VISIBLE);
      holder.time2.setText(dateFormat.format(model.getDate()));
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

  private void showOptions(LostAndFoundModel model, String path){
    Bundle bundle=new Bundle();
    bundle.putString("path", path);
    bundle.putString("model",new GsonBuilder().create().toJson(model));
    com.mailerdaemon.app.LostAndFound.OptionsFragment optionsFragment=new OptionsFragment();
    optionsFragment.setArguments(bundle);
    AppCompatActivity manager=(AppCompatActivity) context;
    optionsFragment.show(manager.getSupportFragmentManager(), null);
  }
}
