package tva.how;

/**
 * ZEMLJEVID DEFIUBRILATORJEV
 */

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tva.how.classesFirebase.AedNaprave;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback{

    private static final String TAG = "MapActivity";

    private FirebaseFirestore db;

    // current location
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 13f;

    // zemljevid (spremenljivka)
    private GoogleMap zemljevid;

    int status;
    String kolekcija;
    String naslov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle bundle = getIntent().getExtras();

        status=1;       // 1 - AedNaprave, 2 - Bolnisnice ali ZdravstveniDomovi
        kolekcija="";
        naslov="";

        if(bundle != null) {
            status = bundle.getInt("status");
            kolekcija = bundle.getString("kolekcija");
            naslov = bundle.getString("naslov");
        }

        // definicija baze
        db = FirebaseFirestore.getInstance();

        // pridobitev trenutne lokacije
        //geteLocationPermissions();

        initMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        zemljevid = googleMap;

        /*
         *  Dodajanje markerjev(značk)
         */
        if (status == 1) {

            // zemljevid se nam prikaže glede na trenutno lokacijo
            // ne deluje!! se izvede else
            if (mLocationPermissionsGranted) {
                getDeviceLocation();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                zemljevid.setMyLocationEnabled(true);
                //zemljevid.getUiSettings().setMyLocationButtonEnabled(false);
            }
            else{
                LatLng lokacijaMaribor = new LatLng(46.558910, 15.638886);
                zemljevid.addMarker(new MarkerOptions().position(lokacijaMaribor).title("Maribor"));
                zemljevid.moveCamera(CameraUpdateFactory.newLatLng(lokacijaMaribor));
            }

            db.collection("AedNaprave")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            /*
                            LatLng lokacijaMaribor = new LatLng(46.558910, 15.638886);
                            zemljevid.addMarker(new MarkerOptions().position(lokacijaMaribor).title("Maribor"));
                            zemljevid.moveCamera(CameraUpdateFactory.newLatLng(lokacijaMaribor));
                            */

                            final List<AedNaprave> listAedNaprave = new ArrayList<>();

                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    AedNaprave aedNaprave = documentSnapshot.toObject(AedNaprave.class);
                                    listAedNaprave.add(aedNaprave);
                                }

                                for(int i=0; i<listAedNaprave.size(); i++) {
                                    String lokacija = listAedNaprave.get(i).getLokacija();
                                    String postnaSt = listAedNaprave.get(i).getPostna_stevilka();
                                    String kraj = listAedNaprave.get(i).getKraj();
                                    String stringNaslov = lokacija +", "+postnaSt+" "+kraj;

                                    zemljevid.addMarker(new MarkerOptions()
                                            .position(getLocationFromAddress(stringNaslov))
                                            .title(stringNaslov)
                                            .snippet(listAedNaprave.get(i).getNaziv_objekta())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.how_app_aed_marker_green)));
                                }

                            } else {
                                Log.w("LOG:", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        else if(status == 2){

            zemljevid.addMarker(new MarkerOptions()
                    .position(getLocationFromAddress(naslov))
                    .title(naslov)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.how_app_hospital_marker2)));

            zemljevid.moveCamera(CameraUpdateFactory.newLatLng(getLocationFromAddress(naslov)));
        }
        else{
            Log.w("LOG:", "Error getting documents.");
        }

        zemljevid.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                db.collection("AedNaprave")
                        .whereEqualTo("naziv_objekta", ""+marker.getSnippet())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                // pop up
                                final AlertDialog.Builder builderPopup = new AlertDialog.Builder(MapActivity.this);

                                // custom dialogPopUpOne set-up
                                View dView = getLayoutInflater().inflate(R.layout.dialog_style_moreinfo, null);

                                TextView tvNaziv = dView.findViewById(R.id.tv_dialog_moreInfo_naziv_text);
                                TextView tvOpis = dView.findViewById(R.id.tv_dialog_moreInfo_opis_text);
                                TextView tvTelSt = dView.findViewById(R.id.tv_dialog_moreInfo_telSt_text);
                                TextView tvZnamka = dView.findViewById(R.id.tv_dialog_moreInfo_znamka_text);
                                TextView tvSerijskaSt = dView.findViewById(R.id.tv_dialog_moreInfo_serijskaSt_text);

                                String text="";

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    AedNaprave aed = documentSnapshot.toObject(AedNaprave.class);
                                    tvNaziv.setText(aed.getNaziv_objekta());
                                    tvOpis.setText(aed.getOpis_lokacije());
                                    tvTelSt.setText(aed.getTelefonska_stevilka());
                                    tvZnamka.setText(aed.getAed_znamka());
                                    tvSerijskaSt.setText(aed.getId_aed());
                                }

                                final Button btnPositive = dView.findViewById(R.id.dialog_moreInfo_positiveButton);

                                builderPopup.setView(dView);

                                final Dialog dialog = builderPopup.create();

                                dialog.show();

                                btnPositive.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                            }
                        });

                return false;
            }
        });

    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        zemljevid.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void geteLocationPermissions() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);

        mapFragment.getMapAsync(MapActivity.this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;


        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }

    }

    // pretvorba iz naslova v geografsko širino in dolžino
    public LatLng getLocationFromAddress(String strNaslov) {

        Geocoder coder = new Geocoder(MapActivity.this);
        List<Address> naslov;
        LatLng valueLatLng = null;

        try {
            // May throw an IOException
            naslov = coder.getFromLocationName(strNaslov, 5);
            if (naslov == null) {
                return null;
            }
            Address lokacija = naslov.get(0);
            valueLatLng = new LatLng(lokacija.getLatitude(), lokacija.getLongitude() );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return valueLatLng;
    }

}
