package com.example.controlescolar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.controlescolar.R;

public class Kardex extends AppCompatActivity {

    private TextView textViewUserName;
    private TextView textViewUserNoCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kardex);

        textViewUserName = findViewById(R.id.textviewUsername);
        textViewUserNoCuenta = findViewById(R.id.textViewNoCuenta);

        // Obtén los datos del Intent o de donde sea que los tengas
        Intent intent = getIntent();
        if (intent != null) {
            String nombre = intent.getStringExtra("nombre");
            String noCuenta = intent.getStringExtra("no_cuenta");


            // Actualiza las vistas con los datos
            textViewUserName.setText(nombre);
            textViewUserNoCuenta.setText(noCuenta);
        }
    }
}
