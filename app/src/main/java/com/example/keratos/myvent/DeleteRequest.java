package com.example.keratos.myvent;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteRequest extends StringRequest {
    private static final String EDIT_URL = "https://azriadam.com/deletesession.php";
    private Map<String, String> params;

    public DeleteRequest(String username, String password, Response.Listener<String> listener){
        super(Method.POST, EDIT_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
