package com.example.keratos.myvent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginStudent = findViewById(R.id.loginStudent);
        Button loginStaff = findViewById(R.id.loginStaff);

        loginStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent clickStudent = new Intent(MainActivity.this, LoginStudent.class);
                startActivity(clickStudent);
            }
        });

        loginStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent clickStaff = new Intent(MainActivity.this, LoginStaff.class);
                startActivity(clickStaff);
            }
        });
    }
}
