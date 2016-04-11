package it.kdevgroup.incaneva;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

/**
 * Created by damiano on 07/04/16.
 */

public class ApiCallSingleton {

    public static final String API_URL = "http://incaneva.it/wp-admin/admin-ajax.php";
    private static final String TAG = "ApiCallSingleton";

    private static ApiCallSingleton ourInstance = null;
    private boolean connectionOpen = false;
    private String result;

    public static ApiCallSingleton getInstance() {
        if (ourInstance == null) {
            ourInstance = new ApiCallSingleton();
        }
        return ourInstance;
    }

    private ApiCallSingleton() {

    }

    /**
     * Effettua la chiamata alle API
     *
     * @param blogValue   OBBLIGATORIO
     * @param oldValue
     * @param limitValue
     * @param offsetValue
     * @param filterValue
     * @return <b>String</b> risultato <br>null se la chiamata non ha prodotto risultati
     */
    public void doCall(String blogValue,
                       String oldValue,
                       String limitValue,
                       String offsetValue,
                       String filterValue,
                       AsyncHttpResponseHandler handler) {

        final String action = "action";
        final String actionValue = "incaneva_events";
        final String blog = "blog";
        final String old = "old";
        final String limit = "limit";
        final String offset = "offset";
        final String filter = "filter";


        RequestParams requestParams = new RequestParams();
        requestParams.add(action, actionValue);
        requestParams.add(blog, blogValue);

        if (oldValue != null) {
            requestParams.add(old, oldValue);
        }

        if (limitValue != null) {
            requestParams.add(limit, limitValue);
        }

        if (offsetValue != null) {
            requestParams.add(offset, offsetValue);
        }

        if (filterValue != null) {
            requestParams.add(filter, filterValue);
        }

        connectionOpen = true;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(API_URL, requestParams, handler);
    }

    /**
     * Valida il responso passato per parametro
     *
     * @param response stringa che contiene il responso
     * @return la stringa con il responso o null se c'è un errore
     * @throws Exception
     */
    public String validateResponse(String response) throws Exception {
        JSONObject object = new JSONObject(response);
        boolean success = object.getBoolean("success");
        if (success) {
            return response;
        } else {
            Log.e(TAG, "onSuccess: " + object.getString("errorMessage"));
            return null;
        }
    }

    public boolean isConnectionOpen(){
        return connectionOpen;
    }

    public void setConnectionClosed(){
        connectionOpen = false;
    }

}
