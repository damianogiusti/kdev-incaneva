package it.kdevgroup.incaneva;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";
    public static final String BUNDLE_KEY_FOR_ARRAY = "listaDiEventi";
    public static final String BUNDLE_KEY_CURRENTSECTION = "sezioneNavigazioneCorrente";
    public static final String BUNDLE_KEY_SHOWOLDEVENTS = "mostraEventiPassati";
    private final String events_id = "1,6,7,8";

    private RecyclerView recyclerView;  //recycler view che conterrà le carte
    private SwipeRefreshLayout swipeRefreshLayout;
    private EventsCardsAdapter cardsAdapter;
    private ArrayList<BlogEvent> blogEventList;
    private int currentCategory;
    private Snackbar internetConnection;
    private Toolbar toolbar;
    private Snackbar snackNoNewEvents;
    private Snackbar snackLookingForEvents;
    private Toast toastUpdateEvents;
    private boolean showOldEvents = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // recupero la lista di eventi se ho un savedInstanceState
        if (savedInstanceState != null) {
            blogEventList = savedInstanceState.getParcelableArrayList(BUNDLE_KEY_FOR_ARRAY);
            currentCategory = savedInstanceState.getInt(BUNDLE_KEY_CURRENTSECTION);
            showOldEvents = savedInstanceState.getBoolean(BUNDLE_KEY_SHOWOLDEVENTS);
            Log.i(TAG, "onCreate: trovati elementi nel bundle");
        }
        if (blogEventList == null) {    // se non ho trovato la lista, la istanzio da zero
            blogEventList = new ArrayList<>();
        }
        if (currentCategory == 0) {      // se non ho trovato la categoria attuale, la inizializzo
            currentCategory = R.id.nav_all;
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        snackNoNewEvents = Snackbar.make(recyclerView, "Nessun evento da mostrare", Snackbar.LENGTH_SHORT);
        snackLookingForEvents = Snackbar.make(recyclerView, "Cerco eventi passati...", Snackbar.LENGTH_INDEFINITE);

        toastUpdateEvents=Toast.makeText(HomeActivity.this, "Aggiornamento eventi...", Toast.LENGTH_SHORT);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isNetworkAvailable()) {
                    internetConnection.show();
                    swipeRefreshLayout.setRefreshing(false);
                } else {    // se non ho recuperato i dati dal bundle (o in futuro da database)
                    getEventsFromServer("10", null, CategoryColorManager.getInstance().getCategoryName(currentCategory));
                    if(recyclerView.getChildCount() == 0) {
                        loadMore();
                    }
                    Log.i("children: ", ""+recyclerView.getChildCount());
                }
