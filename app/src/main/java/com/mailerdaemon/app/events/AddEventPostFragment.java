package com.mailerdaemon.app.events;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.utils.ImageUploadCallBack;
import com.mailerdaemon.app.utils.UploadData;
import com.mailerdaemon.app.utils.ViewUtils;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class AddEventPostFragment extends DialogFragment implements ViewUtils.showProgressBar, ImageUploadCallBack {
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;
  @BindView(R.id.bt_close)
  ImageButton close;
  private ImageView imageView;
  private EditText heading;
  private TextInputEditText detail;
  private String path=null;
  private String downloadUrl=null;
  public AddEventPostFragment(){

  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view
        =inflater.inflate(R.layout.fragment_add_notice,container,false);
    ButterKnife.bind(this,view);
    ImageButton imageButton = view.findViewById(R.id.bt_img);
    imageView=view.findViewById(R.id.image);
    heading=view.findViewById(R.id.head);
    detail=view.findViewById(R.id.detail);
    ImageButton send = view.findViewById(R.id.send);
    final Fragment fragment=this;

    imageButton.setOnClickListener(v -> {
      EasyImage.openChooserWithGallery(fragment,"Pic image", EasyImage.RequestCodes.PICK_PICTURE_FROM_GALLERY);
      EasyImage.configuration(requireContext()).allowsMultiplePickingInGallery();
    });

    send.setOnClickListener(v -> {
      changeProgressBar();
      UploadData.upload(this,path,getContext());
    });
    close.setOnClickListener(v-> dismiss());

    return view;
  }

  private void setDatabase() {
    Date date=new Date();
    //@SuppressLint("SimpleDateFormat") DateFormat dateFormat=new SimpleDateFormat("hh:mm aaa  dd.MM.yy");
    assert this.getArguments() != null;
    String id=this.getArguments().getString("path","");
    PostModel postModel =new PostModel();
    postModel.setDate(date);
    postModel.setDetails(Objects.requireNonNull(detail.getText()).toString());
    postModel.setHeading(heading.getText().toString());
    postModel.setPhoto(downloadUrl);
    PostModel model1=new Gson().fromJson(new Gson().toJson(postModel), PostModel.class);
    FirebaseFirestore.getInstance().document(id).update("posts", FieldValue.arrayUnion(model1));
    dismiss();
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
