package it.kdevgroup.incaneva;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by damiano on 07/04/16.
 */
public class JSONParser {
    public static final String DATA = "data";
    private String jsonBody;

    public JSONParser(String str) {
        this.jsonBody = str;
    }

    public JSONParser() {

    }

    public void setJsonBody(String str) {
        this.jsonBody = str;
    }

    public String getJsonBody() {

        return jsonBody;
    }

    public List<BlogEvent> parseJson() throws Exception {
        List<BlogEvent> eventi = new ArrayList<>();
        JSONObject jsnobject = new JSONObject(jsonBody);
        JSONArray jsonArray = jsnobject.getJSONArray(DATA);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            eventi.add(parseJsonObject(obj));
        }

        return eventi;
    }

    public BlogEvent parseJsonObject(JSONObject obj) throws Exception {
        BlogEvent blogEvent = new BlogEvent();
        blogEvent.setBlogName(obj.getString(BlogEvent.KEY_blogname));
        blogEvent.setBlogNameSlug(obj.getString(BlogEvent.KEY_blogname_slug));
        blogEvent.setID(obj.getInt(BlogEvent.KEY_id));
        blogEvent.setPostTitle(obj.getString(BlogEvent.KEY_post_title));
        blogEvent.setPostContent(obj.getString(BlogEvent.KEY_post_content));
        blogEvent.setCategoryName(obj.getString(BlogEvent.KEY_category_name));
        blogEvent.setCategoryLink(obj.getString(BlogEvent.KEY_category_link));

        JSONArray temp = obj.getJSONArray(BlogEvent.KEY_event_type);
        List<String> tmp = new ArrayList<>();
        for (int i = 0; i < temp.length(); i++) {
            tmp.add(temp.getString(i));
        }
        blogEvent.setEventType(tmp);

        blogEvent.setImageLink(obj.getString(BlogEvent.KEY_post_thumbnail));
        blogEvent.setStartTime(obj.getLong(BlogEvent.KEY_evcal_srow));
        blogEvent.setEndTime(obj.getLong(BlogEvent.KEY_evcal_srow));
        blogEvent.setEventColor(obj.getString(BlogEvent.KEY_evcal_event_color));


        return blogEvent;


    }

}
