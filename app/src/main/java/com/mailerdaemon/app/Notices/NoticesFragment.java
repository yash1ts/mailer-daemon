package com.mailerdaemon.app.Notices;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.R;

import java.util.ArrayList;
import java.util.List;

import Utils.StringRes;

public class NoticesFragment extends Fragment {
    public NoticesFragment(){

    }

    private ShimmerFrameLayout shimmerViewContainer;
    private List<DocumentSnapshot> noticeModels=new ArrayList<>();
    private RecyclerView recyclerView;
    private NoticeRecyclerViewAdapter adapter;
    private FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notices,container,false);
        shimmerViewContainer=view.findViewById(R.id.shimmer_view_container);
        shimmerViewContainer.startShimmer();

        recyclerView=view.findViewById(R.id.rv_notices);
        adapter=new NoticeRecyclerViewAdapter(getContext(),getChildFragmentManager());
        firebaseFirestore=FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        getDatabase();

        return view;
    }

  private void getDatabase() {

    firebaseFirestore.collection(StringRes.FB_Collec_Notice).orderBy("date").get().addOnCompleteListener(task -> {
      if(task.isSuccessful()) {
        noticeModels = task.getResult().getDocuments();
        adapter.setData(Lists.reverse(noticeModels));
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
