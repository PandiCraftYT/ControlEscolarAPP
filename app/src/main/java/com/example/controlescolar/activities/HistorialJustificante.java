package com.example.controlescolar.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
            TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            rowLayoutParams.setMargins(0, 12, 0, 12); // Ajustar márgenes entre filas
            row.setLayoutParams(rowLayoutParams);

            // Establecer un fondo para la fila
            row.setBackgroundResource(R.drawable.background_info); // Reemplaza R.drawable.background_row con el nombre de tu fondo

            // Crear TextView para mostrar el valor del folio con salto de línea
            TextView folioTextView = new TextView(this);
            folioTextView.setText("FOLIO:\n" + justificante.getFolio());
            folioTextView.setTextSize(20); // Tamaño del texto
            folioTextView.setTextColor(Color.BLACK); // Color del texto
            folioTextView.setPadding(0, 12, 24, 12); // Padding del texto
            row.addView(folioTextView);

            // Crear TextView para mostrar el valor de la fecha de solicitud con salto de línea
            TextView fechaSolicitudTextView = new TextView(this);
            fechaSolicitudTextView.setText("FECHA SOLICITUD:\n" + justificante.getFechaSolicitud());
            fechaSolicitudTextView.setTextSize(20); // Tamaño del texto
            fechaSolicitudTextView.setTextColor(Color.BLACK); // Color del texto
            fechaSolicitudTextView.setPadding(0, 12, 24, 12); // Padding del texto
            row.addView(fechaSolicitudTextView);

            // Crear TextView para mostrar el valor del estado con salto de línea
            TextView estadoTextView = new TextView(this);
            estadoTextView.setText("ESTADO:\n" + justificante.getEstado());
            estadoTextView.setTextSize(20); // Tamaño del texto
            estadoTextView.setTextColor(Color.BLACK); // Color del texto
            estadoTextView.setPadding(0, 12, 24, 12); // Padding del texto
            row.addView(estadoTextView);

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
        }
    }

    public void Atras(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
