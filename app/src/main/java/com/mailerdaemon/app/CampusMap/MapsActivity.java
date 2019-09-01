package com.mailerdaemon.app.CampusMap;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;
import com.mailerdaemon.app.R;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    LocationCallback mLocationCallback;
    WeakReference<Activity> ref=new WeakReference<Activity>(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Campus Map");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ref.get());
    }
    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       LatLng ism = new LatLng(23.814110, 86.441207);
        LatLng ismGate = new LatLng(23.809163, 86.442590);
        LatLng Jasper = new LatLng(23.817035, 86.440934);
        LatLng Ruby = new LatLng(23.813257, 86.444626);
        mMap.addMarker(new MarkerOptions().position(ism).title("Heritage Building"));
        mMap.addMarker(new MarkerOptions().position(ismGate).title("Main Entrance"));
        mMap.addMarker(new MarkerOptions().position(Jasper).title("Jasper").alpha(0.5f));
        mMap.addMarker(new MarkerOptions().position(Ruby).title("Ruby").alpha(0.5f));
        LatLng sac = new LatLng(23.817462, 86.437456);
        mMap.addMarker(new MarkerOptions().position(sac).title("SAC"));
        LatLng pm = new LatLng(23.814902, 86.441178);
        mMap.addMarker(new MarkerOptions().position(pm).title("PenMan Auditoritum"));
        LatLng hc = new LatLng(23.811926, 86.439028);
        mMap.addMarker(new MarkerOptions().position(hc).title("Health Centre"));


        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(23.821271, 86.435213),
                        new LatLng(23.819827, 86.434614),
                        new LatLng(23.818309, 86.436635),
                        new LatLng(23.817824, 86.436289),
                        new LatLng(23.815959, 86.439142),
                        new LatLng(23.811823, 86.436986),
                        new LatLng(23.810356, 86.437202),
                        new LatLng(23.809208, 86.441401),
                        new LatLng(23.808920, 86.442469),
                        new LatLng(23.811918, 86.444478),
                        new LatLng(23.811966, 86.447407),
                        new LatLng(23.814787, 86.447845),
                        new LatLng(23.816345, 86.442538),
                        new LatLng(23.817181, 86.442852),
                        new LatLng(23.818064, 86.440134),
                        new LatLng(23.819137, 86.440809),
                        new LatLng(23.819931, 86.439832),
                        new LatLng(23.818626, 86.438828),
                        new LatLng(23.821271, 86.435213)
                ));

        stylePolyline(polyline1);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(ism));
        CameraUpdate update=CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(23.809756, 86.433533),new LatLng(23.820778, 86.449679)),0);
        mMap.moveCamera(update);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000); // two minute interval
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setSmallestDisplacement(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                List<Location> locationList = locationResult.getLocations();
                if (locationList.size() > 0) {
                    //The last location in the list is the newest
                    Location location = locationList.get(locationList.size() - 1);
                    Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                    mLastLocation = location;
                    boolean inside = PolyUtil.containsLocation(mLastLocation.getLongitude(),mLastLocation.getLatitude(), Arrays.asList(new LatLng(23.821271, 86.435213),
                            new LatLng(23.819827, 86.434614),
                            new LatLng(23.818309, 86.436635),
                            new LatLng(23.817824, 86.436289),
                            new LatLng(23.815959, 86.439142),
                            new LatLng(23.811823, 86.436986),
                            new LatLng(23.810356, 86.437202),
                            new LatLng(23.809208, 86.441401),
                            new LatLng(23.808920, 86.442469),
                            new LatLng(23.811918, 86.444478),
                            new LatLng(23.811966, 86.447407),
                            new LatLng(23.814787, 86.447845),
                            new LatLng(23.816345, 86.442538),
                            new LatLng(23.817181, 86.442852),
                            new LatLng(23.818064, 86.440134),
                            new LatLng(23.819137, 86.440809),
                            new LatLng(23.819931, 86.439832),
                            new LatLng(23.818626, 86.438828),
                            new LatLng(23.821271, 86.435213)), true);
                    if(inside==false){
                        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                        Toast.makeText(ref.get(),"You Are not in Location",Toast.LENGTH_LONG).show();
                    }
                    else {

                    if (mCurrLocationMarker != null) {
                        mCurrLocationMarker.remove();
                    }

                    //Place current location marker
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    mCurrLocationMarker = mMap.addMarker(markerOptions);

                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                }}
            }
        };

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }


    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return true;
    }

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 2;

    private void stylePolyline(Polyline polyline) {
        String type = "";
        // Get the data object stored with the polyline.
        if (polyline.getTag() != null) {
            type = polyline.getTag().toString();
        }

       /* switch (type) {
            // If no type is given, allow the API to use the default.
            case "A":
                // Use a custom bitmap as the cap at the start of the line.
                polyline.setStartCap(
                        new CustomCap(
                                BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow), 10));
                break;
            case "B":
                // Use a round cap at the start of the line.
                polyline.setStartCap(new RoundCap());
                break;
        }*/

        polyline.setEndCap(new RoundCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(R.color.map_lines);
        polyline.setJointType(JointType.ROUND);
    }


}
