package com.example.keratos.myvent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.client.android.Intents;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Permission;
import java.util.Queue;

public class Scanner extends AppCompatActivity {
    final static private int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }

        SurfaceView scanner = findViewById(R.id.qr_scanner);

        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(Scanner.this).setBarcodeFormats(Barcode.QR_CODE).build();

        final CameraSource cam = new CameraSource.Builder(Scanner.this, barcodeDetector).setRequestedPreviewSize(640, 480).setAutoFocusEnabled(true).build();

        Intent intent = getIntent();
        final String username_string = intent.getStringExtra("username");
        final String search = intent.getStringExtra("search");

        scanner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(Scanner.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                try{
                    cam.start(holder);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cam.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodeDetected = detections.getDetectedItems();

                //If there's qr code with value detected
                if (qrCodeDetected.size() != 0){
                    barcodeDetector.release();
                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(50);
                    String qr = qrCodeDetected.valueAt(0).displayValue;

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                boolean status = jsonObject.getBoolean("success");
                                String session_date = jsonObject.getString("sessionDate");
                                Integer response_matric = jsonObject.getInt("matric");
                                Integer smallItem = jsonObject.getInt("smallItem");
                                Integer mediumItem = jsonObject.getInt("mediumItem");
                                Integer largeItem = jsonObject.getInt("largeItem");
                                String qr_code = jsonObject.getString("qrCode");
                                Double totalCost = jsonObject.getDouble("totalCost");

                                if (status){
                                    Intent goSessionPage = new Intent(Scanner.this, SessionSelected.class);
                                    goSessionPage.putExtra("username", username_string);
                                    goSessionPage.putExtra("sessionDate", session_date);
                                    goSessionPage.putExtra("student_matric", response_matric);
                                    goSessionPage.putExtra("smallItem", smallItem);
                                    goSessionPage.putExtra("mediumItem", mediumItem);
                                    goSessionPage.putExtra("largeItem", largeItem);
                                    goSessionPage.putExtra("qrCode", qr_code);
                                    goSessionPage.putExtra("totalCost", totalCost);
                                    Scanner.this.startActivity(goSessionPage);
                                }
                            } catch (JSONException jsonException){
                                jsonException.printStackTrace();
                            }
                        }
                    };
                    SearchRequest request = new SearchRequest(search, qr, username_string, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Scanner.this);
                    queue.add(request);
                }

            }
        });
    }
}
