package com.example.inventario_rfid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class configuracion_categorias extends AppCompatActivity {

    private EditText et_categoria;
    private Spinner sp_categoria;
    private Button btn_confirmar_agregar, btn_confirmar_eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_categorias);

        et_categoria = (EditText) findViewById(R.id.id_categoria_et);
        sp_categoria = (Spinner) findViewById(R.id.id_categoria_spinner);
        btn_confirmar_agregar = (Button) findViewById(R.id.btn_confirmar_agregar_categoria);
        btn_confirmar_eliminar = (Button) findViewById(R.id.btn_confirmar_eliminar_categoria);

        Button btn_volver = (Button) findViewById(R.id.btn_volver_setting_categoria);
        btn_volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        DBHelper dbh = DBHelper.getInstance(this);
        List<Categoria> categorias = dbh.getAllCategoria();
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<Categoria>(getApplicationContext(), R.layout.spinner_preguntas_secretas, categorias) {
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
        sp_categoria.setAdapter(adapter);
    }

    // Método para el boton btn_categoria_agregar
    public void btn_agregar(View view) {
        if (et_categoria.getVisibility() == View.GONE) {
            et_categoria.setVisibility(View.VISIBLE);
            btn_confirmar_agregar.setVisibility(View.VISIBLE);
            sp_categoria.setVisibility(View.GONE);
            btn_confirmar_eliminar.setVisibility(View.GONE);
        } else {
            et_categoria.setVisibility(View.GONE);
            btn_confirmar_agregar.setVisibility(View.GONE);
        }
    }

    //Método para el boton btn_categoria_eliminar
    public void btn_eliminar(View view) {
        if (sp_categoria.getVisibility() == View.GONE) {
            et_categoria.setVisibility(View.GONE);
            btn_confirmar_agregar.setVisibility(View.GONE);
            sp_categoria.setVisibility(View.VISIBLE);
            btn_confirmar_eliminar.setVisibility(View.VISIBLE);
        } else {
            sp_categoria.setVisibility(View.GONE);
            btn_confirmar_eliminar.setVisibility(View.GONE);
        }
    }

    // Método btn_confirmar_agregar
    public void btn_confirmar_agregar(View view) {
        DBHelper DBH = DBHelper.getInstance(getApplicationContext());
        String desc_categoria = et_categoria.getText().toString();
        if (desc_categoria.isEmpty()) {
            Toast.makeText(this, "Debe ingresar una categoría", Toast.LENGTH_SHORT).show();
        } else {
            Categoria categoria = new Categoria();
            categoria.desc_cat = desc_categoria;
            DBH.addOrUpdateCategoria(categoria);
            Toast.makeText(getApplicationContext(), "Categoría agregada con éxito", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        }
    }

    // Método btn_confirmar_eliminar
    public void btn_confirmar_eliminar(View view) {
        DBHelper DBH = DBHelper.getInstance(getApplicationContext());
        String tabla = "categoria";
        String columna = "desc_cat";
        String categoria_selec = sp_categoria.getSelectedItem().toString();
        Categoria categoria = DBH.getCategoria(categoria_selec);
        String valor = categoria.desc_cat;
        if (categoria.desc_cat.equals("Escoja una categoría:")) {
            Toast.makeText(this, "Debe seleccionar una categoría", Toast.LENGTH_SHORT).show();
        } else {
            DBH.deleteDetails(tabla, columna, valor);
            Toast.makeText(this, "Categoría borrada con éxito", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        }
    }
}