package tva.how;

/**
 * UČENJAK PRVE POMOČI
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LearnFirstAidBasicsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser = null;

    CardView card_krvavitve, card_opekline, card_zlomi, card_amputacija, card_shock, card_zastrupitev;

    String nazivLekcije;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_first_aid_basics);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        card_krvavitve = findViewById(R.id.card_krvavitev);
        card_opekline = findViewById(R.id.card_opekline);
        card_zlomi = findViewById(R.id.card_zlomi);
        card_amputacija = findViewById(R.id.card_amputacija);
        card_shock = findViewById(R.id.card_sok);
        card_zastrupitev = findViewById(R.id.card_zastrupitev);


        /*
         * Preusmeritev na LearnFirstAidBasicsPageActivity
         */
        card_krvavitve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nazivLekcije = "Krvavitev";
                Log.w("LOG", "Lekcija: -->"+nazivLekcije);

                Intent intent = new Intent(LearnFirstAidBasicsActivity.this, LearnFirstAidBasicsPageActivity.class);
                intent.putExtra("nazivLekcije", nazivLekcije);
                startActivity(intent);

            }
        });

        /*
         * Preusmeritev na LearnFirstAidBasicsPageActivity
         */
        card_opekline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nazivLekcije = "Opekline";
                Log.w("LOG", "Lekcija: -->"+nazivLekcije);

                Intent intent = new Intent(LearnFirstAidBasicsActivity.this, LearnFirstAidBasicsPageActivity.class);
                intent.putExtra("nazivLekcije", nazivLekcije);
                startActivity(intent);
            }
        });

        /*
         * Preusmeritev na LearnFirstAidBasicsPageActivity
         */
        card_zlomi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nazivLekcije = "Zlomi";
                Log.w("LOG", "Lekcija: -->"+nazivLekcije);

                Intent intent = new Intent(LearnFirstAidBasicsActivity.this, LearnFirstAidBasicsPageActivity.class);
                intent.putExtra("nazivLekcije", nazivLekcije);
                startActivity(intent);
            }
        });

        /*
         * Preusmeritev na LearnFirstAidBasicsPageActivity
         */
        card_amputacija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nazivLekcije = "Amputacija";
                Log.w("LOG", "Lekcija: -->"+nazivLekcije);

                Intent intent = new Intent(LearnFirstAidBasicsActivity.this, LearnFirstAidBasicsPageActivity.class);
                intent.putExtra("nazivLekcije", nazivLekcije);
                startActivity(intent);
            }
        });

        /*
         * Preusmeritev na LearnFirstAidBasicsPageActivity
         */
        card_shock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nazivLekcije = "Šok";
                Log.w("LOG", "Lekcija: -->"+nazivLekcije);

                Intent intent = new Intent(LearnFirstAidBasicsActivity.this, LearnFirstAidBasicsPageActivity.class);
                intent.putExtra("nazivLekcije", nazivLekcije);
                startActivity(intent);
            }
        });

        /*
         * Preusmeritev na LearnFirstAidBasicsPageActivity
         */
        card_zastrupitev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nazivLekcije = "Zastrupitve";
                Log.w("LOG", "Lekcija: -->"+nazivLekcije);

                Intent intent = new Intent(LearnFirstAidBasicsActivity.this, LearnFirstAidBasicsPageActivity.class);
                intent.putExtra("nazivLekcije", nazivLekcije);
                startActivity(intent);
            }
        });

    }
}
