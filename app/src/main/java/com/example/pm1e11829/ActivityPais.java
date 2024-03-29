package com.example.pm1e11829;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pm1e11829.Configuraciones.SQLiteConexion;
import com.example.pm1e11829.Configuraciones.Transacciones;
public class ActivityPais  extends AppCompatActivity{
    EditText aptxtCodigo,aptxtPais;
    Button  aptnAtras;
    Button apbtnGuardar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pais);

        aptxtCodigo = (EditText) findViewById(R.id.aptxtCodigo);
        aptxtPais = (EditText) findViewById(R.id.aptxtPais);
        apbtnGuardar = (Button) findViewById(R.id.apbtnGuardar);
        aptnAtras = (Button) findViewById(R.id.apbtnAtras);

        apbtnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertarPais();
            }
        });

        aptnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void InsertarPais() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.codigo,aptxtCodigo.getText().toString());
        valores.put(Transacciones.p_pais,aptxtPais.getText().toString());

        Long resultado = db.insert(Transacciones.tblPaises,Transacciones.codigo,valores);

        Toast.makeText(getApplicationContext(),"Registro Exitoso!!!"+resultado.toString(),Toast.LENGTH_LONG).show();
        db.close();

        limpiarPantalla();

    }

    private void limpiarPantalla() {
        aptxtPais.setText("");
        aptxtCodigo.setText("");
    }
}
