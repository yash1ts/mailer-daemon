package com.mailerdaemon.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.card.MaterialCardView;
import com.mailerdaemon.app.utils.ChromeTab;

import java.util.Objects;

public class ContactUsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ImageView bt_fb,bt_insta,bt_email_md;
        MaterialCardView formLink=findViewById(R.id.google_form);
        MaterialCardView privacyPolicy=findViewById(R.id.privacy_policy);
        bt_fb=findViewById(R.id.contact_fb);
        bt_insta=findViewById(R.id.contact_insta);
        bt_email_md=findViewById(R.id.contact_email2);
        bt_email_md.setOnClickListener(v->{
            Intent send = new Intent(Intent.ACTION_SENDTO);
            String uriText = "mailto:" + "mailerdism@gmail.com" +
                    "?subject=" + Uri.encode("Subject") +
                    "&body=" + Uri.encode("the body of the message");
            Uri uri = Uri.parse(uriText);

            send.setData(uri);
            startActivity(Intent.createChooser(send, "Send mail..."));
        });
        bt_fb.setOnClickListener(v->{
            Uri uri = Uri.parse("http://facebook.com/MDiitism/");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

            likeIng.setPackage("com.facebook.katana");

            try {
                startActivity(likeIng);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        uri));
            }

        });
        bt_insta.setOnClickListener(v->{
            Uri uri = Uri.parse("http://instagram.com/md_iit_dhanbad/");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

            likeIng.setPackage("com.instagram.android");

            try {
                startActivity(likeIng);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        uri));
            }
        });

        formLink.setOnClickListener(v->{
            ChromeTab tab=new ChromeTab(this);
            tab.openTab("https://docs.google.com/forms/d/e/1FAIpQLScrYmD4pCmBc35QkkebFC9AC-HT45t-r5SAzsYja6-TleSVAQ/viewform?usp=sf_link");
        });
        privacyPolicy.setOnClickListener(v->{
            ChromeTab tab=new ChromeTab(this);
            tab.openTab("https://drive.google.com/file/d/1RqZjZB8q-q-Wo0HAo0HgQzo7d3AY1_Cb/view?usp=sharing");
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;

    }
}
