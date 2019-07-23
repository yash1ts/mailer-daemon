package com.mailerdaemon.app.Clubs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
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
  private ChromeTab chromeTab;
  private Boolean access;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_club_detail, container, false);
    ButterKnife.bind(this, view);
    chromeTab=new ChromeTab(getContext());
    club.getHierarchy().setProgressBarImage(new CircularProgressDrawable(getContext()));
    id = getArguments().getString("club_id");
    getDatabase();
    access= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getBoolean("Access",false);
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
      if(details.getClub()!=null)
      club.setImageURI(Uri.parse(details.getClub()));
      members.setText(details.getMembers());
      fb.setOnClickListener(v -> chromeTab.openTab(details.getFb()));
      insta.setOnClickListener(v -> chromeTab.openTab(details.getInsta()));
      youtube.setOnClickListener(v -> chromeTab.openTab(details.getYoutube()));
      web.setOnClickListener(v -> chromeTab.openTab(details.getWeb()));
    }
      if (access) {
        create.setVisibility(View.VISIBLE);
        create.setOnClickListener(v -> {
          EditClubFragment clubFragment = new EditClubFragment();
          Bundle bundle = new Bundle();
          bundle.putString("id", id);
          clubFragment.setArguments(bundle);
          clubFragment.show(getChildFragmentManager(), null);
        });
      }
    }

}
