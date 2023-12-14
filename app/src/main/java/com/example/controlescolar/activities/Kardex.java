package com.example.controlescolar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
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



    private TableLayout tableLayout;


    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kardex);
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        tableLayout = findViewById(R.id.tableLayout);

        textViewUserName = findViewById(R.id.textviewUsername);
        textViewUserNoCuenta = findViewById(R.id.textViewNoCuenta);


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
                            for (ApiInterface.KardexEntry entry : kardexEntries) {
                                addRowToTable(entry.getMateria(), entry.getSemestre(), entry.getCalificacion());
                            }

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

    private int rowCount = 0; // Variable para llevar el conteo de filas agregadas

    private void addRowToTable(String materia, String semestre, String calificacion) {
        TableRow row = new TableRow(this);

        // Columna de Materia
        TextView materiaTextView = new TextView(this);
        materiaTextView.setTextSize(13); // Tamaño del texto (8 SP)
        materiaTextView.setTextColor(getResources().getColor(R.color.black)); // Color del texto
        materiaTextView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL); // Alinear texto a la izquierda y centrado verticalmente

        // Verificar si el texto excede los 20 caracteres
        if (materia.length() > 20) {
            String[] words = materia.split(" ");
            StringBuilder newText = new StringBuilder();

            // Añadir salto de línea después de cada palabra si excede los 20 caracteres
            int charCount = 0;
            for (String word : words) {
                if (charCount + word.length() > 20) {
                    newText.append("\n");
                    charCount = 0;
                }
                newText.append(word).append(" ");
                charCount += word.length() + 1;
            }
            materiaTextView.setText(newText.toString().trim()); // Establecer texto modificado
        } else {
            materiaTextView.setText(materia); // Establecer texto normal
        }

        row.addView(materiaTextView);

        // Columna de Semestre
        TextView semestreTextView = new TextView(this);
        semestreTextView.setText(semestre);
        semestreTextView.setTextSize(13); // Tamaño del texto (8 SP)
        semestreTextView.setTextColor(getResources().getColor(R.color.black)); // Color del texto
        semestreTextView.setGravity(Gravity.CENTER); // Centrar el texto horizontalmente
        row.addView(semestreTextView);

        // Columna de Calificación
        TextView calificacionTextView = new TextView(this);
        calificacionTextView.setText(calificacion);
        calificacionTextView.setTextSize(13); // Tamaño del texto (8 SP)
        calificacionTextView.setTextColor(getResources().getColor(R.color.black)); // Color del texto
        calificacionTextView.setGravity(Gravity.CENTER); // Centrar el texto horizontalmente
        row.addView(calificacionTextView);

        // Agregar la fila a la tabla
        tableLayout.addView(row);

        rowCount++; // Incrementar el contador de filas

        // Agregar un salto de línea después de cada 5 filas
        if (rowCount % 5 == 0) {
            TextView emptyTextView = new TextView(this);
            emptyTextView.setText("\n");
            tableLayout.addView(emptyTextView);
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
