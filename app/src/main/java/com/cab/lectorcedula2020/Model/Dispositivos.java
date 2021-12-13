package com.cab.lectorcedula2020.Model;

/**
 * Created by jaiver-camiso on 15/05/2017.
 */

public class Dispositivos {

    private String identificacion;
    private String nombre;
    private String nacimiento;
    private String sexo;
    private String rh;
    private String fecha_hora;
    private String direccion;
    private String lugar;
    private String state;
    private String idDispositivo;
    private String lat;
    private String longitud;
    private String temperatura;
    private String nota;
    private String pruebaC;
    private int _ID;

    // Constructor de un objeto usuarios
    public Dispositivos(


            String identificacion,
            String nombre,
            String nacimiento,
            String sexo,
            String rh,
            String fecha_hora,
            String direccion,
            String lugar,
            String state,
            String pruebaC,
            String temperatura,
            String nota,
            String lat,
            String longitud,
            String idDispositivo,
            int _ID


            ) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.nacimiento = nacimiento;
        this.sexo = sexo;
        this.rh = rh;
        this.fecha_hora = fecha_hora;
        this.direccion = direccion;
        this.lugar = lugar;
        this.state = state;
        this.idDispositivo = idDispositivo;

        this.lat = lat;
        this.longitud = longitud;
        this.temperatura = temperatura;
        this.nota = nota;
        this.pruebaC = pruebaC;

        this._ID = _ID;
    }

    // Recuperar/establecer pruebaC
    public String getPruebaC() {
        return pruebaC;
    }

    public String setPruebaC(String pruebaC) {
        this.pruebaC = pruebaC;
        return pruebaC;
    }


    // Recuperar/establecer nota
    public String getNota() {
        return nota;
    }

    public String setNota(String nota) {
        this.nota = nota;
        return nota;
    }

    // Recuperar/establecer temperatura
    public String getTemperatura() {
        return temperatura;
    }

    public String setTemperatura(String temperatura) {
        this.temperatura = temperatura;
        return temperatura;
    }

    // Recuperar/establecer longitud
    public String getLongitud() {
        return longitud;
    }

    public String setLongitud(String longitud) {
        this.longitud = longitud;
        return longitud;
    }

    // Recuperar/establecer lat
    public String getLat() {
        return lat;
    }

    public String setLat(String lat) {
        this.lat = lat;
        return lat;
    }

    // Recuperar/establecer idDispositivo
    public String getIdDispositivo() {
        return idDispositivo;
    }

    public String setIdDispositivo(String idDispositivo) {
        this.idDispositivo = idDispositivo;
        return idDispositivo;
    }


    // Recuperar/establecer state
    public String getState() {
        return state;
    }

    public String setState(String state) {
        this.state = state;
        return state;
    }


    // Recuperar/establecer lugar
    public String getLugar() {
        return lugar;
    }

    public String setDLugar(String lugar) {
        this.lugar = lugar;
        return lugar;
    }


    // Recuperar/establecer direccion
    public String getDireccion() {
        return direccion;
    }

    public String setDireccion(String direccion) {
        this.direccion = direccion;
        return direccion;
    }


    // Recuperar/establecer rh
    public String getFecha_hora() {
        return fecha_hora;
    }

    public String setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
        return fecha_hora;
    }


    // Recuperar/establecer rh
    public String getRh() {
        return rh;
    }

    public String setRh(String rh) {
        this.rh = rh;
        return rh;
    }

    // Recuperar/establecer sexo
    public String getSexo() {
        return sexo;
    }

    public String setSexo(String sexo) {
        this.sexo = sexo;
        return sexo;
    }


    // Recuperar/establecer nacimiento
    public String getNacimiento() {
        return nacimiento;
    }

    public String setNacimiento(String nacimiento) {
        this.nacimiento = nacimiento;
        return nacimiento;
    }

    // Recuperar/establecer NOMBRE
    public String getNombre() {
        return nombre;
    }

    public String setNOMBRE(String nombre) {
        this.nombre = nombre;
        return nombre;
    }

    // Recuperar/establecer NOMBRE
    public String getIdentificacion() {
        return identificacion;
    }

    public String setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
        return identificacion;
    }


    // Recuperar/establecer ID
    public int get_ID() {
        return _ID;
    }

    public int set_ID(int _ID) {
        this._ID = _ID;
        return _ID;
    }


}
