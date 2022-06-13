package com.example.inventario_rfid;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    //Singleton
    private static DBHelper sInstance;

    // Dase de Batos
    private static final String DATABASE_NAME = "DB";
    private static final int DATABASE_VERSION = 1;

    // Nombre Tablas
    private static final String TABLE_PROFILES = "perfil";
    private static final String TABLE_USERS = "usuario";
    private static final String TABLE_POSICION = "posicion";
    private static final String TABLE_ITEM = "item";
    private static final String TABLE_CONFIGURACION = "configuracion";
    private static final String TABLE_SUBCATEGORIA = "subcategoria";
    private static final String TABLE_ESCANEO = "escaneo";
    private static final String TABLE_CATEGORIA = "categoria";
    private static final String TABLE_PAREADOS = "pareados";
    private static final String TABLE_SECURITY_QUESTIONS = "pregunta_seguridad";

    // Profile Table Columns
    private static final String KEY_PROFILES_ID = "id_per";
    private static final String KEY_PROFILES_DESC = "desc_per";
    private static final String KEY_PROFILES_PII = "puede_insertar_item";
    private static final String KEY_PROFILES_PEI = "puede_editar_item";
    private static final String KEY_PROFILES_PBI = "puede_borrar_item";
    private static final String KEY_PROFILES_PIU = "puede_insertar_user";
    private static final String KEY_PROFILES_PEU = "puede_editar_user";
    private static final String KEY_PROFILES_PBU = "puede_borrar_user";
    private static final String KEY_PROFILES_CFG = "puede_editar_cfg";


    // User Table Columns
    private static final String KEY_USER_ID = "id_user";
    private static final String KEY_USER_USER = "user";
    private static final String KEY_USER_PASS = "pass";
    private static final String KEY_USER_NAME = "nombre";
    private static final String KEY_USER_SURNAME_FATHER = "appaterno";
    private static final String KEY_USER_SURNAME_MOTHER = "apmaterno";
    private static final String KEY_USER_RUT = "rut";
    private static final String KEY_USER_DV = "dv";
    private static final String KEY_USER_SECRET_QUESTION_ID = "id_sec_que";
    private static final String KEY_USER_SECRET_ANSWER = "respuesta_secreta";
    private static final String KEY_USER_PROFILE_ID = "id_per";


    // Posicion Table Columns
    private static final String KEY_POSICION_ID = "id_pos";
    private static final String KEY_POSICION_DESC = "desc_pos";


    // Item Table Columns
    private static final String KEY_ITEM_ID = "id_item";
    private static final String KEY_ITEM_DESC = "desc_item";
    private static final String KEY_ITEM_SBC = "id_sbc";

    // Configuracion Table Columns
    private static final String KEY_CONFIGURACION_ID = "id_cfg";
    private static final String KEY_CONFIGURACION_DESC = "desc_cfg";
    private static final String KEY_CONFIGURACION_VALUE = "value_cfg";

    // Subcategoria Table Columns
    private static final String KEY_SUBCATEGORIA_ID = "id_sbc";
    private static final String KEY_SUBCATEGORIA_DESC = "desc_sbc";
    private static final String KEY_SUBCATEGORIA_CAT = "id_cat";

    // Escaneo Table Columns
    private static final String KEY_ESCANEO_ESC = "tag_esc";
    private static final String KEY_ESCANEO_ITEM = "id_item";
    private static final String KEY_ESCANEO_USER = "id_user";
    private static final String KEY_ESCANEO_POS = "id_pos";
    private static final String KEY_ESCANEO_SALIDA = "esSalida";

    // Categoria Table Columns
    private static final String KEY_CATEGORIA_ID = "id_cat";
    private static final String KEY_CATEGORIA_DESC = "desc_cat";

    // Pareados Table Columns
    private static final String KEY_PAREADOS_ID = "id_par";
    private static final String KEY_PAREADOS_PAR = "tag_par";
    private static final String KEY_PAREADOS_ITEM = "id_item";
    private static final String KEY_PAREADOS_USER = "id_user";
    private static final String KEY_PAREADOS_POS = "id_pos";
    private static final String KEY_PAREADOS_CREACION = "fec_creacion";
    private static final String KEY_PAREADOS_MODIFICACION = "fec_modificicaion";
    private static final String KEY_PAREADOS_SALIDA = "fec_salida";
    private static final String KEY_PAREADOS_ESSALIDA = "esSalida";

    // Pregunta Seguridad Table Columns
    private static final String KEY_SECURITY_QUESTIONS_ID = "id_sec_que";
    private static final String KEY_SECURITY_QUESTIONS_DESC = "desc_question";


    public static synchronized DBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {

        String CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILES +
                "(" +
                KEY_PROFILES_ID + " INTEGER PRIMARY KEY," +
                KEY_PROFILES_DESC + " TEXT," +
                KEY_PROFILES_PII + " INTEGER," +
                KEY_PROFILES_PEI + " INTEGER," +
                KEY_PROFILES_PBI + " INTEGER," +
                KEY_PROFILES_PIU + " INTEGER," +
                KEY_PROFILES_PEU + " INTEGER," +
                KEY_PROFILES_PBU + " INTEGER," +
                KEY_PROFILES_CFG + " INTEGER" +
                ")";

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," +
                KEY_USER_USER + " TEXT," +
                KEY_USER_PASS + " TEXT," +
                KEY_USER_NAME + " TEXT," +
                KEY_USER_SURNAME_FATHER + " TEXT," +
                KEY_USER_SURNAME_MOTHER + " TEXT," +
                KEY_USER_RUT + " INTEGER," +
                KEY_USER_DV + " TEXT," +
                KEY_USER_SECRET_ANSWER + " TEXT," +
                KEY_USER_PROFILE_ID + " INTEGER REFERENCES " + TABLE_PROFILES + "," +// Define a foreign key
                KEY_USER_SECRET_QUESTION_ID + " INTEGER REFERENCES " + TABLE_SECURITY_QUESTIONS +
                ")";

        String CREATE_POSICION_TABLE = "CREATE TABLE " + TABLE_POSICION +
                "(" +
                KEY_POSICION_ID + " INTEGER PRIMARY KEY," +
                KEY_POSICION_DESC + " TEXT" +
                ")";

        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," +
                KEY_ITEM_DESC + " TEXT," +
                KEY_ITEM_SBC + " INTEGER" +
                ")";

        String CREATE_CONFIGURACION_TABLE = "CREATE TABLE " + TABLE_CONFIGURACION +
                "(" +
                KEY_CONFIGURACION_ID + " INTEGER PRIMARY KEY," +
                KEY_CONFIGURACION_DESC + " TEXT," +
                KEY_CONFIGURACION_VALUE + " TEXT" +
                ")";

        String CREATE_SUBCATEGORIA_TABLE = "CREATE TABLE " + TABLE_SUBCATEGORIA +
                "(" +
                KEY_SUBCATEGORIA_ID + " INTEGER PRIMARY KEY," +
                KEY_SUBCATEGORIA_DESC + " TEXT," +
                KEY_SUBCATEGORIA_CAT + " INTEGER" +
                ")";

        String CREATE_ESCANEO_TABLE = "CREATE TABLE " + TABLE_ESCANEO +
                "(" +
                KEY_ESCANEO_ESC + " TEXT PRIMARY KEY," +
                KEY_ESCANEO_ITEM + " INTEGER," +
                KEY_ESCANEO_USER + " INTEGER," +
                KEY_ESCANEO_POS + " INTEGER," +
                KEY_ESCANEO_SALIDA + " INTEGER" +
                ")";

        String CREATE_CATEGORIA_TABLE = "CREATE TABLE " + TABLE_CATEGORIA +
                "(" +
                KEY_CATEGORIA_ID + " INTEGER PRIMARY KEY," +
                KEY_CATEGORIA_DESC + " TEXT" +
                ")";

        String CREATE_PAREADOS_TABLE = "CREATE TABLE " + TABLE_PAREADOS +
                "(" +
                KEY_PAREADOS_ID + " INTEGER PRIMARY KEY," +
                KEY_PAREADOS_PAR + " TEXT," +
                KEY_PAREADOS_ITEM + " INTEGER," +
                KEY_PAREADOS_USER + " INTEGER," +
                KEY_PAREADOS_POS + " INTEGER," +
                KEY_PAREADOS_CREACION + " TEXT," +
                KEY_PAREADOS_MODIFICACION + " TEXT," +
                KEY_PAREADOS_SALIDA + " TEXT," +
                KEY_PAREADOS_ESSALIDA + " INTEGER" +
                ")";

        String CREATE_SECURITY_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_SECURITY_QUESTIONS +
                "(" +
                KEY_SECURITY_QUESTIONS_ID + " INTEGER PRIMARY KEY," +
                KEY_SECURITY_QUESTIONS_DESC + " TEXT" +
                ")";

        String INSERT_SECURITY_QUESTIONS_TABLE = "INSERT INTO " + TABLE_SECURITY_QUESTIONS + " (" + KEY_SECURITY_QUESTIONS_DESC + ") VALUES" +
                "('Escoja una pregunta de seguridad:')," +
                "('¿Cómo se llama tu tío favorito?')," +
                "('¿Dónde conociste a tu pareja?')," +
                "('¿Cómo se llama tu primo mayor?')," +
                "('¿Cual es el apodo de tu hijo menor?')," +
                "('¿Dónde pasaste tu luna de miel?')," +
                "('¿Cómmo se llama tu profesor favorito?')," +
                "('¿Cúal es tu anime favorito?')," +
                "('¿Donde nacio tu tatarabuela?')";

        String INSERT_PERFIL_TABLE = "INSERT INTO " + TABLE_PROFILES + " (" + KEY_PROFILES_DESC+','+KEY_PROFILES_PII+','+KEY_PROFILES_PEI+','+KEY_PROFILES_PBI+','+
                KEY_PROFILES_PIU+','+KEY_PROFILES_PEU+','+KEY_PROFILES_PBU+','+KEY_PROFILES_CFG + ") VALUES" +
                "('admin',1,1,1,1,1,1,1)";

        String INSERT_USER_TABLE = "INSERT INTO " + TABLE_USERS + " (" + KEY_USER_USER+','+KEY_USER_PASS+','+KEY_USER_NAME+','+KEY_USER_SURNAME_FATHER+','+
                KEY_USER_SURNAME_MOTHER+','+KEY_USER_RUT+','+KEY_USER_DV+','+KEY_USER_SECRET_ANSWER+','+KEY_USER_PROFILE_ID+','+KEY_USER_SECRET_QUESTION_ID + ") VALUES" +
                "('tester','tester','Tester','Testington','Testeani','19999999','1','tester',1,1)";

        String INSERT_CATEGORIA_TABLE = "INSERT INTO " + TABLE_CATEGORIA + " (" + KEY_CATEGORIA_DESC + ") VALUES" +
                "('Escoja una categoría:')";

        String INSERT_SUBCATEGORY_TABLE = "INSERT INTO " + TABLE_SUBCATEGORIA + " ("+KEY_SUBCATEGORIA_DESC + ") VALUES" +
                "('')";

        String INSERT_ITEM_TABLE = "INSERT INTO " + TABLE_ITEM + " ("+KEY_ITEM_DESC + ") VALUES" +
                "('')";


        DB.execSQL(CREATE_PROFILE_TABLE);
        DB.execSQL(CREATE_USERS_TABLE);

        DB.execSQL(CREATE_POSICION_TABLE);

        DB.execSQL(CREATE_CONFIGURACION_TABLE);

        DB.execSQL(CREATE_CATEGORIA_TABLE);
        DB.execSQL(CREATE_SUBCATEGORIA_TABLE);
        DB.execSQL(CREATE_ITEM_TABLE);


        DB.execSQL(CREATE_ESCANEO_TABLE);
        DB.execSQL(CREATE_PAREADOS_TABLE);
        DB.execSQL(CREATE_SECURITY_QUESTIONS_TABLE);
        DB.execSQL(INSERT_SECURITY_QUESTIONS_TABLE);
        DB.execSQL(INSERT_PERFIL_TABLE);
        DB.execSQL(INSERT_USER_TABLE);
        DB.execSQL(INSERT_CATEGORIA_TABLE);
        DB.execSQL(INSERT_SUBCATEGORY_TABLE);
        DB.execSQL(INSERT_ITEM_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAREADOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESCANEO);

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBCATEGORIA);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIA);

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSICION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIGURACION);

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);

            onCreate(db);
        }
    }

    public long addOrUpdateProfile(Perfil perfil) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long perfilId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PROFILES_DESC, perfil.desc_per);
            values.put(KEY_PROFILES_PII, perfil.puede_insertar_item);
            values.put(KEY_PROFILES_PEI, perfil.puede_editar_item);
            values.put(KEY_PROFILES_PBI, perfil.puede_borrar_item);
            values.put(KEY_PROFILES_PIU, perfil.puede_insertar_user);
            values.put(KEY_PROFILES_PEU, perfil.puede_editar_user);
            values.put(KEY_PROFILES_PBU, perfil.puede_borrar_user);
            values.put(KEY_PROFILES_CFG, perfil.puede_editar_cfg);


            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_PROFILES, values, KEY_PROFILES_DESC + "= ?", new String[]{perfil.desc_per});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String profileSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_PROFILES_ID, TABLE_PROFILES, KEY_PROFILES_DESC);
                Cursor cursor = db.rawQuery(profileSelectQuery, new String[]{String.valueOf(perfil.desc_per)});
                try {
                    if (cursor.moveToFirst()) {
                        perfilId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                perfilId = db.insertOrThrow(TABLE_PROFILES, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            String debug = e.toString();
        } finally {
            db.endTransaction();
        }
        return perfilId;
    }

    public long addOrUpdateUser(Usuario user) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_USER,           user.user);
            values.put(KEY_USER_PASS,           user.pass);
            values.put(KEY_USER_NAME,           user.nombre);
            values.put(KEY_USER_SURNAME_FATHER, user.appaterno);
            values.put(KEY_USER_SURNAME_MOTHER, user.apmaterno);
            values.put(KEY_USER_RUT,            user.rut);
            values.put(KEY_USER_DV,             user.dv);
            values.put(KEY_USER_SECRET_ANSWER,  user.answer);
            values.put(KEY_USER_SECRET_QUESTION_ID, user.id_sec_que);
            values.put(KEY_USER_PROFILE_ID,     user.id_per);


            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_USERS, values, KEY_USER_USER + "= ?", new String[]{user.user});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String userSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID, TABLE_USERS, KEY_USER_USER);
                Cursor cursor = db.rawQuery(userSelectQuery, new String[]{String.valueOf(user.user)});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                userId = db.insertOrThrow(TABLE_USERS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            String debug = e.toString();
        } finally {
            db.endTransaction();
        }
        return userId;
    }

    public long addOrUpdatePosicion(Posicion posicion) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long posicionId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_POSICION_DESC, posicion.desc_pos);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_POSICION, values, KEY_POSICION_DESC + "= ?", new String[]{posicion.desc_pos});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String posicionSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_POSICION_ID, TABLE_POSICION, KEY_POSICION_DESC);
                Cursor cursor = db.rawQuery(posicionSelectQuery, new String[]{String.valueOf(posicion.desc_pos)});
                try {
                    if (cursor.moveToFirst()) {
                        posicionId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                posicionId = db.insertOrThrow(TABLE_POSICION, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            String debug = e.toString();
        } finally {
            db.endTransaction();
        }
        return posicionId;
    }

    public long addOrUpdateItem(Item item) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long itemId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_DESC, item.desc_item);
            values.put(KEY_ITEM_SBC, item.id_sbc);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_ITEM, values, KEY_ITEM_DESC + "= ?", new String[]{item.desc_item});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String itemSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_ITEM_ID, TABLE_ITEM, KEY_ITEM_DESC);
                Cursor cursor = db.rawQuery(itemSelectQuery, new String[]{String.valueOf(item.desc_item)});
                try {
                    if (cursor.moveToFirst()) {
                        itemId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                itemId = db.insertOrThrow(TABLE_ITEM, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            String debug = e.toString();
        } finally {
            db.endTransaction();
        }
        return itemId;
    }

    public Boolean deleteDetails(String tableName,String column,String val){
        SQLiteDatabase db=getWritableDatabase();
        String whereClause=column+"=?";
        String whereArgs[]={val};

        int id=db.delete(tableName,whereClause,whereArgs);
        if(id>0){

            return true;

        }else{

            return  false;

        }
    }

    public long addOrUpdateConfiguracion(Configuracion configuracion) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long configuracionId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CONFIGURACION_DESC, configuracion.desc_cfg);
            values.put(KEY_CONFIGURACION_VALUE, configuracion.value_cfg);


            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_CONFIGURACION, values, KEY_CONFIGURACION_DESC + "= ?", new String[]{configuracion.desc_cfg});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String configuracionSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_CONFIGURACION_ID, TABLE_CONFIGURACION, KEY_CONFIGURACION_DESC);
                Cursor cursor = db.rawQuery(configuracionSelectQuery, new String[]{String.valueOf(configuracion.desc_cfg)});
                try {
                    if (cursor.moveToFirst()) {
                        configuracionId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                configuracionId = db.insertOrThrow(TABLE_CONFIGURACION, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            String debug = e.toString();
        } finally {
            db.endTransaction();
        }
        return configuracionId;
    }


    public long addOrUpdateSubcategoria(Subcategoria subcategoria) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long subcategoriaId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_SUBCATEGORIA_DESC, subcategoria.des_sbc);
            values.put(KEY_SUBCATEGORIA_CAT, subcategoria.id_cat);


            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_SUBCATEGORIA, values, KEY_SUBCATEGORIA_DESC + "= ?", new String[]{subcategoria.des_sbc});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String subcategoriaSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_SUBCATEGORIA_ID, TABLE_SUBCATEGORIA, KEY_SUBCATEGORIA_DESC);
                Cursor cursor = db.rawQuery(subcategoriaSelectQuery, new String[]{String.valueOf(subcategoria.des_sbc)});
                try {
                    if (cursor.moveToFirst()) {
                        subcategoriaId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                subcategoriaId = db.insertOrThrow(TABLE_SUBCATEGORIA, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            String debug = e.toString();
        } finally {
            db.endTransaction();
        }
        return subcategoriaId;
    }

    public long addOrUpdateEscaneo(Escaneo escaneo) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long escaneoId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ESCANEO_ESC, escaneo.tag_esc);
            values.put(KEY_ESCANEO_ITEM, escaneo.id_item);
            values.put(KEY_ESCANEO_USER, escaneo.id_user);
            values.put(KEY_ESCANEO_POS, escaneo.id_pos);
            values.put(KEY_ESCANEO_SALIDA, escaneo.esSalida);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_ESCANEO, values, KEY_ESCANEO_ESC + "= ?", new String[]{escaneo.tag_esc});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String escaneoSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_ESCANEO_ESC, TABLE_ESCANEO, KEY_ESCANEO_ESC);
                Cursor cursor = db.rawQuery(escaneoSelectQuery, new String[]{String.valueOf(escaneo.tag_esc)});
                try {
                    if (cursor.moveToFirst()) {
                        escaneoId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                escaneoId = db.insertOrThrow(TABLE_ESCANEO, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            String debug = e.toString();
        } finally {
            db.endTransaction();
        }
        return escaneoId;
    }

    public long addOrUpdateCategoria(Categoria categoria) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long categoriaId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CATEGORIA_DESC, categoria.desc_cat);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_CATEGORIA, values, KEY_CATEGORIA_DESC + "= ?", new String[]{categoria.desc_cat});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String profileSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_CATEGORIA_ID, TABLE_CATEGORIA, KEY_CATEGORIA_DESC);
                Cursor cursor = db.rawQuery(profileSelectQuery, new String[]{String.valueOf(categoria.desc_cat)});
                try {
                    if (cursor.moveToFirst()) {
                        categoriaId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                categoriaId = db.insertOrThrow(TABLE_CATEGORIA, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            String debug = e.toString();
        } finally {
            db.endTransaction();
        }
        return categoriaId;
    }

    public long addOrUpdatePareados(Pareados pareados) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long pareadosId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PAREADOS_PAR, pareados.tag_par);
            values.put(KEY_PAREADOS_ITEM, pareados.id_item);
            values.put(KEY_PAREADOS_USER, pareados.id_user);
            values.put(KEY_PAREADOS_POS, pareados.id_pos);
            values.put(KEY_PAREADOS_CREACION, pareados.fec_creacion);
            values.put(KEY_PAREADOS_MODIFICACION, pareados.fec_modificacion);
            values.put(KEY_PAREADOS_SALIDA, pareados.fec_salida);
            values.put(KEY_PAREADOS_ESSALIDA, pareados.esSalida);


            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_PAREADOS, values, KEY_PAREADOS_PAR + "= ?", new String[]{pareados.tag_par});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String profileSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_PAREADOS_ID, TABLE_PAREADOS, KEY_PAREADOS_PAR);
                Cursor cursor = db.rawQuery(profileSelectQuery, new String[]{String.valueOf(pareados.tag_par)});
                try {
                    if (cursor.moveToFirst()) {
                        pareadosId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                pareadosId = db.insertOrThrow(TABLE_PAREADOS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            String debug = e.toString();
        } finally {
            db.endTransaction();
        }
        return pareadosId;
    }

    public long addOrUpdateSecurityQuestions(SecurityQuestions securityQuestions) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long pareadosId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_SECURITY_QUESTIONS_ID, securityQuestions.id_sec_que);
            values.put(KEY_SECURITY_QUESTIONS_DESC, securityQuestions.desc_question);


            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_SECURITY_QUESTIONS, values, KEY_SECURITY_QUESTIONS_DESC + "= ?", new String[]{securityQuestions.desc_question});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String profileSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_SECURITY_QUESTIONS_ID, TABLE_SECURITY_QUESTIONS, KEY_SECURITY_QUESTIONS_DESC);
                Cursor cursor = db.rawQuery(profileSelectQuery, new String[]{String.valueOf(securityQuestions.desc_question)});
                try {
                    if (cursor.moveToFirst()) {
                        pareadosId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                pareadosId = db.insertOrThrow(TABLE_SECURITY_QUESTIONS, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            String debug = e.toString();
        } finally {
            db.endTransaction();
        }
        return pareadosId;
    }

    @SuppressLint("Range")
    public List<Perfil> getAllProfiles() {
        List<Perfil> profiles = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_PROFILES);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Perfil newProfile = new Perfil();
                    newProfile.id_per = cursor.getInt(cursor.getColumnIndex(KEY_PROFILES_ID));
                    newProfile.desc_per = cursor.getString(cursor.getColumnIndex(KEY_PROFILES_DESC));
                    newProfile.puede_insertar_item = cursor.getInt(cursor.getColumnIndex(KEY_PROFILES_PII));
                    newProfile.puede_insertar_user = cursor.getInt(cursor.getColumnIndex(KEY_PROFILES_PIU));
                    newProfile.puede_editar_item = cursor.getInt(cursor.getColumnIndex(KEY_PROFILES_PEI));
                    newProfile.puede_editar_user = cursor.getInt(cursor.getColumnIndex(KEY_PROFILES_PEU));
                    newProfile.puede_borrar_item = cursor.getInt(cursor.getColumnIndex(KEY_PROFILES_PBI));
                    newProfile.puede_borrar_user = cursor.getInt(cursor.getColumnIndex(KEY_PROFILES_PBU));
                    newProfile.puede_editar_cfg = cursor.getInt(cursor.getColumnIndex(KEY_PROFILES_CFG));

                    profiles.add(newProfile);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            ;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return profiles;
    }
    @SuppressLint("Range")
    public Perfil getProfile(String perfil) {
        List<Perfil> profiles = getAllProfiles();

        for (Perfil perf : profiles) {
            String desc  = perf.desc_per.toString();
            if(desc.equals(perfil)){
                return perf;
            }
        }
        return new Perfil();
    }


    @SuppressLint("Range")
    public List<Usuario> getAllUsers() {
        List<Usuario> users = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_USERS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Usuario newUser = new Usuario();
                    newUser.user = cursor.getString(cursor.getColumnIndex(KEY_USER_USER));
                    newUser.pass = cursor.getString(cursor.getColumnIndex(KEY_USER_PASS));
                    newUser.nombre = cursor.getString(cursor.getColumnIndex(KEY_USER_NAME ));
                    newUser.rut = cursor.getString(cursor.getColumnIndex(KEY_USER_RUT));
                    newUser.dv = cursor.getString(cursor.getColumnIndex(KEY_USER_DV));
                    newUser.id_per =  Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_PROFILE_ID)));
                    newUser.id_user = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                    newUser.id_sec_que = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_SECRET_QUESTION_ID)));
                    newUser.appaterno = cursor.getString(cursor.getColumnIndex(KEY_USER_SURNAME_FATHER));
                    newUser.apmaterno = cursor.getString(cursor.getColumnIndex(KEY_USER_SURNAME_MOTHER));
                    newUser.answer = cursor.getString(cursor.getColumnIndex(KEY_USER_SECRET_ANSWER));

                    users.add(newUser);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            ;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return users;
    }

    @SuppressLint("Range")
    public Usuario getUser(String username) {
        List<Usuario> users = getAllUsers();

        for (Usuario us : users) {
            String uss =us.user.toString();
            if(uss.equals(username)){
                return us;
            }
        }
        return new Usuario();
    }

    @SuppressLint("Range")
    public Usuario getUserRut(String rut) {
        List<Usuario> users = getAllUsers();
        for (Usuario us : users) {
            String rut_encontrado = us.rut.toString();
            if(rut_encontrado.equals(rut)){
                return us;
            }
        }
        return new Usuario();
    }

    public Boolean Login(String usuario, String password){
        Usuario User = getUser(usuario);
        String ppas = User.pass.toString();
        if(ppas.equals(password))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    @SuppressLint("Range")
    public List<Posicion> getAllPositions() {
        List<Posicion> positions = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_POSICION);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Posicion newPosicion = new Posicion();
                    newPosicion.id_pos = cursor.getInt(cursor.getColumnIndex(KEY_POSICION_ID));
                    newPosicion.desc_pos = cursor.getString(cursor.getColumnIndex(KEY_POSICION_DESC));

                    positions.add(newPosicion);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            ;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return positions;
    }
    @SuppressLint("Range")
    public Posicion getPosicion(String posicion) {
        List<Posicion> Positions = getAllPositions();

        for (Posicion pos : Positions) {
            String desc  = pos.desc_pos.toString();
            if(desc.equals(posicion)){
                return pos;
            }
        }
        return new Posicion();
    }

    @SuppressLint("Range")
    public List<Item> getAllItem() {
        List<Item> item = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_ITEM);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Item newItem = new Item();
                    newItem.id_item = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ID));
                    newItem.desc_item = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESC));
                    newItem.id_sbc = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_SBC));

                    item.add(newItem);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            ;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return item;
    }

    @SuppressLint("Range")
    public List<Item> getAllItemWhereId(int id) {
        List<Item> item = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE id_sbc=%s",
                        TABLE_ITEM, id);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Item newItem = new Item();
                    newItem.id_item = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ID));
                    newItem.desc_item = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESC));
                    newItem.id_sbc = cursor.getInt(cursor.getColumnIndex(KEY_ITEM_SBC));

                    item.add(newItem);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            ;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return item;
    }

    @SuppressLint("Range")
    public Item getItem(String item) {
        List<Item> items = getAllItem();

        for (Item it : items) {
            String desc  = it.desc_item.toString();
            if(desc.equals(item)){
                return it;
            }
        }
        return new Item();
    }

    @SuppressLint("Range")
    public List<Configuracion> getAllConfiguracion() {
        List<Configuracion> configuracion = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_CONFIGURACION);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Configuracion newConfiguracion = new Configuracion();
                    newConfiguracion.id_cfg = cursor.getInt(cursor.getColumnIndex(KEY_CONFIGURACION_ID));
                    newConfiguracion.desc_cfg = cursor.getString(cursor.getColumnIndex(KEY_CONFIGURACION_DESC));
                    newConfiguracion.value_cfg = cursor.getString(cursor.getColumnIndex(KEY_CONFIGURACION_VALUE));

                    configuracion.add(newConfiguracion);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            ;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return configuracion;
    }
    @SuppressLint("Range")
    public Configuracion getConfiguracion(String configuracion) {
        List<Configuracion> configuraciones = getAllConfiguracion();

        for (Configuracion cfg : configuraciones) {
            String desc  = cfg.desc_cfg.toString();
            if(desc.equals(configuracion)){
                return cfg;
            }
        }
        return new Configuracion();
    }

    @SuppressLint("Range")
    public List<Subcategoria> getAllSubcategoria() {
        List<Subcategoria> subcategoria = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_SUBCATEGORIA);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Subcategoria newSubcategoria = new Subcategoria();
                    newSubcategoria.id_sbc = cursor.getInt(cursor.getColumnIndex(KEY_SUBCATEGORIA_ID));
                    newSubcategoria.des_sbc = cursor.getString(cursor.getColumnIndex(KEY_SUBCATEGORIA_DESC));
                    newSubcategoria.id_cat = cursor.getInt(cursor.getColumnIndex(KEY_SUBCATEGORIA_CAT));

                    subcategoria.add(newSubcategoria);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            ;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return subcategoria;
    }

    @SuppressLint("Range")
    public List<Subcategoria> getAllSubcategoriaWhereId(int id) {
        List<Subcategoria> subcategoria = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE id_cat=%s",
                        TABLE_SUBCATEGORIA, id);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Subcategoria newSubcategoria = new Subcategoria();
                    newSubcategoria.id_sbc = cursor.getInt(cursor.getColumnIndex(KEY_SUBCATEGORIA_ID));
                    newSubcategoria.des_sbc = cursor.getString(cursor.getColumnIndex(KEY_SUBCATEGORIA_DESC));
                    newSubcategoria.id_cat = cursor.getInt(cursor.getColumnIndex(KEY_SUBCATEGORIA_CAT));

                    subcategoria.add(newSubcategoria);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            ;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return subcategoria;
    }

    @SuppressLint("Range")
    public Subcategoria getSubcategoria(String subcategoria) {
        List<Subcategoria> Subcategoria = getAllSubcategoria();

        for (Subcategoria sbc : Subcategoria) {
            String desc  = sbc.des_sbc.toString();
            if(desc.equals(subcategoria)){
                return sbc;
            }
        }
        return new Subcategoria();
    }

    @SuppressLint("Range")
    public List<Escaneo> getAllEscaneos() {
        List<Escaneo> escaneo = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_PROFILES);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Escaneo newEscaneo = new Escaneo();
                    newEscaneo.tag_esc = cursor.getString(cursor.getColumnIndex(KEY_ESCANEO_ESC));
                    newEscaneo.id_item = cursor.getInt(cursor.getColumnIndex(KEY_ESCANEO_ITEM));
                    newEscaneo.id_user = cursor.getInt(cursor.getColumnIndex(KEY_ESCANEO_USER));
                    newEscaneo.id_pos = cursor.getInt(cursor.getColumnIndex(KEY_ESCANEO_POS));
                    newEscaneo.esSalida = cursor.getInt(cursor.getColumnIndex(KEY_ESCANEO_SALIDA));

                    escaneo.add(newEscaneo);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            ;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return escaneo;
    }

    @SuppressLint("Range")
    public List<Categoria> getAllCategoria() {
        List<Categoria> categoria = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_CATEGORIA);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Categoria newCategoria = new Categoria();
                    newCategoria.id_cat = cursor.getInt(cursor.getColumnIndex(KEY_CATEGORIA_ID));
                    newCategoria.desc_cat = cursor.getString(cursor.getColumnIndex(KEY_CATEGORIA_DESC));

                    categoria.add(newCategoria);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            ;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return categoria;
    }

    @SuppressLint("Range")
    public Categoria getCategoria(String perfil) {
        List<Categoria> cats = getAllCategoria();

        for (Categoria cat : cats) {
            String desc  = cat.desc_cat.toString();
            if(desc.equals(perfil)){
                return cat;
            }
        }
        return new Categoria();
    }

    @SuppressLint("Range")
    public List<Pareados> getAllPareados() {
        List<Pareados> pareados = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_PAREADOS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Pareados newPareados = new Pareados();
                    newPareados.id_par = cursor.getInt(cursor.getColumnIndex(KEY_PAREADOS_ID));
                    newPareados.tag_par = cursor.getString(cursor.getColumnIndex(KEY_PAREADOS_PAR));
                    newPareados.id_item = cursor.getInt(cursor.getColumnIndex(KEY_PAREADOS_ITEM));
                    newPareados.id_user = cursor.getInt(cursor.getColumnIndex(KEY_PAREADOS_USER));
                    newPareados.id_pos = cursor.getInt(cursor.getColumnIndex(KEY_PAREADOS_POS));
                    newPareados.fec_creacion = cursor.getString(cursor.getColumnIndex(KEY_PAREADOS_CREACION));
                    newPareados.fec_modificacion = cursor.getString(cursor.getColumnIndex(KEY_PAREADOS_MODIFICACION));
                    newPareados.fec_salida = cursor.getString(cursor.getColumnIndex(KEY_PAREADOS_SALIDA));
                    newPareados.esSalida = cursor.getInt(cursor.getColumnIndex(KEY_PAREADOS_ESSALIDA));

                    pareados.add(newPareados);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            ;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return pareados;
    }

    @SuppressLint("Range")
    public List<PareadosFecha> PareadosFecha(String Fecha1,String Fecha2) {
        List<PareadosFecha> parsf = new ArrayList<>();


        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s INNER JOIN %s ON %s.%s = %s.%s",
                        TABLE_PAREADOS,
                        TABLE_ITEM,
                        TABLE_ITEM,
                        KEY_ITEM_ID,
                        TABLE_PAREADOS,
                        KEY_PAREADOS_ITEM
                        );

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Date dateI = new SimpleDateFormat("dd/MM/yyyy").parse(Fecha1);
                    Date dateF = new SimpleDateFormat("dd/MM/yyyy").parse(Fecha2);
                    Date date =new SimpleDateFormat("dd/MM/yyyy").parse( cursor.getString(cursor.getColumnIndex(KEY_PAREADOS_MODIFICACION)));
                    if(date.compareTo(dateI) > 0 && date.compareTo(dateF) < 0)
                    {
                        PareadosFecha newPareadosFecha = new PareadosFecha();
                        newPareadosFecha.id_pf = cursor.getInt(cursor.getColumnIndex(KEY_PAREADOS_ID));
                        newPareadosFecha.tag_pf = cursor.getString(cursor.getColumnIndex(KEY_PAREADOS_PAR));
                        newPareadosFecha.item_pf = cursor.getString(cursor.getColumnIndex(KEY_ITEM_DESC));
                        newPareadosFecha.fecha_pf = cursor.getString(cursor.getColumnIndex(KEY_PAREADOS_MODIFICACION));
                        if(cursor.getInt(cursor.getColumnIndex(KEY_PAREADOS_POS)) == 1)
                        {
                            newPareadosFecha.esSalida = "Salida";
                        }
                        else {
                            newPareadosFecha.esSalida = "En Stock";
                        }


                        parsf.add(newPareadosFecha);
                    }

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            ;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return parsf;
    }
    @SuppressLint("Range")
    public Pareados getPareados(String tag) {
        List<Pareados> Pareados = getAllPareados();

        for (Pareados par : Pareados) {
            String desc  = par.tag_par.toString();
            if(desc.equals(tag)){
                return par;
            }
        }
        return new Pareados();
    }

    @SuppressLint("Range")
    public List<SecurityQuestions> getAllSecurityQuestions() {
        List<SecurityQuestions> questions = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_SECURITY_QUESTIONS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    SecurityQuestions newSecurityQuestions = new SecurityQuestions();
                    newSecurityQuestions.id_sec_que = cursor.getInt(cursor.getColumnIndex(KEY_SECURITY_QUESTIONS_ID));
                    newSecurityQuestions.desc_question = cursor.getString(cursor.getColumnIndex(KEY_SECURITY_QUESTIONS_DESC));

                    questions.add(newSecurityQuestions);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            ;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return questions;
    }

    @SuppressLint("Range")
    public SecurityQuestions getSecurityQuesions(String question) {
        List<SecurityQuestions> SecurityQuestions = getAllSecurityQuestions();

        for (SecurityQuestions sec_que : SecurityQuestions) {
            String desc = sec_que.desc_question.toString();
            if (desc.equals(question)) {
                return sec_que;
            }
        }
        return new SecurityQuestions();
    }

    public SecurityQuestions getSecurityQuesionsForID(int id_question) {
        List<SecurityQuestions> SecurityQuestions = getAllSecurityQuestions();
        for (SecurityQuestions sec_que : SecurityQuestions) {
            int id = sec_que.id_sec_que;
            if (id == id_question) {
                return sec_que;
            }
        }
        return new SecurityQuestions();
    }
}
