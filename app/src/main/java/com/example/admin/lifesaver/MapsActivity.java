package com.example.admin.lifesaver;

import android.*;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location myLoc;
    String type="hospital";
    int typ=1;
    ArrayList<Marker> markers;
    List<HashMap<String, String>> allPlaces;
    String selectedReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (!checkGooglePlayServices()){
            Toast.makeText(this, "Google Play Services NOT Available", Toast.LENGTH_SHORT).show();
            finish();
        }

        typ=getIntent().getIntExtra("key",0);
        if(typ==1)
            type="hospital";
        else if(typ==2)
            type="police";
        else if(typ==3)
            type="fire_station";
        else if(typ==4)
            type="ambulance";
        else if(typ==5)
            type="blood_bank";

//        Toast.makeText(this, "Showing "+type, Toast.LENGTH_SHORT).show();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buildGoogleApiClient();

       /* FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.map_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedReference==null)
                    Toast.makeText(MapsActivity.this,"Select a place",Toast.LENGTH_SHORT).show();
                else
                {
                    PlacesTask placesTask=new PlacesTask();
                    placesTask.execute(getDetailsUrl(selectedReference));
                }
            }
        });*/


    }

    private void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
  //      Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
        else
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLocationChanged(Location location) {
        myLoc = location;
        LatLng sydney = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));

        String url = getPlacesUrl(myLoc.getLatitude(), myLoc.getLongitude(),type);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        markers=new ArrayList<>();
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);
  //      Toast.makeText(MapsActivity.this,"Loc changed", Toast.LENGTH_LONG).show();

    }

    private boolean checkGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, 0).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int ind=markers.indexOf(marker);
                if(ind!=-1)
                {
                    HashMap<String, String> selectedPlace=allPlaces.get(ind);
                    selectedReference=selectedPlace.get("reference");
                    return false;
                }
                else
                selectedReference=null;
                return false;
            }
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
        else
        {
            mMap.setMyLocationEnabled(true);
            mMap.clear();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else
        {
            Toast.makeText(this,"Location permission not granted",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private String getPlacesUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 10000);

        if(typ<=3)
           googlePlacesUrl.append("&type=" + nearbyPlace);
        else
            googlePlacesUrl.append("&keyword=" + nearbyPlace);

        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyD2SyjOpjT38zrMKiXfXfZtSmUl42Wuyo8");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    private String getDetailsUrl(String reference) {

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        sb.append("reference="+reference);
        sb.append("&sensor=true");
        sb.append("&key=" + "AIzaSyD2SyjOpjT38zrMKiXfXfZtSmUl42Wuyo8");

        return (sb.toString());
    }


    public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

        String googlePlacesData;
        GoogleMap mMap;
        String url;

        @Override
        protected String doInBackground(Object... params) {
            try {
//                Log.d("GetNearbyPlacesData", "doInBackground entered");
                mMap = (GoogleMap) params[0];
                url = (String) params[1];
                DownloadUrl downloadUrl = new DownloadUrl();
                googlePlacesData = downloadUrl.readUrl(url);
                Log.d("GooglePlacesReadTask", "doInBackground Exit");
            } catch (Exception e) {
                Log.d("GooglePlacesReadTask", e.toString());
            }
            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("GooglePlacesReadTask", "onPostExecute Entered");
//            Toast.makeText(MapsActivity.this,"RESULT:"+result, Toast.LENGTH_LONG).show();

            List<HashMap<String, String>> nearbyPlacesList = null;
            DataParser dataParser = new DataParser();
            nearbyPlacesList =  dataParser.parse(result);
            allPlaces=nearbyPlacesList;
            ShowNearbyPlaces(nearbyPlacesList);
            Log.d("GooglePlacesReadTask", "onPostExecute Exit");
        }

        private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
            for (int i = 0; i < nearbyPlacesList.size(); i++) {

                Log.d("onPostExecute","Entered into showing locations");
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                String placeId = googlePlace.get("id");
                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName + " : " + vicinity);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                markers.add(mMap.addMarker(markerOptions));
                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            }
            MarkerOptions currentmo=new MarkerOptions();
            currentmo.position(new LatLng(myLoc.getLatitude(),myLoc.getLongitude()));
            currentmo.title("you are here");
            currentmo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(currentmo);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(myLoc.getLatitude(),myLoc.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }
    }

    private class PlacesTask extends AsyncTask<String, Integer, String>{

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = new DownloadUrl().readUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google place details in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Place Details in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, HashMap<String,String>>{

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected HashMap<String,String> doInBackground(String... jsonData) {

            HashMap<String, String> hPlaceDetails = null;

            try{
                jObject = new JSONObject(jsonData[0]);

                // Start parsing Google place details in JSON format
                hPlaceDetails = DataParser.parseForDetails(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return hPlaceDetails;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(HashMap<String,String> hPlaceDetails){

            String name = hPlaceDetails.get("name");
            String icon = hPlaceDetails.get("icon");
            String vicinity = hPlaceDetails.get("vicinity");
            String lat = hPlaceDetails.get("lat");
            String lng = hPlaceDetails.get("lng");
            String formatted_address = hPlaceDetails.get("formatted_address");
            String formatted_phone = hPlaceDetails.get("formatted_phone");
            String website = hPlaceDetails.get("website");
            String rating = hPlaceDetails.get("rating");
            String international_phone_number = hPlaceDetails.get("international_phone_number");
            String url = hPlaceDetails.get("url");

//            Toast.makeText(MapsActivity.this,"Detaisl:"+name+website+international_phone_number+url+rating,Toast.LENGTH_SHORT).show();

            TextView t0,t1,t2,t3,t4,t5;
            Button b;

            final Dialog dialog=new Dialog(MapsActivity.this);
            dialog.setContentView(R.layout.maps_dialog);

            t0=(TextView)dialog.findViewById(R.id.maps_details_name2);
            t1=(TextView)dialog.findViewById(R.id.maps_details_address2);
            t2=(TextView)dialog.findViewById(R.id.maps_details_loc2);
            t3=(TextView)dialog.findViewById(R.id.maps_details_cn2);
            t4=(TextView)dialog.findViewById(R.id.maps_details_web2);
            t5=(TextView)dialog.findViewById(R.id.maps_details_rat2);
            b=(Button)dialog.findViewById(R.id.maps_dialog_button);

            dialog.setTitle(name);
            t0.setText(name);
            t1.setText(formatted_address);
            t2.setText(lat+","+lng);
            t3.setText(formatted_phone);
            t4.setText(website);
            t5.setText(rating);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();


        }
    }


}
