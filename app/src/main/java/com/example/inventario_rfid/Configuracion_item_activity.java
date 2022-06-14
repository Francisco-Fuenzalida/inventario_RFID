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

public class Configuracion_item_activity extends AppCompatActivity {

    private Button btn_agregar, btn_eliminar;
    private EditText et_item;
    private Spinner sp_categoria_agregar, sp_subcategoria_agregar, sp_item_eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_item);

        btn_agregar = (Button) findViewById(R.id.btn_item_confirmar_agregar);
        btn_eliminar = (Button) findViewById(R.id.btn_item_confirmar_eliminar);
        et_item = (EditText) findViewById(R.id.id_item);
        sp_categoria_agregar = (Spinner) findViewById(R.id.id_categoria_item_spinner_agregar);
        sp_subcategoria_agregar = (Spinner) findViewById(R.id.id_subcategoria_item_spinner_agregar);
        sp_item_eliminar = (Spinner) findViewById(R.id.id_item_spinner_eliminar);

        Button btn_volver = (Button) findViewById(R.id.btn_volver_setting_item);
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
        sp_categoria_agregar.setAdapter(adapter);
        sp_subcategoria_agregar.setEnabled(false);

        sp_categoria_agregar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String categoria_selec = sp_categoria_agregar.getSelectedItem().toString();
                Categoria categoria = dbh.getCategoria(categoria_selec);
                int id_categoria = categoria.id_cat;

                List<Subcategoria> subcategorias = dbh.getAllSubcategoriaWhereId(id_categoria);
                ArrayAdapter<Subcategoria> adapter2 = new ArrayAdapter<Subcategoria>(getApplicationContext(), R.layout.spinner_preguntas_secretas, subcategorias) {
                };
                sp_subcategoria_agregar.setAdapter(adapter2);
                sp_subcategoria_agregar.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        sp_item_eliminar.setEnabled(true);

        sp_subcategoria_agregar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String subcategoria_selec = sp_subcategoria_agregar.getSelectedItem().toString();
                Subcategoria subcategoria = dbh.getSubcategoria(subcategoria_selec);
                int id_subcategoria = subcategoria.id_sbc;

                List<Item> items = dbh.getAllItemWhereId(id_subcategoria);
                ArrayAdapter<Item> adapter3 = new ArrayAdapter<Item>(getApplicationContext(), R.layout.spinner_preguntas_secretas, items) {
                };
                sp_item_eliminar.setAdapter(adapter3);
                sp_item_eliminar.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    // Método para el boton btn_categoria_agregar
    public void btn_agregar(View view) {
        if (et_item.getVisibility() == View.GONE) {
            et_item.setVisibility(View.VISIBLE);
            sp_categoria_agregar.setVisibility(View.VISIBLE);
            sp_subcategoria_agregar.setVisibility(View.VISIBLE);
            btn_agregar.setVisibility(View.VISIBLE);
            sp_item_eliminar.setVisibility(View.GONE);
            btn_eliminar.setVisibility(View.GONE);
        } else {
            et_item.setVisibility(View.GONE);
            sp_categoria_agregar.setVisibility(View.GONE);
            sp_subcategoria_agregar.setVisibility(View.GONE);
            btn_agregar.setVisibility(View.GONE);
        }
    }

    //Método para el boton btn_categoria_eliminar
    public void btn_eliminar(View view) {
        if (sp_item_eliminar.getVisibility() == View.GONE) {
            et_item.setVisibility(View.GONE);
            sp_categoria_agregar.setVisibility(View.VISIBLE);
            sp_subcategoria_agregar.setVisibility(View.VISIBLE);
            btn_agregar.setVisibility(View.GONE);
            sp_item_eliminar.setVisibility(View.VISIBLE);
            btn_eliminar.setVisibility(View.VISIBLE);
        } else {
            sp_categoria_agregar.setVisibility(View.GONE);
            sp_subcategoria_agregar.setVisibility(View.GONE);
            sp_item_eliminar.setVisibility(View.GONE);
            btn_eliminar.setVisibility(View.GONE);
        }
    }

    // Método para validar el ítem a agregar o eliminar
    public boolean validarItem(String item, String desc_categoria) {
        String categoria_defecto = "Escoja una categoría:";

        if (item.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No ingreso ningún ítem", Toast.LENGTH_SHORT).show();
        } else if (desc_categoria.equals(categoria_defecto)) {
            Toast.makeText(getApplicationContext(), "No selecciono ningúna categoría", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    // Método para el btn_condifrmar que agrega un nuevo itme
    public void btn_agregar_item(View view) {
        try {
            DBHelper DBH = DBHelper.getInstance(getApplicationContext());
            String desc_item = et_item.getText().toString();
            String categoria_selected = sp_categoria_agregar.getSelectedItem().toString();
            Categoria categoria = DBH.getCategoria(categoria_selected);
            String desc_categoria = categoria.desc_cat;
            String subcategoria_selected = sp_subcategoria_agregar.getSelectedItem().toString();
            Subcategoria subcategoria = DBH.getSubcategoria(subcategoria_selected);
            int id_subcategoria = subcategoria.id_sbc;
            if (desc_item.isEmpty()) {
                Toast.makeText(this, "Debe ingresar un ítem", Toast.LENGTH_SHORT).show();
            } else if (desc_categoria.equals("Escoja una categoría:")) {
                Toast.makeText(getApplicationContext(), "No selecciono ningúna categoría", Toast.LENGTH_SHORT).show();
            } else {
                Item item = new Item();
                item.desc_item = desc_item;
                item.id_sbc = id_subcategoria;
                DBH.addOrUpdateItem(item);
                Toast.makeText(getApplicationContext(), "Ítem agregado con éxito", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Debe agregar primero una subcategoría", Toast.LENGTH_SHORT).show();
        }
    }

    // Método btn_confirmar_eliminar
    public void btn_confirmar_eliminar(View view) {
        try {
            DBHelper DBH = DBHelper.getInstance(getApplicationContext());
            String tabla = "item";
            String columna = "desc_item";
            String item_selected = sp_item_eliminar.getSelectedItem().toString();
            Item item = DBH.getItem(item_selected);
            String valor = item.desc_item;
            DBH.deleteDetails(tabla, columna, valor);
            Toast.makeText(this, "Ítem borrado con éxito", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
        } catch (Exception e) {
            Toast.makeText(this, "Ingrese un item", Toast.LENGTH_SHORT).show();
        }
    }
}