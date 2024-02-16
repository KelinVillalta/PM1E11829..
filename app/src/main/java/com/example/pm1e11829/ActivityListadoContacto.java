package com.example.pm1e11829;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm1e11829.Clase.Contactos;
import com.example.pm1e11829.Configuraciones.SQLiteConexion;
import com.example.pm1e11829.Configuraciones.Transacciones;

import java.util.ArrayList;
public class ActivityListadoContacto extends AppCompatActivity  {
    /* Variables globales */
    SQLiteConexion conexion;
    ListView lista;
    ArrayList<Contactos> listaContactos;
    ArrayList <String> ArregloContactos;
    EditText alctxtnombre, txtTelefono;
    Button alcbtnAtras;

    ImageButton btnactualizarContacto, btnEliminar, btnCompartir, btnVerImagen, btnLlamar;
    Intent intent;
    Contactos contacto;


    static final int PETICION_LLAMADA_TELEFONO = 102;


    int previousPosition = 1;
    int count=1;
    long previousMil=0;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_contacto);

        lista = (ListView) findViewById(R.id.LisViewContactos);
        intent = new Intent(getApplicationContext(),ActivityActualizarContacto.class);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);

        obtenerlistaContactos();

        //llenar grip con datos contactos
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_checked,ArregloContactos);
        lista.setAdapter(adp);

        /*BUSCAR CONTACTOS EN LISTA*/
        alctxtnombre = (EditText) findViewById(R.id.alctxtnombre);

        alctxtnombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                adp.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //--------------------------------------LISTA------------------------------------------
        //seteo el contacto seleccionado para luego iniciar la actividad en el boton actualizar



        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                previousPosition=i;
                count=1;
                previousMil=System.currentTimeMillis();
                //un clic
                contacto = listaContactos.get(i);//lleno la lista de contacto
                setContactoSeleccionado();

            }


        });




        alcbtnAtras = (Button) findViewById(R.id.btnAtras);
        btnactualizarContacto = (ImageButton) findViewById(R.id.btnActualizarContacto);
        btnEliminar = (ImageButton) findViewById(R.id.btnaclEliminarContacto);
        btnCompartir = (ImageButton) findViewById(R.id.btnCompartir);
        btnVerImagen = (ImageButton) findViewById(R.id.btnVerImagen);
        btnLlamar = (ImageButton) findViewById(R.id.btnllamar);


        alcbtnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnVerImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lista.getCheckedItemCount() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("No ha selecionado ningun contacto");
                    builder.setMessage("Por favor, seleccione al menos un elemento.");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();


                }else {
                    try {
                        Intent intent = new Intent(getApplicationContext(), ActivityVerFoto.class);
                        intent.putExtra("codigoParaFoto", contacto.getCodigo() + "");
                        startActivity(intent);
                    } catch (NullPointerException e) {
                        Intent intent = new Intent(getApplicationContext(), ActivityVerFoto.class);
                        intent.putExtra("codigoParaFoto", "1");
                        startActivity(intent);
                    }

                }
            }
        });

        btnactualizarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lista.getCheckedItemCount() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("No ha selecionado ningun contacto");
                    builder.setMessage("Por favor, seleccione al menos un elemento.");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();


                }else {

                    startActivity(intent);
                }
            }
        });

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lista.getCheckedItemCount() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("No ha selecionado ningun contacto");
                    builder.setMessage("Por favor, seleccione al menos un elemento.");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();


                }else {
                    enviarContacto();
                }
            }
        });


        //context = this;

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lista.getCheckedItemCount() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("No ha selecionado ningun contacto");
                    builder.setMessage("Por favor, seleccione al menos un elemento.");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    // set title

                    alertDialogBuilder.setTitle("Eliminar Contacto");


                    // set dialog message
                    alertDialogBuilder
                            .setMessage("¿Está seguro de eliminar a " + contacto.getNombreContacto() + " de su lista de contactos?")
                            .setCancelable(false)
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // si el usuario da click en si procede a llamar el metodo de eliminar
                                    eliminarContacto();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }
            }
        });

        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lista.getCheckedItemCount() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("No ha selecionado ningun contacto");
                    builder.setMessage("Por favor, seleccione al menos un elemento.");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    // set title

                    alertDialogBuilder.setTitle("Llamar Contacto");


                    // set dialog message
                    alertDialogBuilder
                            .setMessage("¿Está seguro que desea llamar a " + contacto.getNombreContacto() + "?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // si el usuario da click en si procede a llamar el metodo de llamar
                                    Llamar();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


                }
            }
        });
    }







    private void Llamar() {
        // valido si el permiso para acceder a la telefono esta otorgado
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            // Otorgamos el permiso para acceder al telefono
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE}, PETICION_LLAMADA_TELEFONO);
        }else{
            LlamarContacto();
        }
    }

    private void LlamarContacto() {
        String numero = "+"+contacto.getCodigoPais()+contacto.getNumeroContacto();
       /* Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+numero));
        startActivity(intent);*/


        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + numero));
        startActivity(intent);


    }

    private void eliminarContacto() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        int obtenerCodigo = contacto.getCodigo();
        String nombre = contacto.getNombreContacto();

        db.delete(Transacciones.tablacontactos,Transacciones.id +" = "+ obtenerCodigo, null);

        Toast.makeText(getApplicationContext(), "El contacto " + nombre +" se ha eliminado Correctamente"
                ,Toast.LENGTH_LONG).show();
        db.close();

        //despues de eliminar vuelve a abrir la activida, limpiando los menu anteriores
        Intent intent = new Intent(this, ActivityListadoContacto.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();


    }

    private void enviarContacto(){
        String contactoEnviado = "El numero de "+contacto.getNombreContacto().toString()+
                " es +"+contacto.getCodigoPais()+contacto.getNumeroContacto() ;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, contactoEnviado);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }


    private void setContactoSeleccionado() {


        intent.putExtra("codigo", contacto.getCodigo()+"");
        intent.putExtra("nombreContacto", contacto.getNombreContacto());
        intent.putExtra("numeroContacto", contacto.getNumeroContacto()+"");
        intent.putExtra("codigoPais", contacto.getCodigoPais()+"");
        intent.putExtra("notaContacto", contacto.getNota());
        //startActivity(intent);
    }



    private void obtenerlistaContactos() {
        //conexion a la BD modo lectura
        SQLiteDatabase db = conexion.getReadableDatabase();

        //clase empleados
        Contactos list_contact = null;

        //inicializar array empleados con la clase
        listaContactos = new ArrayList<Contactos>();

        //consulta BD directa
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Transacciones.tablacontactos, null);

        //RECORRER LA TABLA MOVIENDONOS SOBRE EL CURSOR
        while (cursor.moveToNext())
        {
            list_contact = new Contactos();
            list_contact.setCodigo(cursor.getInt(0));
            list_contact.setNombreContacto(cursor.getString(1));
            list_contact.setNumeroContacto(cursor.getInt(2));
            list_contact.setNota(cursor.getString(3));
            list_contact.setCodigoPais(cursor.getString(5));
            listaContactos.add(list_contact);
        }
        cursor.close();
        //metodo para llenar lista
        llenarlista();

    }

    private void llenarlista()
    {
        ArregloContactos = new ArrayList<String>();

        for (int i=0; i<listaContactos.size();i++)
        {
            ArregloContactos.add(listaContactos.get(i).getNombreContacto()+" | "+
                    listaContactos.get(i).getCodigoPais()+
                    listaContactos.get(i).getNumeroContacto());

        }
    }

}
