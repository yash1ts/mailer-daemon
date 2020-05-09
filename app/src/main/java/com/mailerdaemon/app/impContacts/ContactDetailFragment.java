package com.mailerdaemon.app.impContacts;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.utils.ContactFunction;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactDetailFragment extends Fragment implements ContactFunction {

    private RecyclerView recyclerView;
    private List<Contact> contactList=new ArrayList<>();
    private ContactRecyclerAdapter adapter;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contactList= new Gson().fromJson(loadJSONFromAsset(getArguments().getString("type")), FacultyModel.class).getContact();
        View view=inflater.inflate(R.layout.fragment_contact_detail,container,false);
        recyclerView=view.findViewById(R.id.rv_contact_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new ContactRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setData(contactList);
        adapter.notifyDataSetChanged();
        setHasOptionsMenu(true);

        return view;
    }

    private void updateRV(List<Contact> result) {
        DiffUtilCallBack n=new DiffUtilCallBack(adapter.getData(),result);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(n);
        adapter.setData(result);
        diffResult.dispatchUpdatesTo(adapter);
    }

    private String loadJSONFromAsset(String type) {
        String json ;
        try {
            InputStream is = Objects.requireNonNull(getActivity()).getAssets().open(type+".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.imp_contact_search, menu);
        SearchManager searchManager =
                (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Search Contacts");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerView.scrollToPosition(0);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    updateRV(contactList);
                }else{
                    final List<Contact> list = new ArrayList<>();
                    for(Contact it: contactList){
                        if(it.getName().contains(newText.toLowerCase()))
                            list.add(it);
                    }
                    updateRV(list);
                }
                return true;
            }
        });
        searchView.setOnFocusChangeListener((v, hasFocus) -> {
            InputMethodManager manager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (hasFocus)
                manager.showSoftInput(v.findFocus(),InputMethodManager.RESULT_SHOWN);
        });
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void makeCall(String num) {
        if(!num.trim().equals("0")){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num.trim()));
        startActivity(intent);}
        else{
            Toast.makeText(getContext(),"Sorry number not available",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void sendMail(String s) {
        if(!s.trim().isEmpty()) {
            Intent send = new Intent(Intent.ACTION_SENDTO);
            String uriText = "mailto:" + Uri.encode(s.trim()) +
                    "?subject=" + Uri.encode("Subject") +
                    "&body=" + Uri.encode("the body of the message");
            Uri uri = Uri.parse(uriText);

            send.setData(uri);
            startActivity(Intent.createChooser(send, "Send mail..."));
        }
        else{
            Toast.makeText(getContext(),"Sorry email not available",Toast.LENGTH_LONG).show();
        }
    }
}
