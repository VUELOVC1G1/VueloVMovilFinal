package com.luna.vuelav.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.luna.vuelav.sqlite.contracts.PromocionContract;
import com.luna.vuelav.sqlite.contracts.UsuarioContract;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "vuelav.db";

    public static final String SQL_CREATE_USUARIOS =
            "CREATE TABLE " + UsuarioContract.TABLE_NAME + " (" +
                    UsuarioContract._ID + " INTEGER PRIMARY KEY, " +
                    UsuarioContract.COLUMN_NAME_USERNAME + " TEXT NOT NULL, " +
                    UsuarioContract.COLUMN_NAME_PASSWORD + " TEXT NOT NULL)";

    public static final String SQL_CREATE_PROMOCIONES =
            "CREATE TABLE " + PromocionContract.TABLE_NAME + " (" +
                    PromocionContract._ID + " INTEGER PRIMARY KEY, " +
                    PromocionContract.COLUMN_NAME_DESCRIPCION + " TEXT NOT NULL, " +
                    PromocionContract.COLUMN_NAME_DESCUENTO + " INTEGER NOT NULL, " +
                    PromocionContract.COLUMN_NAME_PRECIO + " REAL NOT NULL, " +
                    PromocionContract.COLUMN_NAME_FECHA_VUELO + " TEXT NOT NULL, " +
                    PromocionContract.COLUMN_NAME_RUTA_DESCRIPCION + " TEXT NOT NULL, " +
                    PromocionContract.COLUMN_NAME_ORIGEN + " TEXT NOT NULL, " +
                    PromocionContract.COLUMN_NAME_DESTINO + " TEXT NOT NULL)";


    public DataBaseHelper(@Nullable Context context) {
        super(context, "vuelav.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USUARIOS);
        db.execSQL(SQL_CREATE_PROMOCIONES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
