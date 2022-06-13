package com.example.inventario_rfid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Configuracion_subcategoria_activity extends AppCompatActivity {

    private Button btn_confirmar_agregar_subcategoria, btn_confirmar_eliminar_subcategoria;
    private EditText et_subcategoria;
    private Spinner sp_categoria, sp_subcategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_subcategoria);

        btn_confirmar_agregar_subcategoria = (Button) findViewById(R.id.btn_confirmar_agregar_subcategoria);
        btn_confirmar_eliminar_subcategoria = (Button) findViewById(R.id.btn_confirmar_eliminar_subcategoria);
        et_subcategoria = (EditText) findViewById(R.id.id_subcategoria_et);
        sp_categoria = (Spinner) findViewById(R.id.id_subcategoria_spinner_agregar);
        sp_subcategoria = (Spinner) findViewById(R.id.id_subcategoria_spinner_eliminar);

        Button btn_volver = (Button) findViewById(R.id.btn_volver_setting_subcategoria);
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
        sp_subcategoria.setEnabled(false);

        sp_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String categoria_selec = sp_categoria.getSelectedItem().toString();
                Categoria categoria = dbh.getCategoria(categoria_selec);
                int id_categoria = categoria.id_cat;

                List<Subcategoria> subcategorias = dbh.getAllSubcategoriaWhereId(id_categoria);
                ArrayAdapter<Subcategoria> adapter2 = new ArrayAdapter<Subcategoria>(getApplicationContext(), R.layout.spinner_preguntas_secretas, subcategorias) {
                };
                sp_subcategoria.setAdapter(adapter2);
                sp_subcategoria.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    // Método para el boton btn_categoria_agregar
    public void btn_agregar(View view) {
        if (et_subcategoria.getVisibility() == View.GONE) {
            sp_categoria.setVisibility(View.VISIBLE);
            et_subcategoria.setVisibility(View.VISIBLE);
            btn_confirmar_agregar_subcategoria.setVisibility(View.VISIBLE);
            sp_subcategoria.setVisibility(View.GONE);
            btn_confirmar_eliminar_subcategoria.setVisibility(View.GONE);
        } else {
            sp_categoria.setVisibility(View.GONE);
            et_subcategoria.setVisibility(View.GONE);
            btn_confirmar_agregar_subcategoria.setVisibility(View.GONE);
        }
    }

    //Método para el boton btn_categoria_eliminar
    public void btn_eliminar(View view) {
        if (sp_subcategoria.getVisibility() == View.GONE) {
            sp_categoria.setVisibility(View.VISIBLE);
            et_subcategoria.setVisibility(View.GONE);
            btn_confirmar_agregar_subcategoria.setVisibility(View.GONE);
            sp_subcategoria.setVisibility(View.VISIBLE);
            btn_confirmar_eliminar_subcategoria.setVisibility(View.VISIBLE);
        } else {
            sp_subcategoria.setVisibility(View.GONE);
            btn_confirmar_eliminar_subcategoria.setVisibility(View.GONE);
            sp_categoria.setVisibility(View.GONE);
        }
    }

    // Método para validar la subcategoria a agregar o eliminar
    public boolean validarSubcategoria(String subcategoria, String desc_categoria) {
        String categoria_defecto = "Escoja una categoría:";

        if (subcategoria.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No ingreso ningúna subcategoría", Toast.LENGTH_SHORT).show();
        } else if (desc_categoria.equals(categoria_defecto)){
            Toast.makeText(getApplicationContext(), "No selecciono ningúna categoría", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    // Método boton btn_confirmar_agregar
    public void btn_confirmar_agregar(View view) {
        DBHelper DBH = DBHelper.getInstance(getApplicationContext());
        String desc_subcategoria = et_subcategoria.getText().toString();
        String categoria_selec = sp_categoria.getSelectedItem().toString();
        Categoria categoria = DBH.getCategoria(categoria_selec);
        int id_categoria = categoria.id_cat;
        boolean validada_subcategoria = validarSubcategoria(desc_subcategoria, categoria.desc_cat);
        if (validada_subcategoria) {
            Subcategoria subcategoria = new Subcategoria();
            subcategoria.des_sbc = desc_subcategoria;
            subcategoria.id_cat = id_categoria;
            DBH.addOrUpdateSubcategoria(subcategoria);
            Toast.makeText(getApplicationContext(), "Subcategoría agregada con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error al intentar ingresar subcategoría", Toast.LENGTH_SHORT).show();
        }
    }
}