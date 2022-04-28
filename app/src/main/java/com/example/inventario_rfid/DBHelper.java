package com.example.inventario_rfid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    //Singleton
    private static DBHelper sInstance;

    // Dase de Batos
    private static final String DATABASE_NAME = "DB";
    private static final int DATABASE_VERSION = 1;

    // Nombre Tablas
    private static final String TABLE_PROFILES = "perfil";
    private static final String TABLE_USERS = "usuario";

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
    private static final String KEY_USER_PROFILE_ID = "id_per";

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
                KEY_USER_USER + " INTEGER," +
                KEY_USER_PASS + " INTEGER," +
                KEY_USER_NAME + " INTEGER," +
                KEY_USER_SURNAME_FATHER + " INTEGER," +
                KEY_USER_SURNAME_MOTHER + " INTEGER," +
                KEY_USER_RUT + " INTEGER," +
                KEY_USER_DV + " INTEGER," +
                KEY_USER_PROFILE_ID + " INTEGER REFERENCES " + TABLE_PROFILES + // Define a foreign key
                ")";

        DB.execSQL(CREATE_PROFILE_TABLE);
        DB.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
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
            values.put(KEY_USER_PROFILE_ID,     user.id_per);


            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_USERS, values, KEY_USER_USER + "= ?", new String[]{user.user});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String userSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID, TABLE_USERS, KEY_USER_USER);
                Cursor cursor = db.rawQuery(userSelectQuery, new String[]{String.valueOf(user.id_user)});
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

}
