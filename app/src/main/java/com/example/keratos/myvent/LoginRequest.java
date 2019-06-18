package com.example.keratos.myvent;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    private static final String LOGIN_URL_STUDENT = "https://azriadam.com/studentlogin.php";
    private static final String LOGIN_URL_STAFF = "https://azriadam.com/stafflogin.php";
    private Map<String, String> params;

    //If from student which is add extra String variable to call
    public LoginRequest(String fromStudent, String username, String password, Response.Listener<String> listener){
        super(Method.POST, LOGIN_URL_STUDENT, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    //else if from staff which is less than one extra String variable to call
    public LoginRequest(String username, String password, Response.Listener<String> listener){
        super(Method.POST, LOGIN_URL_STAFF, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
