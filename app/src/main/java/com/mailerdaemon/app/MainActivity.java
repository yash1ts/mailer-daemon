package com.mailerdaemon.app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.mailerdaemon.app.Clubs.ClubDetailBottomSheet;
import com.mailerdaemon.app.Clubs.ClubsFragment;
import com.mailerdaemon.app.Events.AddEventFragment;
import com.mailerdaemon.app.Events.EventsFragment;
import com.mailerdaemon.app.Notices.AddNoticeFragment;
import com.mailerdaemon.app.Notices.NoticesFragment;

import Utils.ChromeTab;
import Utils.StringRes;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;
    private ChromeTab customTab;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId())
            {

                case R.id.bt_nav_events:
                    setFab("event");
                    replaceFragment(new EventsFragment());
                    return true;
                case R.id.bt_nav_clubs:
                    fab.hide();
                    replaceFragment(new ClubsFragment());
                    return true;
                case R.id.bt_nav_notices:
                  setFab("notice");
                    replaceFragment(new NoticesFragment());
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

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
            .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
            .setResizeAndRotateEnabledForNetwork(true)
            .setDownsampleEnabled(true)
            .build();
        Fresco.initialize(this,config);

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

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        addFragment(new NoticesFragment());
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
          case R.id.action_settings:
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(this, LoginActivity.class));
            break;
          case R.id.action_facebook_page:
              customTab.openTab(StringRes.FB_PAGE);
              break;
          case R.id.action_refresh:
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= 26) {
              transaction.setReorderingAllowed(false);
            }
            Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            transaction.detach(fragment).attach(fragment).commit();
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

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void addFragment(Fragment fragment)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit();
    }

    public void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out).replace(R.id.fragment_container,fragment).commit();
    }

    public void setFab(String str){
      fab.show();
      final DialogFragment fragment;
      if(str.equals("event")) {
       fragment=new AddEventFragment();
      } else {
      fragment=new AddNoticeFragment();
      }
      fab.setOnClickListener(v -> fragment.show(getSupportFragmentManager(),null));

    }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Fresco.shutDown();
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
