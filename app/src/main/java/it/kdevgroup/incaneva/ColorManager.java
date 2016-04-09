package it.kdevgroup.incaneva;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mattia on 08/04/16.
 */
public class ColorManager {

    private static ColorManager instance;
    private final Map<Integer, String> colori = new HashMap<>();

    public static ColorManager getInstance() {
        if (instance == null) {
            instance = new ColorManager();
        }
        return instance;
    }

    private ColorManager() {
        colori.put(R.id.nav_all, "#ed811c");
        colori.put(R.id.nav_culture, "#bd2c16");
        colori.put(R.id.nav_nature, "#7d9e22");
        colori.put(R.id.nav_food, "#fab71e");
        colori.put(R.id.nav_sport, "#54ccca");
        colori.put(R.id.nav_passions, "#903c5e");
    }

    public String getHexColor(int eventType) {
        return colori.get(eventType);
    }
}
