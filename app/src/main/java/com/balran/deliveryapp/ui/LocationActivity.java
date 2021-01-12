package com.balran.deliveryapp.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.balran.deliveryapp.Common.Constantes;
import com.balran.deliveryapp.Common.SharedPreferencesManager;
import com.balran.deliveryapp.R;
import com.balran.deliveryapp.ui.Home.DashboardActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient locationService;
    private SearchView sv_location;
    private Button btn_saveLocation;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        setTitle("Seleccione su ubicacion:");

        locationService = LocationServices.getFusedLocationProviderClient(this);
        requestPermissions();

        sv_location = findViewById(R.id.sv_location);
        btn_saveLocation = findViewById(R.id.btn_continue);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sv_location.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                address = sv_location.getQuery().toString();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                address = sv_location.getQuery().toString();
                return false;
            }
        });

        btn_saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager.setSomeStringValue(getApplicationContext(), Constantes.PREF_ADDRESS, address);
                Intent i = new Intent(LocationActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(17);

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng mPosition = mMap.getCameraPosition().target;

                address = getAddress(mPosition.latitude, mPosition.longitude);

                sv_location.setQuery(address, false);
                sv_location.clearFocus();
            }
        });
    }


    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Si el permiso es denegado, se solicita el permiso.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constantes.MY_PERMISSION_REQUEST_FINE_LOCATION);
        } else {
            getMyLocation();
        }
    }

    private void getMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Si el permiso es denegado, se solicita el permiso.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constantes.MY_PERMISSION_REQUEST_FINE_LOCATION);
        } else {

            locationService.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location _location) {
                    if (_location != null) {
                        LatLng location = new LatLng(_location.getLatitude(), _location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
                    } else {
                        Toast.makeText(LocationActivity.this, "Error al obtener la ubicacion.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getThoroughfare();
            add = add + "\n" + obj.getSubThoroughfare();

            Log.e("IGA", "Address" + add);

            String addr = obj.getThoroughfare() + " " + obj.getSubThoroughfare() + ", " + obj.getLocality();
            return addr;
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Constantes.MY_PERMISSION_REQUEST_FINE_LOCATION){
            getMyLocation();
        }
    }
}