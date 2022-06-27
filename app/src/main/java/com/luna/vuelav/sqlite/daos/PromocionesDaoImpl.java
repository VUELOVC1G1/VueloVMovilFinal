package com.luna.vuelav.sqlite.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.luna.vuelav.sqlite.DataBaseHelper;
import com.luna.vuelav.sqlite.contracts.PromocionContract;
import com.luna.vuelav.sqlite.models.PromocionM;

import java.util.ArrayList;
import java.util.List;

public class PromocionesDaoImpl implements IEntityDao<PromocionM, Long> {

    private final SQLiteDatabase db;

    public PromocionesDaoImpl(Context context) {
        DataBaseHelper helper = new DataBaseHelper(context);
        db = helper.getWritableDatabase();
    }

    @Override
    public boolean save(PromocionM promocion) {
        ContentValues values = new ContentValues();
        values.put(PromocionContract._ID, promocion.getId());
        values.put(PromocionContract.COLUMN_NAME_DESCRIPCION, promocion.getDescripcion());
        values.put(PromocionContract.COLUMN_NAME_DESCUENTO, promocion.getDescuento());
        values.put(PromocionContract.COLUMN_NAME_PRECIO, promocion.getPrecio());
        values.put(PromocionContract.COLUMN_NAME_FECHA_VUELO, promocion.getFechaVuelo());
        values.put(PromocionContract.COLUMN_NAME_RUTA_DESCRIPCION, promocion.getRutaDescripcion());
        values.put(PromocionContract.COLUMN_NAME_ORIGEN, promocion.getOrigen());
        values.put(PromocionContract.COLUMN_NAME_DESTINO, promocion.getDestino());

        long newRowId = db.insert(PromocionContract.TABLE_NAME, null, values);

        return newRowId != -1;
    }

    @Override
    public PromocionM getById(Long id) {
        String[] projection = {
                BaseColumns._ID,
                PromocionContract.COLUMN_NAME_DESCRIPCION,
                PromocionContract.COLUMN_NAME_DESCUENTO,
                PromocionContract.COLUMN_NAME_PRECIO,
                PromocionContract.COLUMN_NAME_FECHA_VUELO,
                PromocionContract.COLUMN_NAME_RUTA_DESCRIPCION,
                PromocionContract.COLUMN_NAME_ORIGEN,
                PromocionContract.COLUMN_NAME_DESTINO
        };

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                PromocionContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String descripcion = null, rutaDescripcion = null,
                origen = null, destino = null, fechaVuelo = null;
        int descuento = 0;
        double precio = .0;
        if (cursor.moveToFirst()) {
            descripcion = cursor.getString(
                    cursor.getColumnIndexOrThrow(PromocionContract.COLUMN_NAME_DESCRIPCION));
            descuento = cursor.getInt(
                    cursor.getColumnIndexOrThrow(PromocionContract.COLUMN_NAME_DESCUENTO));
            precio = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(PromocionContract.COLUMN_NAME_PRECIO));
            fechaVuelo = cursor.getString(
                    cursor.getColumnIndexOrThrow(PromocionContract.COLUMN_NAME_FECHA_VUELO));
            rutaDescripcion = cursor.getString(
                    cursor.getColumnIndexOrThrow(PromocionContract.COLUMN_NAME_RUTA_DESCRIPCION));
            origen = cursor.getString(
                    cursor.getColumnIndexOrThrow(PromocionContract.COLUMN_NAME_ORIGEN));
            destino = cursor.getString(
                    cursor.getColumnIndexOrThrow(PromocionContract.COLUMN_NAME_DESTINO));

        } else {
            cursor.close();
            return null;
        }

        cursor.close();
        PromocionM p = new PromocionM();
        p.setDescripcion(descripcion);
        p.setDescuento(descuento);
        p.setPrecio(precio);
        p.setFechaVuelo(fechaVuelo);
        p.setRutaDescripcion(rutaDescripcion);
        p.setOrigen(origen);
        p.setDestino(destino);
        return p;
    }

    public List<PromocionM> getAll() {
        String[] projection = {
                BaseColumns._ID,
                PromocionContract.COLUMN_NAME_DESCRIPCION,
                PromocionContract.COLUMN_NAME_DESCUENTO,
                PromocionContract.COLUMN_NAME_PRECIO,
                PromocionContract.COLUMN_NAME_FECHA_VUELO,
                PromocionContract.COLUMN_NAME_RUTA_DESCRIPCION,
                PromocionContract.COLUMN_NAME_ORIGEN,
                PromocionContract.COLUMN_NAME_DESTINO
        };


        Cursor cursor = db.query(
                PromocionContract.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<PromocionM> list = new ArrayList<>();
        String descripcion = null, rutaDescripcion = null,
                origen = null, destino = null, fechaVuelo = null;
        int descuento = 0;
        double precio = .0;
        while (cursor.moveToNext()) {
            descripcion = cursor.getString(
                    cursor.getColumnIndexOrThrow(PromocionContract.COLUMN_NAME_DESCRIPCION));
            descuento = cursor.getInt(
                    cursor.getColumnIndexOrThrow(PromocionContract.COLUMN_NAME_DESCUENTO));
            precio = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(PromocionContract.COLUMN_NAME_PRECIO));
            fechaVuelo = cursor.getString(
                    cursor.getColumnIndexOrThrow(PromocionContract.COLUMN_NAME_FECHA_VUELO));
            rutaDescripcion = cursor.getString(
                    cursor.getColumnIndexOrThrow(PromocionContract.COLUMN_NAME_RUTA_DESCRIPCION));
            origen = cursor.getString(
                    cursor.getColumnIndexOrThrow(PromocionContract.COLUMN_NAME_ORIGEN));
            destino = cursor.getString(
                    cursor.getColumnIndexOrThrow(PromocionContract.COLUMN_NAME_DESTINO));
            PromocionM p = new PromocionM();
            p.setDescripcion(descripcion);
            p.setDescuento(descuento);
            p.setPrecio(precio);
            p.setFechaVuelo(fechaVuelo);
            p.setRutaDescripcion(rutaDescripcion);
            p.setOrigen(origen);
            p.setDestino(destino);
            list.add(p);
        }

        cursor.close();

        return list;
    }

    @Override
    public boolean update(PromocionM promocion) {
        return false;
    }

    @Override
    public boolean delete(PromocionM promocion) {
        String selection = PromocionContract._ID + " = ?";
        String[] selectionArgs = {promocion.getId().toString()};
        int deletedRows = db.delete(PromocionContract.TABLE_NAME, selection, selectionArgs);
        return deletedRows > 0;
    }

    public boolean deleteAll() {
        int deletedRows = db.delete(PromocionContract.TABLE_NAME, null, null);
        return deletedRows > 0;
    }
}
