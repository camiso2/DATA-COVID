package com.cab.lectorcedula2020;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cab.lectorcedula2020.Model.Dispositivos;
import com.cab.lectorcedula2020.Nclass.AdminSQLiteOpenHelper;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ListaUsuarios extends AppCompatActivity {
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    public static final int READ_PERMISSION_REQUEST_CODE = 1;
    private TextView txtSLista;
    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_users);
        listview = (ListView) findViewById(R.id.listview);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        //String cod = txtNombre.getText().toString();
        Cursor fila = bd.rawQuery("select * from registro_usuario order by _ID desc ", null);
        List<String> dispositivo = new ArrayList<>();
        if (fila.moveToFirst()) {
            do {
                Dispositivos usuarios = new Dispositivos(
                        fila.getString(0),
                        fila.getString(1),
                        fila.getString(2),
                        fila.getString(3),
                        fila.getString(4),
                        fila.getString(5),
                        fila.getString(6),
                        fila.getString(7),
                        fila.getString(8),
                        fila.getString(9),
                        fila.getString(10),
                        fila.getString(11),
                        fila.getString(12),
                        fila.getString(13),
                        fila.getString(14),
                        fila.getInt(15)
                );
                dispositivo.add("\n" +
                        "Identificación : " + usuarios.getIdentificacion() + "\n" +
                        "Nombre : " + usuarios.getNombre() + "\n" +
                        "Fecha Nacimiento : " + usuarios.getNacimiento() + "\n" +
                        "Sexo : " + usuarios.getSexo() + "\n" +
                        "RH : " + usuarios.getRh() + "\n" +
                        "Ingreso : " + usuarios.getFecha_hora() + "\n" +
                        "Dirección : " + usuarios.getDireccion() + "\n" +
                        "Empresa : " + usuarios.getLugar() + "\n" +
                        "Prueba Covid-19 : " + usuarios.getPruebaC() + "\n" +
                        "Temperatura : " + usuarios.getTemperatura() + "\n" +
                        "Nota : " + usuarios.getNota() + "\n" +
                        "latitud : " + usuarios.getLat() + "\n" +
                        "Longitud : " + usuarios.getLongitud() + "\n"
                );
            } while (fila.moveToNext());
        } else
            Toast.makeText(this, "No existe un Usuarios registrados !",
                    Toast.LENGTH_SHORT).show();
        bd.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dispositivo);
        listview.setAdapter(adapter);
        pedirPermisos();
    }
    public void pedirPermisos() {
        // PERMISOS PARA ANDROID 6 O SUPERIOR
        if(ContextCompat.checkSelfPermission(ListaUsuarios.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    ListaUsuarios.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0
            );
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void directory(View v) {
        exportarCSV();
       // openFolder();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void exportarCSV() {
        File carpeta = new File(getFilesDir(), "/rt");
        String archivoAgenda = carpeta.toString() + "/" + "Usuarios.csv";
        //carpeta.mkdirs();
        boolean isCreate = false;
        if(!carpeta.exists()) {
            carpeta.mkdirs();
            //isCreate = carpeta.mkdir();
            Toast.makeText(ListaUsuarios.this, "SE CREO CARPETA !", Toast.LENGTH_LONG).show();
        }
        try {
            FileWriter fileWriter = new FileWriter(archivoAgenda);
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
            SQLiteDatabase bds = admin.getWritableDatabase();
            //String cod = txtNombre.getText().toString();
            Cursor fila = bds.rawQuery("select * from registro_usuario order by _ID desc ", null);

            if(fila != null && fila.getCount() != 0) {
                fila.moveToFirst();
                do {
                    for (int i=0; i<=14;i++) {
                        fileWriter.append(Codificador(fila.getString(i)));
                        fileWriter.append(",");
                    }
                    fileWriter.append(Codificador(fila.getString(15)));
                    fileWriter.append("\n");
                } while(fila.moveToNext());
            } else {
                Toast.makeText(ListaUsuarios.this, "No hay registros.", Toast.LENGTH_LONG).show();
            }

            bds.close();
            fileWriter.close();
            Toast.makeText(ListaUsuarios.this, "SE CREO EL ARCHIVO CSV EXITOSAMENTE", Toast.LENGTH_LONG).show();

        } catch (Exception e) { }

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String Codificador(String cadena) {
        byte[] ptext = cadena.getBytes(ISO_8859_1);
        String resultado = new String(ptext,UTF_8);
        return resultado;
    }
    public void openFolder(){
        String[] mailto = {""};
        Uri uri = Uri.fromFile(new File(Environment.getDataDirectory ().getAbsolutePath() + "/rt/","Usuarios.csv" ));
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporte ingreso de usuarios");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"El informe presenta el ingreso de usuarios a su negocio o empresa ! gracias por utilizar los servicios de  data-covid19. ");
        emailIntent.setType("application/csv");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Enviar informe usando :"));
    }
}
