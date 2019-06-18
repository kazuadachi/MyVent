package com.example.keratos.myvent;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class SessionRequest extends StringRequest {
    private static final String REGISTER_SESSION_URL = "https://azriadam.com/registersession.php";
    private static final String UPDATE_SESSION_URL = "https://azriadam.com/updatesession.php";
    private Map<String, String> params;

    public SessionRequest(String username, String small, String medium, String large, String cost, Response.Listener<String> listener){
        super(Method.POST, REGISTER_SESSION_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("small", small);
        params.put("medium", medium);
        params.put("large", large);
        params.put("cost", cost);
    }

    //Update Session
    public SessionRequest(String updateString, String username, String small, String medium, String large, String cost, Response.Listener<String> listener){
        super(Method.POST, UPDATE_SESSION_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("small", small);
        params.put("medium", medium);
        params.put("large", large);
        params.put("cost", cost);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
