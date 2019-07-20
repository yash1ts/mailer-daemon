package com.mailerdaemon.app.Notices;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.mailerdaemon.app.R;

import java.util.List;

import Utils.AccessDatabse;
import Utils.StringRes;

public class NoticesFragment extends Fragment implements AccessDatabse {
    public NoticesFragment(){

    }

    private ShimmerFrameLayout shimmerViewContainer;
    private List<DocumentSnapshot> noticeModels;
    private RecyclerView recyclerView;
    private NoticeRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notices, container, false);
        shimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        recyclerView = view.findViewById(R.id.rv_notices);
        int px=Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,48f,getResources().getDisplayMetrics()));
        adapter = new NoticeRecyclerViewAdapter(px, getChildFragmentManager());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (noticeModels == null){
            getDatabase();
        }
        else{
            adapter.setData(Lists.reverse(noticeModels));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        return view;
    }

  @Override
  public void getDatabase() {
      shimmerViewContainer.setVisibility(View.VISIBLE);
      shimmerViewContainer.startShimmer();
      FirebaseFirestore.getInstance().collection(StringRes.FB_Collec_Notice).orderBy("date").get(Source.SERVER).addOnCompleteListener(task -> {
      if(task.isSuccessful()) {
        noticeModels = task.getResult().getDocuments();
        adapter.setData(Lists.reverse(noticeModels));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        shimmerViewContainer.stopShimmer();
        shimmerViewContainer.setVisibility(View.GONE);
      }else{
          Toast.makeText(getContext(),StringRes.No_Internet,Toast.LENGTH_LONG).show();
          FirebaseFirestore.getInstance().collection(StringRes.FB_Collec_Notice).orderBy("date").get(Source.CACHE).addOnCompleteListener(task2 -> {
              if(task.isSuccessful()) {
                  noticeModels = task2.getResult().getDocuments();
                  adapter.setData(Lists.reverse(noticeModels));
                  adapter.notifyDataSetChanged();
                  shimmerViewContainer.stopShimmer();
                  shimmerViewContainer.setVisibility(View.GONE);
              }
          });
      }
    });
      recyclerView.scrollToPosition(0);
  }

}
