package it.kdevgroup.incaneva;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mattia on 08/04/16.
 */
public class ColorManager {

    private final Map<String, String> colori = new HashMap<>();

    public ColorManager(){
        colori.put("eventi", "#ed811c");
        colori.put("storia-cultura", "#bd2c16");
        colori.put("natura", "#7d9e22");
        colori.put("enogastronomia", "#fab71e");
        colori.put("sport", "#54ccca");
        colori.put("passioni", "#903c5e");
    }

    public String getHexColor(String eventName){
        return colori.get(eventName);
    }
}
