package it.kdevgroup.incaneva;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseOptions;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adamo on 12/04/2016.
 */
public class CouchBaseDB {

    private static final String TAG = "CouchBaseDB";

    public Manager man;
    public Database db;
    public Context cntx;

    public final String dbname = "incaneva";

    public CouchBaseDB(Context c) {
        cntx = c;
        createManager();
    }

    /**
     * Crea il database manager
     */
    public void createManager() {
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
            db = man.getDatabase(dbname);
            //db = man.openDatabase(dbname, options);

            Log.d("DB costr", "Database creato\n");


        } catch (CouchbaseLiteException e) {
            Log.d("ECCEZIONE", "Impossibile accedere al database\n");

            return;
        }

    }

    private void saveEvent(BlogEvent b, Document document) throws CouchbaseLiteException {
        Map<String, Object> docContent = new HashMap<>();

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
        document.putProperties(docContent);
    }

    /**
     *
     * @param blogE
     * @throws CouchbaseLiteException
     */
    public void saveEvents(ArrayList<BlogEvent> blogE) throws CouchbaseLiteException {
        for (BlogEvent blogEvent : blogE) {
            // ottengo il documento con l'id passato, null se non esiste
            Document document = db.getExistingDocument(String.valueOf(blogEvent.getID()));

            // il documento esiste, valuto se sovrascriverlo
            if (document != null) {
                Log.d(TAG, "saveEvents: il documento esiste gia");
                BlogEvent event = loadEvent(blogEvent.getID());

                // se l'evento nuovo è diverso, sovrascrivo quello vecchio
                if (!blogEvent.equals(event))
                    saveEvent(blogEvent, document);
            }
            // il documento non esiste, lo creo e lo riempio
            else {
                document = db.getDocument(String.valueOf(blogEvent.getID()));
                saveEvent(blogEvent, document);
            }
        }
        Log.d("numero documenti", "" + db.getDocumentCount());
    }

    public BlogEvent loadEvent(int id) {
        // TODO
        return new BlogEvent();
    }

    public ArrayList<BlogEvent> loadEvents() throws CouchbaseLiteException {
        Map<String, Object> tutto = db.getAllDocs(null);
        ArrayList<BlogEvent> eventi = new ArrayList<BlogEvent>();
        for (String id : tutto.keySet()) {
            Document doc = (Document) tutto.get(id);
            BlogEvent b = new BlogEvent();
            b.setID((Integer) doc.getProperty(BlogEvent.KEY_id));
            b.setBlogName((String) doc.getProperty(BlogEvent.KEY_blogname));
            b.setBlogNameSlug((String) doc.getProperty(BlogEvent.KEY_blogname_slug));
            b.setCategoryLink((String) doc.getProperty(BlogEvent.KEY_category_link));
            b.setCategoryName((String) doc.getProperty(BlogEvent.KEY_category_name));
            ArrayList<String> tmp = new ArrayList<>();
            tmp = (ArrayList<String>) doc.getProperty(BlogEvent.KEY_event_type);
            b.setEventType(tmp);
            b.setPostContent((String) doc.getProperty(BlogEvent.KEY_post_content));
            b.setEventColor((String) doc.getProperty(BlogEvent.KEY_evcal_event_color));
            b.setStartTime((Long) doc.getProperty(BlogEvent.KEY_evcal_start_time_min));
            b.setDayofWeek((String) doc.getProperty(BlogEvent.KEY_evcal_week_day));
            b.setEventDay((String) doc.getProperty(BlogEvent.KEY_post_day_numerical));
            b.setEventMonth((String) doc.getProperty(BlogEvent.KEY_post_month_numerical));
            b.setEventMinute((String) doc.getProperty(BlogEvent.KEY_post_time_hour));
            Log.d("loadEvents", "caricato evento con id" + doc.getProperty(BlogEvent.KEY_id));
            eventi.add(b);
            }

            return eventi;

    }

    public BlogEvent loadSingleEvent(String id) throws CouchbaseLiteException {
        Map<String, Object> tutto = db.getAllDocs(null);
        Document doc = (Document) tutto.get(id);
        BlogEvent b = new BlogEvent();
        b.setID((Integer) doc.getProperty(BlogEvent.KEY_id));
        b.setBlogName((String) doc.getProperty(BlogEvent.KEY_blogname));
        b.setBlogNameSlug((String) doc.getProperty(BlogEvent.KEY_blogname_slug));
        b.setCategoryLink((String) doc.getProperty(BlogEvent.KEY_category_link));
        b.setCategoryName((String) doc.getProperty(BlogEvent.KEY_category_name));
        ArrayList<String> tmp = new ArrayList<>();
        tmp = (ArrayList<String>) doc.getProperty(BlogEvent.KEY_event_type);
        b.setEventType(tmp);
        b.setPostContent((String) doc.getProperty(BlogEvent.KEY_post_content));
        b.setEventColor((String) doc.getProperty(BlogEvent.KEY_evcal_event_color));
        b.setStartTime((Long) doc.getProperty(BlogEvent.KEY_evcal_start_time_min));
        b.setDayofWeek((String) doc.getProperty(BlogEvent.KEY_evcal_week_day));
        b.setEventDay((String) doc.getProperty(BlogEvent.KEY_post_day_numerical));
        b.setEventMonth((String) doc.getProperty(BlogEvent.KEY_post_month_numerical));
        b.setEventMinute((String) doc.getProperty(BlogEvent.KEY_post_time_hour));
        Log.d("loadEvents","caricato evento con id"+doc.getProperty(BlogEvent.KEY_id));
        return b;
    }
}

