package com.example.ejercicio2_3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ejercicio2_3.clases.SQLiteConexion;
import com.example.ejercicio2_3.clases.Transacciones;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText descripcion;
    ImageView imagen;
    Button guardar, mostrar, tomar;
    Bitmap bmImagen;
    SQLiteConexion conexion;
    String path;
    static final int REQUEST_CODE_CAMARA = 100;
    static final int REQUEST_TAKE_PHOTO = 101;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        tomar.setOnClickListener(this::onClickTakePhoto);
        mostrar.setOnClickListener(this::onClickShowActivity);
        guardar.setOnClickListener(this::onClickSave);
    }

    private void onClickSave(View view) {
        this.view = view;
        if(emptyFields(descripcion)){
            if(bmImagen.getByteCount() > 0){
                guardarImagen();
            }else message("Debe tomar una foto");
        }else message("Debe ingresar su descipción");
    }

    private void guardarImagen() {
        try {
            conexion = new SQLiteConexion(getApplicationContext(), Transacciones.NameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Transacciones.descripcion, descripcion.getText().toString());

            ByteArrayOutputStream baos = new ByteArrayOutputStream(10480);
            bmImagen.compress(Bitmap.CompressFormat.JPEG, 0 , baos);
            byte[] blob = baos.toByteArray();
            values.put(Transacciones.image, blob);

            Long result = db.insert(Transacciones.tablaPhotograph, Transacciones.id, values);
            db.close();
            message("Datos ingresados exitosamente");
            cleanFields();
        }catch (SQLiteException ex){
            message(ex.getMessage());
        }
    }

    private void onClickTakePhoto(View view) {
        assignPermissions();
    }

    private void onClickShowActivity(View view) {
        Intent ventana = new Intent(getApplicationContext(), ActivityMostrar.class);
        startActivity(ventana);
        finish();
    }

    private void assignPermissions() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA }, REQUEST_CODE_CAMARA);
        }else takePhoto();
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this, "com.example.ejercicio2_3.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            } catch (IOException ex) {
                message(ex.getMessage());
            }catch (Exception e){
                message(e.getMessage());
            }
        }
    }

    public void message(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void cleanFields(){
        descripcion.setText("");
        imagen.setImageBitmap(null);
        bmImagen = null;
    }

    public boolean emptyFields(EditText field){
        return field.getText().toString().length() > 1 ? true : false;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpeg", storageDir);

        path = image.getAbsolutePath();
        return image;
    }

    private void init(){
        descripcion = findViewById(R.id.txtDescripcion);
        imagen = findViewById(R.id.txtImg);
        guardar = findViewById(R.id.btnGuardar);
        mostrar = findViewById(R.id.btnMostrar);
        tomar = findViewById(R.id.btnTomarFoto);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_CAMARA) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) takePhoto();
            else message("Permiso denegado");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap image = BitmapFactory.decodeFile(path);
            bmImagen = image;
            imagen.setImageBitmap(image);
            message("La imagen se guardo exitosamente");
        }
    }
}
