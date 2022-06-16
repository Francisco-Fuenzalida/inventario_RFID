package com.example.inventario_rfid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.zebra.rfid.api3.TagData;


public class Scaner_activity extends AppCompatActivity implements RFIDHandler.ResponseHandlerInterface {
    public TextView statusTextViewRFID = null;
    RFIDHandler rfidHandler;
    private TextView textrfid;
    DBHelper DBH;
    Spinner itemSpin;
    List<Item> litems;
    String[] SQitems;
    Pareados par;
    final static String TAG = "RFID_SAMPLE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaner);
        DBH = DBHelper.getInstance(this);
        litems = DBH.getAllItem();
        // UI
        statusTextViewRFID = findViewById(R.id.txt_confirm_ant);
        textrfid = findViewById(R.id.txt_tag);
        // RFID Handler
        rfidHandler = new RFIDHandler();
        rfidHandler.onCreate(this);
        SQitems = new String[litems.size()];
        for (int i = 0; i < litems.size(); i++) {
            SQitems[i] = litems.get(i).desc_item;
        }
        itemSpin = findViewById(R.id.ddl_items);
        populateSpinner();

        par = new Pareados();

        par.esSalida = 0;

        par.id_pos = DBH.getPosicion("BODEGA_A").id_pos;
        int id = Usuario.id_usuario;
        par.id_user = id; //TODO
        par.fec_salida = "";
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dt = formatter.format(date);
        par.fec_creacion = dt;
        par.fec_modificacion = dt;

        Button btn_volver = (Button) findViewById(R.id.btn_volver_scan);
        btn_volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void populateSpinner() {
        ArrayAdapter<String> x = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,SQitems);
        x.setDropDownViewResource(android.R.layout.simple_spinner_item);
        itemSpin.setAdapter(x);
    }

    public void registerread(String tagid) {
        final StringBuilder sb = new StringBuilder();
        try {
            sb.append(tagid);
            Item itm = DBH.getItem(itemSpin.getSelectedItem().toString());
            par.tag_par = sb.toString();
            par.id_item = itm.id_item;
            DBH.addOrUpdatePareados(par);
            textrfid.setText(sb.toString());
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        rfidHandler.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        String status = rfidHandler.onResume();
        statusTextViewRFID.setText(status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rfidHandler.onDestroy();
    }


    @Override
    public void handleTagdata(TagData[] tagData) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void handleTriggerPress(boolean pressed) {
        if (pressed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textrfid.setText("");
                }
            });
            rfidHandler.performInventory();
        } else
            rfidHandler.stopInventory();
    }
}

