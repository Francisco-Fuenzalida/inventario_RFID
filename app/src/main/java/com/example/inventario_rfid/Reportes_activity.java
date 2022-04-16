package com.example.inventario_rfid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class Reportes_activity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener {

    private TextView dateText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);
        dateText = findViewById(R.id.txt_fecha_ini);

        findViewById(R.id.txt_btn_ini).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogCalendar();
            }
        });

        findViewById(R.id.txt_btn_fin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button btn_volver = (Button) findViewById(R.id.btn_volver_repor);
        btn_volver.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
    }

    private void ShowDialogCalendar(){
        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String date = month + "/" + dayOfMonth + "/" + year;
        dateText.setText(date);
    }
}