package com.mailerdaemon.app.LostAndFound;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mailerdaemon.app.Notices.NoticeModel;
import com.mailerdaemon.app.Notices.OptionsFragment;
import com.mailerdaemon.app.R;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Utils.StringRes;

public class LNFAdapter extends RecyclerView.Adapter<LNFAdapter.Holder> {
  private List<DocumentSnapshot> noticeModels=new ArrayList<>();
  private Context context;
  private FragmentManager fragment;
  private int px;

  public LNFAdapter(Context context, FragmentManager fragment){
    this.context=context;
    this.fragment=fragment;
    px=Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,48f,context.getResources().getDisplayMetrics()));


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
    holder.options.setOnClickListener(v -> getBottomSheet(noticeModels.get(i).getReference()));
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
      CircularProgressDrawable drawable=new CircularProgressDrawable(context);
      drawable.setCenterRadius(px);
      imageView.getHierarchy().setProgressBarImage(drawable);
      date_time=itemView.findViewById(R.id.time);
    }
  }

  private void getBottomSheet(DocumentReference id) {
    Bundle bundle=new Bundle();
    bundle.putString("id", id.getPath());
    OptionsFragment optionsFragment=new OptionsFragment();
    optionsFragment.setArguments(bundle);
    optionsFragment.show(fragment, null);

  }

  private void openImage(String s) {
    new ImageViewer.Builder(context,Arrays.asList(s)).show();
  }

}
