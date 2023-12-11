package com.example.controlescolar.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.controlescolar.R;

import java.util.Calendar;

public class Justificante extends AppCompatActivity {
    // Declaración de variables y constantes

    private static final int PICK_FILE_REQUEST_CODE = 1001;
    private Button btnAdjuntarArchivo;
    private TextView txtNombreArchivo;
    private Button btnEnviarSolicitud;
    private Button btnSelectDates;
    private TextView txtFechasSeleccionadas;
    private String fechaDesde = "";
    private String fechaHasta = "";
    private boolean seleccionandoFechaDesde = true;
    private TextView opcionSeleccionada;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_justificante);



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
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }
    // Método para enviar la solicitud (requiere implementación de la lógica de envío)

    private void enviarSolicitud() {
        // Lógica para enviar la solicitud
    }

    // Método invocado cuando se recibe el resultado de una actividad iniciada con startActivityForResult

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
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
                        if (seleccionandoFechaDesde) {
                            fechaDesde = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            seleccionandoFechaDesde = false;
                        } else {
                            fechaHasta = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            mostrarFechasSeleccionadas();
                        }
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
    }
    // Método para mostrar las fechas seleccionadas

    private void mostrarFechasSeleccionadas() {
        if (!fechaDesde.isEmpty() && !fechaHasta.isEmpty()) {
            String textoFechas = "DE: " + fechaDesde + " A: " + fechaHasta;
            txtFechasSeleccionadas.setText(textoFechas);
            txtFechasSeleccionadas.setVisibility(View.VISIBLE);
            reiniciarFechas();
        }
    }
    // Método para reiniciar las fechas seleccionadas

    private void reiniciarFechas() {
        fechaDesde = "";
        fechaHasta = "";
        seleccionandoFechaDesde = true;
    }
}