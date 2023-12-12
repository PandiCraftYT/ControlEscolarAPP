package com.example.controlescolar.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controlescolar.R;
import com.example.controlescolar.api.ApiInterface;
import com.example.controlescolar.api.RetrofitClient;
import com.google.gson.JsonObject;

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

        // Recorrer la lista y mostrar los datos en la tabla
        for (ApiInterface.Constancia constancia : constancias) {
            // Crear nuevas vistas para cada fila de la tabla
            TableRow newRow = new TableRow(MenuConstancia.this);
            TextView folioTextView = new TextView(MenuConstancia.this);
            TextView estadoTextView = new TextView(MenuConstancia.this);
            TextView fechaTextView = new TextView(MenuConstancia.this);

            // Configurar los textos con los datos de la constancia actual
            folioTextView.setText(constancia.getFolio());
            estadoTextView.setText(constancia.getEstado());
            fechaTextView.setText(constancia.getFechaSolicitud());

            // Agregar las vistas a la fila
            newRow.addView(folioTextView);
            newRow.addView(estadoTextView);
            newRow.addView(fechaTextView);

            // Obtener la tabla y agregar la nueva fila
            TableLayout table = findViewById(R.id.tableLayout); // Asegúrate de tener un TableLayout en tu XML con el id "tableLayout"
            table.addView(newRow);
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
                    // Llamar al método para solicitar constancia
                    solicitarConstancia();
                }
            });

            // Agregar el botón al infoLayout
            LinearLayout infoLayout = findViewById(R.id.infoLayout);
            infoLayout.addView(nuevoBoton);

            botonAgregado = true;
        }
    }

    // Método para realizar la llamada a la API al solicitar constancia
    private void solicitarConstancia() {
        String noCuenta = textViewNoCuenta.getText().toString();

        if (noCuenta != null && !noCuenta.isEmpty()) {
            JsonObject jsonBody = new JsonObject();
            jsonBody.addProperty("no_cuenta", noCuenta);

            // Hacer la llamada a la API para guardar la solicitud de constancia
            Call<ApiInterface.GuardarSolicitudConstanciaAndroidResponse> call = apiInterface.guardarSolicitudConstanciaAndroid(jsonBody);
            call.enqueue(new Callback<ApiInterface.GuardarSolicitudConstanciaAndroidResponse>() {
                @Override
                public void onResponse(Call<ApiInterface.GuardarSolicitudConstanciaAndroidResponse> call, Response<ApiInterface.GuardarSolicitudConstanciaAndroidResponse> response) {
                    if (response.isSuccessful()) {
                        // Procesar la respuesta
                        ApiInterface.GuardarSolicitudConstanciaAndroidResponse guardarResponse = response.body();
                        if (guardarResponse.getStatus().equals("success")) {
                            // Si la solicitud se realiza con éxito, muestra un Toast indicando que la solicitud fue enviada
                            showToast("Solicitud Enviada");
                            recreate();
                        } else {
                            // Manejar el caso en que la solicitud no se pueda realizar
                            showToast("No puedes tener más de dos solicitudes activas a la vez");
                            Log.e(TAG, "Error al solicitar constancia: " + guardarResponse.getMessage());
                        }
                    } else {
                        // Manejar el error en la respuesta del servidor
                        showToast("Error en la respuesta del servidor al solicitar constancia");
                        Log.e(TAG, "Error en la respuesta del servidor al solicitar constancia");
                    }
                }

                @Override
                public void onFailure(Call<ApiInterface.GuardarSolicitudConstanciaAndroidResponse> call, Throwable t) {
                    // Manejar el error en la llamada a la API
                    showToast("Error al solicitar constancia. Verifica tu conexión a Internet.");
                    Log.e(TAG, "Error en la llamada a la API al solicitar constancia", t);
                }
            });
        } else {
            // Manejar el caso en que no se pueda obtener el número de cuenta
            showToast("No se pudo obtener el número de cuenta para solicitar constancia");
            Log.e(TAG, "No se pudo obtener el número de cuenta para solicitar constancia");
        }
    }

    // Método para mostrar un Toast con el mensaje especificado
    private void showToast(String message) {
        Toast.makeText(MenuConstancia.this, message, Toast.LENGTH_SHORT).show();
    }



}