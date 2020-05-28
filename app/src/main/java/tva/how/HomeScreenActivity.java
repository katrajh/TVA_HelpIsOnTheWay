package tva.how;

/**
 * DOMAČI ZASLON oz. GLAVNIO MENI
 *
 * Preusmeritev po prijavi
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tva.how.classesFirebase.AedNaprave;

public class HomeScreenActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser = null;

    CardView card_dodajAed, card_novice, card_prvaPomoc, card_zdrDomovi, card_bolnisnice, card_zemljevid;

    GoogleMap zemljevid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        card_dodajAed = findViewById(R.id.card_dodajAed);
        card_novice = findViewById(R.id.card_novice);
        card_prvaPomoc = findViewById(R.id.card_prvaPomoc);
        card_zdrDomovi = findViewById(R.id.card_zdrDomovi);
        card_bolnisnice = findViewById(R.id.card_bolnisnice);
        card_zemljevid = findViewById(R.id.card_zemljevid);

        /*
         * Preusmeritev na DefibrilatorAddActivity
         */
        card_dodajAed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, DefibrilatorAddActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Preusmeritev na NewsActivity
         */
        card_novice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Preusmeritev na LearnFirstAidBasicActivity
         */
        card_prvaPomoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, LearnFirstAidBasicsActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Preusmeritev na HealthCentersActivity
         */
        card_zdrDomovi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, HealthCentersActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Preusmeritev na HospitalsActivity
         */
        card_bolnisnice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, HospitalsActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Preusmeritev na DefibirlatorMapActivity
         */
        card_zemljevid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, DefibrilatorMapActivity.class);
                startActivity(intent);
            }
        });

        /*
         * Predogled ZEMLJEVID
         */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_mapOnHomeScreen);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onBackPressed() {
        // zakomentirano, da gumb za nazaj ne deluje
        //super.onBackPressed();

        // ob kliku nazaj nas vpraša ali želimo zapustiti aplikacijo
        showPopupDialog(this, "HOW - Help is on the way", "Ali želite zapustiti aplikacijo?", "Da", "Ne");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(currentUser != null) {
            // do nothing
        }
        else {
            Log.w("LOG", "currentUser:_ "+currentUser.getUid());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        final int itemId = item.getItemId();


        final AlertDialog.Builder builderPopup = new AlertDialog.Builder(HomeScreenActivity.this);

        // custom dialogPopUpOne set-up
        View dView = getLayoutInflater().inflate(R.layout.dialog_style, null);

        TextView tvTitle = dView.findViewById(R.id.dialog_title);
        tvTitle.setText("Odjava iz aplikacije");

        TextView tvMsg = dView.findViewById(R.id.dialog_msg);
        tvMsg.setText("Ali se želite odjaviti iz aplikacije?");

        final Button btnPositive = dView.findViewById(R.id.dialog_positiveBtn);
        btnPositive.setText("Da");

        final Button btnNegative = dView.findViewById(R.id.dialog_negativeBtn);
        btnNegative.setText("Ne");

        builderPopup.setView(dView);

        // tako se naredi, da se ob kliku izven obmocja popup-a okno ne zapre
        final Dialog dialog = builderPopup.create();
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentUser != null) {
                    if (itemId == R.id.item_odjava) {
                        FirebaseAuth.getInstance().signOut();
                        //LoginManager.getInstance().logOut();

                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else {
                        // do nothing
                    }
                }

                dialog.dismiss();
            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //da se zapre popup okno
                dialog.dismiss();
            }
        });


        return true;
    }

    // POP-UP onBackPressed
    public void showPopupDialog(Activity activity, String naslov, CharSequence msg, String positiveBtnText, String negativeBtnText) {

        final AlertDialog.Builder builderPopup = new AlertDialog.Builder(activity);

        // custom dialogPopUpOne set-up
        View dView = getLayoutInflater().inflate(R.layout.dialog_style, null);

        TextView tvTitle = dView.findViewById(R.id.dialog_title);
        tvTitle.setText(naslov);

        TextView tvMsg = dView.findViewById(R.id.dialog_msg);
        tvMsg.setText(msg);

        final Button btnPositive = dView.findViewById(R.id.dialog_positiveBtn);
        btnPositive.setText(positiveBtnText);

        final Button btnNegative = dView.findViewById(R.id.dialog_negativeBtn);
        btnNegative.setText(negativeBtnText);

        builderPopup.setView(dView);

        // tako se naredi, da se ob kliku izven obmocja popup-a okno ne zapre
        final Dialog dialog = builderPopup.create();

        dialog.show();

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // IZHOD iz aplikacije na home screen mobitela
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                dialog.dismiss();

                // da se zaključijo vsi procesi
                finish();
            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //da se zapre popup okno
                dialog.dismiss();
            }
        });

    }

    // konfiguracija ZEMLJEVIDA
    @Override
    public void onMapReady(GoogleMap googleMap) {
        zemljevid = googleMap;

        LatLng lokacijaMaribor = new LatLng(46.558910, 15.638886);
        zemljevid.addMarker(new MarkerOptions().position(lokacijaMaribor).title("Maribor"));
        zemljevid.moveCamera(CameraUpdateFactory.newLatLng(lokacijaMaribor));

        // nastavitev max in min zooma
        zemljevid.setMinZoomPreference(12.0f);
        zemljevid.setMaxZoomPreference(30.0f);
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

                                zemljevid.addMarker(new MarkerOptions()
                                        .position(getLocationFromAddress(stringNaslov))
                                        .title(stringNaslov)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.how_app_aed_marker_green)));

                                zemljevid.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        // do nothing on marker click
                                        return true;
                                    }
                                });
                            }

                        } else {
                            Log.w("LOG:", "Error getting documents.", task.getException());
                        }
                    }
                });

        // z onClickListener metodo nas ob kliku na območje predogled zemljevida preusmeri na DefibirlatorMapActivity
        zemljevid.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(HomeScreenActivity.this, DefibrilatorMapActivity.class);
                startActivity(intent);
            }
        });

    }
    // pretvorba iz naslova v geografsko širino in dolžino
    public LatLng getLocationFromAddress(String strNaslov) {

        Geocoder coder = new Geocoder(HomeScreenActivity.this);
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
