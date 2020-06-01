package tva.how;

/**
 * SEZNAM BOLNIŠNIC
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import tva.how.classesFirebase.AedNaprave;
import tva.how.classesFirebase.Bolnisnice;

public class HospitalsActivity extends AppCompatActivity{

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private RecyclerView rv_bolnisnice;

    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals);

        //Za preverjanje ali je uporabnik prijavljen
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // pripravi bazo
        db = FirebaseFirestore.getInstance();

        // pripravi recycler view
        rv_bolnisnice = findViewById(R.id.rv_bolnisnice);
        rv_bolnisnice.setHasFixedSize(true);
        rv_bolnisnice.setLayoutManager(new LinearLayoutManager(this));

        // dobi podatke iz baze
        db.collection("Bolnisnice")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Bolnisnice bolnisnice = new Bolnisnice();
                        List<Bolnisnice> bolnisniceList = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            bolnisnice = documentSnapshot.toObject(Bolnisnice.class);
                            bolnisniceList.add(bolnisnice);
                        }

                        recyclerViewAdapter = new RecyclerViewAdapter(bolnisniceList,HospitalsActivity.this);
                        rv_bolnisnice.setAdapter(recyclerViewAdapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HospitalsActivity.this, "Problem --------", Toast.LENGTH_LONG).show();
                        Log.w("Problem -----", e.getMessage());
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
