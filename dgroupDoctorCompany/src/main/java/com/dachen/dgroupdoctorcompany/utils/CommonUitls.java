package com.dachen.dgroupdoctorcompany.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import com.dachen.dgroupdoctorcompany.R;
import com.dachen.dgroupdoctorcompany.db.dbdao.CompanyContactDao;
import com.dachen.dgroupdoctorcompany.db.dbdao.DoctorDao;
import com.dachen.dgroupdoctorcompany.entity.BaseSearch;
import com.dachen.dgroupdoctorcompany.entity.CompanyContactListEntity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Burt on 2016/3/2.
 */
public class CommonUitls {
    public static  List<BaseSearch> listsHorizon;
    public static List<BaseSearch> getListsHorizon(){
        if (null==listsHorizon){
            listsHorizon = new ArrayList<>();
        }
        return  listsHorizon;
    }
    public static void addCompanyContactListEntity(CompanyContactListEntity entity){

        if (!getListsHorizon().contains(entity)){
            getListsHorizon().add(entity);
        }
    }
    /**
     * 添加所选择中的联系人，而且是去重复的
     */
    public static List<String>  list  ;
    public static void addSelectCreateGroup(String id) {
       //  String key = "SelectCreateGroup";
       // List<String> set = CacheManager.readObject(key);
        if (list == null) {
            list = new ArrayList<String>();
        }

        if (list.contains(id)) {
            list.remove(id);
        } else {
            list.add(id);
        }

       // CacheManager.writeObject(set, key);*/
    }
    /**
     * 得到所选择中的联系人，而且是去重复的
     */
    public static List<String> getSelectCreateGroup() {
        if (null==list){
            list = new ArrayList<>();
        }

        return list;
    }
    public static DisplayImageOptions getOptions(int resId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(resId)
                .showImageOnFail(resId).displayer(new RoundedBitmapDisplayer(10)).cacheInMemory(true).cacheOnDisc(true)
                .build();
        return options;
    }

    public static DisplayImageOptions getHeadOptions() {
        return getOptions(R.drawable.ic_default_head);
    }
    public static boolean isExitUser(Context context,String userId){
        DoctorDao dao = new DoctorDao(context);
        CompanyContactDao entityDao = new CompanyContactDao(context);
        if (null!=dao.queryByUserId(userId)&&dao.queryByUserId(userId).size()>0){
            return true;
        }else if(null!=entityDao.queryByUserId(userId)&&entityDao.queryByUserId(userId).size()>0){
            return true;
        }

        return false;
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            byte[] cert = info.signatures[0].toByteArray();

            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isFastDoubleClick(long lastClickTime){
        long time = System.currentTimeMillis();
        long timeD= time-lastClickTime;
        if(0 < timeD && timeD < 800){
            return true;
        }
        return false;
    }

    /**
     * dp转pixel
     */
    public static float dpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    /**
     * pixel转dp
     */
    public static float pixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

    /**
     * 根据图片的url路径获得Bitmap对象
     * @param url
     * @return
     */
    public static Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap){
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        final int color =0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPX = bitmap.getWidth()/2;
        paint.setAntiAlias(true);
        canvas.drawARGB(0,0,0,0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return outBitmap;
    }
}
