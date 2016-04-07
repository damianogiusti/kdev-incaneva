package it.kdevgroup.incaneva;

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

    public void setStr(String str) {
        this.jsonBody = str;
    }

    public String getStr() {

        return jsonBody;
    }
}
