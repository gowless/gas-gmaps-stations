package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication.helper.PostViewModel;
import com.example.myapplication.R;
import com.example.myapplication.db.Post;
import com.example.myapplication.helper.Checker;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class AddElementActivity extends AppCompatActivity implements OnMapReadyCallback {

    Checker checker;
    PostViewModel postViewModel;
    Button addItem, chooseLocation;
    EditText gasTitle, typeGas, totalNum, totalCost;
    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    ListView lstPlaces;
    private PlacesClient mPlacesClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    private Location mLastKnownLocation;

    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private Place[] mPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Toolbar toolbar = findViewById(R.id.toolbar);

        lstPlaces = findViewById(R.id.listPlaces);

        // Initialize the Places client
        String apiKey = getString(R.string.google_maps_key);
        Places.initialize(getApplicationContext(), apiKey);
        mPlacesClient = Places.createClient(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        checker = new Checker();

        chooseLocation = findViewById(R.id.chooseLocation);
        addItem = findViewById(R.id.addItem);

        gasTitle = findViewById(R.id.gasTitle);
        typeGas = findViewById(R.id.typeGas);
        totalNum = findViewById(R.id.totalNum);
        totalCost = findViewById(R.id.totalCost);


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();


                UUID idOne = UUID.randomUUID();

                if (checker.checkInputs(gasTitle.getText().toString(), typeGas.getText().toString(), totalNum.getText().toString(), totalCost.getText().toString())){
                    Post post = new Post(gasTitle.getText().toString(), typeGas.getText().toString(), totalNum.getText().toString(), totalCost.getText().toString());
                    postViewModel.savePost(post);

                    myRef.child("posts").child(idOne.toString()).setValue(post);
                    startActivity(new Intent(AddElementActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(AddElementActivity.this, "You need to fill up all fields!", Toast.LENGTH_LONG).show();
                }

            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    return false;
                }
                getAutoCompleteResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void getAutoCompleteResults(String query) {
        if (!mLocationPermissionGranted) {
            Log.w(TAG, "Permission is not yet granted.");
            return;
        }

        if (mLastKnownLocation == null) {
            Log.w(TAG, "Last location not known.");
            return;
        }

        // Build request
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setOrigin(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))
                .setQuery(query)
                .build();
        Task<FindAutocompletePredictionsResponse> task = mPlacesClient.findAutocompletePredictions(request);
        task.addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                FindAutocompletePredictionsResponse response = task.getResult();
                if (!task.isSuccessful() || response == null) {
                    Log.w(TAG, "Could not fetch predictions.");
                    return;
                }

                List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                fillPredictionsList(predictions);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_geolocate) {
            pickCurrentPlace();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void getLocationPermission() {

        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Enable the zoom controls for the map
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Prompt the user for permission.
        getLocationPermission();

    }

    private void fillPredictionsList(final List<AutocompletePrediction> predictions) {
        SpannableString[] items = new SpannableString[predictions.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = predictions.get(i).getPrimaryText(null);
        }
        ArrayAdapter<SpannableString> placesAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lstPlaces.setAdapter(placesAdapter);
        lstPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AutocompletePrediction prediction = predictions.get(position);
                displayMarker(prediction);
            }
        });
    }

    private void displayMarker(final AutocompletePrediction prediction) {
        String placeId = prediction.getPlaceId();
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
        Task<FetchPlaceResponse> response = mPlacesClient.fetchPlace(request);
        response.addOnCompleteListener(new OnCompleteListener<FetchPlaceResponse>() {
            @Override
            public void onComplete(@NonNull Task<FetchPlaceResponse> task) {
                FetchPlaceResponse placeResponse = task.getResult();
                if (!task.isSuccessful() || placeResponse == null) {
                    Log.w(TAG, "Could not fetch place.");
                    return;
                }

                Place place = placeResponse.getPlace();

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(place.getName())
                        .position(place.getLatLng())
                        .snippet(prediction.getSecondaryText(null).toString()));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
            }
        });
    }


    private void getCurrentPlaceLikelihoods() {
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.LAT_LNG);

        @SuppressWarnings("MissingPermission") final FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(this,
                new OnCompleteListener<FindCurrentPlaceResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                        FindCurrentPlaceResponse response = task.getResult();
                        if (task.isSuccessful() && response != null) {
                            // Set the count, handling cases where less than 5 entries are returned.
                            int count = Math.min(response.getPlaceLikelihoods().size(), M_MAX_ENTRIES);

                            int i = 0;
                            mPlaces = new Place[count];

                            for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                                Place currPlace = placeLikelihood.getPlace();
                                mPlaces[i] = currPlace;

                                String currLatLng = (currPlace.getLatLng() == null) ?
                                        "" : currPlace.getLatLng().toString();

                                Log.i(TAG, "Place " + currPlace.getName()
                                        + " has likelihood: " + placeLikelihood.getLikelihood()
                                        + " at " + currLatLng);

                                i++;
                                if (i > (count - 1)) {
                                    break;
                                }
                            }


                            fillPlacesList();
                        } else {
                            Exception exception = task.getException();
                            if (exception instanceof ApiException) {
                                ApiException apiException = (ApiException) exception;
                                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                            }
                        }
                    }
                });
    }


    private void getDeviceLocation() {

        try {
            if (mLocationPermissionGranted) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = location;
                            Log.d(TAG, "Latitude: " + mLastKnownLocation.getLatitude());
                            Log.d(TAG, "Longitude: " + mLastKnownLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        }

                        getCurrentPlaceLikelihoods();
                    }
                });
            }
        } catch (Exception e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void pickCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            getDeviceLocation();
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }


    private AdapterView.OnItemClickListener listClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // position will give us the index of which place was selected in the array
            Place place = mPlaces[position];
            String markerSnippet = place.getAddress();
            if (place.getAttributions() != null) {
                markerSnippet += "\n" + TextUtils.join(" ", place.getAttributions());
            }


            mMap.addMarker(new MarkerOptions()
                    .title(place.getName())
                    .position(place.getLatLng())
                    .snippet(markerSnippet));

            // Position the map's camera at the location of the marker.
            gasTitle.setText(place.getAddress());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
        }
    };


    private void fillPlacesList() {
        // Set up an ArrayAdapter to convert likely places into TextViews to populate the ListView
        String[] items = new String[mPlaces.length];
        for (int i = 0; i < mPlaces.length; i++) {
            items[i] = mPlaces[i].getName();
        }
        ArrayAdapter<String> placesAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lstPlaces.setAdapter(placesAdapter);
        lstPlaces.setOnItemClickListener(listClickedHandler);
    }
}