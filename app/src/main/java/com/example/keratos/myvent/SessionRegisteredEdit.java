package com.example.keratos.myvent;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class SessionRegisteredEdit extends AppCompatActivity {
    private String small_string, med_string, l_string, cost_string;
    private int small, medium, large;
    private double cost, small_price = 2.0, med_price = 5.0, large_price = 10.0;
    private String updateString = "goUpdate";
    private Bitmap qr_jpeg_img = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_registered_session);
        ActivityCompat.requestPermissions(SessionRegisteredEdit.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        Intent getSession = getIntent();
        final Integer username = getSession.getIntExtra("username", -1);
        final String password = getSession.getStringExtra("password");
        final String username_s = Integer.toString(username);
        final Integer small_items = getSession.getIntExtra("smallItem", -1);
        final Integer med_items = getSession.getIntExtra("mediumItem", -1);
        final Integer large_items = getSession.getIntExtra("largeItem", -1);
        final String qr_code = getSession.getStringExtra("qrCode");

        final ImageView qr_image = findViewById(R.id.qr_code);
        final Button save_qr = findViewById(R.id.save2phone);

        final Spinner small_item_spinner = findViewById(R.id.getSmallItems);
        final TextView current_small_item = findViewById(R.id.currentSmallItemsValue);
        current_small_item.setText(Integer.toString(small_items));

        final Spinner med_item_spinner = findViewById(R.id.getMediumItems);
        final TextView current_med_item = findViewById(R.id.currentMediumItemsValue);
        current_med_item.setText(Integer.toString(med_items));

        final Spinner large_item_spinner = findViewById(R.id.getLargeItems);
        final TextView current_large_item = findViewById(R.id.currentLargeItemsValue);
        current_large_item.setText(Integer.toString(large_items));

        final Button update_session = findViewById(R.id.update);
        final Button delete_session = findViewById(R.id.cancel);
        final Button back_mainmenu = findViewById(R.id.back2MainMenu);

        //If there is value in String, convert to QR code in Bitmap then to JPEG then set to ImageView
        if(qr_code!= null) {
            //Setting up ImageViewer
            MultiFormatWriter qrWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = qrWriter.encode(qr_code, BarcodeFormat.QR_CODE, 200, 200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap qr_bmp_img = barcodeEncoder.createBitmap(bitMatrix);
                qr_jpeg_img = codec(qr_bmp_img, Bitmap.CompressFormat.JPEG, 100);
                qr_image.setImageBitmap(qr_jpeg_img);
            } catch (WriterException we) {
                we.printStackTrace();
            }
        }

        //Setting up spinner
        ArrayAdapter<CharSequence> smallAdapter = ArrayAdapter.createFromResource(SessionRegisteredEdit.this, R.array.small_items, android.R.layout.simple_spinner_item);
        smallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        small_item_spinner.setAdapter(smallAdapter);
        small_item_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    small_string = small_item_spinner.getItemAtPosition(i).toString();
                    small = Integer.parseInt(small_string);
                } catch (NumberFormatException smallerror){
                    smallerror.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                AlertDialog.Builder nullSmall = new AlertDialog.Builder(SessionRegisteredEdit.this);
                nullSmall.setMessage("No number selected for small items.")
                        .setNegativeButton("Retry", null)
                        .create().show();
            }
        });

        ArrayAdapter<CharSequence> medAdapter = ArrayAdapter.createFromResource(SessionRegisteredEdit.this, R.array.medium_items, android.R.layout.simple_spinner_item);
        medAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        med_item_spinner.setAdapter(medAdapter);
        med_item_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    med_string = med_item_spinner.getItemAtPosition(i).toString();
                    medium = Integer.parseInt(med_string);
                } catch (NumberFormatException medError){
                    medError.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                AlertDialog.Builder nullMed = new AlertDialog.Builder(SessionRegisteredEdit.this);
                nullMed.setMessage("No number selected for medium items.")
                        .setNegativeButton("Retry", null)
                        .create().show();
            }
        });

        ArrayAdapter<CharSequence> largeAdapter = ArrayAdapter.createFromResource(SessionRegisteredEdit.this, R.array.large_items, android.R.layout.simple_spinner_item);
        largeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        large_item_spinner.setAdapter(largeAdapter);
        large_item_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    l_string = large_item_spinner.getItemAtPosition(i).toString();
                    large = Integer.parseInt(l_string);
                } catch (NumberFormatException largeError){
                    largeError.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                AlertDialog.Builder nullLarge = new AlertDialog.Builder(SessionRegisteredEdit.this);
                nullLarge.setMessage("No number selected for large items.")
                        .setNegativeButton("Retry", null)
                        .create().show();
            }
        });

