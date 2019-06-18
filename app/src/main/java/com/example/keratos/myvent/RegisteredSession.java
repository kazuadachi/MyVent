package com.example.keratos.myvent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

public class RegisteredSession extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_registered);
        Button backtoMainMenu = findViewById(R.id.back2MainMenu);
        Intent getQRcode = getIntent();
        final String username = getQRcode.getStringExtra("username");
        final String password = getQRcode.getStringExtra("password");
        final String QRcode = getQRcode.getStringExtra("qr_code");
        final String totalCost = getQRcode.getStringExtra("totalCost");

        ImageView QR =  findViewById(R.id.qr_code);

        TextView costView = findViewById(R.id.cost);
        costView.setText(String.valueOf(totalCost));

        //If there is value in String, convert to QR code in Bitmap then to JPEG then set to ImageView
        if (QRcode != null){
            MultiFormatWriter qrWriter = new MultiFormatWriter();
            try{
                BitMatrix bMatrix = qrWriter.encode(QRcode, BarcodeFormat.QR_CODE, 600, 600);
                BarcodeEncoder barcodeEncoder =new BarcodeEncoder();
                Bitmap QRimage = barcodeEncoder.createBitmap(bMatrix);
                //QRimage is in bitmap, bottom code convert bitmap to jpeg
                Bitmap QRjpeg = codec(QRimage, Bitmap.CompressFormat.JPEG, 100);
                //set ImageViewer value to that qr code
                QR.setImageBitmap(QRjpeg);
            } catch (WriterException we){
                we.printStackTrace();
            }
        }
        // convert string back to integer so the username can be reuse after registering
        final int username_int = Integer.parseInt(username);

        backtoMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back2MainMenu = new Intent(RegisteredSession.this, StudentHome.class);
                back2MainMenu.putExtra("username", username_int); //Integer
                back2MainMenu.putExtra("password", password); //String
                RegisteredSession.this.startActivity(back2MainMenu);
                RegisteredSession.this.finish();
            }
        });
    }

    //Conversion from string to bmp image method
    private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);
        byte[] array = os.toByteArray();
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }
}
