package com.example.controlescolar.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.controlescolar.R;
import com.example.controlescolar.api.ApiInterface;
import com.example.controlescolar.api.RetrofitClient;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialJustificante extends AppCompatActivity {

    private TextView textViewNumCuenta;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_justificante);
        textViewNumCuenta = findViewById(R.id.textViewNoCuenta);
        String noCuenta = getIntent().getStringExtra("no_cuenta");

        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        if (noCuenta != null) {
            textViewNumCuenta.setText(noCuenta);

            // Llamar a la función para obtener y mostrar el historial de justificantes
            obtenerYMostrarHistorialJustificantes(noCuenta);
        }
    }

    private void obtenerYMostrarHistorialJustificantes(String noCuenta) {
        // Crear el objeto JSON con el número de cuenta
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("no_cuenta", noCuenta);

        // Llamar a la API para obtener el historial de justificantes
        Call<ApiInterface.SolicitudJustificanteResponse> call = apiInterface.solicitudJustificante(jsonBody);
        call.enqueue(new Callback<ApiInterface.SolicitudJustificanteResponse>() {
            @Override
            public void onResponse(Call<ApiInterface.SolicitudJustificanteResponse> call, Response<ApiInterface.SolicitudJustificanteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Procesar y mostrar los datos en la tabla
                    mostrarDatosEnTabla(response.body().getData().getJustificantesSolicitados());
                } else {
                    // Manejar errores en la respuesta
                    Log.e(TAG, "Error en la respuesta del servidor");

                }
            }

            @Override
            public void onFailure(Call<ApiInterface.SolicitudJustificanteResponse> call, Throwable t) {
                // Manejar errores en la solicitud
                Log.e(TAG, "Error en la llamada a la API", t);

            }
        });
    }

    private void mostrarDatosEnTabla(List<ApiInterface.Justificante> justificantes) {
        // Obtener la referencia al layout de la tabla
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        // Iterar sobre la lista de justificantes y agregar filas a la tabla
        for (ApiInterface.Justificante justificante : justificantes) {
            TableRow row = new TableRow(this);

            // Crear textviews para cada columna
            TextView folioTextView = new TextView(this);
            folioTextView.setText(justificante.getFolio());
            row.addView(folioTextView);

            TextView fechaSolicitudTextView = new TextView(this);
            fechaSolicitudTextView.setText(justificante.getFechaSolicitud());
            row.addView(fechaSolicitudTextView);

            TextView estadoTextView = new TextView(this);
            estadoTextView.setText(justificante.getEstado());
            row.addView(estadoTextView);



            // Agregar la fila a la tabla
            tableLayout.addView(row);
        }
    }

    public void Atras(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
