package com.mailerdaemon.app.ImpContacts;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.google.gson.Gson;
import com.mailerdaemon.app.R;


import Utils.ContactFunction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ImpContactActivity extends AppCompatActivity implements ContactFunction {

    @BindView(R.id.bt_faculty)
    View bt_faculty;
    @BindView(R.id.bt_admin)
    View bt_admin;
    @BindView(R.id.bt_hostel)
    View bt_hostel;
    @BindView(R.id.bt_senate)
    View bt_senate;

    CardView calander;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imp_contacts);
        ButterKnife.bind(this);


        calander=findViewById(R.id.calander);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contacts");

        ContactDetailFragment fragment=new ContactDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type","contact");
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.rv_contacts, fragment, null).commit();

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


    void openWebView(String url){
        HtmlFragment fragment= new HtmlFragment();
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_detail,fragment, null).addToBackStack(null).commit();
    }

    @Override
    public void makeCall(String s) {

    }

    @Override
    public void sendMail(String s) {

    }
}
