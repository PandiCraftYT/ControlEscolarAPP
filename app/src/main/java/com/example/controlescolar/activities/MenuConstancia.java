package com.example.controlescolar.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.controlescolar.R;
import com.example.controlescolar.api.ApiInterface;
import com.example.controlescolar.api.RetrofitClient;
import com.google.gson.JsonObject;

import java.time.format.TextStyle;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuConstancia extends AppCompatActivity {
    private ScrollView scrollView;

    private ApiInterface apiInterface;

    private TextView textViewGrupoId;

    private TextView textViewNoCuenta;
    private TextView textViewSemestre;
    private boolean botonAgregado = false;

    private LinearLayout infoLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_constancia);
        scrollView = findViewById(R.id.scrollView);
        infoLayout = findViewById(R.id.infoLayout);
        textViewGrupoId = findViewById(R.id.textViewGrupoId);
        textViewSemestre = findViewById(R.id.textViewSemestre);
        textViewNoCuenta = findViewById(R.id.textNumero);
        String noCuenta = getIntent().getStringExtra("no_cuenta");
        String grupoId = getIntent().getStringExtra("grupo_id");
        String semestreId = getIntent().getStringExtra("semestre_id");

        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        if (grupoId != null) {
            textViewGrupoId.setText(grupoId);
        }

        if(semestreId != null){
            textViewSemestre.setText(semestreId);
        }
         if(noCuenta != null){
             textViewNoCuenta.setText(noCuenta);
         }

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("no_cuenta", noCuenta);

        // Hacer la llamada a la API
        Call<ApiInterface.SolicitudConstanciaResponse> call = apiInterface.solicitudConstancia(jsonBody);
        call.enqueue(new Callback<ApiInterface.SolicitudConstanciaResponse>() {
            @Override
            public void onResponse(Call<ApiInterface.SolicitudConstanciaResponse> call, Response<ApiInterface.SolicitudConstanciaResponse> response) {
                if (response.isSuccessful()) {
                    // Procesar la respuesta
                    ApiInterface.SolicitudConstanciaResponse constanciaResponse = response.body();
                    mostrarDatosEnTabla(constanciaResponse);
                } else {
                    // Manejar el error en la respuesta del servidor
                    Log.e(TAG, "Error en la respuesta del servidor");
                }
            }

            @Override
            public void onFailure(Call<ApiInterface.SolicitudConstanciaResponse> call, Throwable t) {
                // Manejar el error en la llamada a la API
                Log.e(TAG, "Error en la llamada a la API", t);
            }
        });

    }

    private void mostrarDatosEnTabla(ApiInterface.SolicitudConstanciaResponse constanciaResponse) {
        // Obtener la lista de constancias
        List<ApiInterface.Constancia> constancias = constanciaResponse.getData().getConstanciasSolicitadas();

        // Obtener la referencia al layout de la tabla
        TableLayout tableLayout = findViewById(R.id.tableLayout); // Asegúrate de tener un TableLayout en tu XML con el id "tableLayout"

        // Iterar sobre la lista de constancias y agregar filas a la tabla
        for (ApiInterface.Constancia constancia : constancias) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            rowLayoutParams.setMargins(0, 12, 0, 12); // Ajustar márgenes entre filas
            row.setLayoutParams(rowLayoutParams);

            // Establecer el color de fondo dependiendo del estado
            int backgroundColor;
            switch (constancia.getEstado()) {
                case "aceptado":
                    backgroundColor = Color.GREEN; // Color verde para estado "ACEPTADO"
                    break;
                case "rechazado":
                    backgroundColor = Color.RED; // Color rojo para estado "RECHAZADO"
                    break;
                default:
                    backgroundColor = Color.GRAY; // Color gris para estado "PENDIENTE"
                    break;
            }
            row.setBackgroundColor(backgroundColor);

            // Crear y configurar TextView para mostrar el valor del folio
            TextView folioTextView = new TextView(this);
            folioTextView.setText("FOLIO:\n" + constancia.getFolio());
            folioTextView.setTextSize(20); // Tamaño del texto
            folioTextView.setTextColor(Color.BLACK); // Color del texto
            folioTextView.setPadding(24, 12, 24, 12); // Padding del texto
            row.addView(folioTextView);

            // Crear y configurar TextView para mostrar el valor del estado
            TextView estadoTextView = new TextView(this);
            estadoTextView.setText("ESTADO:\n" + constancia.getEstado());
            estadoTextView.setTextSize(20); // Tamaño del texto
            estadoTextView.setTextColor(Color.BLACK); // Color del texto
            estadoTextView.setPadding(24, 12, 24, 12); // Padding del texto
            row.addView(estadoTextView);

            // Crear y configurar TextView para mostrar el valor de la fecha de solicitud
            TextView fechaSolicitudTextView = new TextView(this);
            fechaSolicitudTextView.setText("FECHA SOLICITUD:\n" + constancia.getFechaSolicitud());
            fechaSolicitudTextView.setTextSize(20); // Tamaño del texto
            fechaSolicitudTextView.setTextColor(Color.BLACK); // Color del texto
            fechaSolicitudTextView.setPadding(24, 12, 24, 12); // Padding del texto
            row.addView(fechaSolicitudTextView);

            // Agregar la fila a la tabla
            tableLayout.addView(row);

            // Agregar un espacio en blanco al final de cada fila para separar visualmente los datos
            View separatorView = new View(this);
            TableRow.LayoutParams separatorParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    20 // Altura del espacio en blanco (puedes ajustar este valor según la separación deseada)
            );
            separatorView.setLayoutParams(separatorParams);
            tableLayout.addView(separatorView);

            // Dentro del bucle for, después de crear la fila y establecer su color de fondo
            if (constancia.getEstado().equals("aceptado")) {
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Mostrar la alerta
                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuConstancia.this);
                        builder.setTitle("Descargar Archivo")
                                .setMessage("¿Quieres descargar este archivo?")
                                .setPositiveButton("Sí", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Acción al presionar "Sí"
                                        // Redirigir a otra actividad
                                        Intent intent = new Intent(MenuConstancia.this, Estudio.class);
                                        // Puedes enviar datos adicionales si es necesario
                                        intent.putExtra("folio", constancia.getFolio());
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Acción al presionar "No"
                                        dialog.dismiss(); // Cierra la alerta
                                    }
                                })
                                .show();
                    }
                });
            }

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