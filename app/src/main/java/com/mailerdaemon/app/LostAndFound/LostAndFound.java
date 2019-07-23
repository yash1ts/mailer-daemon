package com.mailerdaemon.app.LostAndFound;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.mailerdaemon.app.Notices.NoticeModel;
import com.mailerdaemon.app.Notices.NoticeRecyclerViewAdapter;
import com.mailerdaemon.app.Notices.OptionsFragment;
import com.mailerdaemon.app.R;

import java.util.List;

import Utils.AccessDatabse;
import Utils.DialogOptions;
import Utils.StringRes;

public class LostAndFound extends AppCompatActivity implements AccessDatabse, DialogOptions {
    private ShimmerFrameLayout shimmerViewContainer;
    private List<DocumentSnapshot> noticeModels;
    private RecyclerView recyclerView;
    private NoticeRecyclerViewAdapter adapter;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);
        fab=findViewById(R.id.fab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyclerView = findViewById(R.id.rv_notices);
        int px=Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,48f,getResources().getDisplayMetrics()));
        adapter = new NoticeRecyclerViewAdapter(px, this,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(v->{
            AddLNFPost addLNFPost=new AddLNFPost();
            addLNFPost.show(getSupportFragmentManager(),null);
        });
        getDatabase();
    }

    @Override
    public void getDatabase() {
        shimmerViewContainer.setVisibility(View.VISIBLE);
        shimmerViewContainer.startShimmer();
        FirebaseFirestore.getInstance().collection(StringRes.FB_Lost_Found).orderBy("date").get(Source.SERVER).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                noticeModels = task.getResult().getDocuments();
                adapter.setData(Lists.reverse(noticeModels));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
            }else{
                Toast.makeText(this,StringRes.No_Internet,Toast.LENGTH_LONG).show();
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
