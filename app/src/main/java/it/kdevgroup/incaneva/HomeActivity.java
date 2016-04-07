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
import com.loopj.android.http.AsyncHttpResponseHandler;
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

        getEventsFromServer("6,8","true","33",null,null);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if(drawer != null)
            drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView != null)
            navigationView.setNavigationItemSelectedListener(this);

    }

    //metodo per ripetere la chiamata personalizzando i parametri da passare in base ai filtri (TODO)
    public void getEventsFromServer(String blogs, String old, String limit, String offset, String eventFilter){

        // ESEMPIO DI CHIAMATA
        ApiCallSingleton.getInstance().doCall(blogs, old, limit, offset, eventFilter, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String response = ApiCallSingleton.getInstance().validateResponse(new String(responseBody));
                            if (response != null) {
                                blogEventList = JSONParser.getInstance().parseJsonResponse(response);
                                Log.d(TAG, "onSuccess: ");
                                cardsAdapter = new EventsCardsAdapter(blogEventList);   //adapter personalizzato che accetta la lista di eventi
                                recyclerView.setAdapter(cardsAdapter);                  //l'adapter gestirà le CardView da inserire nel recycler view
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

        switch(id){
            case R.id.nav_all:
                //TODO chiamata default
                break;
            case R.id.nav_nature:
                //TODO filtro "natura"
                break;
            case R.id.nav_culture:
                //TODO filtro "storia"
                break;
            case R.id.nav_food:
                //TODO filtro "enogastronomia"
                break;
            case R.id.nav_sport:
                //TODO filtro "sport"
                break;
            case R.id.nav_passions:
                //TODO filtro "passioni"
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
