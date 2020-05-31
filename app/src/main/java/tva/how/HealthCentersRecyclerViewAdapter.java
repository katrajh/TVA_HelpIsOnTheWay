package tva.how;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tva.how.classesFirebase.ZdravstveniDomovi;

public class HealthCentersRecyclerViewAdapter extends RecyclerView.Adapter<HealthCentersRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "HealthCentersRecyclerViewAdapter";

    private List<ZdravstveniDomovi> listZdravstvenihDomov = new ArrayList<>();
    HealthCentersActivity healthCentersActivity;
    private Context mContext;

    public HealthCentersRecyclerViewAdapter(List<ZdravstveniDomovi> listZdravstvenihDomov, HealthCentersActivity healthCentersActivity) {
        this.listZdravstvenihDomov = listZdravstvenihDomov;
        this.healthCentersActivity = healthCentersActivity;
    }

    @NonNull
    @Override
    public HealthCentersRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_textview_details, parent, false);
        HealthCentersRecyclerViewAdapter.ViewHolder holder = new HealthCentersRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HealthCentersRecyclerViewAdapter.ViewHolder holder, final int position) {

        //holder.image.setText; = itemView.findViewById(R.id.image);
        holder.tv_nazivZdravstvenegaDoma.setText(listZdravstvenihDomov.get(position).getZD_NAZIV());
        holder.tv_krajZdravstvenegaDoma.setText(listZdravstvenihDomov.get(position).getKRAJ_NAZIV());
        holder.tv_naslovZdravstvenegaDoma.setText(listZdravstvenihDomov.get(position).getLOKACIJA());
        holder.tv_regijaZdravstvenegaDoma.setText(listZdravstvenihDomov.get(position).getREGIJA_NAZIV());
    }

    @Override
    public int getItemCount() {
        return listZdravstvenihDomov.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView tv_nazivZdravstvenegaDoma;
        TextView tv_krajZdravstvenegaDoma;
        TextView tv_naslovZdravstvenegaDoma;
        TextView tv_regijaZdravstvenegaDoma;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_nazivZdravstvenegaDoma = itemView.findViewById(R.id.tv_nazivBolnisnice);
            tv_krajZdravstvenegaDoma = itemView.findViewById(R.id.tv_krajBolnisnice);
            tv_naslovZdravstvenegaDoma = itemView.findViewById(R.id.tv_naslovBolnisnice);
            tv_regijaZdravstvenegaDoma = itemView.findViewById(R.id.tv_regijaBolnisnice);
        }
    }
}
