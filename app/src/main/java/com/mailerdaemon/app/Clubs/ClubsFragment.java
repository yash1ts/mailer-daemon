package com.mailerdaemon.app.Clubs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.GsonBuilder;
import com.mailerdaemon.app.Notices.NoticeModel;
import com.mailerdaemon.app.R;

import java.util.List;
import java.util.Objects;

import Utils.AccessDatabse;
import Utils.DialogOptions;
import Utils.StringRes;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class ClubsFragment extends Fragment implements AccessDatabse, DialogOptions {
    private List<ClubIconModel> iconModel;
    private RecyclerView recyclerView;
    private ClubAdapter adapter;
    private ImageButton add_club;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_clubs,container,false);
        adapter=new ClubAdapter(getContext(),this);
        recyclerView=view.findViewById(R.id.rv_clubs);
        recyclerView.setAdapter(adapter);
        editor= Objects.requireNonNull(getActivity()).getSharedPreferences("MAIN",Context.MODE_PRIVATE).edit();
        add_club= view.findViewById(R.id.add_club);
      if(iconModel==null) {
          getDatabase();
      }else{
          adapter.setData(iconModel);
          adapter.notifyDataSetChanged();
      }
        boolean access= getDefaultSharedPreferences(getActivity().getApplicationContext()).getBoolean("Access",false);
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
        String string=getDefaultSharedPreferences(getActivity()).getString("club","");
        assert string != null;
        if(string.equals(""))
        FirebaseFirestore.getInstance().collection(StringRes.FB_Club_Icons).orderBy("tag", Query.Direction.ASCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            iconModel=queryDocumentSnapshots.toObjects(ClubIconModel.class);
            Log.d("ICON",iconModel.toString());
            ClubListModel model=new ClubListModel();
            model.setModelList(iconModel);
            adapter.setData(iconModel);
            adapter.notifyDataSetChanged();
            if(editor!=null)
            editor.putString("club",new GsonBuilder().create().toJson(model)).apply();
        });
        else {
            ClubListModel model = new GsonBuilder().create().fromJson(string, ClubListModel.class);
            iconModel=model.getModelList();
            adapter.setData(iconModel);
            adapter.notifyDataSetChanged();
        }
        FirebaseFirestore.getInstance().collection(StringRes.FB_Club_Icons).orderBy("tag", Query.Direction.ASCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            iconModel = queryDocumentSnapshots.toObjects(ClubIconModel.class);
            ClubListModel model = new ClubListModel();
            model.setModelList(iconModel);
                adapter.setData(iconModel);
                adapter.notifyDataSetChanged();
            if (editor!=null)
                editor.putString("club",new GsonBuilder().create().toJson(model)).apply();
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
        fragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.bottomSheetTransparent);

        fragment.show(getChildFragmentManager(),null);
    }
}
