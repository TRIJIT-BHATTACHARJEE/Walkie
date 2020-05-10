package com.reg.user.regis;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;


public class Find2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EditText et;
    private FusedLocationProviderClient mFusedLocationClient;


    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment.getMapAsync(this);

        final TextView locUpdate= (TextView)findViewById(R.id.locupdate);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        //        return;
        //     }

        // mMap.setMyLocationEnabled(true);

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    SharedPreferences sharedPrefs = getSharedPreferences("sharedprefs", 0);
                    String vehicle_id= sharedPrefs.getString("veh_id_dt", " ");

                    if (location != null) {

                        double latitude= location.getLatitude();
                        double longitude= location.getLongitude();
                        String lat=String.valueOf(latitude);
                        String lng=String.valueOf(longitude);
                        Geocoder geocoder= new Geocoder(getApplicationContext());
                        try {
                            List<Address> addressList= geocoder.getFromLocation(latitude, longitude, 1);
                            String str= addressList.get(0).getSubThoroughfare()+",";
                            str += addressList.get(0).getThoroughfare()+",";
                            str += addressList.get(0).getSubLocality()+",";
                            //str += addressList.get(0).getLocality()+",";
                            //str += addressList.get(0).getAdminArea()+",";

                            String strLocation ="Place:" + str ;
                            locUpdate.setText(strLocation);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("HTTP", "Calling Handler:");


                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    SharedPreferences sharedPrefs = getSharedPreferences("sharedprefs", 0);
                    String vehicle_id= sharedPrefs.getString("veh_id_dt", " ");

                    if (location != null) {

                        double latitude= location.getLatitude();
                        double longitude= location.getLongitude();
                        String lat=String.valueOf(latitude);
                        String lng=String.valueOf(longitude);
                        Geocoder geocoder= new Geocoder(getApplicationContext());
                        try {
                            List<Address> addressList= geocoder.getFromLocation(latitude, longitude, 1);
                            //String str= addressList.get(0).getSubThoroughfare()+",";
                            String str = addressList.get(0).getThoroughfare()+",";
                            str += addressList.get(0).getSubLocality()+",";
                            //str += addressList.get(0).getLocality()+",";
                            //str += addressList.get(0).getAdminArea()+",";
                            String strLocation ="My Location:" + str ;
                            locUpdate.setText(strLocation);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        googleMap.getUiSettings().setCompassEnabled(true);




        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                           mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,
                                    16f));
                            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Your Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        }

                    }
                });


        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.2f));
    }

    public void SearchLoc(View view) throws IOException {

        et = (EditText)findViewById(R.id.editText);
        String searchLocation = et.getText().toString();
        Geocoder geocoder2 = new Geocoder(this);
        List<Address> list = geocoder2.getFromLocationName(searchLocation, 1);
        Address address = list.get(0);
        String locality = address.getLocality();
        Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

        double lat = address.getLatitude();
        double lng = address.getLongitude();

        LatLng latLng = new LatLng(lat, lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));
        mMap.addMarker(new MarkerOptions().position(latLng).title(address.getLocality()));

    }

}
