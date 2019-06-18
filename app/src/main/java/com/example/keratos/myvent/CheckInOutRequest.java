package com.example.keratos.myvent;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CheckInOutRequest extends StringRequest {
    private static final String CHECK_IN_URL = "https://azriadam.com/checkin.php";
    private static final String CHECK_OUT_URL = "https://azriadam.com/checkout.php";
    private Map<String, String> params;

    public CheckInOutRequest(String username, String matric, Response.Listener<String> listener){
        super(Method.POST,CHECK_IN_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("matric", matric);
    }

    public CheckInOutRequest(String check_out, String username, String matric, Response.Listener<String> listener){
        super(Method.POST,CHECK_OUT_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("matric", matric);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
