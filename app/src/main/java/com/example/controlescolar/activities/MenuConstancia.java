package com.example.controlescolar.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import android.os.Environment;
import okhttp3.ResponseBody;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
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
                                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Acción al presionar "Sí"
                                        descargarConstancia(constancia.getFolio(), constancia.getFechaSolicitud());
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
    private void openDownloadedPDFFile(String filename) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

        if (file.exists()) {
            Uri fileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);

            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(fileUri, "application/pdf");
            target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = Intent.createChooser(target, "Abrir archivo");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                showToast("No se pudo abrir el archivo. Instala una aplicación para visualizar PDFs.");
            }
        } else {
            showToast("El archivo no existe.");
        }
    }


    // Método para mostrar un Toast con el mensaje especificado
    private void showToast(String message) {
        Toast.makeText(MenuConstancia.this, message, Toast.LENGTH_SHORT).show();
    }

    private void descargarConstancia(String folio, String fechaSolicitud) {
        final InputStream[] inputStream = {null}; // Declare here

        // Crear el objeto JSON para enviar en la solicitud
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("folio", folio);
        jsonBody.addProperty("fecha_solicitud", fechaSolicitud);

        // Hacer la llamada a la API para descargar la constancia
        Call<ResponseBody> call = apiInterface.generarConstanciaAndroid(jsonBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        try {
                            boolean writtenToDisk = saveToDisk(responseBody, folio + ".pdf");
                            if (writtenToDisk) {
                                showToast("Constancia descargada y guardada");

                                // Abrir el archivo PDF automáticamente después de descargarlo
                                openDownloadedPDFFile(folio + ".pdf");
                            } else {
                                showToast("Error al guardar la constancia");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            showToast("Error al guardar la constancia: " + e.getMessage());
                        }
                    }
                } else {
                    Log.e(TAG, "Error en la respuesta del servidor al descargar constancia");
                    showToast("Error al descargar constancia");
                }
            }
            private boolean saveToDisk(ResponseBody body, String filename) throws IOException {
                try {
                    // Obtener el directorio de almacenamiento externo público
                    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    // Crear un archivo para almacenar la constancia descargada
                    File file = new File(directory, filename);

                    // Obtener el flujo de entrada del cuerpo de la respuesta
                    InputStream inputStream = null;
                    OutputStream outputStream = null;

                    try {
                        byte[] fileReader = new byte[4096];
                        long fileSize = body.contentLength();
                        long fileSizeDownloaded = 0;

                        inputStream = body.byteStream();
                        outputStream = new FileOutputStream(file);

                        while (true) {
                            int read = inputStream.read(fileReader);
                            if (read == -1) {
                                break;
                            }

                            outputStream.write(fileReader, 0, read);
                            fileSizeDownloaded += read;
                        }

                        outputStream.flush();
                        return true;
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Manejar el error en la llamada a la API
                Log.e(TAG, "Error en la llamada a la API al descargar constancia", t);
                showToast("Error al descargar constancia. Verifica tu conexión a Internet.");
            }
        });
    }



}