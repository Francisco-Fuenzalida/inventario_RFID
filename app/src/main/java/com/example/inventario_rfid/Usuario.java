package com.example.inventario_rfid;

public class Usuario {
    public int id_user;
    public String user;
    public String pass;
    public String nombre;
    public String appaterno;
    public String apmaterno;
    public String rut;
    public String dv;
    public String answer;
    public int id_sec_que;
    public int id_per;

    public static String nombre_usuario = "";

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String usuario) {
        this.nombre_usuario = usuario;
    }
}
