package it.kdevgroup.incaneva;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * Created by mattia on 07/04/16.
 */

public class EventsCardsAdapter  extends RecyclerView.Adapter<EventsCardsAdapter.CardViewHolder>{

    private List<BlogEvent> events;  //lista di eventi

    public EventsCardsAdapter(List<BlogEvent> events){
        this.events = events;
    }

    /**
     * Chiamato quando il recycler view ha bisogno di una card per mostrare un evento
     * @param viewGroup view padre di ogni carta (recyclerview in teoria)
     * @param viewType  tipo della view che sarà popolata (CardView)
     * @return oggetto CardViewHolder definito alla fine che setterà i vari TextView presenti nella CardView
     */
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        CardViewHolder cardsHolder = new CardViewHolder(v);
        return cardsHolder;
    }

    /**
     * Setta i dati nella card
     * @param cardsHolder CardViewHolder restituito dal metodo precedente
     * @param position posizione di un evento nella lista
     */
    @Override
    public void onBindViewHolder(CardViewHolder cardsHolder, int position) {
        cardsHolder.blogName.setText(events.get(position).getBlogName());
        cardsHolder.postTitle.setText(events.get(position).getPostTitle());
        cardsHolder.postContent.setText(events.get(position).getPostContent().substring(0,130)+"...");
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * "Contenitore" di ogni card
     */
    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView blogName;
        TextView postTitle;
        TextView postContent;

        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cardView);
            blogName = (TextView)itemView.findViewById(R.id.blogName);
            postTitle = (TextView)itemView.findViewById(R.id.postTitle);
            postContent = (TextView)itemView.findViewById(R.id.postContent);
        }
    }

}