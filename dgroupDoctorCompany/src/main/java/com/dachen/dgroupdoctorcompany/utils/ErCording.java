package com.dachen.dgroupdoctorcompany.utils;

/**
 * Created by Burt on 2016/2/19.
 */

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;


public class ErCording {
    private static int          mQRWidth  = 400;    //二维码的宽度
    private static int          mQRHeight = 400;    //二维码的高度
    private static int          mBmWidth;           //bitmap的像素点宽度
    /**
     * 生成二维码的方法2，传入一个地址就行
     * @param str 生成二维码的链接
     * @return Bitmap 生成二维码的bitmap (相对上面那个方法，这个方法去掉了二维码四边的白色边框)
     */
    public static Bitmap cretaeBitmap(String str) throws WriterException {
        //判断URL合法性
        if (str == null || "".equals(str) || str.length() < 1) {
            return null;
        }
        ErrorCorrectionLevel errorlevel = ErrorCorrectionLevel.H;
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, errorlevel);
        hints.put(EncodeHintType.MARGIN, 0);
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败

        //如果要指定二维码的边框以及容错率，最好给encode方法增加一个参数：hints 一个Hashmap
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, mQRWidth, mQRHeight, hints);
        matrix = updateBit(matrix, 0);
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        //LogUtils.getLogger().lengLog(LogUtils.D, "width= " + width + " height= " + height);
        mBmWidth = width;

        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000; //黑色
                    // pixels[y * width + x] = 0xff0D986C;   //绿色
                } else {
                    //pixels[y * width + x] = 0xffffffff; //白色
                    pixels[y * width + x] = 0xffFDFDFD;   //浅白色
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;

    }

    private static BitMatrix updateBit(BitMatrix matrix, int margin){
        int tempM = margin*2;
        int[] rec = matrix.getEnclosingRectangle();   //获取二维码图案的属性
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
        resMatrix.clear();
        for(int i= margin; i < resWidth- margin; i++){   //循环，将二维码图案绘制到新的bitMatrix中
            for(int j=margin; j < resHeight-margin; j++){
                if(matrix.get(i-margin + rec[0], j-margin + rec[1])){
                    resMatrix.set(i,j);
                }
            }
        }
        return resMatrix;
    }
}
