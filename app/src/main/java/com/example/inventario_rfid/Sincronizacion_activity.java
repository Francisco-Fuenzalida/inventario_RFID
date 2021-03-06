package com.example.inventario_rfid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajts.androidmads.library.ExcelToSQLite;
import com.ajts.androidmads.library.SQLiteToExcel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xml.serializer.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.spec.ECField;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class Sincronizacion_activity extends AppCompatActivity {

    EditText edtFilePäth;
    String directory_path = Environment.getExternalStorageDirectory().getPath() + "/storage/emulated/0/Download/table1.xls";
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizacion);

        Button btn_volver = (Button) findViewById(R.id.btn_volver_sinc);
        Button btn_exportar = (Button) findViewById(R.id.btn_exportar);
        Button btn_importar = (Button) findViewById(R.id.btn_importar);
        TextView txt_view = (TextView) findViewById(R.id.edt_file_path);

        btn_volver.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
               finish();
            }
        });


        File file = new File(directory_path);
        if (!file.exists()) {
            Log.v("File Created", String.valueOf(file.mkdirs()));
        }

        //Variable de SQLToExcel
        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(this, "DB");
        btn_exportar.setOnClickListener(new View.OnClickListener() {
            Context context = getApplicationContext();
            @Override
            public void onClick(View v) {

                guardar();
            }
        });

        btn_importar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              openFileChooser(v);

            }
        });

        edtFilePäth = findViewById(R.id.edt_file_path);




    }
    int requestcode = 1;


    public void onActivityResult(int requestCode, int resulCode, Intent data) {
        super.onActivityResult(requestcode, resulCode, data);
        Context context = getApplicationContext();
        FileInputStream inputStream;
        Intent intent = getIntent();
        Uri uri = data.getData();

        if (requestCode == requestCode && resulCode == Activity.RESULT_OK){
            if (data == null){
                return;
            }



            try{

                FileInputStream inp = (FileInputStream)getContentResolver().openInputStream(uri);
                //String aa = getContentResolver().openInputStream(uri).toString();
                leer(inp);
            }catch(Exception e) {
                e.printStackTrace();
            }



        }

    }
    public void openFileChooser(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent,requestcode);
    }

    public void guardar(){
        //Para generar un archivo (.xls)
        Workbook workbook = new HSSFWorkbook();

        //Para generar un archivo (.xlsx)
        //Workbook workbook = new XSSFWorkbook();
        Cell cell = null;
        //CellStyle cellStyle = workbook.createCellStyle();
        //cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_BLUE.getIndex());
        //cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUD);

        Sheet sheet = null;
        sheet = workbook.createSheet("lista_de_datos_2");

        Row row = null;
        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("ID");
        //cell.setCellStyle(cellStyle);

        sheet.createRow( 1);
        cell = row.createCell(1);
        cell.setCellValue("TAG");

        sheet.createRow( 2);
        cell = row.createCell(2);
        cell.setCellValue("ID_ITEM");

        sheet.createRow( 3);
        cell = row.createCell(3);
        cell.setCellValue("ID_USER");

        sheet.createRow( 4);
        cell = row.createCell(4);
        cell.setCellValue("ID_POS");

        sheet.createRow( 5);
        cell = row.createCell(5);
        cell.setCellValue("FEC_C");

        sheet.createRow( 6);
        cell = row.createCell(6);
        cell.setCellValue("FEC_M");

        sheet.createRow( 7);
        cell = row.createCell(7);
        cell.setCellValue("FEC_S");

        sheet.createRow( 8);
        cell = row.createCell(8);
        cell.setCellValue("SAL");

        //cell.setCellStyle(cellStyle);

        /*row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("ValorDePrueba1");*/

        DBHelper DBH = DBHelper.getInstance(this);
        List<Pareados> pareados = DBH.getAllPareados();
        int i=1;
        for (Pareados par: pareados) {
            int id = par.id_par;
            String pa = par.tag_par;
            int item = par.id_item;
            int id_user = par.id_user;
            int id_pos = par.id_pos;
            String f_c = par.fec_creacion;
            String f_m = par.fec_modificacion;
            String f_s = par.fec_salida;
            int es = par.esSalida;

            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(id);

            //sheet.createRow(i);
            cell = row.createCell(1);
            cell.setCellValue(pa);
            cell = row.createCell(2);
            cell.setCellValue(item);
            cell = row.createCell(3);
            cell.setCellValue(id_user);
            cell = row.createCell(4);
            cell.setCellValue(id_pos);
            cell = row.createCell(5);
            cell.setCellValue(f_c);
            cell = row.createCell(6);
            cell.setCellValue(f_m);
            cell = row.createCell(7);
            cell.setCellValue(f_s);
            cell = row.createCell(8);
            cell.setCellValue(es);
            i ++;
        }

        //Generar un archivo (.xsl)
        //File file = new File(getExternalFilesDir(null), "Datos_De_Prueba.xls");
        File file = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Datos.xls");
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

        //Generar un archivo (.xlsx)
       // File file = new File(getExternalFilesDir(null), "Datos_de_prueba.xlsx");

        FileOutputStream out = null;
        try{
            out = new FileOutputStream(file);
            workbook.write(out);
            Toast.makeText(this, path, Toast.LENGTH_LONG).show();


        }catch(IOException e){
            e.printStackTrace();
            Toast.makeText(this, "ERR", Toast.LENGTH_LONG).show();

        }


    }

    public void leer(FileInputStream file){
        //File file = new File(getExternalFilesDir(null), "Temp.xls");
        FileInputStream inputStream = null;
        DBHelper DB = DBHelper.getInstance(this);
        TextView txt_view = (TextView) findViewById(R.id.edt_file_path);


        String datos ="";



            try {
                inputStream = file;

                POIFSFileSystem fileSystem = new POIFSFileSystem(inputStream);

                //PARA ARCHIVOS HASTA EL 2007 (.XLS)
                HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
                HSSFSheet sheet = workbook.getSheetAt(0);

                //PARA ARCHIVOS DESDE EL 2007 EN ADELANTE (.XLSX)
                //XSSFWorkbook workbook = new XSSFWorkbook(file);
                //XSSFSheet sheet = workbook.getSheetAt(0);

                Iterator<Row> rowIterator = sheet.rowIterator();
                if (sheet.getRow(2) != null ){
                    while (rowIterator.hasNext()) {
                        try {
                            HSSFRow row = (HSSFRow) rowIterator.next();
                            Iterator<Cell> cellIterator = row.cellIterator();


                            List<String> lista = null;
                            Pareados par = new Pareados();
                            while (cellIterator.hasNext()) {
                                HSSFCell cell = (HSSFCell) cellIterator.next();


                                lista.add(cell.toString());

                                datos = datos + " -- " + cell.toString();
                            }
                            datos = datos + "\n";

                            if (!lista.get(0).isEmpty()){ par.id_par = Integer.parseInt(lista.get(0)); }
                            else{ par.id_par =1;}

                            if (!lista.get(1).isEmpty()){ par.tag_par = lista.get(1);}
                            else{ par.tag_par =""; }

                            if (!lista.get(2).isEmpty()){ par.id_item = Integer.parseInt(lista.get(2)); }
                            else{ par.id_item =1; }

                            if (!lista.get(3).isEmpty()){ par.id_user = Integer.parseInt(lista.get(3));}
                            else{ par.id_user =1;  }

                            if (!lista.get(4).isEmpty()){ par.id_pos = Integer.parseInt(lista.get(4)); }
                            else{ par.id_pos =1;}

                            if (!lista.get(5).isEmpty()){par.fec_creacion = lista.get(5); }
                            else{ par.fec_creacion ="";}

                            if (!lista.get(6).isEmpty()){par.fec_modificacion = lista.get(6);  }
                            else{ par.fec_modificacion =""; }

                            if (!lista.get(7).isEmpty()){ par.fec_salida = lista.get(7);  }
                            else{par.fec_salida = ""; }

                            if (!lista.get(8).isEmpty()){par.esSalida = Integer.parseInt(lista.get(8)); }
                            else{par.esSalida =0; }

                            DB.addOrUpdatePareados(par);
                        } catch (Exception e) {
                            e.toString();
                        }

                    }
                    txt_view.setText(datos);// --> esta lina ingresa los datos para que se muestren en una tabla o textview.


                }
                else{
                    Toast.makeText(this, "No existen datos", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }




