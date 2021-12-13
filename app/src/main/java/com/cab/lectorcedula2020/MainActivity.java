package com.cab.lectorcedula2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.cab.lectorcedula2020.Nclass.AdminSQLiteOpenHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button button;
    Camera mCamera;
    private TextView txtResulPrueba, txtDir, txtEmpresa, txtNombre, cedula, sexoUsu, datoNacimiento, txtTipoSangre, txtRH, txtFechaHora, txtCodigoGuarda, txtCodigoDocumento, txtCodigoComparendo;
    private String SalidaSinCedula = "";
    private boolean active = true;
    private EditText txtNota;
    Switch pruebaCovid;
    String sLatitud = "";
    String sLongitud = "";
    Spinner gradosC;
    Spinner decimas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pruebaCovid = (Switch) findViewById(R.id.pruebaCovid);
        txtNota = (EditText) findViewById(R.id.txtNota);
        //---------------------------------------------------------------------
        txtResulPrueba = (TextView) findViewById(R.id.txtResulPrueba);
        pruebaCovid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", "kkkkkkkkkkk" + isChecked);
                if (pruebaCovid.isChecked()) {
                    txtResulPrueba.setText("SI, Realizada");
                } else {
                    txtResulPrueba.setText("NO, Realizada");
                }
            }
        });
        //spinner grados decimales
        decimas = (Spinner) findViewById(R.id.decimas);
        gradosC = (Spinner) findViewById(R.id.gradosC);
        //----------------------------------------------
        button = (Button) this.findViewById(R.id.button);
        txtNombre = (TextView) findViewById(R.id.textView);
        txtEmpresa = (TextView) findViewById(R.id.textView14);
        txtDir = (TextView) findViewById(R.id.textDir);
        txtCodigoComparendo = (TextView) findViewById(R.id.txtCodigoComparendo);
        txtCodigoDocumento = (TextView) findViewById(R.id.txtCodigoDocumento);
        //txtCodigoGuarda = (TextView) findViewById(R.id.txtCodigoGuarda);
        txtFechaHora = (TextView) findViewById(R.id.txtFechaHora);
        txtRH = (TextView) findViewById(R.id.txtRH);
        datoNacimiento = (TextView) findViewById(R.id.txtNacimiento);
        sexoUsu = (TextView) findViewById(R.id.sexoUsu);
        cedula = (TextView) findViewById(R.id.cedula);
        final Activity activity = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Escanear  Documento  del  Usuario");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.getMoreExtras();
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
                hf();

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                } else {
                    locationStart();

                }

            }
        });
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        //latitud.setText("Localización agregada");
        txtDir.setText("");
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }
    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    txtDir.setText(DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        MainActivity mainActivity;
        public MainActivity getMainActivity() {
            return mainActivity;
        }
        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }
        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();
            sLatitud = String.valueOf(loc.getLatitude());
            sLongitud = String.valueOf(loc.getLongitude());
            //latitud.setText(sLatitud);
            //longitud.setText(sLongitud);
            this.mainActivity.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //latitud.setText("GPS Desactivado");
        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            //latitud.setText("GPS Activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
    public void hf() {
        Date date = new Date();
        java.text.DateFormat hourdateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ");
        String horaFecha = hourdateFormat.format(date);
        txtFechaHora.setText(horaFecha);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // toma  cadena  de  caracteres
                String CadenaOriginal = result.getContents();
                String Salida = CadenaOriginal.substring(48);
                char[] aCaracteres;
                aCaracteres = Salida.toCharArray();
                StringBuilder numeroIdentificacion = new StringBuilder(64);
                //este  for  es para  pruebas  de  sotfware
                for (int i = 0; i < aCaracteres.length; i++) {
                    System.out.println("[" + i + "] " + aCaracteres[i]);
                    // txtTipoSangre.setText(result.getContents());
                }
                for (int i = 0; i < 10; i++) {
                    //System.out.println("[" + i + "] " + aCaracteres[i]);
                    numeroIdentificacion.append(aCaracteres[i]);
                }
                String resulta = String.valueOf(numeroIdentificacion);
                cedula.setText(resulta);
                SalidaSinCedula = CadenaOriginal.substring(58);
                char[] aCaracteres2;
                aCaracteres2 = SalidaSinCedula.toCharArray();
                int cont = 0;
                StringBuilder nombre = new StringBuilder(64);
                StringBuilder sexo = new StringBuilder(64);
                StringBuilder fechaNacimiento = new StringBuilder(64);
                StringBuilder RH = new StringBuilder(64);
                for (int i = 0; i < SalidaSinCedula.length(); i++) {
                    //System.out.println("[" + i + "] " + aCaracteres2[i]);
                    if (Character.isLetter(SalidaSinCedula.charAt(i))) {
                        nombre.append(aCaracteres2[i]);
                        cont = 0;
                    } else {
                        cont++;
                        if (cont <= 1) {
                            nombre.append(" ");
                        }
                        if (Character.isDigit(SalidaSinCedula.charAt(i))) {
                            sexo.append(aCaracteres2[i + 1]);
                            //String sexoUsu = String.valueOf(sexo);
                            nombre.append("");
                            int a = i + 2;
                            for (int k = 1; k <= 8; k++) {
                                fechaNacimiento.append(aCaracteres2[a]);
                                if (k == 4) {
                                    fechaNacimiento.append("-");
                                }
                                if (k == 6) {
                                    fechaNacimiento.append("-");
                                }
                                a++;
                            }
                            int x = a + 6;
                            for (int d = 1; d <= 3; d++) {
                                RH.append(aCaracteres2[x]);
                                x++;
                            }
                            break;
                        }
                    }
                }
                String nombreCompleto = String.valueOf(nombre);
                String sexoUsuario = String.valueOf(sexo);
                String fechaNacimientoUsuario = String.valueOf(fechaNacimiento);
                String tipoRH = String.valueOf(RH);
                txtRH.setText(tipoRH);
                datoNacimiento.setText(fechaNacimientoUsuario);
                sexoUsu.setText(sexoUsuario);
                txtNombre.setText(nombreCompleto);
            } else {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelo el lector", Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //recoge  la  hora  y   ffecha  del  sistema  y  lo  agrega  a  un   campo  de  texto
    public void print(View v) {

        if (cedula.getText().equals("NO HAY DATO")) {
            Toast.makeText(this, "Seleccione los datos del Usuario !",
                    Toast.LENGTH_SHORT).show();

        } else {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            String Cedula = cedula.getText().toString();
            String Nombre = txtNombre.getText().toString();
            String nacimiento = datoNacimiento.getText().toString();
            String sexo = sexoUsu.getText().toString();
            String rh = txtRH.getText().toString();
            String fecha_hora = txtFechaHora.getText().toString();
            String direccion = txtDir.getText().toString();
            String pruebaC = txtResulPrueba.getText().toString();
            String lat = sLatitud;
            String longitud = sLongitud;
            String nota = txtNota.getText().toString();
            String temperatura =  gradosC.getSelectedItem().toString()+decimas.getSelectedItem().toString();
            //String lugar = "Empresa de prueba !";
            // String empresa = txtEmpresa.getText().toString();
            String empresa = "Empresa de prueba !";
            String state = "0";
            String idDispositivo = "06546";
            ContentValues registro = new ContentValues();
            registro.put("identificacion", Cedula);
            registro.put("nombre", Nombre);
            registro.put("nacimiento", nacimiento);
            registro.put("sexo", sexo);
            registro.put("rh", rh);
            registro.put("fecha_hora", fecha_hora);
            registro.put("direccion", direccion);
            registro.put("lugar", empresa);
            registro.put("state", state);
            registro.put("pruebac", pruebaC);
            registro.put("lat", lat);
            registro.put("longitud", longitud);
            registro.put("nota", nota);
            registro.put("temperatura", temperatura);
            registro.put("idDispositivo", idDispositivo);
            bd.insert("registro_usuario", null, registro);
            bd.close();
            cedula.setText("NO HAY DATO");
            txtNombre.setText("NO HAY DATO");
            datoNacimiento.setText("AAAA-MM-DD");
            sexoUsu.setText("(M/F)");
            txtRH.setText("(rh)");
            txtFechaHora.setText("AAAA-MM-DD");
            txtDir.setText("centro");
            txtEmpresa.setText("Empresa de prueba !");
            Toast.makeText(this, "Se cargaron los datos del Usuario !",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void btnListaUsuarios(View v) {
        Intent i = new Intent(getApplicationContext(), ListaUsuarios.class);
        startActivity(i);
        animacionActivityRegistroUsuario();
    }
    //animaciones sube activitys
    public void animacionActivityRegistroUsuario() {
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
    //seleccion  de   razas
    public void btnManual(View v) {
        Intent i = new Intent(getApplicationContext(), Manual.class);
        startActivity(i);
        animacionActivityRegistroUsuario();
    }
    //crea  alert  dialogo  para   ingreso  de  codigo  de  agente  de  transito
    public void IngresaCodigoGuarda(View V) {
        IngresaCodigoSeguridad();
    }
    //8ngreso  de  codigo  de  seguridad  del  guarda
    public void IngresaCodigoSeguridad() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("    -- INGRESO  CODIGO --");
        alertDialog.setMessage("Ingrese su Codigo de Agente de Transito");
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(90)});
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.guarda);
        Log.e("el  valor  de INPUT ", input.getText().toString());
        alertDialog.setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (input.getText().toString().equals("")) {
                    String Mensaje = "NO Ingresó Codigo de Agente";
                    Toast toast2 = Toast.makeText(getApplicationContext(), "* " + Mensaje + " *", Toast.LENGTH_LONG);
                    toast2.setGravity(Gravity.CENTER_VERTICAL, 0, -150);
                    toast2.show();

                } else {
                    String ingresoAgente = input.getText().toString();
                    ingresarCodigoAgente(ingresoAgente);
                }

            }
        });
        alertDialog.setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();*/
    }
    //ingresa Codigo  de  Agente
    public void ingresarCodigoAgente(String codAgente) {
        txtCodigoGuarda.setText(codAgente);
        String numeroDocumento = getCode() + "-" + txtCodigoGuarda.getText();
        txtCodigoDocumento.setText(numeroDocumento);
        System.out.println("numero Documento : " + numeroDocumento);
        String Mensaje = "Se Generó el Numero del Documento";
        Toast toast2 = Toast.makeText(getApplicationContext(), "* " + Mensaje + " *", Toast.LENGTH_LONG);
        toast2.setGravity(Gravity.CENTER_VERTICAL, 0, -70);
        toast2.show();
    }
    private String getCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String numeroDocumento = "No : " + date;
        return numeroDocumento;
    }
    public void seleccionaNumeroComparendo(View v) {
        //llenar  el elert  dialgo  con  codigos  de un  xml
        final CharSequence[] items;
        items = getResources().getStringArray(R.array.codigos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione Codigo de Comparendo");
        builder.setIcon(R.drawable.escribe);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String Mensaje = "La Infracción del Usuario es : " + items[item];
                Toast toast2 = Toast.makeText(getApplicationContext(), "* " + Mensaje + " *", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.CENTER_VERTICAL, 0, -70);
                toast2.show();


                txtCodigoComparendo.setText(items[item]);
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void ubicaconMunicipioDepartamento(View v) {
        //llenar  el elert  dialgo  con  codigos  de un  xml
        final CharSequence[] items;
        items = getResources().getStringArray(R.array.municipios);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("      Seleccione Municipio del Quindío");
        builder.setIcon(R.drawable.ubicacion);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String Mensaje = "Seleccionó el Municipio de : " + items[item];
                Toast toast2 = Toast.makeText(getApplicationContext(), "* " + Mensaje + " *", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.CENTER_VERTICAL, 0, -70);
                toast2.show();
                txtEmpresa.setText(items[item]);
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void ingresaDireccionComparendo(View v) {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
        alertDialog2.setTitle("-- Ingrese La Dirección --");
        alertDialog2.setMessage("Ingrese la Direccíon Donde se Hace el Comparendo MAX : 30 caracteres");
        final EditText input2 = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        //input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //input.setText(String.valueOf(racionDispensarAMPM));
        input2.setLayoutParams(lp2);
        alertDialog2.setView(input2);
        alertDialog2.setIcon(R.drawable.ubicacion_direccion);
        Log.e("el  valor  de INPUT ", input2.getText().toString());
        alertDialog2.setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog2, int which) {
                if (input2.getText().toString().equals("")) {
                    String Mensaje = "NO Ingresó La Direccion....";
                    Toast toast2 = Toast.makeText(getApplicationContext(), "* " + Mensaje + " *", Toast.LENGTH_LONG);
                    toast2.setGravity(Gravity.CENTER_VERTICAL, 0, -70);
                    toast2.show();
                } else {
                    ingresarDireccionComparendo(input2.getText().toString());
                }
            }
        });
        alertDialog2.setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog2.show();
    }
    //ingresa direccion  del  comparendo
    public void ingresarDireccionComparendo(String direccion) {
        txtDir.setText(direccion);
        System.out.println("mi  direccion  es : " + direccion);
        String Mensaje = "Se  Guardó La Direccion  Donde  se Reliza  el  Comparendo : ";
        Toast toast2 = Toast.makeText(getApplicationContext(), "* " + Mensaje + direccion + " *", Toast.LENGTH_LONG);
        toast2.setGravity(Gravity.CENTER_VERTICAL, 0, -70);
        toast2.show();

    }

}
