package com.example.inventario_rfid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.inventario_rfid.databinding.ActivitySincronizacionBinding;



public class Sincronizacion_activity extends AppCompatActivity {

    private @NonNull ActivitySincronizacionBinding binding;


    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ActivitySincronizacionBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizacion);

        Button btn_volver = (Button) findViewById(R.id.btn_volver_sinc);

        btn_volver.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(Sincronizacion_activity.this, SecondFragment.class));
            }
        });
    }

}


