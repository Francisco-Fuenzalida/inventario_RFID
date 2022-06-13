package com.example.inventario_rfid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfiguracionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        Button btn_volver = (Button) findViewById(R.id.btn_volver_setting);
        btn_volver.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
    }

    //Método boton que va a categorias.
    public void ir_conf_categoria(View view) {
        Intent intent = new Intent(this, configuracion_categorias.class);
        startActivity(intent);
    }

    //Método boton que va a subcategorias.
    public void ir_conf_subcategoria(View view) {
        Intent intent = new Intent(this, Configuracion_subcategoria_activity.class);
        startActivity(intent);
    }

    //Método boton que va a ítems.
    public void ir_conf_item(View view) {
        Intent intent = new Intent(this, Configuracion_item_activity.class);
        startActivity(intent);
    }
}