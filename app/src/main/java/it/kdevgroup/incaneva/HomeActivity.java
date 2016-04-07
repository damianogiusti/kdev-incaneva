package it.kdevgroup.incaneva;

import android.os.Bundle;
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
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";

    private RecyclerView recyclerView;  //recycler view che conterrà le carte

    private EventsCardsAdapter cardsAdapter;

    private List<BlogEvent> blogEventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearRecyclerManager = new LinearLayoutManager(getApplicationContext());  //manager per la posizione delle carte
        recyclerView.setLayoutManager(linearRecyclerManager);
/*
        //LISTA PER TESTING
        events = new ArrayList<>();
        BlogEvent t = new BlogEvent();
        t.setBlogName("Nome blog 1");
        t.setPostTitle("Titolo post 1");
        t.setPostContent("Contenuto post 1");
        events.add(t);
        t.setBlogName("Nome blog 2");
        t.setPostTitle("Titolo post 2");
        t.setPostContent("Contenuto post 2");
        events.add(t);
        t.setBlogName("Nome blog 3");
        t.setPostTitle("Titolo post 3");
        t.setPostContent("Contenuto post 3");
        events.add(t);
*/
        

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ESEMPIO DI CHIAMATA
        ApiCallSingleton.getInstance().doCall("6,8", "true", "33", null, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String response = ApiCallSingleton.getInstance().validateResponse(new String(responseBody));
                            if (response != null) {
                                blogEventList = JSONParser.getInstance().parseJsonResponse(response);
                                Log.d(TAG, "onSuccess: ");
                                cardsAdapter = new EventsCardsAdapter(blogEventList);  //adapter personalizzato che accetta la lista di eventi
                                                        //si occuperà di popolare N cards con N eventi
                                recyclerView.setAdapter(cardsAdapter);          //l'adapter restituirà le cards popolate alla view
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
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
        List<BlogEvent> filteredList = new ArrayList<>();

        switch(id){     //creazione lista temporanea in base al filtro da reinserire nell'adapter
            case R.id.all_events:
                break;
            case R.id.nature_events:
                for(BlogEvent event: blogEventList){            //foreach per filtrare senza effettuare di nuovo la connessione
                    if(event.getCategoryName().contains("natura"))
                        filteredList.add(event);
                }
                cardsAdapter = new EventsCardsAdapter(filteredList);    //senza librerie esterne bisogna rifare l'adapter
                recyclerView.setAdapter(cardsAdapter);                  //non funziona lo stesso dio cane
                break;
            case R.id.history_events:
                for(BlogEvent event: blogEventList){
                    if(event.getCategoryName().contains("storia"))
                        filteredList.add(event);
                }
                cardsAdapter = new EventsCardsAdapter(filteredList);
                recyclerView.setAdapter(cardsAdapter);
                break;
            case R.id.food_events:
                for(BlogEvent event: blogEventList){
                    if(event.getCategoryName().contains("enogastronomia"))
                        filteredList.add(event);
                }
                cardsAdapter = new EventsCardsAdapter(filteredList);
                recyclerView.setAdapter(cardsAdapter);
                break;
            case R.id.sport_events:
                for(BlogEvent event: blogEventList){
                    if(event.getCategoryName().contains("sport"))
                        filteredList.add(event);
                }
                cardsAdapter = new EventsCardsAdapter(filteredList);
                recyclerView.setAdapter(cardsAdapter);
                break;
            case R.id.passions_events:
                for(BlogEvent event: blogEventList){
                    if(event.getCategoryName().contains("passioni"))
                        filteredList.add(event);
                }
                cardsAdapter = new EventsCardsAdapter(filteredList);
                recyclerView.setAdapter(cardsAdapter);
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
