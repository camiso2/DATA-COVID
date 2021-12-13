package com.cab.lectorcedula2020.Nclass;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jaiver on 03/03/2016.
 */
public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    //llamamos al constructor
    public AdminSQLiteOpenHelper(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    //creamos la tabla
    @Override
    public void onCreate(SQLiteDatabase dbs) {
        dbs.execSQL("create table registro_usuario(identificacion text, nombre text, nacimiento text, sexo text, rh  text, fecha_hora  text,direccion  text,lugar text,  state text,pruebac text, temperatura text, nota text,lat text, longitud text, idDispositivo text, _ID INTEGER PRIMARY KEY AUTOINCREMENT)");
 }
    //borrar la tabla y crear la nueva tabla
    @Override
    public void onUpgrade(SQLiteDatabase dbs, int versionAnte, int versionNue) {
        dbs.execSQL("drop table if exists registro_usuario");
        dbs.execSQL("create table registro_usuario(identificacion text, nombre text, nacimiento text, sexo text, rh  text, fecha_hora  text,direccion  text,lugar text,  state text,pruebac text, temperatura text, nota text,lat text, longitud text, idDispositivo text, _ID INTEGER PRIMARY KEY AUTOINCREMENT)");


    }
}
