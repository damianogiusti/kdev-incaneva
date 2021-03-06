package it.kdevgroup.incaneva;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mattia on 07/04/16.
 */

public class EventsCardsAdapter extends RecyclerView.Adapter<EventsCardsAdapter.CardViewHolder> {

    private ArrayList<BlogEvent> events;  //lista di eventi
    private Context ctx;
    private int filter;

    public EventsCardsAdapter(ArrayList<BlogEvent> events, Context ctx, int filter) {
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
     * Crea una card, chiamato ogni volta che deve essere mostrata una CardView
     *
     * @param cardHolder CardViewHolder restituito dal metodo precedente
     * @param position   posizione di un evento nella lista
     */
    @Override
    public void onBindViewHolder(CardViewHolder cardHolder, final int position) {
        cardHolder.blogName.setText(events.get(position).getBlogName());
        // carico l'immagine con picasso

//        Picasso.with(ctx).setIndicatorsEnabled(true);

        Picasso.with(ctx)
                .load(events.get(position).getImageLink())
                .fit()
//                .networkPolicy(NetworkPolicy.OFFLINE, NetworkPolicy.NO_CACHE)
                .into(cardHolder.postImage);
        cardHolder.postTitle.setText(events.get(position).getPostTitle());
        if (events.get(position).getPostContent().length() < 130)
            cardHolder.postContent.setText(events.get(position).getPostContent());
        else
            cardHolder.postContent.setText(events.get(position).getPostContent().subSequence(0, 130) + "...");

        if (filter == R.id.nav_all) {
            cardHolder.btnShowMore.setTextColor(Color.parseColor(events.get(position).getEventColor()));
            cardHolder.calendario.setBackgroundColor(Color.parseColor(events.get(position).getEventColor()));
        } else {
            cardHolder.btnShowMore.setTextColor(Color.parseColor(CategoryColorManager.getInstance().getHexColor(filter)));
            cardHolder.calendario.setBackgroundColor(Color.parseColor(CategoryColorManager.getInstance().getHexColor(filter)));
        }
        // controllo se l'evento è finito
        Date eventEndDate = new Date(events.get(position).getEndTime() * 1000); // la data x1000 perchè vuole i ms e non i secondi
        Date eventStartDate = new Date(events.get(position).getStartTime() * 1000);
        Date todayDate = new Date();
        if (eventEndDate.before(todayDate)) {
            cardHolder.txtExpired.setVisibility(View.VISIBLE);
            cardHolder.txtExpired.setText("TERMINATO");
            cardHolder.txtExpired.setTextColor(Color.parseColor("#bd2c16"));
        } else if (eventStartDate.before(todayDate)) {
            cardHolder.txtExpired.setVisibility(View.VISIBLE);
            cardHolder.txtExpired.setText("IN CORSO...");
            cardHolder.txtExpired.setTextColor(Color.parseColor("#ffcc00"));
        }

        Date date = new Date(events.get(position).getStartTime() * 1000);

        // es. LUNEDI
        SimpleDateFormat dayofweekFormatter = new SimpleDateFormat("EEE", ctx.getResources().getConfiguration().locale);
        cardHolder.day.setText(dayofweekFormatter.format(date).toUpperCase());

        // es. 01
        SimpleDateFormat daynumberFormatter = new SimpleDateFormat("dd", ctx.getResources().getConfiguration().locale);
        cardHolder.daynumber.setText(daynumberFormatter.format(date));

        // es. FEBBRAIO 2016
        SimpleDateFormat monthYearFormatter = new SimpleDateFormat("MMMM yyyy", ctx.getResources().getConfiguration().locale);
        cardHolder.month.setText(monthYearFormatter.format(date).toUpperCase());

        // es. ORE 1:00
        SimpleDateFormat hoursFormatter = new SimpleDateFormat("H:ss", ctx.getResources().getConfiguration().locale);
        cardHolder.hour.setText("ORE " + hoursFormatter.format(date));
        /*
        if (events.get(position).getEventMinute().equals("")) {
            cardHolder.hour.setText("");
        } else {
            cardHolder.hour.setText("ORE " + events.get(position).getEventHour() + ":" +
                    events.get(position).getEventMinute());
        }
*/
        cardHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(ctx, DetailActivity.class);
                Bundle vBundle = new Bundle();
                vBundle.putParcelable(HomeActivity.BUNDLE_KEY_FOR_ARRAY, events.get(position));
                vIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                vIntent.putExtras(vBundle);
                ctx.startActivity(vIntent);
            }
        });

        cardHolder.btnShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vIntent = new Intent(ctx, DetailActivity.class);
                Bundle vBundle = new Bundle();
                vBundle.putParcelable(HomeActivity.BUNDLE_KEY_FOR_ARRAY, events.get(position));
                vIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                vIntent.putExtras(vBundle);
                ctx.startActivity(vIntent);
            }
        });

        //Log.i("Adapter: ", "Card creata");
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void addEvents(List<BlogEvent> eventsToAdd) {
        for (BlogEvent newEvent : eventsToAdd) {
            events.add(newEvent);
            notifyItemInserted(events.size() - 1);
        }
    }

    public String dayoftheweek(String dowt) {
        String i = dowt;
        String dayString;
        switch (i) {
            case "1":
                dayString = "LUN";
                break;
            case "2":
                dayString = "MAR";
                break;
            case "3":
                dayString = "MER";
                break;
            case "4":
                dayString = "GIO";
                break;
            case "5":
                dayString = "VEN";
                break;
            case "6":
                dayString = "SAB";
                break;
            case "7":
                dayString = "DOM";
                break;
            default:
                dayString = "";
        }

        return dayString;
    }

    public String monthoftheYear(String moty) {
        String i = moty;
        String monthString;
        switch (i) {
            case "01":
                monthString = "GENNAIO ";
                break;
            case "02":
                monthString = "FEBBRAIO ";
                break;
            case "03":
                monthString = "MARZO ";
                break;
            case "04":
                monthString = "APRILE ";
                break;
            case "05":
                monthString = "MAGGIO ";
                break;
            case "06":
                monthString = "GIUGNO ";
                break;
            case "07":
                monthString = "LUGLIO ";
                break;
            case "08":
                monthString = "AGOSTO ";
                break;
            case "09":
                monthString = "SETTEMBRE ";
                break;
            case "10":
                monthString = "OTTOBRE ";
                break;
            case "11":
                monthString = "NOVEMBRE ";
                break;
            case "12":
                monthString = "DICEMBRE ";
                break;
            default:
                monthString = "";
        }
        return monthString;
    }

    /**
     * "Contenitore" di ogni card
     */
    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView blogName;
        TextView postTitle;
        TextView postContent;
        ImageView postImage;
        Button btnShowMore;
        TextView day;
        TextView month;
        TextView hour;
        TextView daynumber;
        TextView txtExpired;
        LinearLayout calendario;
        RelativeLayout contenuto;

        CardViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.cardView);
            blogName = (TextView) itemView.findViewById(R.id.blogName);
            postTitle = (TextView) itemView.findViewById(R.id.postTitle);
            postContent = (TextView) itemView.findViewById(R.id.postContent);
            postImage = (ImageView) itemView.findViewById(R.id.postImage);
            btnShowMore = (Button) itemView.findViewById(R.id.btnMoreInfo);
            day = (TextView) itemView.findViewById(R.id.day);
            month = (TextView) itemView.findViewById(R.id.event_month);
            hour = (TextView) itemView.findViewById(R.id.event_hour);
            daynumber = (TextView) itemView.findViewById(R.id.daynumber);
            calendario = (LinearLayout) itemView.findViewById(R.id.calendar);
            txtExpired = (TextView) itemView.findViewById(R.id.txtExpired);
            contenuto = (RelativeLayout) itemView.findViewById(R.id.contenuto);
        }
    }

}