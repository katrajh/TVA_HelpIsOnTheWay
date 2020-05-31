package tva.how;

/**
 * NOVICE
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

import tva.how.classesFirebase.Novice;

public class NewsActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private RecyclerView rv_novice;

    NewsRecyclerViewAdapter recyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // pripravi bazo
        db = FirebaseFirestore.getInstance();

        // pripravi recycler view
        rv_novice = findViewById(R.id.rv_novice);
        rv_novice.setHasFixedSize(true);
        rv_novice.setLayoutManager(new LinearLayoutManager(this));

        // dobi podatke iz baze
        db.collection("Novice")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        Novice novice = new Novice();
                        List<Novice> noviceList = new ArrayList<>();

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            novice = documentSnapshot.toObject(Novice.class);
                            noviceList.add(novice);
                        }

                        recyclerViewAdapter = new NewsRecyclerViewAdapter(NewsActivity.this, noviceList);
                        rv_novice.setAdapter(recyclerViewAdapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewsActivity.this, "Problem --------", Toast.LENGTH_LONG).show();
                        Log.w("Problem -----", e.getMessage());
                    }
                });



    }

}
