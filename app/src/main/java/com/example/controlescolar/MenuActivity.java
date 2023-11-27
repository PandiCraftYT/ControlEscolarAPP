package com.example.controlescolar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    GridLayout maingrind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        maingrind = (GridLayout) findViewById(R.id.maingrid);

        setSingleEvent(maingrind);
    }

    public void cerrarSesionBtn(View view){
        Toast.makeText(this, "Has Cerrado Sesión con EXITÓ", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MenuActivity.this, LoginActivity.class));
    }
    private void setSingleEvent(GridLayout maingrind) {
        for (int i=0; i < maingrind.getChildCount(); i++){
            CardView cardView = (CardView) maingrind.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalI ==0){
                        Intent intent = new Intent(MenuActivity.this, Kardex.class);
                        startActivity(intent);
                    } else if (finalI == 1) {
                        Intent intent = new Intent(MenuActivity.this, Estudio.class);
                        startActivity(intent);
                    } else if (finalI == 2) {
                        Intent intent = new Intent(MenuActivity.this, Credencial.class);
                        startActivity(intent);
                    } else if (finalI == 3) {
                        String url = "https://www.uas.edu.mx/servicios/calendario/";

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);

                    } else if (finalI == 4) {
                        Intent intent = new Intent(MenuActivity.this, Justificante.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MenuActivity.this, " NO DISPONIBLE", Toast.LENGTH_SHORT).show();
                    }
                    
                }

            });
        }
    }
}
