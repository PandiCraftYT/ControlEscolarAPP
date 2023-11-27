package com.example.controlescolar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class Justificante extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST_CODE = 1001;
    private Button btnAdjuntarArchivo;
    private TextView txtNombreArchivo;
    private Button btnEnviarSolicitud;
    private Button btnSelectDates;
    private TextView txtFechasSeleccionadas;
    private String fechaDesde = "";
    private String fechaHasta = "";
    private boolean seleccionandoFechaDesde = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_justificante);

        btnAdjuntarArchivo = findViewById(R.id.btnAdjuntarArchivo);
        btnEnviarSolicitud = findViewById(R.id.btnEnviarSolicitud);
        txtNombreArchivo = findViewById(R.id.txtNombreArchivo);

        btnSelectDates = findViewById(R.id.btnSelectDates);
        txtFechasSeleccionadas = findViewById(R.id.FechaSeleccionada);

        btnSelectDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorFecha();
            }
        });


        btnAdjuntarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjuntarArchivo();
            }
        });

        btnEnviarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarSolicitud();
            }
        });
    }

    public void Atras(View view){
        startActivity(new Intent(Justificante.this, MenuActivity.class));
    }
    private void adjuntarArchivo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    private void enviarSolicitud() {
        // Lógica para enviar la solicitud
    }


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

    private void mostrarNombreArchivo(String nombreArchivo) {
        txtNombreArchivo.setText(nombreArchivo);
        txtNombreArchivo.setVisibility(View.VISIBLE);
    }
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

    private void mostrarFechasSeleccionadas() {
        if (!fechaDesde.isEmpty() && !fechaHasta.isEmpty()) {
            String textoFechas = "DE: " + fechaDesde + " A: " + fechaHasta;
            txtFechasSeleccionadas.setText(textoFechas);
            txtFechasSeleccionadas.setVisibility(View.VISIBLE);
            reiniciarFechas();
        }
    }

    private void reiniciarFechas() {
        fechaDesde = "";
        fechaHasta = "";
        seleccionandoFechaDesde = true;
    }
}