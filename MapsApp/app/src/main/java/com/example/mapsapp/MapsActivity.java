package com.example.mapsapp;

// String Format types: street_number + route + locality + county + state + country + zipcode

import android.content.Intent;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.widget.TextView;


import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        Intent intent = getIntent();
        String input = intent.getStringExtra(inputActivity.EXTRA_Message);

        // Add a marker at UT Tower and move the camera
        //LatLng utTower = new LatLng(30.286135, -97.739366);
        //mMap.addMarker(new MarkerOptions().position(utTower).title("UT Tower"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(utTower));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(utTower, 16));

        try{
            LatLng coordinates = decodeAddress(this.getApplicationContext(), input);
            mMap.addMarker(new MarkerOptions().position(coordinates)); // edit this for .getAddressLine
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Helper function called by onMapReady that decodes the address input by user in order to
     * obtain the coordinates (latitude, longitude) of the input.
     */
    public LatLng decodeAddress(Context context, String input){

        Geocoder geocoder = new Geocoder(context);
        List<Address> list_of_addresses; // will really only ever be one address present
        LatLng coordinates = null;

        // find the latitude and longitude from address
        try{
            list_of_addresses = geocoder.getFromLocationName(input, 5);

            if(list_of_addresses == null){ // if somehow there wasn't an address typed in
                return null;
            }

            Address address = list_of_addresses.get(0); // get the address that was typed in

            // find the coordinates
            address.getLatitude();
            address.getLongitude();

            // create a coordinate object
            coordinates = new LatLng(address.getLatitude(), address.getLongitude());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return coordinates;
    }
}
