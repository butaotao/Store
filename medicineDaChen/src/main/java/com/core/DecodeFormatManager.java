package com.core;

import com.google.zxing.BarcodeFormat;

import java.util.ArrayList;

/**
 * Created by Burt on 2016/7/7.
 */
public class DecodeFormatManager {
    public static final ArrayList<BarcodeFormat> PRODUCT_FORMATS;
    public static final ArrayList<BarcodeFormat> ONE_D_FORMATS;
    public static final ArrayList<BarcodeFormat> QR_CODE_FORMATS;
    public static final ArrayList<BarcodeFormat> DATA_MATRIX_FORMATS;
    static {
        PRODUCT_FORMATS = new ArrayList<BarcodeFormat>(5);
        PRODUCT_FORMATS.add(BarcodeFormat.UPC_A);
        PRODUCT_FORMATS.add(BarcodeFormat.UPC_E);
        PRODUCT_FORMATS.add(BarcodeFormat.EAN_13);
        PRODUCT_FORMATS.add(BarcodeFormat.EAN_8);
        PRODUCT_FORMATS.add(BarcodeFormat.RSS_14);
        ONE_D_FORMATS = new ArrayList<BarcodeFormat>(PRODUCT_FORMATS.size() + 4);
        ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
        ONE_D_FORMATS.add(BarcodeFormat.CODE_39);
        ONE_D_FORMATS.add(BarcodeFormat.CODE_93);
        ONE_D_FORMATS.add(BarcodeFormat.CODE_128);
        ONE_D_FORMATS.add(BarcodeFormat.ITF);
        QR_CODE_FORMATS = new ArrayList<BarcodeFormat>(1);
        QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE);
        DATA_MATRIX_FORMATS = new ArrayList<BarcodeFormat>(1);
        DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX);
    }
}
