package com.example.controlescolar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
/*import android.widget.Button;*/
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


    private EditText userEdt, passEdt; //Se declaran variables de tipo EditText (campos de entrada de texto) como userEdt, passEdt,
    EditText campo1,campo2; //campo1, y campo2 para manipular campos de entrada en la interfaz.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Busca y asigna los EditText campo1 y campo2 a los elementos de la interfaz definidos en XML mediante sus ID correspondientes.
        campo1=(EditText) findViewById(R.id.editTextNum_Cuenta);
        campo2=(EditText) findViewById(R.id.editTextNumberPassword);

        //Configura un OnClickListener para el TextView textViewOlvidasteNIP.
        TextView textViewOlvidasteNIP = findViewById(R.id.textViewOlvidasteNIP);
        textViewOlvidasteNIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAlerta();
            } // de modo que cuando se haga clic en él, se llame al método mostrarAlerta().
        });
    }


    public void loginBtn(View view){ //Este método se activa cuando se hace clic en un botón asociado con la función loginBtn.

//Comprueba si la validación es exitosa llamando al método validar().
        if(validar()){
            Toast.makeText(this, "Bienvenido\nCarlos Nevarez", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MenuActivity.class)); // Si es verdadero, muestra un mensaje de bienvenida y comienza la MenuActivity.
        }
    }
    public boolean validar() { //Realiza validaciones en los campos campo1 y campo2 para verificar si contienen los valores esperados.
        boolean retorno = true;

        String c1 = campo1.getText().toString();
        String c2 = campo2.getText().toString();
//Comprueba si los campos no están vacíos y si contienen ciertos valores específicos.
        if (!c1.isEmpty()) {
            if (!c1.equals("17507995")) {
                campo1.setError("El Numero de Cuenta que ingresaste es incorrecta.");
                retorno = false;
            }
        } else {
            campo1.setError("Campo Nº Cuenta no puede estar vacío!");
            retorno = false;
        }

        if (!c2.isEmpty()) {
            if (!c2.equals("1234")) {
                campo2.setError("El NIP que ingresaste es incorrecta.");
                retorno = false;
            }
        } else {
            campo2.setError("Campo NIP no puede estar vacío!");
            retorno = false;
        }

        return retorno; // Retorna un valor booleano basado en el resultado de estas validaciones.
    }


    public void mostrarAlerta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Para poder cambiar tu NIP / Numero de Cuenta, es necesario que acudas a tu facultad y pedirle a un administrador que te la cambie.")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Cierra la alerta al presionar "Aceptar"
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    /*
    private void setVariable(){
        loginBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (userEdt.getText().toString().isEmpty() && passEdt.getText().toString().isEmpty()){
                        Toast.makeText(LoginActivity.this, "El Nº Cuenta o NIP es Incorrecto.", Toast.LENGTH_SHORT).show();
                    } else if (userEdt.getText().toString().equals("17507995") && passEdt.getText().toString().equals("1234") ){
                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                    }
                }
        });
    }

    private void initViw(){
        userEdt=findViewById(R.id.editTextNum_Cuenta);
        passEdt=findViewById(R.id.editTextNumberPassword);
        loginBtn=findViewById(R.id.loginBtn);
    }*/
}