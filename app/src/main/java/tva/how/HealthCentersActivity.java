package tva.how;

/**
 * ZDRAVSTVENI DOMOVI
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import tva.how.classesFirebase.Bolnisnice;
import tva.how.classesFirebase.ZdravstveniDomovi;

public class HealthCentersActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private RecyclerView rv_zdravstveniDomovi;

    HealthCentersRecyclerViewAdapter healthCentersRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_centers);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // pripravi bazo
        db = FirebaseFirestore.getInstance();

        // pripravi recycler view
        rv_zdravstveniDomovi = findViewById(R.id.rv_zdravstveniDomovi);
        rv_zdravstveniDomovi.setHasFixedSize(true);
        rv_zdravstveniDomovi.setLayoutManager(new LinearLayoutManager(this));

        // dobi podatke iz baze
        db.collection("ZdravstveniDomovi")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ZdravstveniDomovi zdravstveniDomovi = new ZdravstveniDomovi();
                        List<ZdravstveniDomovi> zdravstveniDomoviList = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            zdravstveniDomovi = documentSnapshot.toObject(ZdravstveniDomovi.class);
                            zdravstveniDomoviList.add(zdravstveniDomovi);
                        }

                        healthCentersRecyclerViewAdapter = new HealthCentersRecyclerViewAdapter(zdravstveniDomoviList, HealthCentersActivity.this);

                        rv_zdravstveniDomovi.setAdapter(healthCentersRecyclerViewAdapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText( HealthCentersActivity.this, "Problem --------", Toast.LENGTH_LONG).show();
                        Log.w("Problem -----", e.getMessage());
                    }
                });
    }
}
