package com.example.ejercicio2_3.clases;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ejercicio2_3.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class Adaptador extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<Photograph> listaDatos;

    public Adaptador(Context context, ArrayList<Photograph> data){
        this.context = context;
        this.listaDatos = data;

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View vista = inflater.inflate(R.layout.list_item, null);
        TextView descripcion = vista.findViewById(R.id.txtListDescripcion);
        ImageView imagen = vista.findViewById(R.id.txtListImg);
        descripcion.setText(listaDatos.get(i).getDescripcion());
        Bitmap bitmap = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(listaDatos.get(i).getImagen());
        bitmap = BitmapFactory.decodeStream(bais);
        imagen.setImageBitmap(bitmap);
        return vista;
    }

    @Override
    public int getCount() {
        return listaDatos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
