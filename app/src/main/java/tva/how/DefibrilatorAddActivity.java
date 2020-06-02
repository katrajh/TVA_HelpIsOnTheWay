package tva.how;

/**
 * DODAJANJE DEFIBRILATORJA
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;

import tva.how.classesFirebase.AedNaprave;

public class DefibrilatorAddActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser = null;

    EditText et_telefonskaStevilka;
    EditText et_znamkaAed;
    EditText et_idStevilkaAed;
    EditText et_lokacijaAed;
    EditText et_imeObjekta;
    EditText et_kraj;
    EditText et_postnaStevilka;
    EditText et_opisLokacijeAed;
    TextView tv_dodajSlikoAed;
    Button btn_dodajAed;

    ProgressBar progressBar_addAed;
    FrameLayout progress_overlay_addAed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defibrilator_add);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Poiščemo komponente v Layoutu
        et_telefonskaStevilka = findViewById(R.id.et_telefonskaStevilka);
        et_znamkaAed = findViewById(R.id.et_znamkaAed);
        et_idStevilkaAed = findViewById(R.id.et_idStevilkaAed);
        et_lokacijaAed = findViewById(R.id.et_lokacijaAed);
        et_imeObjekta = findViewById(R.id.et_imeObjekta);
        et_kraj = findViewById(R.id.et_kraj);
        et_postnaStevilka = findViewById(R.id.et_postnaStevilka);
        et_opisLokacijeAed = findViewById(R.id.et_opisLokacijeAed);
        tv_dodajSlikoAed = findViewById(R.id.tv_dodajSlikoAed);
        btn_dodajAed = findViewById(R.id.btn_dodajAed);

        progress_overlay_addAed = findViewById(R.id.progress_overlay_addAed);
        progressBar_addAed = findViewById(R.id.progress_bar_addAed);

        progress_overlay_addAed.setVisibility(View.INVISIBLE);
        progressBar_addAed.setVisibility(View.INVISIBLE);

        btn_dodajAed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // klic metode
                actionAddAed();
            }
        });

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

    // metoda za dodajanje AED v bazo
    private void actionAddAed() {

        progress_overlay_addAed.setVisibility(View.VISIBLE);
        progressBar_addAed.setVisibility(View.VISIBLE);

        String aedIdStevilka = et_idStevilkaAed.getText().toString();
        String aedZnamka = et_znamkaAed.getText().toString();
        String lokacija = et_lokacijaAed.getText().toString();
        String kraj = et_kraj.getText().toString();
        String postnaSt = et_postnaStevilka.getText().toString();
        String telStevilka = et_telefonskaStevilka.getText().toString();
        String opisLokacije = et_opisLokacijeAed.getText().toString();
        String nazivObjekta = et_imeObjekta.getText().toString();
        String slika = "";
        String uporabnikId = currentUser.getUid();
        Double latitude;
        Double longitude;

        if(aedIdStevilka.length() > 3 && aedZnamka.length() > 3 && lokacija.length() > 3 && kraj.length() > 3 && postnaSt.length() > 3 && telStevilka.length() > 3 && opisLokacije.length() > 3 && nazivObjekta.length() > 2 && uporabnikId.length() > 3) {

            String stringNaslov = lokacija +", "+postnaSt+" "+kraj;

            //Pridobitev geografske širine in dolžine iz naslova
            latitude = getLatitudeLocationFromAddress(this, stringNaslov);
            longitude = getLongitudeLocationFromAddress(this, stringNaslov);

            // dodajanje podatkov v objekt
            AedNaprave aedNaprave = new AedNaprave();
            aedNaprave.setId_aed(aedIdStevilka);
            aedNaprave.setAed_znamka(aedZnamka);
            aedNaprave.setLokacija(lokacija);
            aedNaprave.setKraj(kraj);
            aedNaprave.setPostna_stevilka(postnaSt);
            aedNaprave.setTelefonska_stevilka(telStevilka);
            aedNaprave.setOpis_lokacije(opisLokacije);
            aedNaprave.setNaziv_objekta(nazivObjekta);
            aedNaprave.setUporabnik_id(uporabnikId);
            aedNaprave.setLatitude(latitude);
            aedNaprave.setLongitude(longitude);
            aedNaprave.setSlika(slika);

            db.collection("AedNaprave")
                    .add(aedNaprave) //Dodajanje v bazo, da se ustvari nov dokument
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressBar_addAed.setVisibility(View.GONE);
                            progress_overlay_addAed.setVisibility(View.GONE);
                            Log.d("LOG: ", "DocumentSnapshot added with ID: " + documentReference.getId());

                            Toast.makeText(DefibrilatorAddActivity.this,"V sistem ste uspešno dodali novo AED napravo.", Toast.LENGTH_SHORT).show();

                            //Preusmeritev na prejšnji zaslon
                            Intent intent = new Intent(DefibrilatorAddActivity.this, HomeScreenActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("LOG: ", "Error adding document", e);
                            progressBar_addAed.setVisibility(View.GONE);
                            progress_overlay_addAed.setVisibility(View.GONE);
                            Toast.makeText(DefibrilatorAddActivity.this, "Pri dodajanju AED naprave v bazo je prišlo do napake.", Toast.LENGTH_SHORT).show();
                        }
                    });

            Toast.makeText(this,"V sistem ste uspešno dodali novo AED napravo.", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.w("LOG: ", "Podatki niso vneseni!");
            Toast.makeText(this,"Prosimo izpolnite vsa potrebna polja!", Toast.LENGTH_SHORT).show();
            progressBar_addAed.setVisibility(View.GONE);
            progress_overlay_addAed.setVisibility(View.GONE);
        }
    }

    // pretvorba iz naslova v geografsko širino
    public Double getLatitudeLocationFromAddress(Context context, String strNaslov) {

        Geocoder coder = new Geocoder(context);
        List<Address> naslov;
        //LatLng valueLatLng = null;
        double valueLat = 0.0;

        try {
            // May throw an IOException
            naslov = coder.getFromLocationName(strNaslov, 5);
            if (naslov == null) {
                return null;
            }
            Address lokacija = naslov.get(0);
            //valueLatLng = new LatLng(lokacija.getLatitude(), lokacija.getLongitude());
            valueLat = lokacija.getLatitude();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return valueLat;
    }

    // pretvorba iz naslova v geografsko dolžino
    public Double getLongitudeLocationFromAddress(Context context, String strNaslov) {

        Geocoder coder = new Geocoder(context);
        List<Address> naslov;
        //LatLng valueLatLng = null;
        double valueLong = 0.0;

        try {
            // May throw an IOException
            naslov = coder.getFromLocationName(strNaslov, 5);
            if (naslov == null) {
                return null;
            }
            Address lokacija = naslov.get(0);
            //valueLatLng = new LatLng(lokacija.getLatitude(), lokacija.getLongitude());
            valueLong = lokacija.getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return valueLong;
    }
}
