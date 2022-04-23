package com.example.inventario_rfid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, "rfid.db",  null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {

        DB.execSQL("create table perfil(" +
                "id_per integer primary key autoincrement not null," +
                "desc_per TEXT not null," +
                "puede_insertar_item integer not null," +
                "puede_editar_item integer not null," +
                "puede_eliminar_item integer not null," +
                "puede_insertar_user integer not null," +
                "puede_editar_user integer not null," +
                "puede_eliminar_user integer not null," +
                "puede_editar_cfg integer not null" +
                ")");

        DB.execSQL("create table usuario(" +
                "id_user integer primary key autoincrement not null," +
                "pass text not null," +
                "nombre text not null," +
                "appaterno text not null," +
                "apmaterno text not null," +
                "rut text not null," +
                "dv text not null," +
                "id_per integer" +
                ", foreign key (id_per) references perfil(id_per))");

        DB.execSQL("create table categoria(" +
                "id_cat integer primary key autoincrement not null," +
                "desc_cat text not null" +
                ")");
        DB.execSQL("create table subcategoria(" +
                "id_sbc integer primary key autoincrement not null," +
                "des_sbc text not null," +
                "id_cat integer" +
                ", foreign key (id_cat) references categoria(id_cat))");

        DB.execSQL("create table item(" +
                "id_item integer primary key autoincrement not null," +
                "desc_item text not null," +
                "id_sbc integer" +
                ", foreign key (id_sbc) references subcategoria(id_sbc))");

        DB.execSQL("create table posicion(" +
                "id_pos integer primary key autoincrement not null," +
                "desc_pos text not null" +
                ")");

        DB.execSQL("create table pareados(" +
                "id_par integer primary key autoincrement not null," +
                "tag_par text not null," +
                "id_item integer," +
                "id_user integer," +
                "id_pos integer," +
                "fec_creacion text not null," +
                "fec_modificacion text not null," +
                "fec_salida text not null," +
                "esSalida integer not null," +
                "foreign key (id_item) references item(id_item)," +
                "foreign key (id_user) references usuario(id_user)," +
                "foreign key (id_pos) references posicion(id_pos)" +
                ")");

        DB.execSQL("create table escaneo(" +
                "tag_esc text not null," +
                "id_item integer," +
                "id_user integer," +
                "id_pos integer," +
                "esSalida integer not null," +
                "foreign key (id_item) references item(id_item)," +
                "foreign key (id_user) references usuario(id_user)," +
                "foreign key (id_pos) references posicion(id_pos)" +
                ")");

        DB.execSQL("create table condiguracion(" +
                "id_cfg integer primary key autoincrement not null," +
                "desc_cfg text not null," +
                "valor_cfg text not null" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {

    }


}
