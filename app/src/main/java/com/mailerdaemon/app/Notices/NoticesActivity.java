package com.mailerdaemon.app.Notices;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Source;
import com.google.gson.Gson;
import com.mailerdaemon.app.R;

import java.util.List;
import java.util.Objects;

import Utils.AccessDatabse;
import Utils.DialogOptions;
import Utils.StringRes;

public class NoticesActivity extends AppCompatActivity implements AccessDatabse, DialogOptions {


    private ShimmerFrameLayout shimmerViewContainer;
    private List<DocumentSnapshot> noticeModels;
    private RecyclerView recyclerView;
    private NoticeRecyclerViewAdapter adapter;
    private FloatingActionButton fab_add;
    private Boolean access;
    private SwipeRefreshLayout refresh;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notices");
        access= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Access",false);
        fab_add=findViewById(R.id.fab);
        refresh=findViewById(R.id.refresh);
        if (access){
            fab_add.show();
        }
        shimmerViewContainer = findViewById(R.id.shimmer_view_container);

        recyclerView = findViewById(R.id.rv_notices);
        adapter = new NoticeRecyclerViewAdapter( this,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (noticeModels == null){
            getDatabase();
        }
        else{
            adapter.setData(Lists.reverse(noticeModels));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        fab_add.setOnClickListener(v->{
            AddNoticeFragment fragment=new AddNoticeFragment();
            fragment.show(getSupportFragmentManager(),null);
        });
        refresh.setOnRefreshListener(() -> {
            getDatabase();
            refresh.setRefreshing(false);
        });
    }


  @Override
  public void getDatabase() {
        recyclerView.setVisibility(View.INVISIBLE);
      shimmerViewContainer.setVisibility(View.VISIBLE);
      shimmerViewContainer.startShimmer();
      FirebaseFirestore.getInstance().collection(StringRes.FB_Collec_Notice).orderBy("date", Query.Direction.DESCENDING).limit(15).get(Source.SERVER).addOnCompleteListener(task -> {
      recyclerView.setVisibility(View.VISIBLE);
      if(task.isSuccessful()) {
        noticeModels = Objects.requireNonNull(task.getResult()).getDocuments();
        adapter.setData(noticeModels);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        shimmerViewContainer.stopShimmer();
        shimmerViewContainer.setVisibility(View.GONE);
      }else{
//          Toast.makeText(getContext(),StringRes.No_Internet,Toast.LENGTH_LONG).show();
          FirebaseFirestore.getInstance().collection(StringRes.FB_Collec_Notice).orderBy("date", Query.Direction.DESCENDING).limit(15).get(Source.CACHE).addOnCompleteListener(task2 -> {
              if(task.isSuccessful()) {
                  noticeModels = Objects.requireNonNull(task2.getResult()).getDocuments();
                  adapter.setData(noticeModels);
                  adapter.notifyDataSetChanged();
                  shimmerViewContainer.stopShimmer();
                  shimmerViewContainer.setVisibility(View.GONE);
              }
          });
      }
    });
      recyclerView.scrollToPosition(0);
  }

    @Override
    public void showOptions(NoticeModel model,String path) {
        Bundle bundle=new Bundle();
        bundle.putString("path", path);
        bundle.putString("model", new Gson().toJson(model));
        OptionsFragment optionsFragment=new OptionsFragment();
        optionsFragment.setArguments(bundle);
        optionsFragment.show(getSupportFragmentManager(), null);

    }

    @Override
    public void showDialog(String path) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }
}
