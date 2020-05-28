package tva.how;

/**
 * ZEMLJEVID DEFIUBRILATORJEV
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import tva.how.classesFirebase.AedNaprave;

public class DefibrilatorMapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defibrilator_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        // definicija baze
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        LatLng lokacijaMaribor = new LatLng(46.558910, 15.638886);
        map.addMarker(new MarkerOptions().position(lokacijaMaribor).title("Maribor"));
        map.moveCamera(CameraUpdateFactory.newLatLng(lokacijaMaribor));

        // nastavitev max in min zooma
        // map.setMinZoomPreference(12.0f);
        // map.setMaxZoomPreference(30.0f);

        /*
        *  Dodajanje markerjev(značk) - lokacij AED naprav
        */
        /*
        String naslov = "Ob bregu 22, 2000 Maribor";

        map.addMarker(new MarkerOptions()
                .position(getLocationFromAddress(naslov))
                .title(naslov)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.how_app_aed_marker_green)));
        */

        db.collection("AedNaprave")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        List<AedNaprave> listAedNaprave = new ArrayList<>();

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                AedNaprave aedNaprave = documentSnapshot.toObject(AedNaprave.class);
                                aedNaprave.setId_aed(documentSnapshot.getId());
                                listAedNaprave.add(aedNaprave);
                            }

                            for(int i=0; i<listAedNaprave.size(); i++) {
                                String lokacija = listAedNaprave.get(i).getLokacija();
                                String postnaSt = listAedNaprave.get(i).getPostna_stevilka();
                                String kraj = listAedNaprave.get(i).getKraj();
                                String stringNaslov = lokacija +", "+postnaSt+" "+kraj;

                                map.addMarker(new MarkerOptions()
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
