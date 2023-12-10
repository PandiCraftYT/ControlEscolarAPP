package com.example.controlescolar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.controlescolar.R;

public class MenuJustificante extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_justificante);
    }
    public void Atras(View view){
        startActivity(new  Intent(MenuJustificante.this, MenuActivity.class));

    }
    public void SolicitudJusticante(View view){

        Intent intent = new Intent(MenuJustificante.this, Justificante.class);
        startActivity(intent);
    }
    public void HistorialJustificante(View view){
        Intent intent = new Intent(MenuJustificante.this, HistorialJustificante.class);
        startActivity(intent);
    }
}