package it.kdevgroup.incaneva;

import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by damiano on 07/04/16.
 * But developed by andrea on the same date.
 */
public class JSONParser {

    private static JSONParser ourInstance = null;
    private final Map<String, String> colori = new HashMap<>();
    public static final String DATA = "data";

    public static JSONParser getInstance() {
        if (ourInstance == null) {
            ourInstance = new JSONParser();
        }
        return ourInstance;
    }

    private JSONParser() {
        Log.d("test", "JSONParser: costruito");
        colori.put("eventi", "#ed811c");
        colori.put("storia-cultura", "#bd2c16");
        colori.put("natura", "#7d9e22");
        colori.put("enogastronomia", "#fab71e");
        colori.put("sport", "#54ccca");
        colori.put("passioni", "#903c5e");
    }

    /**
     * Effettua il parsing di una risposta json ottenuta dal server
     *
     * @param jsonBody risposta json ottenuta dal server
     * @return lista di oggetti di tipo BlogEvent
     * @throws Exception
     */
    public ArrayList<BlogEvent> parseJsonResponse(String jsonBody) throws Exception {
        ArrayList<BlogEvent> eventi = new ArrayList<>();
        JSONObject jsnobject = new JSONObject(jsonBody);
        JSONArray jsonArray = jsnobject.getJSONArray(DATA);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            eventi.add(parseJsonObject(obj));
        }
        return eventi;
    }

    /**
     * Effettua il parsing di un singolo oggetto JSON
     *
     * @param obj JSONObject da parsare
     * @return oggetto di tipo BlogEvent parsato
     * @throws Exception
     */
    public BlogEvent parseJsonObject(JSONObject obj) throws Exception {
        BlogEvent blogEvent = new BlogEvent();
        blogEvent.setBlogName(obj.getString(BlogEvent.KEY_blogname));
        blogEvent.setBlogNameSlug(obj.getString(BlogEvent.KEY_blogname_slug));
        blogEvent.setID(obj.getInt(BlogEvent.KEY_id));
        blogEvent.setPostTitle(obj.getString(BlogEvent.KEY_post_title));
        blogEvent.setPostContent(Html.fromHtml(obj.getString(BlogEvent.KEY_post_content)));
        //blogEvent.setPostContent(Html.escapeHtml(obj.getString(BlogEvent.KEY_post_content)));
        blogEvent.setCategoryName(obj.getString(BlogEvent.KEY_category_name));
        blogEvent.setCategoryLink(obj.getString(BlogEvent.KEY_category_link));

        JSONArray temp = obj.getJSONArray(BlogEvent.KEY_event_type);
        String eventColor = obj.getString(BlogEvent.KEY_evcal_event_color);
        List<String> tmp = new ArrayList<>();
        for (int i = 0; i < temp.length(); i++) {
            String tipoEvento = temp.getString(i);

            // se il colore dell'evento è presente, lo imposto da API
            if (eventColor.length() > 0) {
                blogEvent.setEventColor(eventColor);
            }
            // se il colore dell'evento non è presente
            else {
                // se non ho gia memorizzato il colore dell'evento,
                // e il tipo di evento rientra nei colori noti
                if (blogEvent.getEventColor() == null &&
                        colori.containsKey(tipoEvento))
                    blogEvent.setEventColor(colori.get(tipoEvento));
            }
            tmp.add(tipoEvento);
        }
        blogEvent.setEventType(tmp);

        blogEvent.setImageLink(obj.getString(BlogEvent.KEY_post_thumbnail));
        blogEvent.setStartTime(obj.getLong(BlogEvent.KEY_evcal_srow));
        blogEvent.setEndTime(obj.getLong(BlogEvent.KEY_evcal_srow));

        return blogEvent;
    }

}