/*Print function for future updates
        //Line Separator
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(1, 1, 0, 68));

        Document doc = new Document();
        PdfWriter docWriter = null;

        try{
            String saveLocation = Environment.getExternalStorageDirectory().toString() + "/Download/";
            File myDir = new File(saveLocation);
            myDir.mkdirs();
            String fname = "My_QR"+ ".pdf";
            File file = new File(myDir, fname);
            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.setPageSize(PageSize.A4);
            doc.addCreationDate();
            doc.addTitle("QR Code for MyVent");
            doc.add(new Paragraph(""));
            doc.add(new Chunk(lineSeparator));
            doc.add(new Paragraph(""));
            doc.open();

            BarcodeQRCode qrCode2PDF = new BarcodeQRCode(qr_code, 1, 1, null);
            Image qrCodePDFImage = qrCode2PDF.getImage();
            qrCodePDFImage.setAbsolutePosition(10, 500);
            qrCodePDFImage.scalePercent(200);
            doc.add(qrCodePDFImage);

        } catch (FileNotFoundException | com.itextpdf.text.DocumentException error){
            error.printStackTrace();
        } finally {
            if(doc != null){
                doc.close();
            }
            if(docWriter != null){
                docWriter.close();
            }
        }
*/

        //Setting up all the buttons
            //Save button
        save_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = "QR Code";
                saveImage(qr_jpeg_img, filename);

                final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(SessionRegisteredEdit.this).create();
                LayoutInflater inflater = SessionRegisteredEdit.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                final TextView saveImageTitle = dialogView.findViewById(R.id.sessionDialogTitle);
                final TextView saveImageMsg = dialogView.findViewById(R.id.sessionStatusMsg);
                final Button proceed = dialogView.findViewById(R.id.proceed);
                saveImageTitle.setText(R.string.imageSavedTitle);
                saveImageMsg.setText(R.string.imageSavedMsg);

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

        //Update Session button
        update_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cost = totalCost(small, medium, large);
                cost_string = Double.toString(cost);

                if((cost == 0) || (cost < 0)){
                    final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(SessionRegisteredEdit.this).create();
                    LayoutInflater inflater = SessionRegisteredEdit.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                    final TextView updateTitle = dialogView.findViewById(R.id.sessionDialogTitle);
                    final TextView updateMsg = dialogView.findViewById(R.id.sessionStatusMsg);
                    final Button proceed = dialogView.findViewById(R.id.proceed);
                    updateTitle.setText(R.string.sessionNulltitle);
                    updateMsg.setText(R.string.sessionNullmsg);

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
                                boolean updateSuccess = jsonResponse.getBoolean("success");
                                String qrCode = jsonResponse.getString("qr");

                                Log.d("updateResponse", response);

                                if (updateSuccess) {
                                    final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(SessionRegisteredEdit.this).create();
                                    LayoutInflater inflater = SessionRegisteredEdit.this.getLayoutInflater();
                                    View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                                    final TextView updateTitle = dialogView.findViewById(R.id.sessionDialogTitle);
                                    final TextView updateMsg = dialogView.findViewById(R.id.sessionStatusMsg);
                                    final Button proceed = dialogView.findViewById(R.id.proceed);
                                    updateTitle.setText(R.string.updateSessionTitle);
                                    updateMsg.setText(R.string.updateSessionMsg);

                                    proceed.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogBuilder.dismiss();
                                            Intent gotoSessionUpdated = new Intent(SessionRegisteredEdit.this, StudentHome.class);
                                            gotoSessionUpdated.putExtra("username", username);
                                            gotoSessionUpdated.putExtra("password", password);
                                            SessionRegisteredEdit.this.startActivity(gotoSessionUpdated);
                                            finish();
                                        }
                                    });
                                    dialogBuilder.setView(dialogView);
                                    dialogBuilder.show();
                                } else {
                                    AlertDialog.Builder failUpdate = new AlertDialog.Builder(SessionRegisteredEdit.this);
                                    failUpdate.setMessage("There's something wrong here. Please try again.")
                                            .setNegativeButton("Retry", null)
                                            .create().show();
                                }

                            } catch (JSONException updateError) {
                                updateError.printStackTrace();
                            }
                        }
                    };
                    SessionRequest updateSession = new SessionRequest(updateString, username_s, small_string, med_string, l_string, cost_string, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(SessionRegisteredEdit.this);
                    queue.add(updateSession);
                }
            }
        });

        //Delete Session button
        delete_session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String>responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean deleteSuccess = jsonResponse.getBoolean("success");
                            Log.d("deleteResponse", response);
                            if (deleteSuccess){
                                final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(SessionRegisteredEdit.this).create();
                                LayoutInflater inflater = SessionRegisteredEdit.this.getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                                final TextView deleteTitle = dialogView.findViewById(R.id.sessionDialogTitle);
                                final TextView deleteMsg = dialogView.findViewById(R.id.sessionStatusMsg);
                                final Button proceed = dialogView.findViewById(R.id.proceed);
                                deleteTitle.setText(R.string.deleteSessionTitle);
                                deleteMsg.setText(R.string.deleteSessionMsg);
                                proceed.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder.dismiss();
                                        Intent deleteProceed = new Intent(SessionRegisteredEdit.this, StudentHome.class);
                                        deleteProceed.putExtra("username", username);
                                        deleteProceed.putExtra("password", password);
                                        SessionRegisteredEdit.this.startActivity(deleteProceed);
                                        finish();
                                    }
                                });
                                dialogBuilder.setView(dialogView);
                                dialogBuilder.show();
                            }else{
                                AlertDialog.Builder failUpdate = new AlertDialog.Builder(SessionRegisteredEdit.this);
                                failUpdate.setMessage("There's something wrong here. Please try again.")
                                        .setNegativeButton("Retry", null)
                                        .create().show();
                            }
                        } catch (JSONException deleteError){
                            deleteError.printStackTrace();
                        }
                    }
                };
                DeleteRequest deleteSession = new DeleteRequest(username_s, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SessionRegisteredEdit.this);
                queue.add(deleteSession);
            }
        });


        //Back button
        back_mainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToMainMenu = new Intent(SessionRegisteredEdit.this, StudentHome.class);
                backToMainMenu.putExtra("username", username);
                backToMainMenu.putExtra("password", password);
                SessionRegisteredEdit.this.startActivity(backToMainMenu);
                SessionRegisteredEdit.this.finish();
            }
        });
    }

    //save BMP file
    private void saveImage(Bitmap finalBitmap, String image_name) {

        String root = Environment.getExternalStorageDirectory().toString() + "/Download/";
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "My-" + image_name+ ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //convert BMP to JPG with 100% quality
    private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format, int quality){
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        src.compress(format, quality, oStream);
        byte[] outputArray = oStream.toByteArray();
        return BitmapFactory.decodeByteArray(outputArray, 0, outputArray.length);
    }


    //Calculate total cost of all items
    public double totalCost(int s, int m, int l){
        double total = (small_price*s)+(med_price*m)+(large_price*l);
        return total;
    }
}
