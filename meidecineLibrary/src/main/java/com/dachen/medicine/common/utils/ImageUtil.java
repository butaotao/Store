package com.dachen.medicine.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.*;
import android.text.TextUtils;
import android.util.Log;

import com.dachen.medicine.net.NetConfig;

public class ImageUtil {

	private static BitmapCache mCache;

	public static final String SDCARD_CACHE_IMG_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/touna/images/";
	public static final String SDCARD_CACHE_DOWNLOAD_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/touna/Download/";

	public static String getImgCacheUrl() {

		return SDCARD_CACHE_IMG_PATH;
	}

	public static String getDownloadUrl() {
		return SDCARD_CACHE_DOWNLOAD_PATH;
	}

	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		if (bm == null) {
			return null;
		}
		bm.compress(CompressFormat.PNG, 100, bos);
		return bos.toByteArray();
	}

	/**
	 * 获取本地图片
	 * 
	 * @param imagePath
	 * @return
	 */
	public static Bitmap getImage(String imagePath) {
		File file = new File(imagePath);
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			// Log.i("ImageUtil",
			// "文件�?���?��修改时间�?+file.lastModified()+"当前时间:"+System.currentTimeMillis());
			return bitmap;
		}
		return null;
	}

	public static Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读�?不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inPreferredConfig = Config.RGB_565;// 该模式是默认�?可不�?
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时�?图片自动被回�?
		newOpts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	/**
	 * 保存图片到本�?
	 * 
	 * @param imgUrl
	 * @param buffer
	 * @throws IOException
	 */
	public static void saveImage(String imgUrl, byte[] buffer)
			throws IOException {
		File file = new File(imgUrl);
		if (file.exists()) {
			return;
		} else {
			File f = file.getParentFile();
			if (!f.exists()) {
				f.mkdirs();
			}
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(imgUrl);
			fos.write(buffer);
			fos.flush();
			fos.close();
		}
	}

	/**
	 * 从本地或者服务器加载图片
	 * 
	 * @param imgUrl
	 * @param imagePath
	 * @param callback
	 * @return
	 */
	public static Bitmap loadImage(final String imgUrl, final String imagePath,
			final ImageCallback callback, final Context context) {

		if (mCache == null) {
			mCache = BitmapCache.getInstance();
		}

		if (imgUrl == null || imagePath == null || imgUrl.length() < 1) {
			return null;
		}
		Bitmap bitmap = mCache.getBitmapFromSD(imagePath, context);
		if (bitmap != null) {
			return bitmap;
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					Bitmap bitmap = (Bitmap) msg.obj;

					if (callback != null) {
						callback.loadImage(bitmap, imagePath);
					}
				}
			}
		};

		// synchronized ("aa") {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String imgUrla = imgUrl.replaceAll(" ", "%20");
					Log.i("ImageUtil", imgUrla);
					URL url = new URL(imgUrla);
					URLConnection conn = url.openConnection();
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					conn.connect();
					Bitmap bitmap = BitmapFactory
							.decodeStream(new BufferedInputStream(conn
									.getInputStream()));

					// byte[] bt = bitmap2Bytes(bitmap);
					if (imagePath != null && bitmap != null
							&& !bitmap.isRecycled()) {
						// saveImage(imagePath, bt);
						File file = new File(imagePath);
						if (!file.exists()) {
							file.createNewFile();
						}
						FileOutputStream fos = new FileOutputStream(imagePath);
						bitmap.compress(CompressFormat.PNG, 100, fos);

						Message msg = new Message();
						msg.obj = bitmap;
						handler.sendMessage(msg);
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolManager.getInstance().addTask(runnable);
		// }

		return null;
	}

	public static String md5(String str) {
		try {
			if (str == null) {
				str = "123";
			}
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(str.getBytes());
			return byteToString(localMessageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			return str;
		}
	}

	/***
	 * 将b数组转换成十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToString(byte[] b) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xff);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			str.append(hex.toUpperCase());
		}
		try {
			FileInputStream is = new FileInputStream("");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String infor;
			while ((infor = br.readLine()) != null) {
				System.out.print(infor + "---");
				infor = br.readLine();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return str.toString();
	}

	public interface ImageCallback {
		public void loadImage(Bitmap bitmap, String imagePath);
	}

	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		options.inJustDecodeBounds = true;
		// BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 400, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static Bitmap toRoundCorner(Bitmap bitmap) {
		// Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
		// bitmap.getHeight(), Config.ARGB_8888);
		// Canvas canvas = new Canvas(output);
		// Paint paint = new Paint();
		// paint.setAntiAlias(true);
		// Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		// RectF rectF = new RectF(rect);
		// canvas.drawOval(rectF, paint);
		// paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas.drawBitmap(bitmap, rect, rectF, paint);
		// bitmap = null;
		// getCirclePhoto(bitmap);
		if (bitmap != null && !bitmap.isRecycled()) {
			return getCirclePhoto(bitmap);
		}
		return bitmap;
	}

	public static Bitmap getCirclePhoto(Bitmap sourceBitmap) {
		int width = sourceBitmap.getWidth();
		int height = sourceBitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = (height - width) / 2;
			bottom = top + width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sourceBitmap, src, dst, paint);

		if (sourceBitmap != null && !sourceBitmap.isRecycled()
				&& sourceBitmap != output) {
			sourceBitmap.recycle();
		}

		// Paint paint1 = new Paint();
		// paint1.setAntiAlias(true);
		// paint1.setColor(0xffff771f);
		// paint1.setStyle(Style.STROKE);
		// paint1.setStrokeWidth(1);
		//
		// canvas.drawCircle(dst_right / 2, dst_bottom / 2, roundPx - 1,
		// paint1);

		return output;
	}

	public static int readPictureDegree(String filePath) {
		int degree = 0;
		try {
			ExifInterface exi = new ExifInterface(filePath);
			int orientation = exi.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return degree;
	}

	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩�?
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图�?
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

	/**
	 * 压缩Bitmap
	 * 
	 * @param image
	 *            图片�?
	 * @param size
	 *            压缩到多少K
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image, int size) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.JPEG, 70, baos);// 质量压缩方法，这�?00表示不压缩，把压缩后的数据存放到baos�?
		if (baos.toByteArray().length / 1024 > 100
				&& baos.toByteArray().length / 1024 < 500) {
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.JPEG, 60, baos);// 这里压缩options%，把压缩后的数据存放到baos�?
		}
		if (baos.toByteArray().length / 1024 > 500
				&& baos.toByteArray().length / 1024 < 1024) {
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.JPEG, 50, baos);// 这里压缩options%，把压缩后的数据存放到baos�?
		}
		if (baos.toByteArray().length / 1024 > 1024) {
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.JPEG, 40, baos);// 这里压缩options%，把压缩后的数据存放到baos�?
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream�?
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		try {
			isBm.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			isBm.reset();

		}
		return bitmap;
	}

	public static Bitmap revitionImageSize(InputStream is, int size)
			throws IOException {
		// 取得图片
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 这个参数代表，不为bitmap分配内存空间，只记录�?��该图片的信息（例如图片大小），说白了就是为了内存优化
		options.inJustDecodeBounds = true;
		// 通过创建图片的方式，取得options的内容（这里就是利用了java的地�?��递来赋�?�?
		BitmapFactory.decodeStream(is, null, options);
		// 关闭�?
		is.close();

		// 生成压缩的图�?
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			// 这一步是根据要设置的大小，使宽和高都能满�?
			if ((options.outWidth >> i <= size)
					&& (options.outHeight >> i <= size)) {
				// 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
				// 这个参数表示 新生成的图片为原始图片的几分之一�?
				options.inSampleSize = (int) Math.pow(2.0D, i);
				// 这里之前设置为了true，所以要改为false，否则就创建不出图片
				options.inJustDecodeBounds = false;

				bitmap = BitmapFactory.decodeStream(is, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	public static boolean saveImage(Bitmap photo, String filename)
			throws Exception {
		CompressFormat format = CompressFormat.JPEG;
		int quality = 100;
		if (photo != null) {
			int fileCount = getBitmapSize(photo);
			if (fileCount > 1024 * 500)
				quality = 80;
			if (fileCount > 1024 * 1024)
				quality = 70;
			if (fileCount > 2 * 1024 * 1024)
				quality = 60;
			if (fileCount > 4 * 1024 * 1024)
				quality = 50;
			if (fileCount > 7 * 1024 * 1024)
				quality = 40;
			if (fileCount > 10 * 1024 * 1024)
				quality = 30;
			if (fileCount > 20 * 1024 * 1024)
				quality = 20;
			if (fileCount > 30 * 1024 * 1024)
				quality = 10;
			Log.d("touna", "quality---> " + quality + "\nfileCount--> "
					+ fileCount);
		} else
			return false;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean temp = photo.compress(format, quality, stream);
		File file = new File(filename);
		if (file.exists() && file.isFile()) {
			Log.d("touna", "filesize--> " + file.length());
		}
		// if(photo != null && !photo.isRecycled()){
		// photo.recycle();
		// photo = null;
		// }
		return temp;
	}

	@SuppressLint("NewApi")
	private static int getBitmapSize(Bitmap bitmap) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // API 19
			return bitmap.getAllocationByteCount();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {// API
																			// 12
			return bitmap.getByteCount();
		}
		return bitmap.getRowBytes() * bitmap.getHeight(); // earlier version
	}
	public static String getUrl(String url){
		if (!android.text.TextUtils.isEmpty(url)&&url.contains(NetConfig.MEDIELWEBFILES)){
			return  "";
		}
		return "/"+NetConfig.MEDIELWEBFILES+"/";
	}
	public static String getUrl(String url,String ip,String session,int net){
		if (!TextUtils.isEmpty(url)&&(url.contains("http://")||url.contains("https://"))){
			return  url;
		}
		if (net == 200){
			return ip+":"+ NetConfig.PORTMEDIE+url;
		}
		return getEncodeUrl(ip+ImageUtil.getUrl(url)+url+"?a="+session);
	}
	public static String getEncodeUrl(String url){
		String s = "";
		try {
		if (url.contains("/")) {
			int t = url.lastIndexOf("/");
			String s1 = url.substring(0, t+1);
			String s2 = url.substring( t+1);
		
			String m =URLEncoder.encode(s2, "utf-8");
			if (m.contains("%3Fa%3D")) {
				m = m.replace("%3Fa%3D", "?a=");
			}
			s = s1+m;
			return s;
		}
	
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return url;
	}

}
