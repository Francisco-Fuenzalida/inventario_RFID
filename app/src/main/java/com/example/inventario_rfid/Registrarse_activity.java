package com.example.inventario_rfid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registrarse_activity extends AppCompatActivity {

    private EditText et_email, et_pass, et_confirmar_pass, et_nombre, et_appaterno, et_apmaterno, et_rut, et_respuesta;
    private Spinner sp_questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        et_email = (EditText) findViewById(R.id.idNombreUser);
        et_pass = (EditText) findViewById(R.id.idPassword);
        et_confirmar_pass = (EditText) findViewById(R.id.idPasswordRepeat);
        et_nombre = (EditText) findViewById(R.id.idNombre);
        et_appaterno = (EditText) findViewById(R.id.idApPaterno);
        et_apmaterno = (EditText) findViewById(R.id.idApMaterno);
        et_rut = (EditText) findViewById(R.id.idRut);
        sp_questions = (Spinner) findViewById(R.id.idPregSecreta);
        et_respuesta = (EditText) findViewById(R.id.idRespSecreta);

        DBHelper dbh = DBHelper.getInstance(this);
        List<SecurityQuestions> questions = dbh.getAllSecurityQuestions();
        ArrayAdapter<SecurityQuestions> adapter = new ArrayAdapter<SecurityQuestions>(getApplicationContext(), R.layout.spinner_preguntas_secretas, questions) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        sp_questions.setAdapter(adapter);
    }

    //Para validar el email.
    public boolean validaEmail(String email) {
        Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Para validar el email.
    public boolean validaFormatoRut(String rut) {
        Pattern pattern = Pattern.compile("^([-0-9a-zA-Z]+[-])+[-0-9a-zA-Z]{1,6}$");
        Matcher matcher = pattern.matcher(rut);
        return matcher.matches();
    }

    //Método para validar el rut.
    public boolean validaRut(String rut) {
        boolean validacion = false;
        try {
            rut = rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        return validacion;
    }

    //Método para comprara contraseña 1 y 2
    public boolean compararContrasenas(String pass1, String pass2) {
        if (pass1.equals(pass2)) {
            return true;
        } else {
            return false;
        }
    }

    //Método para validar el formato de los datos antes de REGISTRAR
    public Boolean validacionRegistro(String email, String contrasena, String confirmar_contrasena, String nombre, String ap_paterno, String ap_materno, String rut,
                                      String pregunta, String respuesta) {
        String variable = "Escoja una pregunta de seguridad:";

        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "El campo Email esta vacio", Toast.LENGTH_SHORT).show();
        } else if (this.validaEmail(email) == false) {
            Toast.makeText(getApplicationContext(), "El Email ingresado no es valido", Toast.LENGTH_SHORT).show();
        } else if (contrasena.isEmpty()) {
            Toast.makeText(getApplicationContext(), "El campo Contraseñá esta vacio", Toast.LENGTH_SHORT).show();
        } else if (contrasena.length() < 4) {
            Toast.makeText(getApplicationContext(), "La contraseña debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show();
        } else if (confirmar_contrasena.isEmpty()) {
            Toast.makeText(getApplicationContext(), "El campo Confirmar Contraseñá esta vacio", Toast.LENGTH_SHORT).show();
        } else if (this.compararContrasenas(contrasena, confirmar_contrasena) == false) {
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        } else if (nombre.isEmpty()) {
            Toast.makeText(getApplicationContext(), "El campo Nombre esta vacio", Toast.LENGTH_SHORT).show();
        } else if (ap_paterno.isEmpty()) {
            Toast.makeText(getApplicationContext(), "El campo Apellido Paterno esta vacio", Toast.LENGTH_SHORT).show();
        } else if (ap_materno.isEmpty()) {
            Toast.makeText(getApplicationContext(), "El campo Apellido Materno esta vacio", Toast.LENGTH_SHORT).show();
        } else if (rut.isEmpty()) {
            Toast.makeText(getApplicationContext(), "El campo Rut esta vacio", Toast.LENGTH_SHORT).show();
        } else if (this.validaRut(rut) == false) {
            Toast.makeText(getApplicationContext(), "El Rut es incorrecto o esta en un formato invalido", Toast.LENGTH_SHORT).show();
        } else if (this.validaFormatoRut(rut) == false) {
            Toast.makeText(getApplicationContext(), "El Rut es incorrecto o esta en un formato invalido", Toast.LENGTH_SHORT).show();
        } else if (respuesta.isEmpty()) {
            Toast.makeText(getApplicationContext(), "El campo Respuesta esta vacio", Toast.LENGTH_SHORT).show();
        } else if (pregunta.equals(variable)) {
            Toast.makeText(getApplicationContext(), "Debe seleccionar una pregunta de seguridad", Toast.LENGTH_SHORT).show();
        } else if (respuesta.length() < 10) {
            Toast.makeText(getApplicationContext(), "La respuesta debe contener al menos 10 caracteres", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }


    public Boolean revisarRegistro(String email) {
        DBHelper DBH = DBHelper.getInstance(getApplicationContext());
        Usuario User = DBH.getUser(email);
        String email_existente = User.user;
        try{
            if (email_existente.equals(email)) {
                return false;
            }
        } catch (Exception e){
        }
        return true;
    }

    //Método para el boton YA ESTOY REGISTRADO
    public void yaEstoyRegistrado(View view) {
        finish();
    }

    //Método para el boton REGISTRARSE
    public void registrarse(View view) {
        DBHelper DBH = DBHelper.getInstance(getApplicationContext());
        String email = et_email.getText().toString().toLowerCase();
        String pass = et_pass.getText().toString();
        String confirm_pass = et_confirmar_pass.getText().toString();
        String nombre = et_nombre.getText().toString();
        String ap_paterno = et_apmaterno.getText().toString();
        String ap_materno = et_appaterno.getText().toString();
        String rut = et_rut.getText().toString().toUpperCase();
        String pregunta = sp_questions.getSelectedItem().toString();
        String respuesta = et_respuesta.getText().toString();
        boolean validacion = validacionRegistro(email, pass, confirm_pass, nombre, ap_paterno, ap_materno, rut, pregunta, respuesta);
        SecurityQuestions questions = DBH.getSecurityQuesions(pregunta);
        int id_question = questions.id_sec_que;
        if (validacion == true) {
            try {
                boolean revisar_registro = revisarRegistro(email);
                if (revisar_registro) {
                    String dv = String.valueOf(rut.charAt(rut.length() - 1));
                    rut = rut.substring(0, rut.length() - 2);
                    Usuario usuario = new Usuario();
                    usuario.user = email;
                    usuario.pass = pass;
                    usuario.nombre = nombre;
                    usuario.appaterno = ap_paterno;
                    usuario.apmaterno = ap_materno;
                    usuario.rut = rut;
                    usuario.dv = dv;
                    usuario.answer = respuesta;
                    usuario.id_sec_que = id_question;
                    usuario.id_per = 1;
                    DBH.addOrUpdateUser(usuario);
                    Toast.makeText(getApplicationContext(), "Te has registrado con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "El Email ingresado ya existe", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error al intentar registrar usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }
}