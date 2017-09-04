package store.data.design.com.hikers_watch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locManager;
    String provider;
    Double lat,lon;
    Double alt;
    Float bearing;
    Float speed;
    Float accuracy;
    TextView txtLat, txtLon, txtAccu, txtAlt, txtAdd, txtBear, txtSpeed;
    Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locManager.getBestProvider(new Criteria(), false);
        System.out.println("provider val is " + provider);
        System.out.println("locManager val is " + locManager);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.
                PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.
                ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = locManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            onLocationChanged(location);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.
                PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.
                ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locManager.removeUpdates(this);
        }
    }

    @Override
    public void onResume(){
    super.onResume();
        System.out.println("reached onresume");
        //400milisecs, 1 metr, this is our context
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.
                PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.
                ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("reached main");
         //refreshes location after given specification
            locManager.requestLocationUpdates(locManager.NETWORK_PROVIDER, 1, 1, this);
            /* giving location
            location = locManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);*/
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("reached on location change ");
        lat = location.getLatitude();
        lon = location.getLongitude();
        alt = location.getAltitude();
        bearing = location.getBearing();
        speed = location.getSpeed();
        accuracy = location.getAccuracy();
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> listAddress = geocoder.getFromLocation(lat, lon, 1);
            if (listAddress != null && listAddress.size() > 0) {
                Log.i("PlaceInfo", listAddress.get(0).toString());
                String addressHolder = "";
                for(int i = 0; i <= listAddress.get(0).getMaxAddressLineIndex(); i++){
                    addressHolder += listAddress.get(0).getAddressLine(i);
                }
                txtAdd.setText("Address:\n"+ addressHolder);
            }
        }catch(Exception e){
            System.out.println("exception skasdf is " + e);
        }
        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLon = (TextView) findViewById(R.id.txtLon);
        txtAccu = (TextView) findViewById(R.id.txtAccu);
        txtAlt = (TextView) findViewById(R.id.txtAlt);
        txtAdd = (TextView) findViewById(R.id.txtAdd);
        txtBear = (TextView) findViewById(R.id.txtBear);
        txtSpeed = (TextView) findViewById(R.id.txtSpeed);
        txtLat.setText("Latitude: "+lat.toString());
        txtLon.setText("Longitude: " + lon.toString());
        txtAccu.setText("Accuracy: " + accuracy.toString()+"m");
        txtAlt.setText("Altitude: " + alt.toString()+"m");
        txtBear.setText("Bearing: " + bearing.toString());
        txtSpeed.setText("Speed: " + speed.toString()+"m/s");
        //txtSpeed.setText("Latitude: "+speed.toString());

        Log.i("Latitude ", lat.toString());
        Log.i("Longitude", lon.toString());
        Log.i("altitude ", alt.toString());
        Log.i("Bearing ", bearing.toString());
        Log.i("Speed ", speed.toString());
        Log.i("Accuracy ", accuracy.toString());


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
