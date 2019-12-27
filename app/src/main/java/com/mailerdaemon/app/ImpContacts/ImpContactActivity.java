package com.mailerdaemon.app.ImpContacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mailerdaemon.app.R;

import java.util.Objects;

import Utils.ContactFunction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ImpContactActivity extends AppCompatActivity implements ContactFunction {

    @BindView(R.id.bt_faculty)
    LinearLayout bt_faculty;
    @BindView(R.id.bt_admin)
    LinearLayout bt_admin;
    @BindView(R.id.bt_hostel)
    LinearLayout bt_hostel;
    @BindView(R.id.bt_senate)
    LinearLayout bt_senate;
    @BindView(R.id.more)
    LinearLayout bt_more;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imp_contacts);
        ButterKnife.bind(this);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contacts");

        bt_faculty.setOnClickListener(v -> {
            String[] tabs = new String[]{"All", "ACH","AGL","AGP","AM","AP","CHE","CIV","CSE","ECE","EE","ESE","FME","HSS","ME","MECH","MME","MS","PE"};
            String[] pages = new String[]{"faculty_all","ACH","AGL","AGP","AM","AP","CHE","CIV","CSE","ECE","EE","ESE","FME","HSS","ME","MECH","MME","MS","PE"};
            openDetail(tabs, pages);
        });

        bt_admin.setOnClickListener(v -> {
            String[] tabs = new String[]{"Deans", "Associate Deans", "HOD", "HOC"};
            String[] pages = new String[]{"deans", "associate_deans", "hod", "hoc"};
            openDetail(tabs, pages);
        });
        bt_hostel.setOnClickListener(v -> openWebView("file:///android_asset/warden.html"));
        bt_senate.setOnClickListener(v->{

        });
        bt_more.setOnClickListener(v->{

        });
    }

    public void openDetail(String[] tabs, String[] pages) {
        ContactFragmentViewPager fragment = new ContactFragmentViewPager();
        Bundle bundle = new Bundle();
        bundle.putStringArray("tabs", tabs);
        bundle.putStringArray("pages", pages);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, null).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;

    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getFragments().isEmpty())
        super.onBackPressed();
        else getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().getFragments().get(0)).commit();
    }

    void openWebView(String url){
        HtmlFragment fragment= new HtmlFragment();
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragment, null).addToBackStack(null).commit();
    }

    @Override
    public void makeCall(String num) {
        if(!num.trim().equals("0")){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num.trim()));
            startActivity(intent);}
        else{
            Toast.makeText(this,"Sorry number not available",Toast.LENGTH_LONG).show();
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
            Toast.makeText(this,"Sorry email not available",Toast.LENGTH_LONG).show();
        }
    }
}
