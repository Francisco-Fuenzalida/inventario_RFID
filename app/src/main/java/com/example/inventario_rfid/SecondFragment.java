package com.example.inventario_rfid;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.inventario_rfid.databinding.FragmentSecondBinding;

import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private TextView tv_user;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String text = Usuario.nombre_usuario;
        binding.txtUser.setText(text);


        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        binding.btnSincronizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(SecondFragment.this.getActivity(), Sincronizacion_activity.class);
                startActivity(myintent);
            }
        });

        binding.btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbh = DBHelper.getInstance(getContext());
                List<Item> items = dbh.getAllItem();
                int cant_item = items.size();
                System.out.println(cant_item);
                if (cant_item <= 1){
                    Toast.makeText(getActivity(), "Agregu?? por lo menos un ??tem antes de escanear", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SecondFragment.this.getActivity(), Scaner_activity.class);
                    startActivity(intent);
                }
            }
        });
        binding.btnDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondFragment.this.getActivity(), Reportes_activity.class);
                startActivity(intent);
            }
        });
        binding.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondFragment.this.getActivity(), ConfiguracionActivity.class);
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