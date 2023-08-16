package com.example.busproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DriverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        EditText edit = findViewById(R.id.edit);
        Button button =findViewById(R.id.button);
        Intent intent = new Intent(getApplicationContext(), DriverDrive.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("busNum",edit.getText().toString());
                startActivity(intent);
            }
        });
    }
}