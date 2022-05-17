package com.example.inventario_rfid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.inventario_rfid.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;


    EditText user, pass;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    //Método para validar el formato de los datos antes de INGRESAR
    public Boolean validacionLogin(String usuario, String contrasena) {
        if (usuario.isEmpty()) {
            Toast.makeText(getActivity(), "El campo email esta vacio", Toast.LENGTH_SHORT).show();
        } else if (contrasena.isEmpty()) {
            Toast.makeText(getActivity(), "El campo contraseñá esta vacio", Toast.LENGTH_SHORT).show();
        } else if (contrasena.length() < 4) {
            Toast.makeText(getActivity(), "La contraseña debe tener al menos 4 caracteres", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = getView().findViewById(R.id.idNombreUser);
        pass = getView().findViewById(R.id.idPassword);
        try {
            DBHelper DBH = DBHelper.getInstance(getContext());
            Perfil perf = new Perfil();
            perf.desc_per = "admin";
            perf.puede_insertar_item = 1;
            perf.puede_editar_item = 1;
            perf.puede_borrar_item = 1;
            perf.puede_insertar_user = 1;
            perf.puede_editar_user = 1;
            perf.puede_borrar_user = 1;
            perf.puede_editar_cfg = 1;


            DBH.addOrUpdateProfile(perf);
            Perfil pf = DBH.getProfile("admin");

            Usuario usuario = new Usuario();
            usuario.user = "tester";
            usuario.pass = "tester";
            usuario.nombre = "Tester";
            usuario.appaterno = "Testington";
            usuario.apmaterno = "Testeani";
            usuario.rut = "19999999";
            usuario.dv = "1";
            usuario.id_per = pf.id_per;

            DBH.addOrUpdateUser(usuario);
        } catch (Exception e) {
            ;
        }

        //Método para iniciar sesión e ir al SecondFragment.java
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = user.getText().toString();
                String contrasena = pass.getText().toString();
                boolean validacion = validacionLogin(usuario, contrasena);
                if (validacion == true) {
                    try {
                        DBHelper dbh = DBHelper.getInstance(getContext());
                        boolean go = dbh.Login(usuario, contrasena);
                        if (go) {
                            Toast.makeText(getActivity(), "Login Exitoso", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(FirstFragment.this)
                                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Email o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Método para al presionar el btn_registarse iniciar el Registrarse_activity.java
        binding.btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(FirstFragment.this.getActivity(), Registrarse_activity.class);
                startActivity(myintent);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}