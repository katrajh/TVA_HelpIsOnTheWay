package tva.how;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import tva.how.classesFirebase.Bolnisnice;

public class HospitalsRecyclerViewAdapter extends RecyclerView.Adapter<HospitalsRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "HospitalsRecyclerViewAdapter";

    private List<Bolnisnice> listBolnisnice = new ArrayList<>();
    HospitalsActivity hospitalsActivity;
    private Context mContext;

    public HospitalsRecyclerViewAdapter(List<Bolnisnice> listBolnisnice, HospitalsActivity hospitalActivity) {
        this.listBolnisnice = listBolnisnice;
        this.hospitalsActivity = hospitalActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_textview_details, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //holder.image.setText; = itemView.findViewById(R.id.image);

        //Napolnim vrednosti v komponente
        holder.tv_nazivBolnisnice.setText(listBolnisnice.get(position).getBOLNISNICA_NAZIV());
        holder.tv_krajBolnisnice.setText(listBolnisnice.get(position).getKRAJ_NAZIV());
        holder.tv_naslovBolnisnice.setText(listBolnisnice.get(position).getLOKACIJA());
        holder.tv_regijaBolnisnice.setText(listBolnisnice.get(position).getREGIJA_NAZIV());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(hospitalsActivity, MapActivity.class);
                intent.putExtra("status", 2);
                intent.putExtra("kolekcija", "Bolnisnice");
                intent.putExtra("naslov", holder.tv_naslovBolnisnice.getText());

                hospitalsActivity.startActivity(intent);

                Toast.makeText(hospitalsActivity, "kliknil si me haha", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return listBolnisnice.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView tv_nazivBolnisnice;
        TextView tv_krajBolnisnice;
        TextView tv_naslovBolnisnice;
        TextView tv_regijaBolnisnice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Komponente poiščemo znotraj layouta
            image = itemView.findViewById(R.id.image);
            tv_nazivBolnisnice = itemView.findViewById(R.id.tv_nazivBolnisnice);
            tv_krajBolnisnice = itemView.findViewById(R.id.tv_krajBolnisnice);
            tv_naslovBolnisnice = itemView.findViewById(R.id.tv_naslovBolnisnice);
            tv_regijaBolnisnice = itemView.findViewById(R.id.tv_regijaBolnisnice);
        }
    }
}
