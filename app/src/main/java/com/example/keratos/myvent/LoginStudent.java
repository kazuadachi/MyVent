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

public class LoginStudent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_login);

        final EditText etUsername = findViewById(R.id.username_student_textbox);
        final EditText etPassword = findViewById(R.id.password_student_textbox);
        final Button submitStudent = findViewById(R.id.submit_student);
        final String student = "student";

        submitStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                etUsername.getText().clear();
                etPassword.getText().clear();


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                //go to student homepage if successful login
                                Integer studentUsername = Integer.parseInt(jsonResponse.getString("username"));
                                String studentPassword = jsonResponse.getString("password");
                                Intent gotoHomepage = new Intent(LoginStudent.this, StudentHome.class);
                                gotoHomepage.putExtra("username", studentUsername);
                                gotoHomepage.putExtra("password", studentPassword);

                                LoginStudent.this.startActivity(gotoHomepage);
                            } else {
                                //Display popup if fail to login
                                AlertDialog.Builder alert = new AlertDialog.Builder(LoginStudent.this);
                                alert.setMessage("Login failed. Either wrong username, password or both.")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                //execute above try block using LoginRequest and Volley class
                LoginRequest request2Login = new LoginRequest(student, username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginStudent.this);
                queue.add(request2Login);
            }
        });
    }
}
