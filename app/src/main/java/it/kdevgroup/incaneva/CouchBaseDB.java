package it.kdevgroup.incaneva;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseOptions;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adamo on 12/04/2016.
 */
public class CouchBaseDB {

    private static final String TAG = "CouchBaseDB";
    private static final String KEY_TYPE = "type";
    private static final String VALUE_TYPE = BlogEvent.class.getSimpleName();
    private static final String VIEW_NEW_EVENTS = "NewEventsView";
    private static final String VIEW_OLD_EVENTS = "OldEventsView";
    private static final String DB_NAME = "incaneva";

    public Manager man;
    public Database db;
    public Context cntx;


    public CouchBaseDB(Context c) {
        cntx = c;
        createManager();
        createViewForNewBlogEvents();
    }

    /**
     * Crea il database manager
     */
    private void createManager() {
        try {
            man = new Manager(new AndroidContext(cntx), Manager.DEFAULT_OPTIONS);
            Log.d("MAN Costruttore", "Manager Creato\n");
        } catch (IOException e) {
            Log.d("Eccezione DB", "Impossibile creare l'oggetto Manager");
            e.printStackTrace();
        }
        if (!Manager.isValidDatabaseName(DB_NAME)) {
            Log.d(" controllo nome db ", "Nome del Database errato");

        } else {
            try {
                DatabaseOptions options = new DatabaseOptions();
                options.setCreate(true);
                db = man.getDatabase(DB_NAME);
                //db = man.openDatabase(DB_NAME, options);
                Log.d("DB costr", "Database creato\n");


            } catch (CouchbaseLiteException e) {
                Log.d("ECCEZIONE", "Impossibile accedere al database\n");
                e.printStackTrace();
            }
        }
    }

