package it.kdevgroup.incaneva;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Iterator;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mattia on 07/04/16.
 */

public class EventsCardsAdapter extends RecyclerView.Adapter<EventsCardsAdapter.CardViewHolder> {

    private List<BlogEvent> events;  //lista di eventi
    private Context ctx;
    private int filter;

    public EventsCardsAdapter(List<BlogEvent> events, Context ctx, int filter) {
        this.events = events;
        this.ctx = ctx;
        this.filter = filter;
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
        // carico l'immagine con picasso
        Picasso.with(ctx)
                .load(events.get(position).getImageLink())
                .fit()
//                .networkPolicy(NetworkPolicy.OFFLINE, NetworkPolicy.NO_CACHE)
                .into(cardsHolder.postImage);
        cardsHolder.postTitle.setText(events.get(position).getPostTitle());
        if (events.get(position).getPostContent().length() < 130)
            cardsHolder.postContent.setText(events.get(position).getPostContent());
        else
            cardsHolder.postContent.setText(events.get(position).getPostContent().subSequence(0, 130) + "...");

        if (filter == R.id.nav_all)
            cardsHolder.btnShowMore.setTextColor(Color.parseColor(events.get(position).getEventColor()));
        else {
            cardsHolder.btnShowMore.setTextColor(Color.parseColor(CategoryColorManager.getInstance().getHexColor(filter)));
        }

        cardsHolder.day.setText(dayoftheweek(events.get(position).getDayofWeek()) +
                events.get(position).getEventDay());

        cardsHolder.month.setText(monthoftheYear(events.get(position).getEventMonth()));

        cardsHolder.hour.setText(events.get(position).getEventHour()+":"+
                events.get(position).getEventMinute());

        cardsHolder.btnShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void addEvents(List<BlogEvent> eventsToAdd){
        for(BlogEvent newEvent : eventsToAdd){
            events.add(newEvent);
            notifyItemInserted(events.size()-1);
        }
    }

    public void changeEvents(List<BlogEvent> newEvents, int filter){
        this.filter = filter;
        for(BlogEvent eventToRemove : events){
            notifyItemRemoved(events.indexOf(eventToRemove));
            events.remove(eventToRemove);
        }

        for(BlogEvent newEvent : newEvents){
            events.add(newEvent);
            notifyItemInserted(events.indexOf(newEvent));
        }
    }

    public String dayoftheweek(String dowt){
        String i=dowt;
        String dayString;
        switch (i){
            case "1": dayString="LUN ";
                break;
            case "2": dayString="MAR ";
                break;
            case "3": dayString="MER ";
                break;
            case "4": dayString="GIO ";
                break;
            case "5": dayString="VEN ";
                break;
            case "6": dayString="SAB ";
                break;
            case "7": dayString="DOM ";
                break;
            default: dayString="";
        }

        return dayString;
    }

    public String monthoftheYear(String moty){
        String i=moty;
        String monthString;
        switch (i){
            case "01": monthString="Gennaio";
                break;
            case "02": monthString="Febbraio";
                break;
            case "03": monthString="Marzo";
                break;
            case "04": monthString="Aprile";
                break;
            case "05": monthString="Maggio";
                break;
            case "06": monthString="Giugno";
                break;
            case "07": monthString="Luglio";
                break;
            case "08": monthString="Agosto";
                break;
            case "09": monthString="Settembre";
                break;
            case "10": monthString="Ottobre";
                break;
            case "11": monthString="Novembre";
                break;
            case "12": monthString="Dicembre";
                break;
            default: monthString="";
        }
        return monthString;
    }

    /**
     * "Contenitore" di ogni card
     */
    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView blogName;
        TextView postTitle;
        TextView postContent;
        ImageView postImage;
        Button btnShowMore;
        TextView day;
        TextView month;
        TextView hour;

        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            blogName = (TextView) itemView.findViewById(R.id.blogName);
            postTitle = (TextView) itemView.findViewById(R.id.postTitle);
            postContent = (TextView) itemView.findViewById(R.id.postContent);
            postImage = (ImageView) itemView.findViewById(R.id.postImage);
            btnShowMore = (Button) itemView.findViewById(R.id.btnMoreInfo);
            day=(TextView)itemView.findViewById(R.id.event_day);
            month=(TextView)itemView.findViewById(R.id.event_month);
            hour=(TextView)itemView.findViewById(R.id.event_hour);
        }
    }

}