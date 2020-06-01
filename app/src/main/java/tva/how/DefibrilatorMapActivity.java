package tva.how;

/**
 * ZEMLJEVID DEFIUBRILATORJEV
 */

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

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
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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

public class DefibrilatorMapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    GoogleMap zemljevid;
    private FirebaseFirestore db;

    // current location
    public double latitudeCurrentLocation;
    public double longitudeCurrentLocation;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    int status;
    String kolekcija;
    String naslov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defibrilator_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        status=1;
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
        getCurrentLocation();

    }

    public static boolean isLocationEnabled(Context context) {
        //...............
        return true;
    }

    protected void getCurrentLocation() {
        if (isLocationEnabled(DefibrilatorMapActivity.this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //You can still do this if you like, you might get lucky:
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                latitudeCurrentLocation = location.getLatitude();
                longitudeCurrentLocation = location.getLongitude();
                //Toast.makeText(DefibrilatorMapActivity.this, "latitude:" + latitudeCurrentLocation + " longitude:" + longitudeCurrentLocation, Toast.LENGTH_SHORT).show();
            } else {
                //This is what you need:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
        }
        else
        {
            //prompt user to enable location....
            //.................
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {

        //remove location callback:
        locationManager.removeUpdates(this);

        //open the map:
        latitudeCurrentLocation = location.getLatitude();
        longitudeCurrentLocation = location.getLongitude();
        //Toast.makeText(DefibrilatorMapActivity.this, "latitude:" + latitudeCurrentLocation + " longitude:" + longitudeCurrentLocation, Toast.LENGTH_SHORT).show();
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

    public void searchNearestPlace(String v2txt) {
        //.....
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        zemljevid = googleMap;

        //LatLng trenutnaLokacija = new LatLng(latitudeCurrentLocation, longitudeCurrentLocation);
        //zemljevid.addMarker(new MarkerOptions().position(trenutnaLokacija).title("Trenutna lokacija"));
        //zemljevid.moveCamera(CameraUpdateFactory.newLatLng(trenutnaLokacija));

        /*
        *  Dodajanje markerjev(značk)
        */
        if (status == 1) {
            db.collection("AedNaprave")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            LatLng lokacijaMaribor = new LatLng(46.558910, 15.638886);
                            zemljevid.addMarker(new MarkerOptions().position(lokacijaMaribor).title("Maribor"));
                            zemljevid.moveCamera(CameraUpdateFactory.newLatLng(lokacijaMaribor));

                            List<AedNaprave> listAedNaprave = new ArrayList<>();

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
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.how_app_hospital_marker)));

            zemljevid.moveCamera(CameraUpdateFactory.newLatLng(getLocationFromAddress(naslov)));
        }
        else{
            Log.w("LOG:", "Error getting documents.");
        }



    }


    // pretvorba iz naslova v geografsko širino in dolžino
    public LatLng getLocationFromAddress(String strNaslov) {

        Geocoder coder = new Geocoder(DefibrilatorMapActivity.this);
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
