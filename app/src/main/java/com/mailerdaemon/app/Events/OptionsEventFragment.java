package com.mailerdaemon.app.Events;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.Notices.NoticeModel;
import com.mailerdaemon.app.R;

import Utils.StringRes;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OptionsEventFragment extends BottomSheetDialogFragment {

  @BindView(R.id.option_delete)
  View delete;
  @BindView(R.id.option_Copy)
  View copy;
  @BindView(R.id.option_download)
      View download;
  String id;
  private DocumentReference reference;
  private NoticeModel model;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_options,container,false);

    id= getArguments().getString("id");
    model=getArguments().getParcelable("model");
    ButterKnife.bind(this,view);
    reference=FirebaseFirestore.getInstance().document(id);
    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

    DownloadManager manager = (DownloadManager) getContext()
        .getSystemService(Context.DOWNLOAD_SERVICE);


    delete.setOnClickListener(v ->{
      reference.update("posts", FieldValue.arrayRemove(model));
      getDialog().dismiss();
    });
    copy.setOnClickListener(v -> {

        ClipData clip = ClipData.newPlainText("Copy", model.getHeading()+"\n"+model.getDetails());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(),"Copied",Toast.LENGTH_SHORT).show();
        getDialog().dismiss();
      });
    download.setOnClickListener(v->{


          DownloadManager.Request request = new DownloadManager.Request(Uri.parse(model.getPhoto()));
          request.setDescription("notice-image");
          request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
          request.setTitle(model.getHeading());
          manager.enqueue(request);
          getDialog().dismiss();


    });
    return view;
  }
}