//                toastUpdateEvents.show();
            }
        });

        /**
         * questa chiamata iniziale permette di usare swapAdapter successivamente
         * IMPORTANTE: CON QUESTA CHIAMATA SI LEGANO L'ADAPTER E blogEventList, CHE MODIFICANDOLA
         * CON METODI DELL'ADAPTER (addEvents) VERRÀ MODIFICATA ANCHE NELL'ACTIVITY
         */
        cardsAdapter = new EventsCardsAdapter(blogEventList, getApplicationContext(), currentCategory);   //adapter personalizzato che accetta la lista di eventi, context dell'app e filtro per la categoria
        recyclerView.setAdapter(cardsAdapter);                  //l'adapter gestirà le CardView da inserire nel recycler view

        internetConnection = Snackbar.make(recyclerView, "Sei offline, Controlla la tua connessione", Snackbar.LENGTH_INDEFINITE);

        // --- LAYOUT MANAGER
        /*
        Qui gioco di cast. GridLayoutManager eredita da LinearLayoutManager, quindi lo dichiaro
        come Linear ma lo istanzio come Grid, per poter avere disponibili i metodi del Linear, tra
        i quali quello che mi consente di stabilire qual'è l'ultimo elemento della lista completamente
        visibile. FIGATTAAA
         */
        final LinearLayoutManager layoutManager;
        int colonne = 1;
        // se lo schermo è orizzontale, allora le colonne da utilizzare sono due
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            colonne = 2;
        }
        layoutManager = new GridLayoutManager(this, colonne, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // resta in ascolto dello scorrimento della lista di card
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                boolean enableRefreshCircle = true;

                // se l'elemento visibile è il primo, allora ho la possibilità di aggiornare il contenuto
                if (recyclerView != null && recyclerView.getChildCount() > 0) {
                    enableRefreshCircle = (layoutManager.findFirstCompletelyVisibleItemPosition() == 0);
                }
                swipeRefreshLayout.setEnabled(enableRefreshCircle);

                // se è visualizzato l'ultimo elemento, chiamo il server
                if ((layoutManager.findLastCompletelyVisibleItemPosition() == blogEventList.size() - 1 || blogEventList.size() == 0)
                        && !ApiCallSingleton.getInstance().isConnectionOpen()) {
                    loadMore();
                }
            }
        });

        // --- CONTROLLO CONNESSIONE DATI E CHIAMATA API

        if (!isNetworkAvailable()) {
            internetConnection.show();
        } else if (blogEventList.size() == 0) {    // se non ho recuperato i dati dal bundle (o in futuro da database)
            getEventsFromServer("10", null, null);
        } else if (blogEventList.size() > 0) {
            showFilteredEvents(blogEventList, currentCategory);
        }

        // --- INZIALIZZAZIONE NAVIGATION DRAWER

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null)
            drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
            navigationView.setNavigationItemSelectedListener(this);
    }

    // metodo per ripetere la chiamata personalizzando i parametri da passare in base ai filtri
    public void getEventsFromServer(final String limit, final String offset, final String eventFilter) {

        if (!ApiCallSingleton.getInstance().isConnectionOpen()) {
            // ESEMPIO DI CHIAMATA
            // settato parametro old a false per ottenere un layout dell'app simile alla pagina
            // http://incaneva.it/blog/category/eventi/ e aggirare il fatto che le chiamate al php
            // ritornino l'evento più recente per primo
            ApiCallSingleton.getInstance().doCall(events_id, "false", limit, offset, eventFilter, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                String response = ApiCallSingleton.getInstance().validateResponse(new String(responseBody));
                                if (response != null) {
                                    Log.i(TAG, "onSuccess: chiamata avvenuta con successo, ");
                                    blogEventList = JSONParser.getInstance().parseJsonResponse(response);
                                    Collections.reverse(blogEventList);
                                    Log.i("NUOVA LISTA DA FILTRO", "" + blogEventList.size());
                                    //cardsAdapter.changeEvents(newItems, currentCategory);
                                    showFilteredEvents(blogEventList, currentCategory);
                                }
                            } catch (Exception e) {
                                Snackbar.make(recyclerView, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            Snackbar.make(recyclerView, "Connessione fallita [" + statusCode + "]", Snackbar.LENGTH_LONG).show();
                            error.printStackTrace();
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                            showRefreshCircle(true);    // mostra rotellina caricamento
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            showRefreshCircle(false);   // nasconde rotellina caricamento
                            if(cardsAdapter.getItemCount() == 0)
                                snackNoNewEvents.show();
                            ApiCallSingleton.getInstance().setConnectionClosed();
                        }
                    }
            );
        }
    }

    //chiamata solo per eventi passati
    public void loadMore() {
        //controllo se c'è già una connessione attiva
        if (!ApiCallSingleton.getInstance().isConnectionOpen() && showOldEvents) {
            int offset = 0;
            Date today = new Date();
            Date eventStartDate;
            for(BlogEvent event : blogEventList){
                eventStartDate = new Date(event.getStartTime() * 1000);
                if(!eventStartDate.before(today))
                    offset++;
            }
            ApiCallSingleton.getInstance().doCall(
                    events_id,
                    "true", // voglio eventi passati
                    "6",
                    Integer.toString(offset),
                    CategoryColorManager.getInstance().getCategoryName(currentCategory),
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                String response = ApiCallSingleton.getInstance().validateResponse(new String(responseBody));
                                if (response != null) {
                                    Log.i(TAG, "onSuccess: chiamata avvenuta con successo");
                                    final List<BlogEvent> newItems;
                                    if ((newItems = JSONParser.getInstance().parseJsonResponse(response)).size() > 0) { //controllo se l'array nuovo non è vuoto
                                        cardsAdapter.addEvents(newItems);
                                        Log.i("blogEventList more", "" + blogEventList.size());
                                        Log.i("more events", "" + newItems.size());
                                        recyclerView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //call smooth scroll
                                                recyclerView.smoothScrollToPosition(cardsAdapter.getItemCount() - newItems.size());
                                            }
                                        });
                                    } else {
                                        snackNoNewEvents.show();
                                    }
                                }
                            } catch (JSONException e) {
                                snackNoNewEvents.show();
                                e.printStackTrace();
                            } catch (Exception e) {
                                Snackbar.make(recyclerView, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            Snackbar.make(recyclerView, "Connessione fallita [" + statusCode + "]", Snackbar.LENGTH_LONG).show();
                            error.printStackTrace();
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                            snackLookingForEvents.show();
                            showRefreshCircle(true);
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            snackLookingForEvents.dismiss();
                            showRefreshCircle(false);
                            ApiCallSingleton.getInstance().setConnectionClosed();
                        }
                    }
            );
        }
    }

    /**
     * Cambia la lista degli eventi
     *
     * @param newEvents   nuova lista da mostrare
     * @param eventFilter filtro da usare per le Card
     */
    public void showFilteredEvents(ArrayList<BlogEvent> newEvents, int eventFilter) {
        //cardsAdapter.changeEvents(eventFilter);
        cardsAdapter = new EventsCardsAdapter(newEvents, getApplicationContext(), eventFilter);
        recyclerView.swapAdapter(cardsAdapter, false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.show_old_events).setChecked(showOldEvents);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.show_old_events) {
            showOldEvents = !showOldEvents;
            item.setChecked(showOldEvents);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int categoriaScelta = item.getItemId();
        boolean isNetworkAvailable = isNetworkAvailable();
        if (isNetworkAvailable) {
            internetConnection.dismiss();
            String categoryName = CategoryColorManager.getInstance().getCategoryName(categoriaScelta);
            if (categoryName == null) {
                Log.w(TAG, "onNavigationItemSelected: categoryName non trovato nella mappa");
            }
            //questo if è per evitare chiamate inutili se sono già su quella categoria
            if (categoriaScelta != currentCategory) {
                getEventsFromServer("8", null, categoryName);
                currentCategory = categoriaScelta;
            }
        } else {
            internetConnection.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);

        return isNetworkAvailable;
    }

    // Metodo che controlla la possibilità di accedere a internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Mostra il pallino di refresh della pagina
     * @param flag
     */
    private void showRefreshCircle(final boolean flag) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(flag);
                if (flag) {
                    swipeRefreshLayout.setColorSchemeColors(
                            Color.parseColor(CategoryColorManager.getInstance().getHexColor(currentCategory)));
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLE_KEY_FOR_ARRAY, blogEventList);
        outState.putInt(BUNDLE_KEY_CURRENTSECTION, currentCategory);
        outState.putBoolean(BUNDLE_KEY_SHOWOLDEVENTS, showOldEvents);
        Log.i(TAG, "onSaveInstanceState: salvo elementi nel bundle");
    }
}
