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
import java.util.ArrayList;
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
        final StringBuilder sb = new StringBuilder();
        for (int index = 0; index < tagData.length; index++) {
            sb.append(tagData[index].getTagID() + "\n");
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
//                textrfid.append(sb.toString());
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

