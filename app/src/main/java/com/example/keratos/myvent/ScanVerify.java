package com.example.keratos.myvent;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.BarcodeFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ScanVerify extends AppCompatActivity {
    final static private int SCAN_VERIFY_PERMISSIONS_REQUEST_CAMERA = 1;
    private String check_string = "check out";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_verify);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, SCAN_VERIFY_PERMISSIONS_REQUEST_CAMERA);
        }

        SurfaceView scanner = findViewById(R.id.verify_scanner);
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(ScanVerify.this).setBarcodeFormats(Barcode.QR_CODE).build();
        final CameraSource cam = new CameraSource.Builder(ScanVerify.this, barcodeDetector).setRequestedPreviewSize(640, 480).setAutoFocusEnabled(true).build();

        Intent getIntentValue = getIntent();
        final Integer username = getIntentValue.getIntExtra("username", -1);
        final String username_string = Integer.toString(username);
        final String matric = getIntentValue.getStringExtra("matric");
        final String qr_student = getIntentValue.getStringExtra("qrCode");

        scanner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(ScanVerify.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
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
                    String qr_item = qrCodeDetected.valueAt(0).displayValue;

                    if(qr_item.equals(qr_student)){
                        final Response.Listener<String> checkOUTlistener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject checkOUTresponse = new JSONObject(response);
                                    boolean success = checkOUTresponse.getBoolean("success");
                                    String matricResponse = checkOUTresponse.getString("matric");

                                    if(success){
                                        final AlertDialog dialogBuilder = new AlertDialog.Builder(ScanVerify.this).create();
                                        LayoutInflater inflater = ScanVerify.this.getLayoutInflater();
                                        View dialogView = inflater.inflate(R.layout.custom_dialog, null);

                                        final TextView checkOUTTitle = dialogView.findViewById(R.id.sessionDialogTitle);
                                        final TextView checkOUTMsg = dialogView.findViewById(R.id.sessionStatusMsg);
                                        final Button proceed2 = dialogView.findViewById(R.id.proceed);
                                        checkOUTTitle.setText(R.string.checkOUTtitle);
                                        checkOUTMsg.setText(R.string.checkOUTmsg);

                                        proceed2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent successCheckOUT = new Intent(ScanVerify.this, StaffHome.class);
                                                successCheckOUT.putExtra("username", username); //Integer
                                                ScanVerify.this.startActivity(successCheckOUT);
                                                finish();
                                                dialogBuilder.dismiss();
                                            }
                                        });
                                        dialogBuilder.setView(dialogView);
                                        dialogBuilder.show();
                                    }
                                } catch (JSONException checkOUTerror){
                                    checkOUTerror.printStackTrace();
                                }
                            }
                        };
                        CheckInOutRequest checkOUT = new CheckInOutRequest(check_string,  username_string, matric, checkOUTlistener);
                        RequestQueue checkOUTqueue = Volley.newRequestQueue(ScanVerify.this);
                        checkOUTqueue.add(checkOUT);
                    } else {
                        final AlertDialog dialogBuilder2 = new AlertDialog.Builder(ScanVerify.this).create();
                        LayoutInflater inflater2 = ScanVerify.this.getLayoutInflater();
                        View dialogView2 = inflater2.inflate(R.layout.custom_dialog, null);

                        final TextView checkOUTTitle = dialogView2.findViewById(R.id.sessionDialogTitle);
                        final TextView checkOUTMsg = dialogView2.findViewById(R.id.sessionStatusMsg);
                        final Button proceed = dialogView2.findViewById(R.id.proceed);
                        checkOUTTitle.setText(R.string.checkOUTtitle);
                        checkOUTMsg.setText(R.string.checkOUTfail);

                        proceed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder2.dismiss();
                            }
                        });
                        dialogBuilder2.setView(dialogView2);
                        dialogBuilder2.show();
                    }
                }
            }
        });
    }
}
