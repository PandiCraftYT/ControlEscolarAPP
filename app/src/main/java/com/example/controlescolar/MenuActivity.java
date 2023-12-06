package com.example.controlescolar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    GridLayout maingrind;
    TextView textViewWelcome;
    TextView textViewAlumno;
    TextView textViewNumCuenta;
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

        Intent intent = getIntent();
        if (intent != null) {
            String noCuenta = intent.getStringExtra("no_cuenta");
            String nombre = intent.getStringExtra("nombre");

            if (noCuenta != null && nombre != null) {
                textViewNumCuenta.setText(getString(R.string.txt_ncuentaMenu) + noCuenta);
                textViewAlumno.setText(getString(R.string.txt_Alumno) + nombre);
            }
        }

    }
    // Método para manejar el botón de cerrar sesión

    public void cerrarSesionBtn(View view){
        // Mostrar un mensaje al cerrar sesión y redirigir a la pantalla de inicio de sesión

        Toast.makeText(this, "Has Cerrado Sesión con EXITÓ", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MenuActivity.this, LoginActivity.class));
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

                        Intent intent = new Intent(MenuActivity.this, Kardex.class);
                        startActivity(intent);
                    } else if (finalI == 1) {
                        // Abre la actividad Estudio

                        Intent intent = new Intent(MenuActivity.this, Estudio.class);
                        startActivity(intent);
                    } else if (finalI == 2) {
                        // Abre la actividad Credencial

                        Intent intent = new Intent(MenuActivity.this, Credencial.class);
                        startActivity(intent);
                    } else if (finalI == 3) {
                        // Abre un navegador web con la URL proporcionada

                        String url = "https://www.uas.edu.mx/servicios/calendario/";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);

                    } else if (finalI == 4) {
                        // Abre la actividad Justificante

                        Intent intent = new Intent(MenuActivity.this, Justificante.class);
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
