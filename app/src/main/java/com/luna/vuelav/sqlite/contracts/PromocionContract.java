package com.luna.vuelav.sqlite.contracts;

import android.provider.BaseColumns;

public class PromocionContract implements BaseColumns {
    public static final String TABLE_NAME = "promociones";
    public static final String COLUMN_NAME_DESCRIPCION = "descripcion";
    public static final String COLUMN_NAME_DESCUENTO = "descuento";
    public static final String COLUMN_NAME_PRECIO = "precio";
    public static final String COLUMN_NAME_FECHA_VUELO = "fecha_vuelo";
    public static final String COLUMN_NAME_RUTA_DESCRIPCION = "ruta_descripcion";
    public static final String COLUMN_NAME_ORIGEN = "origen";
    public static final String COLUMN_NAME_DESTINO = "destino";
}
