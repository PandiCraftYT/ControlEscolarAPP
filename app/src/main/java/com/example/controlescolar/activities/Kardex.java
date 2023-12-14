package com.example.controlescolar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.controlescolar.R;
import com.example.controlescolar.api.ApiInterface;
import com.example.controlescolar.api.RetrofitClient;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kardex extends AppCompatActivity {
    private TextView textViewUserName;
    private TextView textViewUserNoCuenta;
    private TextView textViewCarrera;
    private TextView textViewSemestre;

    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kardex);
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        textViewUserName = findViewById(R.id.textviewUsername);
        textViewUserNoCuenta = findViewById(R.id.textViewNoCuenta);
        textViewCarrera = findViewById(R.id.textViewCarreraa); //arregla eso isaac que salga su carrera y su semestre xD
        textViewSemestre = findViewById(R.id.textViewSemestre);

        // Obtén los datos del Intent o de donde sea que los tengas
        Intent intent = getIntent();
        if (intent != null) {
            String noCuenta = intent.getStringExtra("no_cuenta");
            String nombre = intent.getStringExtra("nombre");

            // Actualiza las vistas con los datos
            textViewUserName.setText(nombre);
            textViewUserNoCuenta.setText(noCuenta);

            // Llamado al servicio getkardex
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("no_cuenta", noCuenta);

            Call<ApiInterface.KardexResponse> call = apiInterface.getKardex(requestBody);
            call.enqueue(new Callback<ApiInterface.KardexResponse>() {
                @Override
                public void onResponse(Call<ApiInterface.KardexResponse> call, Response<ApiInterface.KardexResponse> response) {
                    if (response.isSuccessful()) {
                        ApiInterface.KardexResponse kardexResponse = response.body();
                        if (kardexResponse != null && kardexResponse.isSuccess()) {
                            // Manejar la respuesta exitosa aquí
                            ApiInterface.KardexData kardexData = kardexResponse.getData();
                            List<ApiInterface.KardexEntry> kardexEntries = kardexData.getCurriculo();
                            // Actualizar las vistas con la información del kardex
                            // Ejemplo: textViewCarrera.setText("Carrera: " + kardexData.getCarrera());
                            // textViewSemestre.setText("Semestre: " + kardexData.getSemestre());
                        } else {
                            // Manejar la respuesta no exitosa aquí
                            String errorMessage = (kardexResponse != null) ? kardexResponse.getMessage() : "Unknown error";
                            // Mostrar o registrar el mensaje de error
                        }
                    } else {
                        // Manejar la respuesta no exitosa aquí
                        // Mostrar o registrar el mensaje de error
                    }
                }

                @Override
                public void onFailure(Call<ApiInterface.KardexResponse> call, Throwable t) {
                    // Manejar el fallo de la llamada aquí
                    // Mostrar o registrar el mensaje de error
                }
            });
        }
    }

    public void Atras(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
        returnIntent.putExtra("nombre", getIntent().getStringExtra("nombre"));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
