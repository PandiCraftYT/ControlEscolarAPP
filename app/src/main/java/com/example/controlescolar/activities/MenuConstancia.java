package com.example.controlescolar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.controlescolar.R;

public class MenuConstancia extends AppCompatActivity {
    private ScrollView scrollView;
    private boolean botonAgregado = false;

    private LinearLayout infoLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_constancia);
        scrollView = findViewById(R.id.scrollView);
        infoLayout = findViewById(R.id.infoLayout);

    }
    public void Atras(View view){
        Intent intent = new Intent(MenuConstancia.this, MenuActivity.class);
        intent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
        intent.putExtra("nombre", getIntent().getStringExtra("nombre"));
        startActivity(intent);
        // Para finalizar esta actividad después de pasar a la nueva actividad
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
        if (!botonAgregado) {
            // Crear un nuevo botón
            Button nuevoBoton = new Button(this);
            nuevoBoton.setText("Solicitar Constancia");
            nuevoBoton.setBackgroundResource(R.drawable.boton_personalizado);

            // Establecer el diseño deseado para el nuevo botón
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 200
            );
            layoutParams.setMargins(0, 16, 0, 0); // Ajustar márgenes si es necesario
            nuevoBoton.setLayoutParams(layoutParams);

            // Agregar un listener de clic al botón
            nuevoBoton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acción a realizar al hacer clic en el botón
                    Intent intent = new Intent(MenuConstancia.this, Estudio.class); // Reemplaza NuevaActividad con el nombre de tu actividad de destino
                    startActivity(intent);
                }
            });

            // Agregar el botón al infoLayout
            LinearLayout infoLayout = findViewById(R.id.infoLayout);
            infoLayout.addView(nuevoBoton);

            botonAgregado = true;
        }
    }

}