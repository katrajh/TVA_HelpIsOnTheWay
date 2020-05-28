package tva.how;

/**
 * REGISTRACIJA
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import tva.how.classesFirebase.Users;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    EditText et_firstName, et_lastName, et_email, et_pass1, et_pass2;
    Button btn_signMeUp;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // definicija baze
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        et_firstName = findViewById(R.id.et_imeOsebe);
        et_lastName = findViewById(R.id.et_priimekOsebe);
        et_email = findViewById(R.id.et_email);
        et_pass1 = findViewById(R.id.et_geslo);
        et_pass2 = findViewById(R.id.et_ponoviGeslo);
        btn_signMeUp = findViewById(R.id.btn_registrirajMe);
        progressBar = findViewById(R.id.progress_bar_reg);

        progressBar.setVisibility(View.INVISIBLE);

        btn_signMeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });

    }

    private void registerNewUser() {

        progressBar.setVisibility(View.VISIBLE);

        final String email = et_email.getText().toString();
        final String geslo1 =  et_pass1.getText().toString();
        final String ime = et_firstName.getText().toString();
        final String priimek = et_lastName.getText().toString();
        final String geslo2 =  et_pass2.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Prosimo, vnesite e-mail...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(geslo1)) {
            Toast.makeText(getApplicationContext(), "Prosimo, vnesite geslo!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(geslo2)) {
            Toast.makeText(getApplicationContext(), "Prosimo, ponovite geslo!", Toast.LENGTH_LONG).show();
            return;
        }

        if (geslo1.equals(geslo2)) {
            if (geslo1.length() > 6) {
                mAuth.createUserWithEmailAndPassword(email, geslo1)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Registracija uspešna!",
                                            Toast.LENGTH_LONG).show();
                                    Log.d("LOG", "createUserWithEmail:success");
                                    progressBar.setVisibility(View.GONE);

                                    FirebaseUser fuser = mAuth.getCurrentUser();

                                    Users user= new Users();
                                    user.setEmail(email);
                                    user.setGeslo(geslo1);
                                    user.setIme(ime);
                                    user.setPriimek(priimek);
                                    user.setUserId(fuser.getUid());
                                    user.setTypeOfAccount("email");

                                    db.collection("Users").document(""+fuser.getUid())
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.w("LOG Registration", "Succesfully added new user");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("LOG Registration", "Error adding document", e);
                                                }
                                            });

                                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    // finish();

                                } else {
                                    Log.w("LOG", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Registracija neuspešna, poskusite ponovno",
                                            Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }

                            }
                        });
            }
            else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Geslo mora vsebovati vsaj 6 znakov!", Toast.LENGTH_LONG).show();
            }
        }
        else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Gesli se ne ujemata!", Toast.LENGTH_LONG).show();
        }


    }
}
