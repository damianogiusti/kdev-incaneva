package it.kdevgroup.incaneva;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseOptions;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adamo on 12/04/2016.
 */
public class CouchBaseDB {

    public Manager man;
    public Database db;
    public Context cntx;

    public final String dbname = "incaneva";

    public CouchBaseDB(Context c) {
        cntx = c;
        createMan();
    }

    /**
     * Crea il database manager
     */
    public void createMan() {
        try {
            man = new Manager(new AndroidContext(cntx), Manager.DEFAULT_OPTIONS);
            Log.d("MAN Costruttore", "Manager Creato\n");
        } catch (IOException e) {
            Log.d("Eccezione DB", "Impossibile creare l'oggetto Manager");
            e.printStackTrace();
            return;
        }
        if (!Manager.isValidDatabaseName(dbname)) {
            Log.d(" controllo nome db ", "Nome del Database errato");
            return;
        }

        try {
            DatabaseOptions options = new DatabaseOptions();
            options.setCreate(true);
            db=man.getDatabase(dbname);
            //db = man.openDatabase(dbname, options);

            Log.d("DB costr", "Database creato\n");


        } catch (CouchbaseLiteException e) {
            Log.d("ECCEZIONE", "Impossibile accedere al database\n");

            return;
        }

    }

    public void saveEvents(ArrayList<BlogEvent> blogE) {
        for (BlogEvent b : blogE) {
            Map<String, Object> docContent = new HashMap<String, Object>();
            docContent.put(BlogEvent.KEY_id, b.getID());
            docContent.put(BlogEvent.KEY_blogname, b.getBlogName());
            docContent.put(BlogEvent.KEY_blogname_slug, b.getBlogNameSlug());
            docContent.put(BlogEvent.KEY_category_link, b.getCategoryLink());
            docContent.put(BlogEvent.KEY_category_name, b.getCategoryName());
            docContent.put(BlogEvent.KEY_event_type, b.getEventType());
            docContent.put(BlogEvent.KEY_post_content, b.getPostContent());
            docContent.put(BlogEvent.KEY_evcal_event_color, b.getEventColor());
            docContent.put(BlogEvent.KEY_evcal_start_time_min, b.getStartTime());
            docContent.put(BlogEvent.KEY_evcal_week_day, b.getDayofWeek());
            docContent.put(BlogEvent.KEY_post_day_numerical, b.getEventDay());
            docContent.put(BlogEvent.KEY_post_month_numerical, b.getEventMonth());
            docContent.put(BlogEvent.KEY_post_time_hour, b.getEventHour());


            Document document = db.createDocument();
            try {
                    /*
                    QueryOptions P = new QueryOptions();
                    Map<String, Object> tutti = database.getAllDocs(P);
                    BlogEvent all = (BlogEvent) tutti;
                    */

                document.putProperties(docContent);

                Log.d("saveEvents()", "Documento scritto nel database " + dbname + "\n con ID = " + document.getId() + "\n");
            } catch (CouchbaseLiteException e) {
                Log.d("saveEvents()", "Impossibile memorizzare il documento nel database: " + e.toString());
            }
        }
    }

    public ArrayList<BlogEvent> loadEvents() throws CouchbaseLiteException {
        Map<String, Object> tutto = db.getAllDocs(null);
        /*
        Map<String, Object> documento = retrievedDocument.getProperties();
        Log.d(TAG, ""+documento.get("KEY_id"));
         */

        return null;
    }

}
