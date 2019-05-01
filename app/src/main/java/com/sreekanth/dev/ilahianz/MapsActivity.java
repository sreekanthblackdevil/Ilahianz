package com.sreekanth.dev.ilahianz;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final float DEFAULT_ZOOM = 15f;
    public static String MARKER_NAME;
    public static LatLng LOCATION;
    public static float ZOOM;
    private GoogleMap mMap;
    private int MAP_TYPE = 1;

    public static float getZOOM() {
        return ZOOM;
    }

    public static void setZOOM(float ZOOM) {
        MapsActivity.ZOOM = ZOOM;
    }

    public static LatLng getLOCATION() {
        return LOCATION;
    }

    public static void setLOCATION(LatLng location, String marker) {
        LOCATION = location;
        MARKER_NAME = marker;
        ZOOM = DEFAULT_ZOOM;
    }

    public static void setLOCATION(LatLng location, String marker, float zoom) {
        LOCATION = location;
        MARKER_NAME = marker;
        ZOOM = zoom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        final ImageView map_type_btn = findViewById(R.id.maptype_btn);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        map_type_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MAP_TYPE == 1) {
                    MAP_TYPE = 2;
                    mMap.setMapType(MAP_TYPE);
                    Toast.makeText(MapsActivity.this, "Satellite Map", Toast.LENGTH_SHORT).show();
                } else {
                    MAP_TYPE = 1;
                    mMap.setMapType(MAP_TYPE);
                    Toast.makeText(MapsActivity.this, "Normal Map", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        mMap.setTrafficEnabled(true);
        mMap.addMarker(getMarkerOption());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOCATION, ZOOM));
    }


    private MarkerOptions getMarkerOption() {

        MarkerOptions options = new MarkerOptions();

        options.title(MARKER_NAME);
        options.position(LOCATION);
        options.icon(getIcon());

        return options;
    }

    private BitmapDescriptor getIcon() {

        return null;
    }
}
