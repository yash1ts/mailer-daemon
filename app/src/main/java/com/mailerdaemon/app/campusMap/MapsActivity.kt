package com.mailerdaemon.app.campusMap

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mailerdaemon.app.R
import com.mailerdaemon.app.campusMap.MapsActivity
import java.lang.ref.WeakReference
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val COLOR_BLACK_ARGB = -0x1000000
        private const val POLYLINE_STROKE_WIDTH_PX = 2
    
    var mLocationRequest: LocationRequest? = null
    var mLastLocation: Location? = null
    var mCurrLocationMarker: Marker? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var mLocationCallback: LocationCallback? = null
    var ref = WeakReference<Activity>(this)
    private var mMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        Objects.requireNonNull(supportActionBar)?.setDisplayShowHomeEnabled(true)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = (supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?)!!
        mapFragment.getMapAsync(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Campus Map"
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ref.get()!!)
    }

    public override fun onPause() {
        super.onPause()

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val ism = LatLng(23.814110, 86.441207)
        val ismGate = LatLng(23.809163, 86.442590)
        val Jasper = LatLng(23.817035, 86.440934)
        val Ruby = LatLng(23.813257, 86.444626)
        mMap!!.addMarker(MarkerOptions().position(ism).title("Heritage Building"))
        mMap!!.addMarker(MarkerOptions().position(ismGate).title("Main Entrance"))
        mMap!!.addMarker(MarkerOptions().position(Jasper).title("Jasper").alpha(0.5f))
        mMap!!.addMarker(MarkerOptions().position(Ruby).title("Ruby").alpha(0.5f))
        val sac = LatLng(23.817462, 86.437456)
        mMap!!.addMarker(MarkerOptions().position(sac).title("SAC"))
        val pm = LatLng(23.814902, 86.441178)
        mMap!!.addMarker(MarkerOptions().position(pm).title("PenMan Auditoritum"))
        val hc = LatLng(23.811926, 86.439028)
        mMap!!.addMarker(MarkerOptions().position(hc).title("Health Centre"))
        val polyline1 = googleMap.addPolyline(PolylineOptions()
                .clickable(true)
                .add(
                        LatLng(23.821271, 86.435213),
                        LatLng(23.819827, 86.434614),
                        LatLng(23.818309, 86.436635),
                        LatLng(23.817824, 86.436289),
                        LatLng(23.815959, 86.439142),
                        LatLng(23.811823, 86.436986),
                        LatLng(23.810356, 86.437202),
                        LatLng(23.809208, 86.441401),
                        LatLng(23.808920, 86.442469),
                        LatLng(23.811918, 86.444478),
                        LatLng(23.811966, 86.447407),
                        LatLng(23.814787, 86.447845),
                        LatLng(23.816345, 86.442538),
                        LatLng(23.817181, 86.442852),
                        LatLng(23.818064, 86.440134),
                        LatLng(23.819137, 86.440809),
                        LatLng(23.819931, 86.439832),
                        LatLng(23.818626, 86.438828),
                        LatLng(23.821271, 86.435213)
                ))
        stylePolyline(polyline1)
        val width1 = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = (width1 * 0.12).toInt() // offset from edges of the map 12% of screen
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(ism))
        val update = CameraUpdateFactory.newLatLngBounds(LatLngBounds(LatLng(23.809756, 86.433533), LatLng(23.820778, 86.449679)), width1, height, padding)
        mMap!!.moveCamera(update)
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 60000 // two minute interval
        mLocationRequest!!.fastestInterval = 60000
        mLocationRequest!!.smallestDisplacement = 10f
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                var locationList: List<Location> = ArrayList()
                if (locationResult != null) locationList = locationResult.locations
                if (locationList.size > 0) {
                    //The last location in the list is the newest
                    val location = locationList[locationList.size - 1]
                    mLastLocation = location
                    
                    if (mCurrLocationMarker != null) {
                        mCurrLocationMarker!!.remove()
                    }

                    //Place current location marker
                    val latLng = LatLng(location.latitude, location.longitude)
                    val markerOptions = MarkerOptions()
                    markerOptions.position(latLng)
                    markerOptions.title("Current Position")
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                    mCurrLocationMarker = mMap!!.addMarker(markerOptions)

                    //move map camera
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                mMap!!.isMyLocationEnabled = true
            } else {
                //Request Location Permission
                checkLocationPermission()
            }
        } else {
            mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
            mMap!!.isMyLocationEnabled = true
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK") { dialogInterface: DialogInterface?, i: Int ->
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(this@MapsActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    MY_PERMISSIONS_REQUEST_LOCATION)
                        }
                        .create()
                        .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) { // If request is cancelled, the result arrays are empty.
            if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                    mMap!!.isMyLocationEnabled = true
                }
            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return true
    }

    private fun stylePolyline(polyline: Polyline) {

        polyline.endCap = RoundCap()
        polyline.width = POLYLINE_STROKE_WIDTH_PX.toFloat()
        polyline.color = R.color.map_lines
        polyline.jointType = JointType.ROUND
    }
    
}
