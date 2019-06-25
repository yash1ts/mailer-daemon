package com.mailerdaemon.app.Notices;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.R;

import Utils.StringRes;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OptionsFragment extends BottomSheetDialogFragment {
  public OptionsFragment(){

  }
  @BindView(R.id.option_delete)
  View delete;
  @BindView(R.id.option_Copy)
  View copy;
  @BindView(R.id.option_download)
      View download;
  String id;
  private DocumentReference reference;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_options,container,false);
    id=getArguments().getString("id");
    ButterKnife.bind(this,view);
    reference=FirebaseFirestore.getInstance().collection(StringRes.FB_Collec_Notice).document(id);
    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

    DownloadManager manager = (DownloadManager) getContext()
        .getSystemService(Context.DOWNLOAD_SERVICE);


    delete.setOnClickListener(v ->{
      reference.delete();
      getDialog().dismiss();
    });
    copy.setOnClickListener(v -> reference.get().addOnCompleteListener(task ->{
      if(task.isSuccessful())
      {
        NoticeModel noticeModel=task.getResult().toObject(NoticeModel.class);
        ClipData clip = ClipData.newPlainText("Copy", noticeModel.getHeading()+"\n"+noticeModel.getDetails());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(),"Copied",Toast.LENGTH_SHORT).show();
        getDialog().dismiss();
      }
    }));
    download.setOnClickListener(v->{
      reference.get().addOnCompleteListener(task ->{
        if(task.isSuccessful())
        {
          NoticeModel noticeModel=task.getResult().toObject(NoticeModel.class);
          DownloadManager.Request request = new DownloadManager.Request(Uri.parse(noticeModel.getPhoto()));
          request.setDescription("notice-image");
          request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
          request.setTitle(noticeModel.getHeading());
          manager.enqueue(request);
          getDialog().dismiss();
        }
      });


    });
    return view;
  }
}
