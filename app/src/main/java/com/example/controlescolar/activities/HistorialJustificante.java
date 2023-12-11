package com.example.controlescolar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.controlescolar.R;

public class HistorialJustificante extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_justificante);
    }

    public void Atras(View view){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
        returnIntent.putExtra("nombre", getIntent().getStringExtra("nombre"));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}