package it.kdevgroup.incaneva;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";

    private RecyclerView recyclerView;  //recycler view che conterrà le carte
    private EventsCardsAdapter cardsAdapter;
    private List<BlogEvent> blogEventList;
    private int currentSection;

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

        blogEventList = new ArrayList<>();

        cardsAdapter = new EventsCardsAdapter(blogEventList, getApplicationContext());   //adapter personalizzato che accetta la lista di eventi
        recyclerView.setAdapter(cardsAdapter);                  //l'adapter gestirà le CardView da inserire nel recycler view

        /*TODO chiamare il server con questo metodo quando l'utente arriva alla fine dello scroll
        //Tocheck if  recycler is on bottom
        if(layoutManager.lastCompletelyVisibleItemPosition()==data.size()-1){
            //Its at bottom ..
        }
        */

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        if (!isNetworkAvailable()) {
            Snackbar.make(recyclerView, "Sei offline, Controlla la tua connessione", Snackbar.LENGTH_INDEFINITE).show();
        } else {

            getEventsFromServer("6,8", "true", "33", null, null);
            currentSection = R.id.nav_all;

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

    }

    //metodo per ripetere la chiamata personalizzando i parametri da passare in base ai filtri (TODO)
    public void getEventsFromServer(String blogs, String old, String limit, String offset, String eventFilter) {

        // ESEMPIO DI CHIAMATA
        ApiCallSingleton.getInstance().doCall(blogs, old, limit, offset, eventFilter, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String response = ApiCallSingleton.getInstance().validateResponse(new String(responseBody));
                            if (response != null) {
                                blogEventList = JSONParser.getInstance().parseJsonResponse(response);
                                Log.d(TAG, "onSuccess: ");
                                showEvents(blogEventList);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        error.printStackTrace();
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
    public void showEvents(List<BlogEvent> events) {
        cardsAdapter = new EventsCardsAdapter(blogEventList, getApplicationContext());   //adapter personalizzato che accetta la lista di eventi
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
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_all:
                if (!isNetworkAvailable()) {
                    Snackbar.make(recyclerView, "Sei offline, Controlla la tua connessione", Snackbar.LENGTH_INDEFINITE).show();
                }
                if (currentSection != R.id.nav_all) {
                    getEventsFromServer("1,6,7,8,9", "true", "8", null, null);
                    currentSection = R.id.nav_all;
                }
                break;
            case R.id.nav_nature:
                if (!isNetworkAvailable()) {
                    Snackbar.make(recyclerView, "Sei offline, Controlla la tua connessione", Snackbar.LENGTH_INDEFINITE).show();
                }
                if (currentSection != R.id.nav_nature) {
                    getEventsFromServer("1,6,7,8,9", "true", "8", null, "natura");
                    currentSection = R.id.nav_nature;
                }
                break;
            case R.id.nav_culture:
                if (!isNetworkAvailable()) {
                    Snackbar.make(recyclerView, "Sei offline, Controlla la tua connessione", Snackbar.LENGTH_INDEFINITE).show();
                }
                if (currentSection != R.id.nav_culture) {
                    getEventsFromServer("1,6,7,8,9", "true", "8", null, "cultura");
                    currentSection = R.id.nav_culture;
                }
                break;
            case R.id.nav_food:
                if (!isNetworkAvailable()) {
                    Snackbar.make(recyclerView, "Sei offline, Controlla la tua connessione", Snackbar.LENGTH_INDEFINITE).show();
                }
                if (currentSection != R.id.nav_food) {
                    getEventsFromServer("1,6,7,8,9", "true", "8", null, "enogastronomia");
                    currentSection = R.id.nav_food;
                }
                break;
            case R.id.nav_sport:
                if (!isNetworkAvailable()) {
                    Snackbar.make(recyclerView, "Sei offline, Controlla la tua connessione", Snackbar.LENGTH_INDEFINITE).show();
                }
                if (currentSection != R.id.nav_sport) {
                    getEventsFromServer("1,6,7,8,9", "true", "8", null, "sport");
                    currentSection = R.id.nav_sport;
                }
                break;
            case R.id.nav_passions:
                if (!isNetworkAvailable()) {
                    Snackbar.make(recyclerView, "Sei offline, Controlla la tua connessione", Snackbar.LENGTH_INDEFINITE).show();
                }
                if (currentSection != R.id.nav_passions) {
                    getEventsFromServer("1,6,7,8,9", "true", "8", null, "passioni");
                    currentSection = R.id.nav_passions;
                }
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Metodo che controlla la possibilità di accedere a internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
