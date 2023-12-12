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