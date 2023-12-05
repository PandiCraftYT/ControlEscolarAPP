package com.example.controlescolar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText campo1, campo2;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("MiApp", "Activity Login creada correctamente.");
        campo1 = findViewById(R.id.editTextNum_Cuenta);
        campo2 = findViewById(R.id.editTextNumberPassword);

        // Configuración de Retrofit
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
    }

    public void loginBtn(View view) {
        Log.d("MiApp", "Botón de inicio de sesión presionado.");
        String noCuenta = campo1.getText().toString();
        String password = campo2.getText().toString();
        Log.d("MiApp", "Intentando autenticar con noCuenta: " + noCuenta);
        if (TextUtils.isEmpty(noCuenta) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor, completa ambos campos", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("no_cuenta", noCuenta);
        jsonBody.addProperty("password", password);

        // Enviar la solicitud con el objeto JSON
        Call<ApiInterface.ApiResponse> call = apiInterface.authenticate(jsonBody);

        call.enqueue(new Callback<ApiInterface.ApiResponse>() {
            @Override
            public void onResponse(Call<ApiInterface.ApiResponse> call, Response<ApiInterface.ApiResponse> response) {
                Log.d("MiApp", "Respuesta de la API recibida.");
                if (response.isSuccessful()) {
                    ApiInterface.ApiResponse apiResponse = response.body();

                    if (apiResponse != null && apiResponse.isSuccess()) {
                        // Inicio de sesión exitoso
                        Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                    } else {
                        // Credenciales incorrectas
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Error en la respuesta del servidor

                    Toast.makeText(LoginActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiInterface.ApiResponse> call, Throwable t) {
                // Manejar errores aquí
                Log.e("MiApp", "Error al conectarse al servidor", t);
                Toast.makeText(LoginActivity.this, "Error al conectarse al servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
