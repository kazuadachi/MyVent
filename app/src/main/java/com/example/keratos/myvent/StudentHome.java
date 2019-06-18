package com.example.keratos.myvent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class StudentHome extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_student);

        final Button logoutButton = findViewById(R.id.logoutStudent);
        final Button registerSession = findViewById(R.id.registerSession);
        final Button editOrViewSession = findViewById(R.id.editOrViewSession);

        Intent getUser = getIntent();
        final Integer username = getUser.getIntExtra("username", -1);
        final String password = getUser.getStringExtra("password");
        final String username_string = Integer.toString(username);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent studentLogout = new Intent(StudentHome.this, MainActivity.class);
                StudentHome.this.startActivity(studentLogout);
                StudentHome.this.finish();
            }
        });

        registerSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent studentRegister = new Intent(StudentHome.this, SessionRegister.class);
                studentRegister.putExtra("username", username); //Integer
                studentRegister.putExtra("password", password); //String
                StudentHome.this.startActivity(studentRegister);
            }
        });

        editOrViewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                int smallItem = jsonResponse.getInt("smallItem");
                                int mediumItem = jsonResponse.getInt("mediumItem");
                                int largeItem = jsonResponse.getInt("largeItem");
                                String qrCode = jsonResponse.getString("qrCode");
                                Intent goSuccessEdit = new Intent(StudentHome.this, SessionRegisteredEdit.class);
                                goSuccessEdit.putExtra("username", username);
                                goSuccessEdit.putExtra("password", password);
                                goSuccessEdit.putExtra("smallItem", smallItem);
                                goSuccessEdit.putExtra("mediumItem", mediumItem);
                                goSuccessEdit.putExtra("largeItem", largeItem);
                                goSuccessEdit.putExtra("qrCode", qrCode);
                                StudentHome.this.startActivity(goSuccessEdit);
                            } else {
                                final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(StudentHome.this).create();
                                LayoutInflater inflater = StudentHome.this.getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                                final TextView viewTitle = dialogView.findViewById(R.id.sessionDialogTitle);
                                final TextView viewMsg = dialogView.findViewById(R.id.sessionStatusMsg);
                                final Button proceed = dialogView.findViewById(R.id.proceed);
                                viewTitle.setText(R.string.viewSessionNullTitle);
                                viewMsg.setText(R.string.viewSessionNullMsg);

                                proceed.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder.dismiss();
                                    }
                                });
                                dialogBuilder.setView(dialogView);
                                dialogBuilder.show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                EditRequest cubaSaja = new EditRequest(username_string, responseListener);
                RequestQueue queue = Volley.newRequestQueue(StudentHome.this);
                queue.add(cubaSaja);
            }
        });
    }
}
