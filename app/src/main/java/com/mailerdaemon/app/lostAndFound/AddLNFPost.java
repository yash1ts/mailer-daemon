package com.mailerdaemon.app.lostAndFound;

import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.ConstantsKt;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.utils.AccessDatabase;
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

public class AddLNFPost  extends DialogFragment implements ImageUploadCallBack, ViewUtils.showProgressBar {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.bt_close)
    ImageButton bt_close;
    private ImageButton imageButton;
    private ImageView imageView;
    private EditText heading;
    private TextInputEditText detail;
    private ImageButton send;
    private String path=null;
    private String downloadUrl=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_add_notice,container,false);
        ButterKnife.bind(this,view);
        imageButton=view.findViewById(R.id.bt_img);
        imageView=view.findViewById(R.id.image);
        heading=view.findViewById(R.id.head);
        detail=view.findViewById(R.id.detail);
        detail.setText("____ belonging to Mr./Mrs. ___ has been lost/found somewhere between ______. If found, please contact ______.\n (item description)");
        send=view.findViewById(R.id.send);

        bt_close.setOnClickListener(v->dismiss());

        imageButton.setOnClickListener(v -> {
            EasyImage.openChooserWithGallery(this,"Pic image", EasyImage.RequestCodes.PICK_PICTURE_FROM_GALLERY);
            EasyImage.configuration(Objects.requireNonNull(getContext())).allowsMultiplePickingInGallery();
        });

        send.setOnClickListener(v ->{
            changeProgressBar();
            Snackbar.make(getActivity().findViewById(R.id.layout_lnf), "Our team will verify your request", Snackbar.LENGTH_LONG).show();
            UploadData.upload(this, path, getContext());
        });

        return view;
    }

    private void setDatabase() {
        Date date=new Date();
        LostAndFoundModel noticeModel=new LostAndFoundModel();
        noticeModel.setDate(date);
        noticeModel.setDetails(Objects.requireNonNull(detail.getText()).toString());
        noticeModel.setHeading(heading.getText().toString());
        noticeModel.setPhoto(downloadUrl);
        noticeModel.setVerified(false);
        String uid= Objects.requireNonNull(getContext()).getSharedPreferences("MAIN", Context.MODE_PRIVATE).getString("uid","");
        noticeModel.setUid(uid);
        FirebaseFirestore.getInstance().collection(ConstantsKt.FB_Lost_Found).document().set(noticeModel);
        changeProgressBar();
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
    public void onSuccess(String downloadUrl) {
        this.downloadUrl=downloadUrl;
        setDatabase();
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
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        AccessDatabase databse= (AccessDatabase) getActivity();
        assert databse != null;
        databse.getDatabase();
    }

}
