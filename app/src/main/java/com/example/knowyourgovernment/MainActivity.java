package com.example.knowyourgovernment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{
    private static int MY_LOCATION_REQUEST_CODE_ID = 329;
    private LocationManager locationManager;
    private Criteria criteria;
    private ArrayList<Government> govList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GovAdapter govAdapter;
    private View about;
    private TextView locationView;
    private String latLong;
    private String currLocation;
    private int position;
    public static final String GOV_OFFICIAL = "GOV";
    public static final String LOCATION = "LOC";
    private static final int RETURNED = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        //criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        //criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);

        about = findViewById(R.id.aboutActivity);
        //createList();

        setContentView(R.layout.activity_main);
        locationView = findViewById(R.id.locationViewB);
        recyclerView = findViewById(R.id.recyclerView);
        govAdapter = new GovAdapter(govList, this);
        recyclerView.setAdapter(govAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        govAdapter.notifyDataSetChanged();
        if (networkStatus()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        MY_LOCATION_REQUEST_CODE_ID);
            } else {
                setLocationServices();
                new GovDownloaderAsyncTask(MainActivity.this).execute(currLocation);
            }
        }
        else{
            locationView.setText("No Data For Location");
            networkError();
        }
    }

    @Override
    public void onClick(View v) {
        Intent selectedGov = new Intent(this, OfficialActivity.class);
        position = recyclerView.getChildLayoutPosition(v);
        Government selection = govList.get(position);
        selectedGov.putExtra(GOV_OFFICIAL, selection);
        selectedGov.putExtra(LOCATION, locationView.getText().toString());
        startActivityForResult(selectedGov, RETURNED);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent noteData){
        if (requestCode == RETURNED){
            if (resultCode ==   RESULT_OK){
                return;
        }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Intent selectedGov = new Intent(this, OfficialActivity.class);
        startActivity(selectedGov);
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.aboutInfo:
                openAboutActivity(about);
            case R.id.searchOption:
                changeLocation();
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void openAboutActivity(View v){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void setLocation(String location){
        locationView.setText(location);
    }

    public void GovDataResult(ArrayList<Government> govData){
        if (govData == null){
            Toast.makeText(this, "Error accessing Data", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            govList.clear();
            govList.addAll(govData);
            govAdapter.notifyDataSetChanged();
        }
    }

    public void changeLocation(){
        if (networkStatus()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            final View locationView = inflater.inflate(R.layout.location_text, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter a City, State, or a Zip Code");
            builder.setView(locationView);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    EditText loc = locationView.findViewById(R.id.locationText);
                    new GovDownloaderAsyncTask(MainActivity.this).execute(loc.getText().toString());
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    return;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            networkError();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull
            String[] permissions, @NonNull
                    int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_LOCATION_REQUEST_CODE_ID) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                setLocationServices();
                return;
            }
        }

    }

    @SuppressLint("MissingPermission")
    private void setLocationServices() {

        String bestProvider = locationManager.getBestProvider(criteria, true);


        Location currentLocation = locationManager.getLastKnownLocation(bestProvider);
        if (currentLocation != null) {
            latLong =
                    String.format(Locale.getDefault(),
                            "%.4f, %.4f", currentLocation.getLatitude(), currentLocation.getLongitude());
            doLatLon(latLong);
        } else {
            Toast.makeText(this, "Error with location", Toast.LENGTH_SHORT).show();
        }


    }

    public void doLatLon(String loc) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses;
            if (loc.trim().isEmpty()) {
                Toast.makeText(this, "Enter Lat & Lon coordinates first!", Toast.LENGTH_LONG).show();
                return;
            }
            String[] latLon = loc.split(",");
            double lat = Double.parseDouble(latLon[0]);
            double lon = Double.parseDouble(latLon[1]);

            addresses = geocoder.getFromLocation(lat, lon, 1);
            displayAddresses(addresses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayAddresses(List<Address> addresses) {
        StringBuilder sb = new StringBuilder();
        if (addresses.size() == 0) {
            Toast.makeText(this, "Error with addresses", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Address ad : addresses) {

            String curr = (ad.getPostalCode() == null ? "" : ad.getPostalCode());

            if (!curr.trim().isEmpty())
                sb.append("").append(curr.trim());
        }
        currLocation = (sb.toString());
    }

    private boolean networkStatus(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null){
            return false;
        }
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else
            return false;
    }

    private void networkError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Data cannot be accessed/loaded without a network connection");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
