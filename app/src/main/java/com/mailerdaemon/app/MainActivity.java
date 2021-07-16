package com.mailerdaemon.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mailerdaemon.app.attendance.AttendanceFragment;
import com.mailerdaemon.app.clubs.ClubsFragment;
import com.mailerdaemon.app.events.EventsActivity;
import com.mailerdaemon.app.home.HomeFragment;
import com.mailerdaemon.app.lostAndFound.LostAndFound;
import com.mailerdaemon.app.notices.NoticesActivity;
import com.mailerdaemon.app.utils.ChromeTab;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    AttendanceFragment fragment1;
    HomeFragment fragment2;
    ClubsFragment fragment3;
    boolean access;
    int current;
    @BindView(R.id.fab_notices)
    FloatingActionButton notices;
    @BindView(R.id.fab_events)
    FloatingActionButton events;
    @BindView(R.id.fab_lost_found)
    FloatingActionButton lost_found;
    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId())
            {

                case R.id.bt_nav_home:
                    if(fragment2==null)
                        fragment2=new HomeFragment();
                    replaceFragment(fragment2,2);
                    return true;
                case R.id.bt_nav_clubs:
                    if(fragment3==null)
                        fragment3=new ClubsFragment();
                    replaceFragment(fragment3,3);
                    return true;
                case R.id.bt_nav_attendance:
                    if(fragment1==null)
                        fragment1=new AttendanceFragment();
                    replaceFragment(fragment1,1);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        ChromeTab customTab = new ChromeTab(this);
        current=1;
        ButterKnife.bind(this);
        access=getSharedPreferences("GENERAL", Context.MODE_PRIVATE).getBoolean("Access",false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.bt_nav_home);
        notices.setOnClickListener(v-> startActivity(new Intent(this, NoticesActivity.class)));
        events.setOnClickListener(v-> startActivity(new Intent(this, EventsActivity.class)));
        lost_found.setOnClickListener(v-> startActivity(new Intent(this, LostAndFound.class)));
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
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
            case R.id.action_settings:
                startActivity(new Intent(this,SettingsActivity.class));
                break;
            default:  return super.onOptionsItemSelected(item);

        }
        return true;
    }


    public void replaceFragment(Fragment fragment,int tag)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }

}
