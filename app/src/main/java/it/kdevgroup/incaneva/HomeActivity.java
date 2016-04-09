package it.kdevgroup.incaneva;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";
    public static final String BUNDLE_KEY_FOR_ARRAY = "listaDiEventi";
    public static final String BUNDLE_KEY_CURRENTSECTION = "sezioneNavigazioneCorrente";

    private RecyclerView recyclerView;  //recycler view che conterrà le carte
    private EventsCardsAdapter cardsAdapter;
    private ArrayList<BlogEvent> blogEventList;
    private int currentSection;
    private Snackbar internetConnection;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearRecyclerManager = new LinearLayoutManager(getApplicationContext());  //manager per la posizione delle carte
        recyclerView.setLayoutManager(linearRecyclerManager);

        // recupero la lista di eventi se ho un savedInstanceState
        if (savedInstanceState != null) {
            blogEventList = savedInstanceState.getParcelableArrayList(BUNDLE_KEY_FOR_ARRAY);
            currentSection = savedInstanceState.getInt(BUNDLE_KEY_CURRENTSECTION);
            Log.d(TAG, "onCreate: trovati elementi nel bundle");
        }
        if (blogEventList == null) {    // se non ho trovato la lista, la istanzio da zero
            blogEventList = new ArrayList<>();
        }
        if (currentSection == 0) {      // se non ho trovato la sezione attuale, la inizializzo
            currentSection = R.id.nav_all;
        }

        //questa chiamata iniziale permette di usare swapAdapter successivamente
        cardsAdapter = new EventsCardsAdapter(blogEventList, getApplicationContext(), currentSection);   //adapter personalizzato che accetta la lista di eventi
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
        this.recyclerView.setLayoutManager(layoutManager);

        // resta in ascolto dello scorrimento della lista di card
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // se è visualizzato l'ultimo elemento, chiamo il server
                if (layoutManager.findLastCompletelyVisibleItemPosition() == blogEventList.size() - 1)
                    Toast.makeText(HomeActivity.this, "Arrivato alla fine", Toast.LENGTH_SHORT).show();
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        if (!isNetworkAvailable()) {
            internetConnection.show();
        } else if (blogEventList.size() == 0) {    // se non ho recuperato i dati dal bundle (o in futuro da database)
            getEventsFromServer("6,8", "true", "33", null, null);
        } else if (blogEventList.size() > 0) {
            showEvents(blogEventList, currentSection);
        }


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
    public void getEventsFromServer(final String blogs, final String old, final String limit, final String offset, final String eventFilter) {

        // ESEMPIO DI CHIAMATA
        ApiCallSingleton.getInstance().doCall(blogs, old, limit, offset, eventFilter, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String response = ApiCallSingleton.getInstance().validateResponse(new String(responseBody));
                            if (response != null) {
                                Log.d(TAG, "onSuccess: chiamata avvenuta con successo");
                                blogEventList = JSONParser.getInstance().parseJsonResponse(response);
                                showEvents(blogEventList, currentSection);
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
//                        getEventsFromServer(blogs, old, limit, offset, eventFilter);
//                        Snackbar.make(recyclerView, "Problema di connessione al server", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }
                }
        );
    }

    /**
     * Aggiorna la lista degli eventi
     *
     * @param events List<> di eventi da mostrare
     */
    public void showEvents(List<BlogEvent> events, int eventFilter) {
        cardsAdapter = new EventsCardsAdapter(events, getApplicationContext(), eventFilter);   //adapter personalizzato che accetta la lista di eventi
        recyclerView.swapAdapter(cardsAdapter, false);                  //l'adapter gestirà le CardView da inserire nel recycler view
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int sezioneCorrente = item.getItemId();
        boolean isNetworkAvailable = isNetworkAvailable();
        if (isNetworkAvailable) {
            String categoryName = CategoryColorManager.getInstance().getCategoryName(sezioneCorrente);
            if (categoryName == null) {
                Log.w(TAG, "onNavigationItemSelected: categoryName non trovato nella mappa");
            }
            getEventsFromServer("1,6,7,8,9", "true", "8", null, categoryName);
            currentSection = sezioneCorrente;
        } else {
            internetConnection.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);

        return isNetworkAvailable;
/*
        switch (sezioneCorrente) {
            case R.id.nav_all:
                if (!isNetworkAvailable()) {
                } else {
                    internetConnection.dismiss();
                    if (currentSection != R.id.nav_all) {
                        getEventsFromServer("1,6,7,8,9", "true", "8", null, null);
                        currentSection = R.id.nav_all;
                    }
                }

                break;
            case R.id.nav_nature:
                if (!isNetworkAvailable()) {
                    internetConnection.show();
                } else {
                    internetConnection.dismiss();
                    if (currentSection != R.id.nav_nature) {
                        getEventsFromServer("1,6,7,8,9", "true", "8", null, "natura");
                        currentSection = R.id.nav_nature;
                    }
                }

                break;
            case R.id.nav_culture:
                if (!isNetworkAvailable()) {
                    internetConnection.show();
                } else {
                    internetConnection.dismiss();
                    if (currentSection != R.id.nav_culture) {
                        getEventsFromServer("1,6,7,8,9", "true", "8", null, "cultura");
                        currentSection = R.id.nav_culture;
                    }
                }

                break;
            case R.id.nav_food:
                if (!isNetworkAvailable()) {
                    internetConnection.show();
                } else {
                    internetConnection.dismiss();
                    if (currentSection != R.id.nav_food) {
                        getEventsFromServer("1,6,7,8,9", "true", "8", null, "enogastronomia");
                        currentSection = R.id.nav_food;
                    }
                }
                break;
            case R.id.nav_sport:
                if (!isNetworkAvailable()) {
                    internetConnection.show();
                } else {
                    internetConnection.dismiss();
                    if (currentSection != R.id.nav_sport) {
                        getEventsFromServer("1,6,7,8,9", "true", "8", null, "sport");
                        currentSection = R.id.nav_sport;
                    }
                }
                break;
            case R.id.nav_passions:
                if (!isNetworkAvailable()) {
                    internetConnection.show();
                } else {
                    internetConnection.dismiss();
                    if (currentSection != R.id.nav_passions) {
                        getEventsFromServer("1,6,7,8,9", "true", "8", null, "passioni");
                        currentSection = R.id.nav_passions;
                    }
                }
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        return isNetworkAvailable();*/
    }

    // Metodo che controlla la possibilità di accedere a internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(this.BUNDLE_KEY_FOR_ARRAY, blogEventList);
        outState.putInt(this.BUNDLE_KEY_CURRENTSECTION, currentSection);
        Log.d(TAG, "onSaveInstanceState: salvo elementi nel bundle");
    }
}
