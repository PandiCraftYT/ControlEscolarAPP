package com.example.controlescolar.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.controlescolar.R;
import com.example.controlescolar.api.ApiInterface;
import com.example.controlescolar.api.RetrofitClient;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Justificante extends AppCompatActivity {
    // Declaración de variables y constantes

    private String noCuenta;
    private ApiInterface apiInterface;
    private Uri uri;
    private static final int PICK_FILE_REQUEST_CODE = 1001;
    private Button btnAdjuntarArchivo;
    private TextView txtNombreArchivo;
    private Button btnEnviarSolicitud;
    private Button btnSelectDates;

    TextView textViewNumCuenta;

    EditText editTextRazonInasistencia;
    private TextView txtFechasSeleccionadas;
    private String fechaDesde = "";
    private String fechaHasta = "";
    private boolean seleccionandoFechaDesde = true;
    private TextView opcionSeleccionada;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_justificante);
        textViewNumCuenta = findViewById(R.id.textViewNoCuenta);
        editTextRazonInasistencia = findViewById(R.id.editTextRazonInasistencia);

        noCuenta = getIntent().getStringExtra("no_cuenta");

        Spinner spinnerOpciones = findViewById(R.id.spinnerOpciones);
        opcionSeleccionada = findViewById(R.id.opcionSeleccionada);

        // Crear un ArrayAdapter con el texto "MOTIVO" como primer elemento
        String[] opciones = {"MOTIVO", "PERSONALES", "SALUD", "COVID-19"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                if (position == spinnerOpciones.getSelectedItemPosition()) {
                    view.setBackgroundResource(R.drawable.spinner_selected_item_border);
                } else {
                    view.setBackgroundResource(android.R.color.transparent);
                }

                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOpciones.setAdapter(adapter);

        spinnerOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                opcionSeleccionada.setText("Opción seleccionada: " + selectedItem);
                // Aplicar el estilo al texto de la opción seleccionada
                opcionSeleccionada.setTextAppearance(R.style.OpcionSeleccionadaStyle);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Acción cuando no se selecciona nada en el Spinner (opcional)
            }
        });


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOpciones.setAdapter(adapter);
        // Inicialización de vistas y asignación de listeners
        btnAdjuntarArchivo = findViewById(R.id.btnAdjuntarArchivo);
        btnEnviarSolicitud = findViewById(R.id.btnEnviarSolicitud);
        txtNombreArchivo = findViewById(R.id.txtNombreArchivo);
        btnSelectDates = findViewById(R.id.btnSelectDates);
        txtFechasSeleccionadas = findViewById(R.id.FechaSeleccionada);

        // Listener para el botón de selección de fechas

        btnSelectDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorFecha();
            }
        });

        // Listener para el botón de adjuntar archivo

        btnAdjuntarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjuntarArchivo();
            }
        });
        // Listener para el botón de enviar solicitud

        btnEnviarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarSolicitud();
            }
        });
    }
    // Método para manejar la acción de retroceder

    public void Atras(View view){

        Intent returnIntent = new Intent();
        returnIntent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
        returnIntent.putExtra("nombre", getIntent().getStringExtra("nombre"));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
    // Método para adjuntar un archivo

    private void adjuntarArchivo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Filtra por tipo de archivo de imagen
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    // Método para enviar la solicitud (requiere implementación de la lógica de envío)

    private void enviarSolicitud() {
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        // Obtener la opción seleccionada en el Spinner
        Spinner spinnerOpciones = findViewById(R.id.spinnerOpciones);
        String motivoSeleccionado = spinnerOpciones.getSelectedItem().toString();
        String archivoData = leerContenidoArchivo(uri);
        String razonInasistencia = editTextRazonInasistencia.getText().toString();


        if (txtNombreArchivo.getVisibility() == View.VISIBLE) {
            // Si hay un archivo adjunto
            // Obtener la URI del archivo seleccionado
            uri = obtenerUriArchivoSeleccionado();
            // Leer el contenido del archivo y convertirlo a base64

            // Resto de la lógica para enviar la solicitud...
        } else {
            archivoData = "sin evidencia";
            // Lógica para el caso en que no se adjunta un archivo
        }
        // Crear el objeto JSON con los datos de la solicitud
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("no_cuenta", noCuenta);
        jsonBody.addProperty("fecha_solicitud", obtenerFechaActual());
        jsonBody.addProperty("fecha_justificar", fechaDesde); // Usar fechaDesde como fecha_justificar
        jsonBody.addProperty("motivo", motivoSeleccionado);
        jsonBody.addProperty("descripcion_motivo", razonInasistencia); // Puedes cambiar esto según la lógica de tu aplicación
        jsonBody.addProperty("archivo_nombre", obtenerNombreArchivoAdjunto());
        jsonBody.addProperty("archivo_data", archivoData); // Puedes cambiar esto según la lógica de tu aplicación

        Call<ApiInterface.GuardarSolicitudJustificanteAndroidResponse>call=apiInterface.guardarSolicitudJustificanteAndroid(jsonBody);
        Context applicationContext = getApplicationContext();
        call.enqueue(new Callback<ApiInterface.GuardarSolicitudJustificanteAndroidResponse>() {
            @Override
            public void onResponse(Call<ApiInterface.GuardarSolicitudJustificanteAndroidResponse> call, Response<ApiInterface.GuardarSolicitudJustificanteAndroidResponse> response) {
                if (response.isSuccessful()) {
                    // Procesar la respuesta
                    ApiInterface.GuardarSolicitudJustificanteAndroidResponse guardarResponse = response.body();
                    if (guardarResponse.getStatus().equals("success")) {
                        // Si la solicitud se realiza con éxito, muestra un Toast indicando que la solicitud fue enviada
                        Toast.makeText(applicationContext, "Solicitud Enviada", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("no_cuenta", getIntent().getStringExtra("no_cuenta"));
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        // Manejar el caso en que la solicitud no se pueda realizar
                        Toast.makeText(applicationContext,"No puedes tener más de Cinco solicitudes activas a la vez", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error al solicitar : " + guardarResponse.getMessage());
                    }
                } else {
                    // Manejar el error en la respuesta del servidor
                    Toast.makeText(applicationContext,"Error en la respuesta del servidor al solicitar constancia", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error en la respuesta del servidor al solicitar constancia");
                }
            }

            @Override
            public void onFailure(Call<ApiInterface.GuardarSolicitudJustificanteAndroidResponse> call, Throwable t) {
                // Manejar el error en la llamada a la API
                Toast.makeText(applicationContext,"Error al solicitar constancia. Verifica tu conexión a Internet.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error en la llamada a la API al solicitar constancia", t);
            }
        });


    }    // Método invocado cuando se recibe el resultado de una actividad iniciada con startActivityForResult

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                uri = data.getData();
                String nombreArchivo = obtenerNombreArchivo(uri);
                mostrarNombreArchivo(nombreArchivo);
            }
        }
    }

    // Método para obtener el nombre del archivo seleccionado

    private String obtenerNombreArchivo(Uri uri) {
        String nombreArchivo = null;
        try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nombreIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                nombreArchivo = cursor.getString(nombreIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nombreArchivo;
    }
    // Método para mostrar el nombre del archivo seleccionado

    private void mostrarNombreArchivo(String nombreArchivo) {
        txtNombreArchivo.setText(nombreArchivo);
        txtNombreArchivo.setVisibility(View.VISIBLE);
    }
    // Método para mostrar el selector de fechas

    private void mostrarSelectorFecha() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Formatear la fecha seleccionada al formato "yyyy-MM-dd"
                        fechaDesde = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                        mostrarFechasSeleccionadas();
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    // Método para mostrar las fechas seleccionadas

    private void mostrarFechasSeleccionadas() {
        if (!fechaDesde.isEmpty()) {
            String textoFechas = "Fecha seleccionada: " + fechaDesde;
            txtFechasSeleccionadas.setText(textoFechas);
            txtFechasSeleccionadas.setVisibility(View.VISIBLE);
        }
    }
    // Método para reiniciar las fechas seleccionadas
    private String obtenerFechaActual() {
        // Puedes implementar la lógica para obtener la fecha actual en el formato deseado
        // Aquí se utiliza simplemente la fecha actual en formato "yyyy-MM-dd" como ejemplo
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String obtenerNombreArchivoAdjunto() {
        // Puedes implementar la lógica para obtener el nombre del archivo adjunto
        // En este ejemplo, se obtiene el nombre del archivo seleccionado
        return txtNombreArchivo.getText().toString();
    }
    private void reiniciarFechas() {
        fechaDesde = "";
    }


    private Uri obtenerUriArchivoSeleccionado() {
        // Lógica para obtener la URI del archivo seleccionado
        // Devuelve la URI del archivo seleccionado en el selector de archivos
        // Puedes usar la variable 'uri' que ya tienes en onActivityResult
        return uri;
    }

    private String leerContenidoArchivo(Uri uri) {
        try {
            if (uri != null) {
                // Verificar que el tipo de archivo sea una imagen
                if (getContentResolver().getType(uri).startsWith("image/")) {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    if (inputStream != null) {
                        // Verificar que haya datos disponibles para leer
                        if (inputStream.available() > 0) {
                            // Leer el contenido del archivo y convertirlo a base64
                            byte[] bytes = new byte[inputStream.available()];
                            inputStream.read(bytes);
                            inputStream.close();
                            return Base64.encodeToString(bytes, Base64.DEFAULT);
                        }
                    }
                } else {
                    Log.e("leerContenidoArchivo", "Tipo de archivo no válido");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }



}