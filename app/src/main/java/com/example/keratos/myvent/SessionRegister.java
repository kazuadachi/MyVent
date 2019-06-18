package com.example.keratos.myvent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionRegister extends AppCompatActivity {
    private int small, medium, large;
    private double cost, s_value = 2.0, m_value = 5.0, l_value = 10.0;
    private String s_string, m_string, l_string, cost_string;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_session);

        final Spinner smallItems = findViewById(R.id.getSmallItems);
        final Spinner medItems = findViewById(R.id.getMediumItems);
        final Spinner largeItems = findViewById(R.id.getLargeItems);
        final Button register = findViewById(R.id.register);
        final Button back = findViewById(R.id.back2MainMenu);
        final TextView smallItemHint = findViewById(R.id.smallItemsHint);
        final TextView mediumItemHint = findViewById(R.id.mediumItemsHint);
        final TextView largeItemHint = findViewById(R.id.largeItemsHint);

        //Setting up items hint popup window
        smallItemHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(SessionRegister.this).create();
                LayoutInflater inflater = SessionRegister.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_popup, null);

                final TextView smallItemTitle = dialogView.findViewById(R.id.popupTitle);
                final TextView smallItemDescription = dialogView.findViewById(R.id.itemDesc);
                final Button proceed = dialogView.findViewById(R.id.proceed);
                final ImageView image1 = dialogView.findViewById(R.id.item1);
                final ImageView image2 = dialogView.findViewById(R.id.item2);
                final ImageView image3 = dialogView.findViewById(R.id.item3);

                smallItemTitle.setText(R.string.itemHintTitle);
                smallItemDescription.setText(R.string.smallItem);
                image1.setImageResource(R.drawable.small_box);
                image1.getLayoutParams().height = 450;
                image1.getLayoutParams().width = 450;
                image2.setImageResource(R.drawable.pail);
                image2.getLayoutParams().height = 450;
                image2.getLayoutParams().width = 450;
                image3.setImageResource(R.drawable.small_suitcase);
                image3.getLayoutParams().height = 450;
                image3.getLayoutParams().width = 450;

                proceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                });
                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
            }
        });

        mediumItemHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(SessionRegister.this).create();
                LayoutInflater inflater = SessionRegister.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_popup, null);

                final TextView mediumItemTitle = dialogView.findViewById(R.id.popupTitle);
                final TextView mediumItemDescription = dialogView.findViewById(R.id.itemDesc);
                final Button proceed = dialogView.findViewById(R.id.proceed);
                final ImageView image1 = dialogView.findViewById(R.id.item1);
                final ImageView image2 = dialogView.findViewById(R.id.item2);

                mediumItemTitle.setText(R.string.itemHintTitle);
                mediumItemDescription.setText(R.string.mediumItem);
                image1.setImageResource(R.drawable.medium_box);
                image1.getLayoutParams().height = 450;
                image1.getLayoutParams().width = 450;
                image2.setImageResource(R.drawable.medium_luggage);
                image2.getLayoutParams().height = 450;
                image2.getLayoutParams().width = 450;

                proceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                });
                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
            }
        });

        largeItemHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(SessionRegister.this).create();
                LayoutInflater inflater = SessionRegister.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_popup, null);

                final TextView largeItemTitle = dialogView.findViewById(R.id.popupTitle);
                final TextView largeItemDescription = dialogView.findViewById(R.id.itemDesc);
                final Button proceed = dialogView.findViewById(R.id.proceed);
                final ImageView image1 = dialogView.findViewById(R.id.item1);
                final ImageView image2 = dialogView.findViewById(R.id.item2);

                largeItemTitle.setText(R.string.itemHintTitle);
                largeItemDescription.setText(R.string.largeItem);
                image1.setImageResource(R.drawable.large_box);
                image1.getLayoutParams().height = 450;
                image1.getLayoutParams().width = 450;
                image2.setImageResource(R.drawable.large_luggage);
                image2.getLayoutParams().height = 450;
                image2.getLayoutParams().width = 450;

                proceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                });
                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
            }
        });

        Intent getUsername = getIntent();
        final Integer username = getUsername.getIntExtra("username", -1);
        final String password = getUsername.getStringExtra("password");
        final String username_string = Integer.toString(username);

        //Get value from small spinner
        ArrayAdapter<CharSequence> smallAdapter = ArrayAdapter.createFromResource(SessionRegister.this, R.array.small_items, android.R.layout.simple_spinner_item);
        smallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        smallItems.setAdapter(smallAdapter);
        smallItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    s_string = smallItems.getItemAtPosition(i).toString();
                    small = Integer.parseInt(s_string);
                } catch(NumberFormatException smallE){
                    smallE.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                AlertDialog.Builder nullSmall = new AlertDialog.Builder(SessionRegister.this);
                nullSmall.setMessage("No number selected for small items.")
                        .setNegativeButton("Retry", null)
                        .create().show();
            }
        });

        //Get value from medium spinner
        ArrayAdapter<CharSequence> medAdapter = ArrayAdapter.createFromResource(SessionRegister.this, R.array.medium_items, android.R.layout.simple_spinner_item);
        medAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medItems.setAdapter(medAdapter);
        medItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    m_string = medItems.getItemAtPosition(i).toString();
                    medium = Integer.parseInt(m_string);
                } catch (NumberFormatException medE){
                    medE.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                AlertDialog.Builder nullMed = new AlertDialog.Builder(SessionRegister.this);
                nullMed.setMessage("No number selected for medium items.")
                        .setNegativeButton("Retry", null)
                        .create().show();
            }
        });

        //Get value from large spinner
        ArrayAdapter<CharSequence> largeAdapter = ArrayAdapter.createFromResource(SessionRegister.this, R.array.large_items, android.R.layout.simple_spinner_item);
        largeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        largeItems.setAdapter(largeAdapter);
        largeItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    l_string = largeItems.getItemAtPosition(i).toString();
                    large = Integer.parseInt(l_string);
                } catch (NumberFormatException largeE){
                    largeE.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                AlertDialog.Builder nullLarge = new AlertDialog.Builder(SessionRegister.this);
                nullLarge.setMessage("No number selected for large items.")
                        .setNegativeButton("Retry", null)
                        .create().show();
            }
        });

        //When register button clicked
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cost = totalCost(small, medium, large);
                cost_string = Double.toString(cost);

                if((cost == 0) || (cost < 0)){
                    final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(SessionRegister.this).create();
                    LayoutInflater inflater = SessionRegister.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                    final TextView registerTitle = dialogView.findViewById(R.id.sessionDialogTitle);
                    final TextView registerMsg = dialogView.findViewById(R.id.sessionStatusMsg);
                    final Button proceed = dialogView.findViewById(R.id.proceed);
                    registerTitle.setText(R.string.sessionNulltitle);
                    registerMsg.setText(R.string.sessionNullmsg);

                    proceed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    });
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.show();
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean registerSuccess = jsonResponse.getBoolean("success");
                                String qrCode = jsonResponse.getString("qrCode");

                                if (registerSuccess) {
                                    Intent gotoSessionRegistered = new Intent(SessionRegister.this, RegisteredSession.class);
                                    gotoSessionRegistered.putExtra("qr_code", qrCode); //string
                                    gotoSessionRegistered.putExtra("totalCost", cost_string); //string
                                    gotoSessionRegistered.putExtra("username", username_string); //string
                                    gotoSessionRegistered.putExtra("password", password); //string
                                    SessionRegister.this.startActivity(gotoSessionRegistered);
                                    SessionRegister.this.finish();
                                } else {
                                    AlertDialog.Builder failRegister = new AlertDialog.Builder(SessionRegister.this);
                                    failRegister.setMessage("You have registered session for this semester period")
                                            .setNegativeButton("Cancel Registration", null)
                                            .create().show();
                                }
                            } catch (JSONException registerErrror) {
                                registerErrror.printStackTrace();
                            }
                        }
                    };
                    //instantiate register session request with all necessary variable
                    SessionRequest registerSesion = new SessionRequest(username_string, s_string, m_string, l_string, cost_string, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(SessionRegister.this);
                    queue.add(registerSesion);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToMainMenu = new Intent(SessionRegister.this, StudentHome.class);
                backToMainMenu.putExtra("username", username);
                SessionRegister.this.startActivity(backToMainMenu);
                SessionRegister.this.finish();

            }
        });
    }

    //Calculate total cost of all items
    public double totalCost(int s, int m, int l){
        double total = (s_value*s)+(m_value*m)+(l_value*l);
        return total;
    }
}
