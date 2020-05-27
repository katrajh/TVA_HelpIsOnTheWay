package tva.how;

/**
 * ZEMLJEVID DEFIUBRILATORJEV
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;

public class DefibrilatorMapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defibrilator_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        LatLng lokacijaMaribor = new LatLng(46.558910, 15.638886);
        map.addMarker(new MarkerOptions().position(lokacijaMaribor).title("Maribor"));
        map.moveCamera(CameraUpdateFactory.newLatLng(lokacijaMaribor));

        // nastavitev max in min zooma
        map.setMinZoomPreference(12.0f);
        map.setMaxZoomPreference(30.0f);

        String naslov = "Ob bregu 22";

        map.addMarker(new MarkerOptions()
                .position(getLocationFromAddress(this, naslov))
                //.anchor(0.5f, 0.5f)
                .title(naslov)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.how_app_aed_marker_green)));


    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

}
