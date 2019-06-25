package com.mailerdaemon.app.Clubs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.R;

import java.io.File;
import java.util.List;

import Utils.ImageUploadCallBack;
import Utils.StringRes;
import Utils.UploadData;
import Utils.ViewUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class EditClubFragment extends DialogFragment implements ViewUtils.showProgressBar, ImageUploadCallBack {

  private String downloadUrl;
  private SimpleDraweeView imageView;
  private ImageButton imageButton,send;
  private String path,id;
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

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_edit_club,container,false);
    imageButton=view.findViewById(R.id.bt_img);
    imageView=view.findViewById(R.id.image);
    send=view.findViewById(R.id.send);
    ButterKnife.bind(this,view);
    id=getArguments().getString("id");

  imageButton.setOnClickListener(v -> {
    EasyImage.openChooserWithGallery(this,"Pick image", EasyImage.RequestCodes.PICK_PICTURE_FROM_GALLERY);
    EasyImage.configuration(getContext()).allowsMultiplePickingInGallery();
  });

    send.setOnClickListener(v -> {
      changeProgressBar();
      UploadData.upload(this, path,getContext());
    });
    return view;
}

  private void setDatabase() {
    ClubDetailModel clubDetailModel=new ClubDetailModel();
    clubDetailModel.setClub(downloadUrl);
    clubDetailModel.setDescription(description.getText().toString());
    clubDetailModel.setFb(fb.getText().toString());
    clubDetailModel.setInsta(insta.getText().toString());
    clubDetailModel.setMembers(members.getText().toString());
    clubDetailModel.setYoutube(youtube.getText().toString());
    clubDetailModel.setWeb(web.getText().toString());
    clubDetailModel.setName(club_name.getText().toString());
    FirebaseFirestore.getInstance().collection(StringRes.FB_Collec_Club).document(id).set(clubDetailModel);
    ClubIconModel clubIconModel=new ClubIconModel();
    clubIconModel.setTag(id);
    clubIconModel.setUrl(downloadUrl);
    FirebaseFirestore.getInstance().collection(StringRes.FB_Club_Icons).document(id).set(clubIconModel);
    getDialog().dismiss();
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
}
