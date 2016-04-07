package it.kdevgroup.incaneva;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by damiano on 07/04/16.
 */
public class JSONParser {
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

    public List<BlogEvent> parseJson(){
        JSONObject jsnobject = new JSONObject(jsonBody);
        try {
            JSONArray jsonArray = jsnobject.getJSONArray("locations");
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                } catch (Exception e) {
                    Log.d("Cazzo: ", "Non carica il JSON Object");
                }
            }
        }catch (Exception e){
            Log.d("Cazzo: ", "non carica il JSON Array");
        }
    }
}
