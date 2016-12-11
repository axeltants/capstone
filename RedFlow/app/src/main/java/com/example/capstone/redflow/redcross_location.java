package com.example.capstone.redflow;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class redcross_location extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redcross_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng Red1 = new LatLng(10.363270, 123.950881);
        mMap.addMarker(new MarkerOptions().position(Red1).title("Philippine National Red Cross Cebu"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Red1,11));

        LatLng Red2 = new LatLng(10.310390, 123.948757);
        mMap.addMarker(new MarkerOptions().position(Red2).title("Philippine red Cross-Lapu-Lapu/Cordova chapter"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Red2));

        LatLng Red3 = new LatLng(10.304339, 123.894873);
        mMap.addMarker(new MarkerOptions().position(Red3).title("Philippine Red Cross Eastern Visayas Regional Blood Center"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Red3));

        LatLng Red4 = new LatLng(10.312334, 123.891842);
        mMap.addMarker(new MarkerOptions().position(Red4).title("Philippine Red Cross Cebu chapter"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Red4));

        LatLng Red5 = new LatLng(10.312332, 123.891942);
        mMap.addMarker(new MarkerOptions().position(Red5).title("Eastern Visayas Regional Blood center (Red Cross)"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Red5));
    }
}
