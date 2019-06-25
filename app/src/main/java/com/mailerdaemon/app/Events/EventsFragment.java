package com.mailerdaemon.app.Events;

import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.Notices.NoticeModel;
import com.mailerdaemon.app.R;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import Utils.StringRes;

public class EventsFragment extends Fragment {
  private ShimmerFrameLayout shimmerViewContainer;
  private RecyclerView recyclerView;
  private EventsParentAdapter adapter;
  private FirebaseFirestore firebaseFirestore;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.fragment_events,container,false);
    shimmerViewContainer=view.findViewById(R.id.shimmer_view_container);
    shimmerViewContainer.startShimmer();

    recyclerView=view.findViewById(R.id.rv_events);
    adapter=new EventsParentAdapter(getContext(),this);
    firebaseFirestore=FirebaseFirestore.getInstance();
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(adapter);

    getDatabase();

    return view;
  }

  private void getDatabase() {

    firebaseFirestore.collection(StringRes.FB_Collec_Event).orderBy("date").get().addOnCompleteListener(task -> {
      if(task.isSuccessful()) {
        List<DocumentSnapshot> snap=task.getResult().getDocuments();
        Log.d("DB",task.getResult().toObjects(EventModel.class).toString());
        adapter.setData(Lists.reverse(snap));
        adapter.notifyDataSetChanged();
        shimmerViewContainer.stopShimmer();
        shimmerViewContainer.setVisibility(View.GONE);
      }else{

      }

    });
  }

  @Override
  public void onResume() {
    super.onResume();
    getDatabase();
  }
}


