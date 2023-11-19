package com.example.ejercicio2_3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ejercicio2_3.clases.Adaptador;
import com.example.ejercicio2_3.clases.Photograph;
import com.example.ejercicio2_3.clases.SQLiteConexion;
import com.example.ejercicio2_3.clases.Transacciones;

import java.util.ArrayList;

public class ActivityMostrar extends AppCompatActivity {

    ListView lista;
    Button btnAtras;
    SQLiteConexion conexion;
    Photograph photo;
    ArrayList<Photograph> listaDatos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);
        init();
        btnAtras.setOnClickListener(this::onClickGoBack);
    }

    private void onClickGoBack(View view) {
        Intent ventana = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(ventana);
        finish();
    }

    private void init(){
        lista = findViewById(R.id.txtListaImagenes);
        btnAtras = findViewById(R.id.btnAtras);
        cargarLista();
        try {
            lista.setAdapter(new Adaptador(this, listaDatos));
        } catch (Exception ex) {
            message(ex.getMessage());
        }
    }

    protected void cargarLista(){
        try {
            conexion = new SQLiteConexion(getApplicationContext(), Transacciones.NameDatabase, null, 1);
            SQLiteDatabase db = conexion.getReadableDatabase();
            Cursor cursor = db.rawQuery(Transacciones.consultPhotograph, null);
            while(cursor.moveToNext()) {
                photo = new Photograph();
                photo.setId(cursor.getInt(0));
                photo.setDescripcion(cursor.getString(1));
                photo.setImagen(cursor.getBlob(2));
                listaDatos.add(photo);
            }
            cursor.close();
        }catch (SQLiteException ex){
            message(ex.getMessage());
        }
    }

    public void message(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}