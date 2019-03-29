package com.infisoln.siddhant.maps_demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    LocationManager lm;
    LatLng currentPos;
    GoogleMap map;
    int count = 0;
    LatLng lastKnowLocPos;
    public static final String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        map = googleMap;

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            startLocationUpdates(lm);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    12345);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("MissingPermission")
    void startLocationUpdates(LocationManager lm) {
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                3,
                this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(Location location) {

        Location lastKnownLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        currentPos = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

        currentPos = new LatLng(location.getLatitude(), location.getLongitude());
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 10));
        map.addMarker(new MarkerOptions().position(currentPos).title("My Loc"));

        if (count != 0) {
            map.addPolyline(new PolylineOptions().add(currentPos, lastKnowLocPos));
        }

        lastKnowLocPos = currentPos;
        count++;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
