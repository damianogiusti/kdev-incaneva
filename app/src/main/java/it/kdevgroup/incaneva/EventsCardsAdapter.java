package it.kdevgroup.incaneva;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mattia on 07/04/16.
 */

public class EventsCardsAdapter extends RecyclerView.Adapter<EventsCardsAdapter.CardViewHolder> {

    private List<BlogEvent> events;  //lista di eventi

    public EventsCardsAdapter(List<BlogEvent> events) {
        this.events = events;
    }

    /**
     * Chiamato quando il recycler view ha bisogno di una card per mostrare un evento
     *
     * @param viewGroup view padre di ogni carta (recyclerview in teoria)
     * @param viewType  tipo della view che sarà popolata (CardView)
     * @return oggetto CardViewHolder definito alla fine che setterà i vari TextView presenti nella CardView
     */
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        return new CardViewHolder(v);
    }

    /**
     * Setta i dati nella card
     *
     * @param cardsHolder CardViewHolder restituito dal metodo precedente
     * @param position    posizione di un evento nella lista
     */
    @Override
    public void onBindViewHolder(CardViewHolder cardsHolder, int position) {
        cardsHolder.blogName.setText(events.get(position).getBlogName());
        cardsHolder.postTitle.setText(events.get(position).getPostTitle());
        if (events.get(position).getPostContent().length() < 130)
            cardsHolder.postContent.setText(events.get(position).getPostContent());
        else
            cardsHolder.postContent.setText(events.get(position).getPostContent().subSequence(0, 130) + "...");
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void insertItems(List<BlogEvent> eventsToInsert){
        //svuotamento iniziale della lista di eventi già presenti
        Iterator<BlogEvent> it = events.iterator();
        while(it.hasNext()){
            it.next();
            notifyItemRemoved(events.indexOf(it));
            it.remove();
        }
        it = eventsToInsert.iterator();
        //inserimento dei nuovi elementi ottenuti con il filtro
        /*
        while(it.hasNext()){
            it.next();
            notifyItemRemoved(events.indexOf(it));
        }
        */
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
            cv = (CardView) itemView.findViewById(R.id.cardView);
            blogName = (TextView) itemView.findViewById(R.id.blogName);
            postTitle = (TextView) itemView.findViewById(R.id.postTitle);
            postContent = (TextView) itemView.findViewById(R.id.postContent);
        }
    }

}