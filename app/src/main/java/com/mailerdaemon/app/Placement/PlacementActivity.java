package com.mailerdaemon.app.Placement;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mailerdaemon.app.R;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import Utils.AccessDatabse;
import Utils.SearchObservabel;
import Utils.StringRes;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PlacementActivity extends AppCompatActivity implements  AccessDatabse {

    private ShimmerFrameLayout shimmerViewContainer;
    private List<DocumentSnapshot> models;
    private RecyclerView recyclerView;
    private PlacementRecyclerViewAdapter adapter;
    private FloatingActionButton fab_add;
    private Boolean access;
    private SwipeRefreshLayout refresh;
    private SearchView searchView;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Placement Daemon");
        access= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Access",false);
        fab_add=findViewById(R.id.fab);
        refresh=findViewById(R.id.refresh);
        if (access){
            fab_add.show();
        }
        shimmerViewContainer = findViewById(R.id.shimmer_view_container);

        recyclerView = findViewById(R.id.rv_notices);
        adapter = new PlacementRecyclerViewAdapter( this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (models == null){
            getDatabase();
        }
        else{
            adapter.setData(Lists.reverse(models));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        fab_add.setOnClickListener(v->{
            AddPlacementFragment fragment=new AddPlacementFragment();
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
        FirebaseFirestore.getInstance().collection(StringRes.FB_Collec_PLACEMENT).orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            recyclerView.setVisibility(View.VISIBLE);
            if(task.isSuccessful()) {
                models = Objects.requireNonNull(task.getResult()).getDocuments();
                Log.d("MODEL",task.getResult().toObjects(PlacementModel.class).toString());
                adapter.setData(models);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
            }
        });
        recyclerView.scrollToPosition(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.imp_contact_search,menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Search");
        SearchObservabel.fromView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap((Function<String, ObservableSource<List<DocumentSnapshot>>>) this::dataFromNetwork)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateRV);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return true;
    }
    private Observable<List<DocumentSnapshot>> dataFromNetwork(final String query) {
        if(query.isEmpty())
            return Observable.just(models);
        else
            return Observable.just(models).flatMap(Observable::fromIterable).
                    filter(pojo->{
                        return pojo.toObject(PlacementModel.class).getData().toLowerCase().contains(query.toLowerCase());
                    }
                    )
                    .toList().toObservable();
    }

    private void updateRV(List<DocumentSnapshot> result) {
        DiffUtilCallback n=new DiffUtilCallback(adapter.getData(),result);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(n);
        adapter.setData(result);
        diffResult.dispatchUpdatesTo(adapter);
    }
}
