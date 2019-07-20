package com.mailerdaemon.app.ImpContacts;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.mailerdaemon.app.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import Utils.SearchObservabel;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ContactDetailFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactRecyclerAdapter adapter;
    SearchView searchView;

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contactList= new Gson().fromJson(loadJSONFromAsset(getArguments().getString("type")), FacultyModel.class).getContact();
        View view=inflater.inflate(R.layout.fragment_contact_detail,container,false);
        recyclerView=view.findViewById(R.id.rv_contact_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new ContactRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        setHasOptionsMenu(true);
        //searchView=view.findViewById(R.id.search_view);
        //getActivity().setActionBar(view.findViewById(R.id.toolbar));


        return view;
    }
    private Observable<List<Contact>> dataFromNetwork(final String query) {
        if(query.isEmpty())
            return Observable.just(contactList);
        else
            return Observable.just(contactList).flatMap(Observable::fromIterable).
                    filter(pojo-> pojo.getName().contains(query))
                    .toList().toObservable();
    }

    private void updateRV(List<Contact> result) {
        DiffUtilCallBack n=new DiffUtilCallBack(adapter.getData(),result);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(n);
        adapter.setData(result);
        diffResult.dispatchUpdatesTo(adapter);
    }

    public String loadJSONFromAsset(String type) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(type+".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.imp_contact_search, menu);
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        menu.findItem(R.id.search).expandActionView();
        searchView.setQueryHint("Search Contacts");
        SearchObservabel.fromView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMap((Function<String, ObservableSource<List<Contact>>>) this::dataFromNetwork)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {updateRV(result);});
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        super.onCreateOptionsMenu(menu,inflater);

    }


}
