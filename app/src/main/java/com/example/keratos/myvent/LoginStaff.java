package com.example.keratos.myvent;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginStaff extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_login);

        final EditText staffUsername = findViewById(R.id.username_staff_textbox);
        final EditText staffPassword = findViewById(R.id.password_staff_textbox);
        final Button submitStaff = findViewById(R.id.submit_staff);

        submitStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = staffUsername.getText().toString();
                final String password = staffPassword.getText().toString();
                staffUsername.getText().clear();
                staffPassword.getText().clear();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success){
                                //go to staff homepage if successful login
                                String staffUsername =jsonResponse.getString("username");
                                Integer username = Integer.parseInt(staffUsername);
                                Intent gotoHomepage = new Intent(LoginStaff.this, StaffHome.class);
                                gotoHomepage.putExtra("username", username); //Integer

                                LoginStaff.this.startActivity(gotoHomepage);
                            } else {
                                AlertDialog.Builder alert = new AlertDialog.Builder(LoginStaff.this);
                                alert.setMessage("Login failed. Either wrong username, password or both")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest request2Login = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginStaff.this);
                queue.add(request2Login);
            }
        });
    }
}
