package com.example.keratos.myvent;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SearchRequest extends StringRequest {
    private static final String MATRIC_SEARCH_URL = "https://azriadam.com/findmatric.php";
    private static final String QR_SEARCH_URL = "https://azriadam.com/findqr.php";
    private Map<String, String> params;

    //Search using student matric number
    public SearchRequest(String username, String matric, Response.Listener<String> listener){
        super(Method.POST, MATRIC_SEARCH_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("matric", matric);
    }

    //Search using QR code from student
    public SearchRequest(String different, String qrCode, String username, Response.Listener<String> listener){
        super(Method.POST, QR_SEARCH_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("qrCode", qrCode);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
