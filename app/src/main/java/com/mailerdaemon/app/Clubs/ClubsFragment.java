package com.mailerdaemon.app.Clubs;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.R;

import java.util.List;

import Utils.AccessDatabse;
import Utils.StringRes;

public class ClubsFragment extends Fragment implements AccessDatabse {
    private View view;
    private List<ClubIconModel> iconModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      view=inflater.inflate(R.layout.fragment_clubs,container,false);
      if(iconModel==null) {
          getDatabase();
      }
      else
        setView();

      return view;
    }


    @Override
    public void getDatabase() {
        FirebaseFirestore.getInstance().collection(StringRes.FB_Club_Icons).get().addOnSuccessListener(queryDocumentSnapshots -> {
            iconModel=queryDocumentSnapshots.toObjects(ClubIconModel.class);
            setView();
        });
    }

    public void setView()
    {int i=0;
        if(StringRes.No_Club>iconModel.size()){
            for(ClubIconModel model:iconModel) {
                SimpleDraweeView imageView=view.findViewWithTag(i+1+"");
                imageView.getHierarchy().setProgressBarImage(new CircularProgressDrawable(getContext()));
                imageView.setImageURI(Uri.parse(model.getUrl()));
                i++;
            }}
    }
}
