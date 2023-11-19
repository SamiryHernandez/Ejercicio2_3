package com.example.ejercicio2_3.clases;

public class Transacciones {

    public static final String NameDatabase = "DBE24";

    public static final String tablaPhotograph = "photograph";

    public static final String id = "id";
    public static final String descripcion = "descripcion";
    public static final String image = "imagen";

    public static final String CreateTablePhotograph = "CREATE TABLE "+tablaPhotograph+"("+
            id+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            descripcion+" TEXT," +
            image+" BLOB)";

    public static final String DropTablePhotograph = "DROP TABLE IF EXISTS " + tablaPhotograph;

    public  static final String consultPhotograph = "SELECT * FROM "+tablaPhotograph;
}
