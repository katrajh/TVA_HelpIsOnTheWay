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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser = null;

    CardView card_dodajAed, card_novice, card_prvaPomoc, card_zdrDomovi, card_bolnisnice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        card_dodajAed = findViewById(R.id.card_dodajAed);
        card_novice = findViewById(R.id.card_novice);
        card_prvaPomoc = findViewById(R.id.card_prvaPomoc);
        card_zdrDomovi = findViewById(R.id.card_zdrDomovi);
        card_bolnisnice = findViewById(R.id.card_bolnisnice);

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
                Intent intent = new Intent(HomeScreenActivity.this, LearnFirstAidBasicActivity.class);
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

}
