package com.example.controlescolar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.controlescolar.R;

public class MenuJustificante extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_justificante);

    }
    public void Atras(View view){
        Intent intent = new Intent(MenuJustificante.this, MenuActivity.class);
        intent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
        intent.putExtra("nombre", getIntent().getStringExtra("nombre"));
        startActivity(intent);
    }
    public void SolicitudJusticante(View view){

        Intent intent = new Intent(MenuJustificante.this, Justificante.class);
        intent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
        intent.putExtra("nombre", getIntent().getStringExtra("nombre"));
        startActivity(intent);
    }
    public void HistorialJustificante(View view){
        Intent intent = new Intent(MenuJustificante.this, HistorialJustificante.class);
        intent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
        intent.putExtra("nombre", getIntent().getStringExtra("nombre"));
        startActivity(intent);
    }
}