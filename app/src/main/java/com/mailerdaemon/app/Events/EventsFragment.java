package com.mailerdaemon.app.Events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Source;
import com.mailerdaemon.app.Notices.NoticeModel;
import com.mailerdaemon.app.R;

import java.util.List;

import Utils.AccessDatabse;
import Utils.DialogOptions;
import Utils.StringRes;

public class EventsFragment extends Fragment implements AccessDatabse, DialogOptions {
  private ShimmerFrameLayout shimmerViewContainer;
  private RecyclerView recyclerView;
  private EventsParentAdapter adapter;
  private FirebaseFirestore firebaseFirestore;
  private List<DocumentSnapshot> snap;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_events,container,false);
    shimmerViewContainer=view.findViewById(R.id.shimmer_view_container);
    shimmerViewContainer.startShimmer();

    recyclerView=view.findViewById(R.id.rv_events);
    Boolean access=PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getBoolean("Access",false);
    adapter=new EventsParentAdapter(this,access,getContext());
    firebaseFirestore=FirebaseFirestore.getInstance();
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);

    if(snap==null)
    {shimmerViewContainer.setVisibility(View.VISIBLE);
    getDatabase();}
    else {
      adapter.setData(snap);
      adapter.notifyDataSetChanged();
    }

    return view;
  }
  @Override
  public void getDatabase() {

    firebaseFirestore.collection(StringRes.FB_Collec_Event).orderBy("date", Query.Direction.ASCENDING).get(Source.SERVER).addOnCompleteListener(task -> {
      if(task.isSuccessful()) {
        snap=task.getResult().getDocuments();
        Log.d("DB",task.getResult().toObjects(EventModel.class).toString());
        if(snap!=null)
        adapter.setData(snap);
        adapter.notifyDataSetChanged();
        shimmerViewContainer.stopShimmer();
        shimmerViewContainer.setVisibility(View.GONE);
      }else{
        Toast.makeText(getContext(),StringRes.No_Internet,Toast.LENGTH_LONG).show();
        firebaseFirestore.collection(StringRes.FB_Collec_Event).orderBy("date", Query.Direction.ASCENDING).get(Source.CACHE).addOnCompleteListener(task2 -> {
          if(task.isSuccessful()) {
            snap=task2.getResult().getDocuments();
            Log.d("DB",task.getResult().toObjects(EventModel.class).toString());
            adapter.setData(snap);
            adapter.notifyDataSetChanged();
            shimmerViewContainer.stopShimmer();
            shimmerViewContainer.setVisibility(View.GONE);
          }else{
            Toast.makeText(getContext(),StringRes.No_Internet,Toast.LENGTH_LONG).show();
          }

        });
      }

    });
  }


  @Override
  public void showOptions(NoticeModel model, String path) {
    Bundle bundle=new Bundle();
    bundle.putParcelable("model",model);
    bundle.putString("path",path);
    OptionsEventFragment optionsFragment=new OptionsEventFragment();
    optionsFragment.setArguments(bundle);
    optionsFragment.show(getChildFragmentManager(),null);
  }

  @Override
  public void showDialog(String path) {
    Bundle bundle=new Bundle();
    bundle.putString("path",path);
    AddEventPostFragment dialog=new AddEventPostFragment();
    dialog.setArguments(bundle);
    dialog.show(getChildFragmentManager(),null);
  }
}


