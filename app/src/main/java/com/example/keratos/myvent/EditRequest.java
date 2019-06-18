package com.example.keratos.myvent;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public  class EditRequest extends StringRequest{
    private static final String EDIT_URL = "https://azriadam.com/requestsession.php";
    private Map<String, String> params;

    public EditRequest(String username, Response.Listener<String> listener){
        super(Method.POST, EDIT_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
