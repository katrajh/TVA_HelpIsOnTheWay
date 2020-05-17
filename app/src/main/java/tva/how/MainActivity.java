package tva.how;

/**
 * PRVI ZASLON (gumb za registracijo/prijavo)
 */

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

    }

    @Override
    public void onStart() {
        super.onStart();

        currentUser = mAuth.getCurrentUser();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(currentUser != null) {
            Log.w("LOG", "currentUser: "+currentUser.getUid());

            Intent intent = new Intent(this, HomeScreenActivity.class);
            startActivity(intent);

            //da se zaklučijo vsi procesi
            finish();
        }
        else {
            // do nothing
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        showPopupDialog(this, "HOW - Help is on the way", "Ali želite zapustiti aplikacijo?", "Da", "Ne");
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

                mAuth.signOut();
                // IZHOD iz aplikacije na home screen mobitela
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    public void btn_action_login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
