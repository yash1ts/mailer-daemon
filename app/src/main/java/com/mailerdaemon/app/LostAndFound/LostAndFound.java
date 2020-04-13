package com.mailerdaemon.app.LostAndFound;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Utils.AccessDatabse;
import Utils.StringRes;

public class LostAndFound extends AppCompatActivity implements AccessDatabse {
    private ShimmerFrameLayout shimmerViewContainer;
    private List<DocumentSnapshot> noticeModels;
    private RecyclerView recyclerView;
    private LNFAdapter adapter;
    private SwipeRefreshLayout refresh;
    private TextView no_posts;
    private CardView rejected;
    private View rejectedClose;
    private boolean access;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);
        FloatingActionButton fab = findViewById(R.id.fab);
        no_posts=findViewById(R.id.tv_no_post);
        access= PreferenceManager.getDefaultSharedPreferences(this).getBoolean("Access",false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lost And Found");
        rejected=findViewById(R.id.card_rejected);
        rejectedClose=findViewById(R.id.rejected_close);
        shimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyclerView = findViewById(R.id.rv_notices);
        adapter = new LNFAdapter(this);
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
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection("user").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            UserModel model= Objects.requireNonNull(task.getResult()).toObject(UserModel.class);
                if(model!=null)
                if (model.getRejectedPost()) {
                rejected.setVisibility(View.VISIBLE);
                rejectedClose.setOnClickListener(v -> {
                    model.setRejectedPost(false);
                    rejected.setVisibility(View.GONE);
                    firestore.collection("user").document(FirebaseAuth.getInstance().getUid()).set(model);
                });
            }
            }
        });
        firestore.collection(StringRes.FB_Lost_Found).orderBy("date", Query.Direction.DESCENDING).limit(20).get().addOnCompleteListener(task -> {
            recyclerView.setVisibility(View.VISIBLE);
            if(task.isSuccessful()) {
                noticeModels = Objects.requireNonNull(task.getResult()).getDocuments();
                List<LostAndFoundModel> lnfmodel=task.getResult().toObjects(LostAndFoundModel.class);
                List<DocumentSnapshot> snapshots=new ArrayList<>();
                if(!access){
                    for(int i=0;i<lnfmodel.size();i++){
                        if(lnfmodel.get(i).getVerified())
                            snapshots.add(noticeModels.get(i));
                    }
                    adapter.setData(snapshots);
                }else
                adapter.setData(noticeModels);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
                if (adapter.getItemCount()==0)
                    no_posts.setVisibility(View.VISIBLE);
                else no_posts.setVisibility(View.GONE);
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


}
