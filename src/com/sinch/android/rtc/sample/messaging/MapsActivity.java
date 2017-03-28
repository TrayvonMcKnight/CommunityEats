package com.sinch.android.rtc.sample.messaging;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private String name;

    private Marker GUM;
    private Marker AT;
    private Marker UNCG;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            name = extras.getString("name");
        }

        if (mMap != null) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }


            mMap.setMyLocationEnabled(true);

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
        LatLng Greensboro = new LatLng(36.0726, -79.7920);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Greensboro));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Greensboro, 10));
        char nameArray[] = name.toCharArray();
        if(nameArray[0] == 'D' || nameArray[0] == 'd'){
            //Add marker on Greensboro Urban Ministry

            GUM = mMap.addMarker(new MarkerOptions().position(new LatLng(36.0639848, -79.7942711)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title("Recipient: Greensboro Urban Ministry"));
        }

        if(nameArray[0] == 'R' || nameArray[0] == 'r'){
            //Add a marker on A&T
            AT = mMap.addMarker(new MarkerOptions().position(new LatLng(36.0723, -79.7745))
                    .title("Donor: NC A&T"));

            //Add marker on UNCG
            UNCG = mMap.addMarker(new MarkerOptions().position(new LatLng(36.0663, -79.8063))
                    .title("Donor: UNCG"));
        }

        // Add a marker in Greensboro and move the camera
        //LatLng Greensboro = new LatLng(36.0726, -79.7920);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Greensboro));







        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            String location = "geo:37.7749,-122.4194";
            @Override
            public boolean onMarkerClick(Marker marker) {

                if(marker.equals(AT)){
                    name = "Donor: NC A&T";
                    location = "google.navigation:q=North+Carolina+A&T+State+University";
                }
                if(marker.equals(UNCG)){
                    name = "Donor: UNCG";
                    location = "google.navigation:q=University+Of+North+Carolina+at+Greensboro";
                }
                if(marker.equals(GUM)){
                    name = "Recipient: Greensboro Urban Ministry";
                    location = "google.navigation:q=Greensboro+Urban+Ministry";
                }

                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("Select Option")
                        .setMessage(name)
                        .setPositiveButton("MESSAGE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MapsActivity.this, MessagingActivity.class);
                                intent.putExtra("name", name);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("NAVIGATE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Uri gmmIntentUri = Uri.parse(location);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
            }
        });



    }


}
