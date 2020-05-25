package tva.how;

/**
 * PRIJAVA
 */

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser = null;

    // definicija polj in spremenljivk
    EditText et_email, et_geslo;
    Button btn_prijava;
    TextView tv_pozabljenoGeslo;
    TextView tv_registracija;

    ProgressBar progressBar;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;

    /**
    // Facebook
    private CallbackManager fbCallbackManager;

    // Google
    GoogleSignInClient googleSignInClient;
    int googleRequestCode = 9001;

    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Preverjanje ali je uporanbik že prijalvjen --> potem ga preusmerimo na domači zaslon
        if(currentUser != null) {
            Log.w("LOG", "currentUser:_ "+currentUser.getUid());
            Intent intent = new Intent(this, HomeScreenActivity.class);
            startActivity(intent);

            //da se zaklučijo vsi procesi
            finish();
        }
        else {
            // do nothing
        }

        et_email = findViewById(R.id.et_emailLogin);
        et_geslo = findViewById(R.id.et_gesloLogin);
        tv_pozabljenoGeslo = findViewById(R.id.tv_pozabljenoGeslo);
        btn_prijava = findViewById(R.id.btn_prijava);
        tv_registracija = findViewById(R.id.tv_ustvariteRacun);

        progressBar = findViewById(R.id.progress_bar_log);
        progressBar.setVisibility(View.INVISIBLE);

        progressBarHolder = findViewById(R.id.progress_overlay_log);

        /*
         * gumb za preusmertiev na zaslon REGISTRACIJA
         */
        tv_registracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });


        /*
         * EMAIL in GESLO prijava
         */
        btn_prijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginEmailAndPassword();
            }
        });

        /*
         * POZABLJENO GESLO
         */
        tv_pozabljenoGeslo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // pop up reset password
                final AlertDialog.Builder builderPopup = new AlertDialog.Builder(LoginActivity.this);

                // custom dialogPopUpOne set-up
                View dView = getLayoutInflater().inflate(R.layout.dialog_style_resetpassword, null);

                TextView tvTitle = dView.findViewById(R.id.dialog_resetPassTitle);
                tvTitle.setText("Zahtevek za novo geslo");

                final EditText et_email = dView.findViewById(R.id.dialog_resetPassEmail);

                TextView tvMsg = dView.findViewById(R.id.dialog_resetPassMsg);
                tvMsg.setText("Če ste pozabili geslo, v spodnji obrazec vpišite svoj e-mail, kliknite na gumb 'Resetiraj geslo' in preverite vaš poštni predal za nadaljne napotke.");

                final Button btnPositive = dView.findViewById(R.id.dialog_resetPasssPositiveBtn);
                btnPositive.setText("Resetiraj geslo");

                builderPopup.setView(dView);

                final Dialog dialog = builderPopup.create();

                dialog.show();

                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String email = et_email.getText().toString();

                        mAuth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this,
                                                    "Preverite poštni predal vašega e-maila!",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(LoginActivity.this,
                                                    "Neuspešno pošiljanje sporočila za resetiranje gesla!",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                        dialog.dismiss();
                                    }
                                });
                    }
                });
            }
        });


    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//
//        if(currentUser != null) {
//            Log.w("LOG", "currentUser:_ "+currentUser.getUid());
//            Intent intent = new Intent(this, HomeScreenActivity.class);
//            startActivity(intent);
//        }
//        else {
//            // do nothing
//        }
//    }

    // EMAIL in GESLO prijava
    private void loginEmailAndPassword() {

        progressBar.setVisibility(View.VISIBLE);

        final String email = et_email.getText().toString();
        final String geslo = et_geslo.getText().toString();

        if (TextUtils.isEmpty(email)) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Prosimo, vnesite e-mail ...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(geslo)) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Prosimo, vnesite geslo!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, geslo)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                                // zatemnitev ozadja
                                new DarkBackgroundTask().execute();

                                Log.d("LOG-LoginPage", "signInWithEmail:success");
                                progressBar.setVisibility(View.GONE);
                                progressBarHolder.setVisibility(View.GONE);
                            progressBarHolder.setClickable(false);

                            FirebaseUser user = mAuth.getCurrentUser();

                            // pri resetiranju gesla rabimo referenco na uporabnika
                            final DocumentReference uporabnikRef;
                            uporabnikRef= db.collection("Users").document(""+user.getUid());

                            db.runTransaction(new Transaction.Function<Object>() {
                                @Nullable
                                @Override
                                public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    String upGesloShranjeno = transaction.get(uporabnikRef).get("geslo").toString();

                                    if(geslo != null && !geslo.equals(upGesloShranjeno)) {
                                        transaction.update(uporabnikRef, "geslo", ""+geslo);
                                    }
                                    return upGesloShranjeno;
                                }
                            });

                            Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                            startActivity(intent);
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            progressBarHolder.setVisibility(View.GONE);
                            progressBarHolder.setClickable(false);
                            Log.w("LOG-LoginPage", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Prijava neuspešna",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    // zatemnjeno ozadje za progress barrom
    private class DarkBackgroundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            btn_prijava.setEnabled(false);

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);

            btn_prijava.setEnabled(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < 5; i++) {
                    Log.d("Log Login", "Emulating some task.. Step " + i);
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
