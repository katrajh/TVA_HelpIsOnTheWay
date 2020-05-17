package tva.how;

/**
 * DODAJANJE DEFIBRILATORJA
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DefibrilatorAddActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser = null;

    EditText et_telefonskaStevilka;
    EditText et_znamkaAed;
    EditText et_idStevilkaAed;
    EditText et_lokacijaAed;
    EditText et_imeObjekta;
    EditText et_opisLokacijeAed;
    TextView tv_dodajSlikoAed;
    Button btn_dodajAed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defibrilator_add);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        et_telefonskaStevilka = findViewById(R.id.et_telefonskaStevilka);
        et_znamkaAed = findViewById(R.id.et_znamkaAed);
        et_idStevilkaAed = findViewById(R.id.et_idStevilkaAed);
        et_lokacijaAed = findViewById(R.id.et_lokacijaAed);
        et_imeObjekta = findViewById(R.id.et_imeObjekta);
        et_opisLokacijeAed = findViewById(R.id.et_opisLokacijeAed);
        tv_dodajSlikoAed = findViewById(R.id.tv_dodajSlikoAed);
        btn_dodajAed = findViewById(R.id.btn_dodajAed);

        btn_dodajAed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // to do!!
                // klic metode
                // actionAddAed();
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
}
