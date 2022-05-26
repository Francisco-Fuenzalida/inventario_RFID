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
                        } else {
                            Toast.makeText(getActivity(), "La contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "El email no está registrado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Método para al presionar el btn_registarse iniciar el Registrarse_activity.java
        binding.btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstFragment.this.getActivity(), Registrarse_activity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}