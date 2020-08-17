package com.mailerdaemon.app.events;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.mailerdaemon.app.ConstantsKt;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.ShowData;
import com.mailerdaemon.app.utils.AccessDatabase;
import com.mailerdaemon.app.utils.DialogOptions;

import java.util.List;
import java.util.Objects;

public class EventsActivity extends AppCompatActivity implements AccessDatabase, DialogOptions {
  private ShimmerFrameLayout shimmerViewContainer;
  private EventsParentAdapter adapter;
  private FirebaseFirestore firebaseFirestore;
  private List<DocumentSnapshot> snap;
  private SwipeRefreshLayout refresh;
  private TextView not_events;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event);
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Events");
    shimmerViewContainer=findViewById(R.id.shimmer_view_container);
    shimmerViewContainer.startShimmer();
    not_events=findViewById(R.id.tv_no_event);
    FloatingActionButton fab_add = findViewById(R.id.fab);
    refresh=findViewById(R.id.refresh);

    RecyclerView recyclerView = findViewById(R.id.rv_events);
    Boolean access = getSharedPreferences("GENERAL", Context.MODE_PRIVATE).getBoolean("Access", false);
    if (access){
      fab_add.show();
    }

    adapter=new EventsParentAdapter(this, access,this);
    firebaseFirestore=FirebaseFirestore.getInstance();
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);

    if(snap==null)
    {shimmerViewContainer.setVisibility(View.VISIBLE);
      getDatabase();}
    else {
      adapter.setData(snap);
      adapter.notifyDataSetChanged();
    }
    fab_add.setOnClickListener(v->{
      AddEventFragment fragment=new AddEventFragment();
      fragment.show(getSupportFragmentManager(),null);
    });
    refresh.setOnRefreshListener(()->{
      getDatabase();
      refresh.setRefreshing(false);
    });
  }

  @Override
  public void getDatabase() {

    firebaseFirestore.collection(ConstantsKt.FB_EVENT)
            .orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
      if(task.isSuccessful()) {
        snap= Objects.requireNonNull(task.getResult()).getDocuments();
        if(snap.size()==0)
          not_events.setVisibility(View.VISIBLE);
        else not_events.setVisibility(View.GONE);
        adapter.setData(snap);
        adapter.notifyDataSetChanged();
        shimmerViewContainer.stopShimmer();
        shimmerViewContainer.setVisibility(View.GONE);
      }
    });
  }

  @Override
  public void showOptions(PostModel model, String path) {
    Bundle bundle=new Bundle();
    bundle.putString("model",new Gson().toJson(model));
    bundle.putString("path",path);
    OptionsEventFragment optionsFragment=new OptionsEventFragment();
    optionsFragment.setArguments(bundle);
    optionsFragment.show(getSupportFragmentManager(),null);
  }

  @Override
  public void showDialog(String path) {
    Bundle bundle=new Bundle();
    bundle.putString("path",path);
    AddEventPostFragment dialog=new AddEventPostFragment();
    dialog.setArguments(bundle);
    dialog.show(getSupportFragmentManager(),null);
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == android.R.id.home)
      onBackPressed();
    return true;
  }
}


