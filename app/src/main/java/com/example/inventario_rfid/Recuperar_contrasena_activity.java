package com.example.inventario_rfid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Recuperar_contrasena_activity extends AppCompatActivity {

    private EditText et_email, et_respuesta, et_pass, et_confirmar_pass;
    private TextView tv_pregunta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        et_email = (EditText) findViewById(R.id.id_email_2);
        et_respuesta = (EditText) findViewById(R.id.id_respuesta_secreta_2);
        et_pass = (EditText) findViewById(R.id.id_contrasena_2);
        et_confirmar_pass = (EditText) findViewById(R.id.id_repetir_contrasena_2);
        tv_pregunta = (TextView) findViewById(R.id.id_pregunta);
    }

    public Boolean revisarRegistro(String email) {
        DBHelper DBH = DBHelper.getInstance(getApplicationContext());
        Usuario User = DBH.getUser(email);
        String email_existente = User.user;
        try {
            if (email_existente.equals(email)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    //Método para comprara contraseña 1 y 2
    public boolean compararContrasenas(String pass1, String pass2) {
        if (pass1.equals(pass2)) {
            return true;
        } else {
            return false;
        }
    }

    //Método para el boton BUSCAR USUARIO
    public void buscarUsuario(View view) {
        DBHelper DBH = DBHelper.getInstance(getApplicationContext());
        String email = et_email.getText().toString();
        boolean revisar_registro = revisarRegistro(email);
        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "El campo Email esta vacio", Toast.LENGTH_SHORT).show();
        } else {
            if (revisar_registro) {
                Usuario usuario = DBH.getUser(email);
                SecurityQuestions securityQuestions = DBH.getSecurityQuesionsForID(usuario.id_sec_que);
                tv_pregunta.setText(securityQuestions.desc_question);
            } else {
                Toast.makeText(getApplicationContext(), "El Email ingresado no ha sido registrado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Método para el boton RECUPERAR CUENTA
    public void recuperarContrasenna(View view) {
        DBHelper DBH = DBHelper.getInstance(getApplicationContext());
        String email = et_email.getText().toString();
        String pregunta = tv_pregunta.getText().toString();
        if (email.isEmpty() || pregunta.isEmpty()){
            Toast.makeText(getApplicationContext(), "Prinero busque su usuario", Toast.LENGTH_SHORT).show();
        } else {
            String respuesta = et_respuesta.getText().toString();
            String pass = et_pass.getText().toString();
            String confirm_pass = et_confirmar_pass.getText().toString();
            if (respuesta.isEmpty()) {
                Toast.makeText(getApplicationContext(), "El campo Respuesta esta vacio", Toast.LENGTH_SHORT).show();
            } else if (pass.isEmpty()) {
                Toast.makeText(getApplicationContext(), "El campo Contraseñá esta vacio", Toast.LENGTH_SHORT).show();
            } else if (pass.length() < 4) {
                Toast.makeText(getApplicationContext(), "La contraseña debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show();
            } else if (confirm_pass.isEmpty()) {
                Toast.makeText(getApplicationContext(), "El campo Confirmar Contraseñá esta vacio", Toast.LENGTH_SHORT).show();
            } else if (this.compararContrasenas(pass, confirm_pass) == false) {
                Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            } else {
                boolean revisar_registro = revisarRegistro(email);
                if (revisar_registro) {
                    Usuario usuario = DBH.getUser(email);
                    String respuesta2 = usuario.answer;
                    if (this.compararContrasenas(respuesta, respuesta2) == false) {
                        Toast.makeText(getApplicationContext(), "La respuesta es incorrecta", Toast.LENGTH_SHORT).show();
                    } else {
                        usuario.pass = pass;
                        DBH.addOrUpdateUser(usuario);
                        Toast.makeText(getApplicationContext(), "La contraseña fue cambiada exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "El Email no esta registrado", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}