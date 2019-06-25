package com.mailerdaemon.app.Clubs;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CircularProgressDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.R;

import java.util.List;

import Utils.StringRes;

public class ClubsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_clubs,container,false);
      FirebaseFirestore.getInstance().collection(StringRes.FB_Club_Icons).get().addOnSuccessListener(queryDocumentSnapshots -> {
         List<ClubIconModel> iconModel=queryDocumentSnapshots.toObjects(ClubIconModel.class);
         int i=0;
         if(StringRes.No_Club>iconModel.size()){
        for(ClubIconModel model:iconModel) {
           SimpleDraweeView imageView=view.findViewWithTag(i+1+"");
           imageView.getHierarchy().setProgressBarImage(new CircularProgressDrawable(getContext()));
           imageView.setImageURI(Uri.parse(model.getUrl()));
           i++;
         }}
      });

      return view;
    }



}
