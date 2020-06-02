package tva.how;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tva.how.NewsActivity;
import tva.how.NewsRecyclerViewHolder;
import tva.how.R;
import tva.how.classesFirebase.Novice;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewHolder> {

    NewsActivity newsActivity;
    List<Novice> listNovice;

    public NewsRecyclerViewAdapter(NewsActivity newsActivity, List<Novice> listNovice) {
        this.newsActivity = newsActivity;
        this.listNovice = listNovice;
    }

    @NonNull
    @Override
    public NewsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(newsActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.layout_text_view_news, parent, false);

        return new NewsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRecyclerViewHolder holder, int position) {

        holder.tv_novice_naslov.setText(listNovice.get(position).getNaslov_novice());
        holder.tv_novice_datum.setText(listNovice.get(position).getDatum_novice());
        holder.tv_novice_besedilo.setText(listNovice.get(position).getVsebina_novice());
        holder.tv_novice_kraj.setText(listNovice.get(position).getKraj());

        // Da je vsaka druga vrstica drugačne barve
        if(position%2 == 0) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(newsActivity, R.color.colorWhite));
        }
        else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(newsActivity, R.color.colorAccentBright));
        }

    }

    @Override
    public int getItemCount() {
        return listNovice.size();
    }
}
