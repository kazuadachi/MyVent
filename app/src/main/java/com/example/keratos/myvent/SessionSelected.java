package com.example.keratos.myvent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class SessionSelected extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_session);

        TextView sessionDate = findViewById(R.id.dateValue);
        TextView usernameMatric = findViewById(R.id.usernameValue);
        TextView smallValue = findViewById(R.id.smallValue);
        TextView mediumValue = findViewById(R.id.mediumValue);
        TextView largeValue = findViewById(R.id.largeValue);
        TextView costValue = findViewById(R.id.costValue);
        Button check_in = findViewById(R.id.checkIn);
        final Button check_out = findViewById(R.id.checkOut);
        Button back2MainMenu = findViewById(R.id.back2MainMenu);

        Intent intent = getIntent();
        final String username_string = intent.getStringExtra("username");
        final Integer username = Integer.parseInt(username_string);
        final String session_date = intent.getStringExtra("sessionDate");
        final String matric = Integer.toString(intent.getIntExtra("student_matric", -1));
        final String smallItem = Integer.toString(intent.getIntExtra("smallItem", -1));
        final String mediumItem = Integer.toString(intent.getIntExtra("mediumItem", -1));
        final String largeItem = Integer.toString(intent.getIntExtra("largeItem", -1));
        final String qrCode = intent.getStringExtra("qrCode");
        final String totalCost = Double.toString(intent.getDoubleExtra("totalCost", -1));

        sessionDate.setText(session_date);
        usernameMatric.setText(matric);
        smallValue.setText(smallItem);
        mediumValue.setText(mediumItem);
        largeValue.setText(largeItem);
        costValue.setText(totalCost);

        check_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject checkINresponse = new JSONObject(response);
                            boolean checkINsuccess = checkINresponse.getBoolean("success");
                            String matricResponse = checkINresponse.getString("matric");

                            if(checkINsuccess == true){
                                final AlertDialog dialogBuilder = new AlertDialog.Builder(SessionSelected.this).create();
                                LayoutInflater inflater = SessionSelected.this.getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                                final TextView checkinTitle = dialogView.findViewById(R.id.sessionDialogTitle);
                                final TextView checkinMsg = dialogView.findViewById(R.id.sessionStatusMsg);
                                final Button proceed = dialogView.findViewById(R.id.proceed);
                                checkinTitle.setText(matricResponse);
                                checkinMsg.setText(R.string.checkINmsg);

                                proceed.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent successCheckIN = new Intent(SessionSelected.this, StaffHome.class);
                                        successCheckIN.putExtra("username", username); //Integer
                                        SessionSelected.this.startActivity(successCheckIN);
                                        SessionSelected.this.finish();
                                        dialogBuilder.dismiss();
                                    }
                                });
                                dialogBuilder.setView(dialogView);
                                dialogBuilder.show();
                            } else if (!checkINsuccess){
                                final AlertDialog dialogBuilder = new AlertDialog.Builder(SessionSelected.this).create();
                                LayoutInflater inflater = SessionSelected.this.getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                                final TextView checkinTitle = dialogView.findViewById(R.id.sessionDialogTitle);
                                final TextView checkinMsg = dialogView.findViewById(R.id.sessionStatusMsg);
                                final Button proceed = dialogView.findViewById(R.id.proceed);
                                checkinTitle.setText(R.string.checkINtitle);
                                checkinMsg.setText(R.string.checkINfail);

                                proceed.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder.dismiss();
                                    }
                                });
                                dialogBuilder.setView(dialogView);
                                dialogBuilder.show();
                            }
                        } catch (JSONException failCheckIN){
                            failCheckIN.printStackTrace();
                        }
                    }
                };
                CheckInOutRequest checkIN = new CheckInOutRequest(username_string, matric, responseListener);
                RequestQueue checkINqueue = Volley.newRequestQueue(SessionSelected.this);
                checkINqueue.add(checkIN);
            }
        });

        check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkOutToScan = new Intent(SessionSelected.this, ScanVerify.class);
                checkOutToScan.putExtra("username", username); //Integer staff username
                checkOutToScan.putExtra("matric", matric); //String student username
                checkOutToScan.putExtra("qrCode", qrCode);
                SessionSelected.this.startActivity(checkOutToScan);
            }
        });

        back2MainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToMainMenu = new Intent(SessionSelected.this, StaffHome.class);
                backToMainMenu.putExtra("username", username);
                SessionSelected.this.startActivity(backToMainMenu);
                SessionSelected.this.finish();
            }
        });
    }
}
