package com.mailerdaemon.app.Clubs;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.widget.CircularProgressDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.mailerdaemon.app.R;

import Utils.ChromeTab;
import Utils.StringRes;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ClubDetailBottomSheet extends BottomSheetDialogFragment {

  private String id;
  @BindView(R.id.club_des)
  TextView description;
  @BindView(R.id.club_members)
  TextView members;
  @BindView(R.id.club_name)
  TextView name;
  @BindView(R.id.club_edit)
  ImageView create;
  @BindView(R.id.club_fb)
  ImageView fb;
  @BindView(R.id.club_insta)
  ImageView insta;
  @BindView(R.id.club_youtube)
  ImageView youtube;
  @BindView(R.id.club_web)
  ImageView web;
  @BindView(R.id.club_icon)
  SimpleDraweeView club;
  @BindView(R.id.club_delete)
  ImageView delete;
  private ChromeTab chromeTab;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_club_detail, container, false);
    ButterKnife.bind(this, view);
    chromeTab=new ChromeTab(getContext());
    club.getHierarchy().setProgressBarImage(new CircularProgressDrawable(getContext()));
    id = getArguments().getString("club_id");
    getDatabase();
    return view;
  }

  private void getDatabase() {
    FirebaseFirestore.getInstance().collection(StringRes.FB_Collec_Club).document(id).get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        setView(task.getResult().toObject(ClubDetailModel.class));
      }else
      {
        Toast.makeText(getContext(), StringRes.No_Internet,Toast.LENGTH_LONG).show();
        FirebaseFirestore.getInstance().collection(StringRes.FB_Collec_Club).document(id).get(Source.CACHE).addOnSuccessListener(documentSnapshot -> setView(task.getResult().toObject(ClubDetailModel.class)));
      }
    });
  }
    private void setView (ClubDetailModel details){
    if(details!=null) {
      description.setText(details.getDescription());
      name.setText(details.getName());
      club.setImageURI(Uri.parse(details.getClub()));
      members.setText(details.getMembers());
      fb.setOnClickListener(v -> chromeTab.openTab(details.getFb()));
      insta.setOnClickListener(v -> chromeTab.openTab(details.getInsta()));
      youtube.setOnClickListener(v -> chromeTab.openTab(details.getYoutube()));
      web.setOnClickListener(v -> chromeTab.openTab(details.getWeb()));
      delete.setOnClickListener(v -> {
        FirebaseFirestore.getInstance().collection("club").document(id).delete();
        Toast.makeText(getContext(),"Deleted, Refresh to see",Toast.LENGTH_SHORT).show();
        getDialog().dismiss();
      });
    }
      create.setOnClickListener(v -> {
        EditClubFragment clubFragment = new EditClubFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        clubFragment.setArguments(bundle);
        clubFragment.show(getChildFragmentManager(), null);
        getDialog().dismiss();
      });
    }

}
