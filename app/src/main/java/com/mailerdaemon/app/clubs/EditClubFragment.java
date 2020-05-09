package com.mailerdaemon.app.clubs;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.GsonBuilder;
import com.mailerdaemon.app.ConstantsKt;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.utils.AccessDatabase;
import com.mailerdaemon.app.utils.ImageUploadCallBack;
import com.mailerdaemon.app.utils.UploadData;
import com.mailerdaemon.app.utils.ViewUtils;

import java.io.File;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class EditClubFragment extends DialogFragment implements ViewUtils.showProgressBar, ImageUploadCallBack {

  @BindView(R.id.club_edit_youtube)
  EditText youtube;
  @BindView(R.id.club_edit_insta)
  EditText insta;
  @BindView(R.id.club_edit_name)
  EditText club_name;
  @BindView(R.id.club_edit_fb)
  EditText fb;
  @BindView(R.id.club_edit_web)
  EditText web;
  @BindView(R.id.club_edit_des)
  EditText description;
  @BindView(R.id.club_edit_members)
  EditText members;
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;
  private String downloadUrl;
  private String previous_Url;
  private SimpleDraweeView imageView;
  private String path=null;
  private int id;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_edit_club,container,false);
    ImageButton imageButton = view.findViewById(R.id.bt_img);
    imageView=view.findViewById(R.id.image);
    ImageButton send = view.findViewById(R.id.send);
    ButterKnife.bind(this,view);
    assert getArguments() != null;
    id=getArguments().getInt("id");
    String s=getArguments().getString("data","");
    ClubDetailModel model= new GsonBuilder().create().fromJson(s,ClubDetailModel.class);
    if(!s.equals("")){
    club_name.setText(model.getName());
    description.setText(model.getDescription());
    members.setText(model.getMembers());
    previous_Url=model.getClub();
    if (model.getClub()!=null)
    imageView.setImageURI(Uri.parse(model.getClub()));
    fb.setText(model.getFb());
    insta.setText(model.getInsta());
    youtube.setText(model.getYoutube());
    web.setText(model.getWeb());}
    imageButton.setOnClickListener(v -> {
      EasyImage.openChooserWithGallery(this,"Pick image", EasyImage.RequestCodes.PICK_PICTURE_FROM_GALLERY);
      EasyImage.configuration(Objects.requireNonNull(getContext())).allowsMultiplePickingInGallery();
    });

    send.setOnClickListener(v -> {
      changeProgressBar();
      UploadData.upload(this, path,getContext());
    });
    return view;
  }

  private void setDatabase() {
    ClubDetailModel clubDetailModel=new ClubDetailModel();
    if(downloadUrl!=null)
    clubDetailModel.setClub(downloadUrl);
    else clubDetailModel.setClub(previous_Url);
    clubDetailModel.setDescription(description.getText().toString());
    clubDetailModel.setFb(fb.getText().toString());
    clubDetailModel.setInsta(insta.getText().toString());
    clubDetailModel.setMembers(members.getText().toString());
    clubDetailModel.setYoutube(youtube.getText().toString());
    clubDetailModel.setWeb(web.getText().toString());
    clubDetailModel.setName(club_name.getText().toString());
    FirebaseFirestore.getInstance().collection(ConstantsKt.FB_CLUB).document(String.format("%d",id)).set(clubDetailModel);
    ClubIconModel clubIconModel=new ClubIconModel();
    clubIconModel.setTag(id);
    if(downloadUrl!=null)
    clubIconModel.setUrl(downloadUrl);
    else clubIconModel.setUrl(previous_Url);
    FirebaseFirestore.getInstance().collection(ConstantsKt.FB_CLUB_ICON).document(String.format("%d",id)).set(clubIconModel);
    Objects.requireNonNull(getDialog()).dismiss();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
      @Override
      public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
        imageView.setImageURI(Uri.fromFile(imageFiles.get(0)));
        path=imageFiles.get(0).getPath();
      }
    });
  }

  @Override
  public void changeProgressBar() {
    if(progressBar.isShown())
    {
      progressBar.setVisibility(View.GONE);
    }
    else progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void onSuccess(String downloadUrl) {
    changeProgressBar();
    this.downloadUrl=downloadUrl;
    setDatabase();
  }

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {
    super.onDismiss(dialog);
    assert getParentFragment() != null;
    if(getParentFragment().getClass().equals(ClubsFragment.class)){
      AccessDatabase databse=(AccessDatabase)getParentFragment();
      databse.getDatabase();
    }
  }
}
