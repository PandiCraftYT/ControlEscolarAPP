package com.example.controlescolar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

    private TextView textViewGrupoId;

    private TextView textViewSemestre;

    private TextView textViewNoCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_kardex);
        scrollView = findViewById(R.id.scrollView);
        infoLayout = findViewById(R.id.infoLayout);
        textViewGrupoId = findViewById(R.id.textViewGrupoId);
        textViewSemestre = findViewById(R.id.textViewSemestre);
        textViewNoCuenta = findViewById(R.id.textViewNoCuenta);
        // Obtén los datos del Intent o de donde sea que los tengas
        String noCuenta = getIntent().getStringExtra("no_cuenta");
        String grupoId = getIntent().getStringExtra("grupo_id");
        String semestreId = getIntent().getStringExtra("semestre_id");

        if (grupoId != null) {
            textViewGrupoId.setText(grupoId);
        }

        if(semestreId != null){
            textViewSemestre.setText(semestreId);
        }
        if(noCuenta != null){
            textViewNoCuenta.setText(noCuenta);
        }

    }

    public void Atras(View view){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
        returnIntent.putExtra("nombre", getIntent().getStringExtra("nombre"));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
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
        Intent intent = new Intent(MenuKardex.this, Kardex.class);
        intent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
        intent.putExtra("nombre", getIntent().getStringExtra("nombre"));
        startActivity(intent);
    }

    public void onNumeroClick(View view) {
        // Método para abrir una nueva actividad al presionar el número

    }
}