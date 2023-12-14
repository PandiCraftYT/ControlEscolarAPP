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
import android.os.Environment;
import android.util.Log;
import android.view.View;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
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
        TableLayout tableLayout = findViewById(R.id.tableLayout); // Referencia al layout de la tabla

        for (ApiInterface.Justificante justificante : justificantes) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            rowLayoutParams.setMargins(0, 12, 0, 12);
            row.setLayoutParams(rowLayoutParams);

            int backgroundColor = Color.GRAY; // Color por defecto para el estado "PENDIENTE"

            switch (justificante.getEstado()) {
                case "aceptado":
                    backgroundColor = Color.GREEN;
                    break;
                case "rechazado":
                    backgroundColor = Color.RED;
                    break;
                // Puedes agregar más casos según necesites
            }

            row.setBackgroundColor(backgroundColor); // Establecer color de fondo de la fila

            TextView folioTextView = new TextView(this);
            folioTextView.setText("FOLIO:\n" + justificante.getFolio());
            folioTextView.setTextSize(20);
            folioTextView.setTextColor(Color.BLACK);
            folioTextView.setPadding(0, 12, 24, 12);
            row.addView(folioTextView);

            TextView fechaSolicitudTextView = new TextView(this);
            fechaSolicitudTextView.setText("FECHA SOLICITUD:\n" + justificante.getFechaSolicitud());
            fechaSolicitudTextView.setTextSize(20);
            fechaSolicitudTextView.setTextColor(Color.BLACK);
            fechaSolicitudTextView.setPadding(0, 12, 24, 12);
            row.addView(fechaSolicitudTextView);

            TextView estadoTextView = new TextView(this);
            estadoTextView.setText("ESTADO:\n" + justificante.getEstado());
            estadoTextView.setTextSize(20);
            estadoTextView.setTextColor(Color.BLACK);
            estadoTextView.setPadding(0, 12, 24, 12);
            row.addView(estadoTextView);

            tableLayout.addView(row); // Agregar fila a la tabla

            View separatorView = new View(this);
            TableRow.LayoutParams separatorParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    20
            );
            separatorView.setLayoutParams(separatorParams);
            tableLayout.addView(separatorView); // Agregar separador visual

            if (justificante.getEstado().equals("aceptado")) {
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HistorialJustificante.this);
                        builder.setTitle("Descargar Archivo")
                                .setMessage("¿Quieres descargar este archivo?")
                                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        descargarJustificante(justificante.getFolio(), justificante.getFechaSolicitud());
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });
            }
        }
    }



    // Método para descargar un justificante
    private void descargarJustificante(String folio, String fechaSolicitud) {
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("folio", folio);
        jsonBody.addProperty("fecha_solicitud", fechaSolicitud);

        Call<ResponseBody> call = apiInterface.descargarJustificante(jsonBody); // Reemplaza con el método correspondiente de tu API para descargar justificantes

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        try {
                            boolean writtenToDisk = saveJustificanteToDisk(responseBody, folio + ".pdf");
                            if (writtenToDisk) {
                                showToast("Justificante descargado y guardado");

                                // Abrir el archivo PDF automáticamente después de descargarlo
                                openDownloadedPDFFile(folio + ".pdf");
                            } else {
                                showToast("Error al guardar el justificante");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            showToast("Error al guardar el justificante: " + e.getMessage());
                        }
                    }
                } else {
                    Log.e(TAG, "Error en la respuesta del servidor al descargar justificante");
                    showToast("Error al descargar justificante");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Error en la llamada a la API al descargar justificante", t);
                showToast("Error al descargar justificante. Verifica tu conexión a Internet.");
            }
        });
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
    // Método para guardar el justificante en el dispositivo
    private boolean saveJustificanteToDisk(ResponseBody body, String filename) throws IOException {
        try {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(directory, filename);

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

    // Método para mostrar un Toast con el mensaje especificado
    private void showToast(String message) {
        Toast.makeText(HistorialJustificante.this, message, Toast.LENGTH_SHORT).show();
    }

    public void Atras(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