    /**
     * Crea la View che, tramite una query con la sua map function,
     * servirà ad ottenere i dati di tipo BlogEvent salvati nel DB
     * con data futura rispetto ad oggi.
     */
    private void createViewForNewBlogEvents() {
        View view = db.getView(VIEW_NEW_EVENTS);
        view.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                Object obj = document.get(KEY_TYPE);

                // se ho un valore corrispondente alla chiave "type", allora ho un BlogEvent
                if (obj != null && obj.equals(VALUE_TYPE)) {

                   /* TODO: controllo sulla data per caricare dal DB eventi futuri
                   Object abstractDate = document.get(BlogEvent.KEY_evcal_erow);
                    Date date = null;

                    if (abstractDate instanceof Long)
                        date = new Date((Long) abstractDate);
                    else if (abstractDate instanceof Integer)
                        date = new Date((Integer) abstractDate);

                    // se la data esiste e viene dopo oggi, incluso oggi
                    if (date != null && date.after(new Date())) {
                        BlogEvent event = new BlogEvent(document);
                        emitter.emit(BlogEvent.KEY_id, document.get(BlogEvent.KEY_id)); // salvo id
                        emitter.emit(VALUE_TYPE, event);                                // salvo BlogEvent
                    }*/

                    BlogEvent event = new BlogEvent(document);
                    emitter.emit(BlogEvent.KEY_id, document.get(BlogEvent.KEY_id)); // salvo id
                    emitter.emit(VALUE_TYPE, event);                                // salvo BlogEvent
                }
            }

        }, "1");
    }

    /**
     * Crea la View che, tramite una query con la sua map function,
     * servirà ad ottenere i dati di tipo BlogEvent salvati nel DB
     * con data passata rispetto ad oggi.
     */
    private void createViewForOldBlogEvents() {
        View view = db.getView(VIEW_OLD_EVENTS);
        view.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                Object obj = document.get(KEY_TYPE);

                // se ho un valore corrispondente alla chiave "type", allora ho un BlogEvent
                if (obj != null && obj.equals(VALUE_TYPE)) {
                    BlogEvent event = new BlogEvent(document);

                    emitter.emit(BlogEvent.KEY_id, document.get(BlogEvent.KEY_id)); // salvo id
                    emitter.emit(VALUE_TYPE, event);                                // salvo BlogEvent
                }
            }
        }, "1");
    }

    /**
     * Salva un singolo evento nel database
     *
     * @param blogEvent
     * @param document
     * @throws CouchbaseLiteException
     */
    private void saveEvent(BlogEvent blogEvent, Document document) throws CouchbaseLiteException {
        Map<String, Object> docContent = new HashMap<>();
        if (document.getProperties() != null)
            docContent.putAll(document.getProperties());

        docContent.put(KEY_TYPE, VALUE_TYPE);
        docContent.put(BlogEvent.KEY_id, blogEvent.getID());
        docContent.put(BlogEvent.KEY_blogname, blogEvent.getBlogName());
        docContent.put(BlogEvent.KEY_post_title, blogEvent.getPostTitle());
        docContent.put(BlogEvent.KEY_blogname_slug, blogEvent.getBlogNameSlug());
        docContent.put(BlogEvent.KEY_category_link, blogEvent.getCategoryLink());
        docContent.put(BlogEvent.KEY_category_name, blogEvent.getCategoryName());
        docContent.put(BlogEvent.KEY_event_type, blogEvent.getEventType());
        docContent.put(BlogEvent.KEY_post_content, Html.toHtml(blogEvent.getPostContent()));
        docContent.put(BlogEvent.KEY_evcal_event_color, blogEvent.getEventColor());
        docContent.put(BlogEvent.KEY_evcal_srow, blogEvent.getStartTime());
        docContent.put(BlogEvent.KEY_evcal_erow, blogEvent.getEndTime());
        docContent.put(BlogEvent.KEY_post_time_hour, blogEvent.getEventHour());
        docContent.put(BlogEvent.KEY_post_thumbnail, blogEvent.getImageLink());
        document.putProperties(docContent);

    }

    /**
     * Salva una lista di BlogEvent nel database
     *
     * @param blogEventList lista di eventi
     * @throws CouchbaseLiteException
     */
    public void saveEvents(List<BlogEvent> blogEventList) throws CouchbaseLiteException {
        for (BlogEvent blogEvent : blogEventList) {
            // ottengo il documento con l'id passato, null se non esiste
            Document document = db.getExistingDocument(String.valueOf(blogEvent.getID()));


            // il documento esiste, valuto se sovrascriverlo
            if (document != null) {
                Log.d(TAG, "saveEvents: il documento esiste gia");
                BlogEvent event = loadSingleEvent(String.valueOf(blogEvent.getID()));

                // se l'evento nuovo è diverso, sovrascrivo quello vecchio
                if (!blogEvent.equals(event))
                    saveEvent(blogEvent, document);
            }
            // il documento non esiste, lo creo e lo riempio
            else {
                Log.d(TAG, "saveEvents: creo il documento");
                document = db.getDocument(String.valueOf(blogEvent.getID()));
                saveEvent(blogEvent, document);
            }
        }
        Log.d("numero documenti", "" + db.getDocumentCount());
    }

    /**
     * Carica gli eventi dal database nel ArrayList
     *
     * @return
     * @throws CouchbaseLiteException
     */
    public ArrayList<BlogEvent> loadEvents() throws CouchbaseLiteException {
        Map<String, Object> allDocuments = getAllBlogEventDocs();

        ArrayList<BlogEvent> events = new ArrayList<>();
        for (String id : allDocuments.keySet()) {
            events.add(loadSingleEvent(id));
        }

        return events;

    }

    /**
     * Resistuisce un evento passando l'ID
     *
     * @param id
     * @return
     * @throws CouchbaseLiteException
     */
    private BlogEvent loadSingleEvent(String id) throws CouchbaseLiteException {
//        Map<String, Object> allDocs = db.getAllDocs(null);

        Document doc = db.getDocument(id);
        BlogEvent blogEvent = new BlogEvent(doc.getProperties());
        Log.d("loadEvents", "caricato evento con id = " + doc.getProperty(BlogEvent.KEY_id));
        return blogEvent;
    }

    /**
     * Dice se il database è vuoto
     *
     * @return
     */
    public boolean isDatabaseEmpty() {
        return db.getDocumentCount() == 0;
    }

    /**
     * Restituisce tutti i documenti contenti BlogEvent
     *
     * @return
     * @throws CouchbaseLiteException
     */
    private Map<String, Object> getAllBlogEventDocs() throws CouchbaseLiteException {
        View view = db.getView(VIEW_NEW_EVENTS);
        Query query = view.createQuery();
        query.setMapOnly(true);
        QueryEnumerator result = query.run();

        Map<String, Object> map = new HashMap<>();
        for (QueryRow row : result) {
            map.put(row.getDocumentId(), row.getValue());
        }

        return map;
    }
}

