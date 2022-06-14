package com.example.inventario_rfid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Reportes_activity extends AppCompatActivity {

    static TextView dateText;
    static TextView dateText2;
    DBHelper db;
    //--------- DATE PICKER 1 ----
    String date1;
    public static final String DATE_DIALOG_1 = "datePicker1";
    static TextView txt_date1;
    private static int mYear1;
    private static int mMonth1;
    private static int mDay1;
    //----- DATE PICKER 1

    //----------------------------------------------------------DATE PICKER 2
    String date2;
    public static final String DATE_DIALOG_2 = "datePicker2";
    static TextView txt_date2;
    private static int mYear2;
    private static int mMonth2;
    private static int mDay2;
    //----------------------------------------------------------DATE PICKER 2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DBHelper.getInstance(this);

        setContentView(R.layout.activity_reportes);
        dateText = findViewById(R.id.txt_fecha_ini);
        dateText2 = findViewById(R.id.txt_fecha_fin);
        Button btn_cargar = (Button) findViewById(R.id.btn_cargarDatos);
        btn_cargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MostrarDatos(dateText.toString(), dateText2.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.txt_btn_ini).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment1 = new DatePickerFragment1();
                newFragment1.show(getSupportFragmentManager(), DATE_DIALOG_1);
            }
        });

        findViewById(R.id.txt_btn_fin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment2 = new DatePickerFragment2();
                newFragment2.show(getSupportFragmentManager(), DATE_DIALOG_2);

            }
        });

        Button btn_volver = (Button) findViewById(R.id.btn_volver_repor);
        btn_volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


    }

    public static class DatePickerFragment1 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default date
            //Date Time NOW
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // get selected date
            mYear1 = year;
            mMonth1 = month;
            mDay1 = day;
            // show selected date to date button
            dateText.setText(new StringBuilder()
                    .append(mYear1).append("-")
                    .append(mMonth1 + 1).append("-")
                    .append(mDay1).append(" "));
            String test = dateText.toString();
        }
    }

    public static class DatePickerFragment2 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // set default date


            //Date Time NOW
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // get selected date
            mYear2 = year;
            mMonth2 = month;
            mDay2 = day;
            // show selected date to date button
            dateText2.setText(new StringBuilder()
                    .append(mDay2).append(" ")
                    .append(mMonth2 + 1).append("-")
                    .append(mYear2).append("-"));
            String test = dateText2.toString();


        }
    }


    public void MostrarDatos(String DateText1, String DateText2) throws ParseException {
        Context context = getApplicationContext();
        TableLayout tbl_datos = (TableLayout) findViewById(R.id.tbl_datos);



        try {
            if (ValidacionDatos(DateText1, DateText2)) {

                // AÑADIR LAS QUERYS PARA HACER EL SELECT E INSERTAR EN LA TABLA VISUALMENTE.
                TableRow fila =new TableRow(this);
                TextView tex;


                //Con esta seccion se añaden los datos desde la tabla de bd hacia acá

                List<PareadosFecha> lista= db.PareadosFecha(DateText1, DateText2);

                for (PareadosFecha Par : lista){
                    //fila.addView(Par);
                    tex = new EditText(this);
                    tex.setGravity(Gravity.CENTER_VERTICAL);
                    tex.setText(Par.id_pf);

                    //tex.setText(Par[0]);
                    fila.addView(tex);
                    //tbl_datos.addView(context, Par);

                }


                tbl_datos.addView(fila);


            } else {
                Toast.makeText(context, "Datos No Válidos", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }

    }

    public boolean ValidacionDatos(String DateText1, String DateText2) throws ParseException {
        Boolean EsValido = false;
        Context context = getApplicationContext();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = sdf.parse(DateText1);
        Date strDate2 = sdf.parse(DateText2);
        if (DateText1.isEmpty() && DateText2.isEmpty()) {
            //Ambas fechas se encuentran vacias
            Toast.makeText(context, "Ambas fechas se encuentran vacias", Toast.LENGTH_SHORT).show();
        } else if (DateText1.isEmpty()) {
            // Mostrar un diagolo de que la fecha 1 está vacia
            Toast.makeText(context, "La fecha de Inicio se encuentra vacia", Toast.LENGTH_SHORT).show();
        } else if (DateText2.isEmpty()) {
            //Mostrar un dialogo que la fecha2 está vacia
            Toast.makeText(context, "La fecha de Fin se encuentra vacia", Toast.LENGTH_SHORT).show();
        } else if (!strDate2.after(strDate)) {
            //Mostrar un dialogo que la fecha 2 es antes que la primera
            Toast.makeText(context, "La fecha de Fin no puede ser anterior a la de Inicio", Toast.LENGTH_SHORT).show();
        } else {
            EsValido = true;
            Toast.makeText(context, "Fechas Correctas", Toast.LENGTH_SHORT).show();
        }

        return EsValido;
    }

}



