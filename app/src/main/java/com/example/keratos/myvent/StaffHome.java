package com.example.keratos.myvent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class StaffHome extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_staff);

        final Button logoutButton = findViewById(R.id.logoutStaff);
        final Button searchByMatric = findViewById(R.id.searchMatric);
        final Button searchByQR = findViewById(R.id.qrScan);
        final EditText matricText = findViewById(R.id.matric);

        Intent getIntent = getIntent();
        final Integer username = getIntent.getIntExtra("username", -1);
        final String username_string = Integer.toString(username);

        searchByMatric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String matric_string = matricText.getText().toString();
                matricText.getText().clear();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject matricResponse = new JSONObject(response);
                            boolean success = matricResponse.getBoolean("success");
                            String session_date = matricResponse.getString("sessionDate");
                            Integer response_matric = matricResponse.getInt("matric");
                            Integer smallItem = matricResponse.getInt("smallItem");
                            Integer mediumItem = matricResponse.getInt("mediumItem");
                            Integer largeItem = matricResponse.getInt("largeItem");
                            String qr_code = matricResponse.getString("qrCode");
                            Double totalCost = matricResponse.getDouble("totalCost");
                            Log.d("Response", response);

                            if(success){
                                Intent goSessionPage = new Intent(StaffHome.this, SessionSelected.class);
                                goSessionPage.putExtra("username", username_string);
                                goSessionPage.putExtra("sessionDate", session_date);
                                goSessionPage.putExtra("student_matric", response_matric);
                                goSessionPage.putExtra("smallItem", smallItem);
                                goSessionPage.putExtra("mediumItem", mediumItem);
                                goSessionPage.putExtra("largeItem", largeItem);
                                goSessionPage.putExtra("qrCode", qr_code);
                                goSessionPage.putExtra("totalCost", totalCost);
                                StaffHome.this.startActivity(goSessionPage);
                            } else{
                                final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(StaffHome.this).create();
                                LayoutInflater inflater = StaffHome.this.getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                                final TextView viewTitle = dialogView.findViewById(R.id.sessionDialogTitle);
                                final TextView viewMsg = dialogView.findViewById(R.id.sessionStatusMsg);
                                final Button proceed = dialogView.findViewById(R.id.proceed);
                                viewTitle.setText(R.string.searchbyMatricTitle);
                                viewMsg.setText(R.string.searchbyMatricMsg);

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
                SearchRequest searchMatric = new SearchRequest(username_string, matric_string, responseListener);
                RequestQueue queue = Volley.newRequestQueue(StaffHome.this);
                queue.add(searchMatric);
            }
        });

        searchByQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchDefine = "nothing";
                Intent gotoScanner = new Intent(StaffHome.this, Scanner.class);
                gotoScanner.putExtra("username", username_string);
                gotoScanner.putExtra("search", searchDefine);
                StaffHome.this.startActivity(gotoScanner);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent staffLogout = new Intent(StaffHome.this, MainActivity.class);
                StaffHome.this.startActivity(staffLogout);
                StaffHome.this.finish();
            }
        });
    }
}
