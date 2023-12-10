package com.example.controlescolar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.controlescolar.R;

public class MenuKardex extends AppCompatActivity {
    private ScrollView scrollView;
    private LinearLayout infoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_kardex);

        scrollView = findViewById(R.id.scrollView);
        infoLayout = findViewById(R.id.infoLayout);

        // Recuperar datos del Intent
        Intent intent = getIntent();
        if (intent != null) {
            String noCuenta = intent.getStringExtra("no_cuenta");
            String nombre = intent.getStringExtra("nombre");

        }
    }

    public void toggleInfoVisibility(View view) {
        if (scrollView.getVisibility() == View.VISIBLE) {
            scrollView.setVisibility(View.GONE);
        } else {
            scrollView.setVisibility(View.VISIBLE);
            fillInfo(); // Método para llenar la información al mostrarse la sección
        }
    }

    private void fillInfo() {
        // Lógica para agregar la información a la sección correspondiente
        // Por ejemplo:
        // Aquí puedes agregar botones, textos, imágenes, etc., al infoLayout según tus especificaciones
    }


    public void onCarreraButtonClick(View view) {
        // Este método se ejecutará al presionar el botón "CARRERA"
        // Puedes agregar la lógica que desees aquí
    }

    public void onNumeroClick(View view) {
        // Método para abrir una nueva actividad al presionar el número
        Intent intent = new Intent(MenuKardex.this, Kardex.class);
        intent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
        intent.putExtra("nombre", getIntent().getStringExtra("nombre"));
        startActivity(intent);
    }
}