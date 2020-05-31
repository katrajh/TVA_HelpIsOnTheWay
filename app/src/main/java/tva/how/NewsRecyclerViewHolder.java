package tva.how;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_novice_naslov, tv_novice_besedilo, tv_novice_datum, tv_novice_kraj;

    public NewsRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_novice_naslov = itemView.findViewById(R.id.tv_novica_naslov);
        tv_novice_datum = itemView.findViewById(R.id.tv_novica_datum);
        tv_novice_besedilo = itemView.findViewById(R.id.tv_novica_besedilo);
        tv_novice_kraj = itemView.findViewById(R.id.tv_novica_kraj);

    }

}
