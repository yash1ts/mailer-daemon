package com.mailerdaemon.app.LostAndFound;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Source;
import com.mailerdaemon.app.Notices.NoticeModel;
import com.mailerdaemon.app.Notices.OptionsFragment;
import com.mailerdaemon.app.R;

import java.util.List;
import java.util.Objects;

import Utils.AccessDatabse;
import Utils.DialogOptions;
import Utils.StringRes;

public class LostAndFound extends AppCompatActivity implements AccessDatabse, DialogOptions {
    private ShimmerFrameLayout shimmerViewContainer;
    private List<DocumentSnapshot> noticeModels;
    private RecyclerView recyclerView;
    private LNFAdapter adapter;
    private SwipeRefreshLayout refresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);
        FloatingActionButton fab = findViewById(R.id.fab);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lost And Found");
        shimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyclerView = findViewById(R.id.rv_notices);
        adapter = new LNFAdapter(this,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(v->{
            AddLNFPost addLNFPost=new AddLNFPost();
            addLNFPost.show(getSupportFragmentManager(),null);
        });
        refresh=findViewById(R.id.refresh);
        refresh.setOnRefreshListener(() -> {
            getDatabase();
            refresh.setRefreshing(false);
            recyclerView.setVisibility(View.INVISIBLE);
        });
        getDatabase();
    }

    @Override
    public void getDatabase() {
        shimmerViewContainer.setVisibility(View.VISIBLE);
        shimmerViewContainer.startShimmer();
        FirebaseFirestore.getInstance().collection(StringRes.FB_Lost_Found).orderBy("date", Query.Direction.DESCENDING).limit(15).get(Source.SERVER).addOnCompleteListener(task -> {
            recyclerView.setVisibility(View.VISIBLE);
            if(task.isSuccessful()) {
                noticeModels = Objects.requireNonNull(task.getResult()).getDocuments();
                adapter.setData(noticeModels);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
            }else{
                Toast.makeText(this,StringRes.No_Internet,Toast.LENGTH_LONG).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return true;
    }

    @Override
    public void showOptions(NoticeModel model, String path) {
        Bundle bundle=new Bundle();
        bundle.putString("path", path);
        bundle.putParcelable("model",model);
        OptionsFragment optionsFragment=new OptionsFragment();
        optionsFragment.setArguments(bundle);
        optionsFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void showDialog(String path) {

    }
}
