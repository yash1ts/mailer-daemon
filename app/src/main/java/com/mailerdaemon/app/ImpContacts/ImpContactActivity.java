package com.mailerdaemon.app.ImpContacts;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.google.gson.Gson;
import com.mailerdaemon.app.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImpContactActivity extends AppCompatActivity {

    @BindView(R.id.bt_faculty)
    View bt_faculty;
    @BindView(R.id.bt_admin)
    View bt_admin;
    @BindView(R.id.bt_hostel)
    View bt_hostel;
    @BindView(R.id.bt_senate)
    View bt_senate;

    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactRecyclerAdapter adapter;
    CardView calander;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imp_contacts);
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setResizeAndRotateEnabledForNetwork(true)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);
        ButterKnife.bind(this);
        contactList = new Gson().fromJson(loadJSONFromAsset("contact"), FacultyModel.class).getContact();
        recyclerView = findViewById(R.id.contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactRecyclerAdapter();
        adapter.setData(contactList);
        recyclerView.setAdapter(adapter);

        calander=findViewById(R.id.calander);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bt_faculty.setOnClickListener(v -> {
            String[] tabs = new String[]{"All", "All"};
            String[] pages = new String[]{"faculty_all", "faculty_all"};
            openDetail(tabs, pages);
        });

        bt_admin.setOnClickListener(v -> {
            String[] tabs = new String[]{"Deans", "Associate Deans", "HOD", "HOC"};
            String[] pages = new String[]{"deans", "associate_deans", "hod", "hoc"};
            openDetail(tabs, pages);
        });
        bt_hostel.setOnClickListener(v -> {
            openWebView("file:///android_asset/warden.html");
        });
        bt_senate.setOnClickListener(v->{
            String[] tabs = new String[]{"Deans", "Associate Deans", "HOD", "HOC"};
            String[] pages = new String[]{"deans", "associate_deans", "hod", "hoc"};
            openDetail(tabs, pages);
        });
        calander.setOnClickListener(v->{openWebView("file:///android_asset/calander.html");});
    }

    public void openDetail(String[] tabs, String[] pages) {
        ContactFragmentViewPager fragment = new ContactFragmentViewPager();
        Bundle bundle = new Bundle();
        bundle.putStringArray("tabs", tabs);
        bundle.putStringArray("pages", pages);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_detail, fragment, null).addToBackStack(null).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    public String loadJSONFromAsset(String type) {
        String json = null;
        try {
            InputStream is = getAssets().open(type + ".json");
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

    void openWebView(String url){
        HtmlFragment fragment= new HtmlFragment();
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_detail,fragment, null).addToBackStack(null).commit();
    }

}
