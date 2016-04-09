package it.kdevgroup.incaneva;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mattia on 08/04/16.
 */
public class CategoryColorManager {

    private static CategoryColorManager instance;
    private final Map<Integer, String> colori;
    private final Map<String, Integer> categorie;

    public static CategoryColorManager getInstance() {
        if (instance == null) {
            instance = new CategoryColorManager();
        }
        return instance;
    }

    private CategoryColorManager() {
        colori = new HashMap<>();
        colori.put(R.id.nav_all, "#ed811c");
        colori.put(R.id.nav_culture, "#bd2c16");
        colori.put(R.id.nav_nature, "#7d9e22");
        colori.put(R.id.nav_food, "#fab71e");
        colori.put(R.id.nav_sport, "#54ccca");
        colori.put(R.id.nav_passions, "#903c5e");

        categorie = new HashMap<>();
        categorie.put("eventi", R.id.nav_all);
        categorie.put("storia-cultura", R.id.nav_culture);
        categorie.put("natura", R.id.nav_nature);
        categorie.put("enogastronomia", R.id.nav_food);
        categorie.put("sport", R.id.nav_sport);
        categorie.put("passioni", R.id.nav_passions);
    }


    public String getHexColor(int eventType) {
        return colori.get(eventType);
    }

    public String getHexColor(String eventType) {
        return colori.get(categorie.get(eventType));
    }

    public String getCategoryName(int id) {
        for (String key : categorie.keySet()) {
            if (categorie.get(key) == id)
                return key;
        }
        return null;
    }

    public boolean existsCategory(String category) {
        return categorie.containsKey(category);
    }

    public Map<Integer, String> getColori() {
        return colori;
    }
}
