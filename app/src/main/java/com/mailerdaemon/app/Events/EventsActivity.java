package com.mailerdaemon.app.Events;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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
import com.google.gson.Gson;
import com.mailerdaemon.app.Notices.NoticeModel;
import com.mailerdaemon.app.R;

import java.util.List;
import java.util.Objects;

import Utils.AccessDatabse;
import Utils.DialogOptions;
import Utils.StringRes;

public class EventsActivity extends AppCompatActivity implements AccessDatabse, DialogOptions {
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
    Boolean access = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Access", false);
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

    firebaseFirestore.collection(StringRes.FB_Collec_Event).orderBy("date", Query.Direction.DESCENDING).get(Source.SERVER).addOnCompleteListener(task -> {
      if(task.isSuccessful()) {
        snap= Objects.requireNonNull(task.getResult()).getDocuments();
        if(snap.size()==0)
          not_events.setVisibility(View.VISIBLE);
        else not_events.setVisibility(View.GONE);
        adapter.setData(snap);
        adapter.notifyDataSetChanged();
        shimmerViewContainer.stopShimmer();
        shimmerViewContainer.setVisibility(View.GONE);
      }else{
        Toast.makeText(this,StringRes.No_Internet,Toast.LENGTH_LONG).show();
        firebaseFirestore.collection(StringRes.FB_Collec_Event).orderBy("date", Query.Direction.DESCENDING).get(Source.CACHE).addOnCompleteListener(task2 -> {
          if(task.isSuccessful()) {
            snap= Objects.requireNonNull(task2.getResult()).getDocuments();
            adapter.setData(snap);
            adapter.notifyDataSetChanged();
            shimmerViewContainer.stopShimmer();
            shimmerViewContainer.setVisibility(View.GONE);
          }else{
            Toast.makeText(this,StringRes.No_Internet,Toast.LENGTH_LONG).show();
          }

        });
      }

    });
  }


  @Override
  public void showOptions(NoticeModel model, String path) {
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


