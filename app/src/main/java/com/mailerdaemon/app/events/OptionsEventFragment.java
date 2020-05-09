package com.mailerdaemon.app.events;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.mailerdaemon.app.ConstantsKt;
import com.mailerdaemon.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OptionsEventFragment extends BottomSheetDialogFragment {

  @BindView(R.id.option_delete)
  View delete;
  @BindView(R.id.option_Copy)
  View copy;
  @BindView(R.id.option_download)
      View download;
  String path;
  private DocumentReference reference;
  private PostModel model;
  private Boolean access;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_options,container,false);

    path = getArguments().getString("path");
    model=new Gson().fromJson(getArguments().getString("model"), PostModel.class);
    ButterKnife.bind(this,view);
    reference=FirebaseFirestore.getInstance().document(path);
    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

    DownloadManager manager = (DownloadManager) getContext()
        .getSystemService(Context.DOWNLOAD_SERVICE);
    access= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getBoolean("Access",false);

    if(access){
        delete.setVisibility(View.VISIBLE);
        delete.setOnClickListener(v ->{
            reference.update("posts", FieldValue.arrayRemove(model));
            Log.d("DELETE",path);
            Toast.makeText(getContext(), ConstantsKt.DONE_REFRESH,Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
        });
    }

    copy.setOnClickListener(v -> {

        ClipData clip = ClipData.newPlainText("Copy", model.getHeading()+"\n"+model.getDetails());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(),"Copied",Toast.LENGTH_SHORT).show();
        getDialog().dismiss();
      });
    if(model.getPhoto()==null){
        download.setVisibility(View.GONE);
    }else
    download.setOnClickListener(v->{
          DownloadManager.Request request = new DownloadManager.Request(Uri.parse(model.getPhoto()));
          request.setDescription("event-image");
          request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
          request.setTitle(model.getHeading());
          manager.enqueue(request);
          getDialog().dismiss();


    });
    return view;
  }
}
