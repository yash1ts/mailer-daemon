package com.mailerdaemon.app.LostAndFound;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.GsonBuilder;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.UserModel;

import java.util.Objects;

import Utils.AccessDatabse;
import Utils.StringRes;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OptionsFragment extends BottomSheetDialogFragment {
    @BindView(R.id.option_delete)
    View delete;
    @BindView(R.id.option_Copy)
    View copy;
    @BindView(R.id.option_download)
    View download;
    @BindView(R.id.option_verify)
    View verify;
    private String path;
    private DocumentReference reference;
    private LostAndFoundModel model;
    private Boolean access;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_options_lnf,container,false);

        assert getArguments() != null;
        path = getArguments().getString("path");
        model=new GsonBuilder().create().fromJson(getArguments().getString("model"),LostAndFoundModel.class);
        ButterKnife.bind(this,view);
        reference=FirebaseFirestore.getInstance().document(path);
        ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CLIPBOARD_SERVICE);

        DownloadManager manager = (DownloadManager) Objects.requireNonNull(getContext())
                .getSystemService(Context.DOWNLOAD_SERVICE);
        access= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getBoolean("Access",false);

        if(access){
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(v ->{
                if(!model.getVerified()) {
                    UserModel user = new UserModel();
                    user.setRejectedPost(true);
                    FirebaseFirestore.getInstance().collection("user").document(model.getUid()).set(user, SetOptions.mergeFields("rejectedPost"));
                }
                reference.delete();
                AccessDatabse activity= (AccessDatabse) getActivity();
                activity.getDatabase();
                Toast.makeText(getContext(), StringRes.Done_Refresh,Toast.LENGTH_SHORT).show();
                Objects.requireNonNull(getDialog()).dismiss();
            });
            if(!model.getVerified())
            verify.setVisibility(View.VISIBLE);
            verify.setOnClickListener(v->{
                model.setVerified(true);
                reference.set(model);
                AccessDatabse activity= (AccessDatabse) getActivity();
                activity.getDatabase();
                Objects.requireNonNull(getDialog()).dismiss();
            });
        }

        copy.setOnClickListener(v -> {
            ClipData clip = ClipData.newPlainText("Copy", model.getHeading()+"\n"+model.getDetails());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(),"Copied",Toast.LENGTH_SHORT).show();
            Objects.requireNonNull(getDialog()).dismiss();
        });
        if(model.getPhoto()==null){
            download.setVisibility(View.GONE);
        }else
            download.setOnClickListener(v->{
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(model.getPhoto()));
                request.setDescription("notice-image");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setTitle(model.getHeading());
                manager.enqueue(request);
                Objects.requireNonNull(getDialog()).dismiss();


            });
        return view;
    }

}
