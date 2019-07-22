package com.mailerdaemon.app;

import android.content.Intent;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.mailerdaemon.app.CampusMap.MapsActivity;
import com.mailerdaemon.app.Clubs.ClubDetailBottomSheet;
import com.mailerdaemon.app.Clubs.ClubsFragment;
import com.mailerdaemon.app.Events.AddEventFragment;
import com.mailerdaemon.app.Events.EventsFragment;
import com.mailerdaemon.app.ImpContacts.ImpContactActivity;
import com.mailerdaemon.app.LostAndFound.LostAndFound;
import com.mailerdaemon.app.Notices.AddNoticeFragment;
import com.mailerdaemon.app.Notices.NoticesFragment;

import Utils.AccessDatabse;
import Utils.ChromeTab;
import Utils.StringRes;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;
    private ChromeTab customTab;
    NoticesFragment fragment1;
    EventsFragment fragment2;
    ClubsFragment fragment3;
    private BottomNavigationView bottomNavigationView;
    boolean access;
    int current;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId())
            {

                case R.id.bt_nav_events:
                    setFab("event");
                    if(fragment2==null)
                        fragment2=new EventsFragment();
                    replaceFragment(fragment2,2);
                    return true;
                case R.id.bt_nav_clubs:
                    fab.hide();
                    if(fragment3==null)
                        fragment3=new ClubsFragment();
                    replaceFragment(fragment3,3);
                    return true;
                case R.id.bt_nav_notices:
                    setFab("notice");
                    if(fragment1==null)
                        fragment1=new NoticesFragment();
                    replaceFragment(fragment1,1);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customTab=new ChromeTab(this);
        current=1;
        access=PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("Access",false);
        Log.d("Access", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Name","gfrt"));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        fab=findViewById(R.id.fab);
        fab.hide();

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.bt_nav_notices);
        setFab("Notice");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
          case R.id.action_facebook_page:
              customTab.openTab(StringRes.FB_PAGE);
              break;
          case R.id.action_refresh:
                AccessDatabse fragment=(AccessDatabse)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                fragment.getDatabase();
                break;
            default:  return super.onOptionsItemSelected(item);

        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this, ImpContactActivity.class));

        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(this, MapsActivity.class));

        } else if (id == R.id.nav_tools) {
            startActivity(new Intent(this, LostAndFound.class));

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.logout) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(this, LoginActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void replaceFragment(Fragment fragment,int tag)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();

//        if(tag>current)
//            fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_in_left).commit();
//        else
            fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }


    public void setFab(String str){
        if(access) {
            fab.show();
            final DialogFragment fragment;
            if (str.equals("event")) {
                fragment = new AddEventFragment();
                fragment.setTargetFragment(fragment2,123);
            } else {
                fragment = new AddNoticeFragment();
                fragment.setTargetFragment(fragment1,123);
            }
            fab.setOnClickListener(v -> fragment.show(getSupportFragmentManager(), null));
        }
    }



  public void openDetail(View view){
      Bundle bundle=new Bundle();
      bundle.putString("club_id",view.getTag().toString());
      ClubDetailBottomSheet fragment=new ClubDetailBottomSheet();
      fragment.setArguments(bundle);
      fragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.theme);
      fragment.show(getSupportFragmentManager(),null);

  }

}
