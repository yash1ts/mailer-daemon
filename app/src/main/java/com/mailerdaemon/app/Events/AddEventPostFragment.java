package com.mailerdaemon.app.Events;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.Notices.NoticeModel;
import com.mailerdaemon.app.R;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Utils.ImageUploadCallBack;
import Utils.StringRes;
import Utils.UploadData;
import Utils.ViewUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class AddEventPostFragment extends DialogFragment implements ViewUtils.showProgressBar, ImageUploadCallBack {
  public AddEventPostFragment(){

  }

  private ImageButton imageButton;
  private ImageView imageView;
  private EditText heading;
  private TextInputEditText detail;
  private ImageButton send;
  String path;
  String downloadUrl;
  String name;
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view
        =inflater.inflate(R.layout.fragment_add_notice,container,false);
    ButterKnife.bind(this,view);
    imageButton=view.findViewById(R.id.bt_img);
    imageView=view.findViewById(R.id.image);
    heading=view.findViewById(R.id.head);
    detail=view.findViewById(R.id.detail);
    send=view.findViewById(R.id.send);
    final Fragment fragment=this;

    imageButton.setOnClickListener(v -> {
      EasyImage.openChooserWithGallery(fragment,"Pic image", EasyImage.RequestCodes.PICK_PICTURE_FROM_GALLERY);
      EasyImage.configuration(getContext()).allowsMultiplePickingInGallery();
    });

    send.setOnClickListener(v -> {
      UploadData.upload(this::onSuccess,path,getContext());
    });

    return view;
  }

  private void setDatabase() {
    Date date=new Date();
    DateFormat dateFormat=new SimpleDateFormat();
    String name=this.getArguments().getString("name");
    NoticeModel models=new NoticeModel(heading.getText().toString(),detail.getText().toString(),downloadUrl,dateFormat.format(date));
    FirebaseFirestore.getInstance().collection(StringRes.FB_Collec_Notice).document(name).collection("posts").document().set(models);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
      @Override
      public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
        imageView.setImageURI(Uri.fromFile(imageFiles.get(0)));
        path=imageFiles.get(0).getPath();
        name=imageFiles.get(0).getName();
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
