package com.sreekanth.dev.ilahianz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sreekanth.dev.ilahianz.Supports.Supports;

import java.util.HashMap;

import static com.sreekanth.dev.ilahianz.model.Literals.LOCATION_REQUEST;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final float DEFAULT_ZOOM = 15f;
    public static String MARKER_NAME;
    public static LatLng LOCATION;
    public static float ZOOM;
    private GoogleMap mMap;
    private FirebaseUser fuser;
    Location location;
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
        fuser = FirebaseAuth.getInstance().getCurrentUser();
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
                    Snackbar.make(v, "Satellite Map", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {
                    MAP_TYPE = 1;
                    mMap.setMapType(MAP_TYPE);
                    Snackbar.make(v, "Normal Map", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        mMap.setTrafficEnabled(true);
        mMap.addCircle(getCircleOptions(LOCATION));
        mMap.addMarker(getMarkerOption());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                location = mMap.getMyLocation();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
            }
        } else {
            mMap.setMyLocationEnabled(true);
            location = mMap.getMyLocation();
        }
        mMap.setBuildingsEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LOCATION, ZOOM));
    }

    private CircleOptions getCircleOptions(LatLng location) {
        CircleOptions options = new CircleOptions();
        options.center(location);
        options.fillColor(getResources().getColor(R.color.tblue));
        options.radius(300);
        options.strokeColor(getResources().getColor(R.color.darkBlue));
        options.strokeWidth(1);
        return options;
    }


    private MarkerOptions getMarkerOption() {

        MarkerOptions options = new MarkerOptions();

        options.title(MARKER_NAME);
        options.alpha(5);
        options.position(LOCATION);
        options.icon(getIcon());
        options.alpha(1);

        return options;
    }

    private BitmapDescriptor getIcon() {

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUEST && resultCode == RESULT_OK) {
            onMapReady(mMap);
        }
    }

    private void status(String status, String last_seen) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users").child(fuser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        hashMap.put("lastSeen", last_seen);
        reference.updateChildren(hashMap);

    }

    private void updateLocation(String latitude, String longitude) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users").child(fuser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Latitude", latitude);
        hashMap.put("Longitude", longitude);
        reference.updateChildren(hashMap);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!Supports.Connected(this)) {
            updateLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
