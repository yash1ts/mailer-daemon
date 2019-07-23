package com.mailerdaemon.app.Clubs;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.Notices.NoticeModel;
import com.mailerdaemon.app.R;

import java.util.List;

import Utils.AccessDatabse;
import Utils.DialogOptions;
import Utils.StringRes;

public class ClubsFragment extends Fragment implements AccessDatabse, DialogOptions {
    private List<ClubIconModel> iconModel;
    private RecyclerView recyclerView;
    private ClubAdapter adapter;
    private ImageButton add_club;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_clubs,container,false);
        adapter=new ClubAdapter(getContext(),this);
        recyclerView=view.findViewById(R.id.rv_clubs);
        recyclerView.setAdapter(adapter);
        add_club= view.findViewById(R.id.add_club);
      if(iconModel==null) {
          getDatabase();
      }else{
          adapter.setData(iconModel);
          adapter.notifyDataSetChanged();
      }
        Boolean access= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getBoolean("Access",false);
        if(access) {
          add_club.setVisibility(View.VISIBLE);
          add_club.setOnClickListener(v -> {
              EditClubFragment clubFragment = new EditClubFragment();
              Bundle bundle = new Bundle();
              bundle.putString("id", iconModel.size() + 1 + "");
              clubFragment.setArguments(bundle);
              clubFragment.show(getChildFragmentManager(), null);
          });
      }

      return view;
    }


    @Override
    public void getDatabase() {
        FirebaseFirestore.getInstance().collection(StringRes.FB_Club_Icons).get().addOnSuccessListener(queryDocumentSnapshots -> {
            iconModel=queryDocumentSnapshots.toObjects(ClubIconModel.class);
            adapter.setData(iconModel);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void showOptions(NoticeModel model, String path) {

    }

    @Override
    public void showDialog(String path) {
        Bundle bundle=new Bundle();
        bundle.putString("club_id",path);
        ClubDetailBottomSheet fragment=new ClubDetailBottomSheet();
        fragment.setArguments(bundle);
        fragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.theme);
        fragment.show(getChildFragmentManager(),null);
    }
}
