package com.example.controlescolar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.controlescolar.R;

public class Calendario extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        // Obtener referencia del WebView desde el layout
        webView = findViewById(R.id.webView);

        // Habilitar JavaScript (si es necesario)
        webView.getSettings().setJavaScriptEnabled(true);

        // Configurar un WebViewClient para navegar en el WebView
        webView.setWebViewClient(new WebViewClient());

        // Cargar el URL deseado en el WebView
        String url = "https://www.uas.edu.mx/servicios/calendario/";
        webView.loadUrl(url);
    }
    public void Atras(View view){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
        returnIntent.putExtra("nombre", getIntent().getStringExtra("nombre"));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}