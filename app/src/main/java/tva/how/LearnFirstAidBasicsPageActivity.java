package tva.how;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import tva.how.classesFirebase.OsnovePrvePomoci;

public class LearnFirstAidBasicsPageActivity extends AppCompatActivity {

    // Firebase baza
    private FirebaseFirestore db;

    String valueExtra_nazivLekcije;

    TextView tv_naslovLekcije, tv_besediloLekcije;
    Button btn_gotoyoutubevideo;
    ImageView img_slikaLekcija;

    List<OsnovePrvePomoci> listOfOsnove;
    OsnovePrvePomoci osnovePrvePomoci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_first_aid_basics_page);

        db = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            valueExtra_nazivLekcije = bundle.getString("nazivLekcije");
        }

        tv_naslovLekcije = findViewById(R.id.tv_naslovLekcije);
        tv_besediloLekcije = findViewById(R.id.tv_besediloLekcije);
        btn_gotoyoutubevideo = findViewById(R.id.btn_gotoyoutubevideo);
        img_slikaLekcija = findViewById(R.id.img_slikaLekcija);

        db.collection("OsnovePrvePomoci").whereEqualTo("nazivLekcije", valueExtra_nazivLekcije).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        osnovePrvePomoci = new OsnovePrvePomoci();
                        listOfOsnove = new ArrayList<>();

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            osnovePrvePomoci = documentSnapshot.toObject(OsnovePrvePomoci.class);
                        }

                        tv_naslovLekcije.setText(osnovePrvePomoci.getNazivLekcije());
                        tv_besediloLekcije.setText(osnovePrvePomoci.getBesediloLekcije());

                    }
                });


    }
}
