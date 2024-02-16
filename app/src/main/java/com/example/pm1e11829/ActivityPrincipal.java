package com.example.pm1e11829;
import static com.example.pm1e11829.R.id.btn_lista;
import static com.example.pm1e11829.R.id.btn_paises;
import static com.example.pm1e11829.R.id.btn_registrar;
import static com.example.pm1e11829.R.id.btn_salir;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
public class ActivityPrincipal extends AppCompatActivity{
    ImageButton btningresar;
    ImageButton btnlista;
    ImageButton btnpaises;
    ImageButton btnSalir;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        btningresar = (ImageButton) findViewById(btn_registrar);
        btnlista = (ImageButton) findViewById(btn_lista);
        btnpaises = (ImageButton) findViewById(btn_paises);
        btnSalir = (ImageButton) findViewById(btn_salir);
        btningresar.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        });

        btnlista.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),ActivityListadoContacto.class);
            startActivity(intent);
        });

        btnpaises.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),ActivityPais.class);
            startActivity(intent);
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Salir();
            }

            private void Salir() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPrincipal.this);
                builder.setMessage("¿Estás seguro de que deseas salir de la aplicación?")
                        .setCancelable(false)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
