package com.example.controlescolar.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlescolar.R;

public class MenuActivity extends AppCompatActivity {

    GridLayout maingrind;
    TextView textViewWelcome;
    TextView textViewAlumno;
    TextView textViewNumCuenta;

    TextView textViewGrupo;

    TextView textViewSemestre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // Referencia al GridLayout desde el archivo de diseño

        maingrind = (GridLayout) findViewById(R.id.maingrid);
        // Configuración de eventos para cada elemento en el GridLayout

        setSingleEvent(maingrind);

        textViewWelcome = findViewById(R.id.textView);
        textViewAlumno = findViewById(R.id.textView2);
        textViewNumCuenta = findViewById(R.id.textView9);
        textViewGrupo = findViewById(R.id.textViewgrupo);
        textViewSemestre = findViewById(R.id.textViewsemestre);
        Intent intent = getIntent();
        if (intent != null) {
            String noCuenta = intent.getStringExtra("no_cuenta");
            String nombre = intent.getStringExtra("nombre");
            String Grupo = intent.getStringExtra("grupo_id");
            String Semestre = intent.getStringExtra("semestre_id");
            if (noCuenta != null && nombre != null) {
                textViewNumCuenta.setText(noCuenta);
                textViewAlumno.setText(getString(R.string.txt_Alumno) + "\n" + nombre);
                textViewGrupo.setText(Grupo);
                textViewSemestre.setText(Semestre);
            }
        }

    }
    // Método para manejar el botón de cerrar sesión

    public void cerrarSesionBtn(View view){
        // Mostrar un mensaje al cerrar sesión y redirigir a la pantalla de inicio de sesión

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity(); // Finaliza todas las actividades

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // Verifica el requestCode que usaste en startActivityForResult()
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String noCuenta = data.getStringExtra("no_cuenta");
                    String nombre = data.getStringExtra("nombre");

                    // Usa los datos obtenidos como sea necesario
                    // Por ejemplo:
                    textViewNumCuenta.setText(noCuenta);
                    textViewAlumno.setText(nombre);

                }
            }
        }
    }
    // Método para asignar eventos a los elementos del GridLayout

    private void setSingleEvent(GridLayout maingrind) {
        // Recorre todos los elementos del GridLayout

        for (int i=0; i < maingrind.getChildCount(); i++){
            // Obtiene cada elemento como una CardView

            CardView cardView = (CardView) maingrind.getChildAt(i);
            final int finalI = i;
            // Asigna un OnClickListener a cada CardView

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Realiza acciones según la posición de la CardView en el GridLayout

                    if(finalI ==0){
                        // Abre la actividad Kardex

                        Intent intent = new Intent(MenuActivity.this, MenuKardex.class);
                        intent.putExtra("no_cuenta", textViewNumCuenta.getText().toString());
                        intent.putExtra("nombre", textViewAlumno.getText().toString());
                        intent.putExtra("grupo_id", textViewGrupo.getText().toString());
                        intent.putExtra("semestre_id", textViewSemestre.getText().toString());
                        startActivityForResult(intent, 1); // 1 es el requestCode
                    } else if (finalI == 1) {
                        // Abre la actividad Estudio

                        Intent intent = new Intent(MenuActivity.this, MenuConstancia.class);
                        intent.putExtra("no_cuenta", textViewNumCuenta.getText().toString());
                        intent.putExtra("nombre", textViewAlumno.getText().toString());
                        intent.putExtra("grupo_id", textViewGrupo.getText().toString());
                        intent.putExtra("semestre_id", textViewSemestre.getText().toString());
                        Log.d(TAG, "grupoId: " + textViewGrupo);
                        Log.d(TAG, "semestreId: " + textViewSemestre);
                        startActivityForResult(intent, 1); // 1 es el requestCode
                    } else if (finalI == 2) {
                        // Abre la actividad Credencial
                        Intent intent = new Intent(MenuActivity.this, MenuJustificante.class);
                        intent.putExtra("no_cuenta", textViewNumCuenta.getText().toString());
                        intent.putExtra("nombre", textViewAlumno.getText().toString());
                        startActivityForResult(intent, 1); // 1 es el requestCode
                    } else if (finalI == 3) {
                        // Abre un navegador web con la URL proporcionada
                        String url = "https://www.uas.edu.mx/servicios/calendario/";

                        Intent intent = new Intent(MenuActivity.this, Calendario.class);
                        intent.putExtra("no_cuenta", textViewNumCuenta.getText().toString());
                        intent.putExtra("nombre", textViewAlumno.getText().toString());
                        intent.putExtra("url_calendario", url);
                        startActivity(intent);

                    }else {
                        // Muestra un mensaje si la opción no está disponible / en caso que tengamos otro cardview

                        Toast.makeText(MenuActivity.this, " NO DISPONIBLE", Toast.LENGTH_SHORT).show();
                    }
                    
                }

            });

        }
    }
}
