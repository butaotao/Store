package com.dachen.medicine.common.utils;

import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

public class BitmapCache {
	static private BitmapCache cache;
	/** 用于Chche内容的存�? */
	private Hashtable<String, MySoftRef> hashRefs;
	/** 垃圾Reference的队列（�?��用的对象已经被回收，则将该引用存入队列中�? */
	private ReferenceQueue<Bitmap> q;

	private Context mContext;

	/**
	 * 继承SoftReference，使得每�? 个实例都具有可识别的标识�?
	 */
	private class MySoftRef extends SoftReference<Bitmap> {
		private String _key = "0";

		public MySoftRef(Bitmap bmp, ReferenceQueue<Bitmap> q, String key) {
			super(bmp, q);
			_key = key;
		}
	}

	private BitmapCache() {
		hashRefs = new Hashtable<String, MySoftRef>();
		q = new ReferenceQueue<Bitmap>();
	}

	/**
	 * 取得缓存器实�?
	 */
	public static BitmapCache getInstance() {
		if (cache == null) {
			cache = new BitmapCache();
		}
		return cache;
	}

	/**
	 * 以软引用的方式对�?��Bitmap对象的实例进行引用并保存该引�?
	 */
	private void addCacheBitmap(Bitmap bmp, String key) {
		cleanCache();// 清除垃圾引用
		MySoftRef ref = new MySoftRef(bmp, q, key);
		hashRefs.put(key, ref);
	}

	/**
	 * 根据自己的需要从网络或本地path下获取，重新获取相应Bitmap对象的实�?
	 */
	public Bitmap getBitmap(String imagePath, Context context) {
		mContext = context;
		Bitmap bmp = null;
		// 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得�?
		if (hashRefs.size() > 20) {
			cleanCache();
		}
		if (hashRefs.containsKey(imagePath)) {
			MySoftRef ref = (MySoftRef) hashRefs.get(imagePath);
			bmp = (Bitmap) ref.get();
		}
		// 如果没有软引用，或�?从软引用中得到的实例是null，重新构建一个实例，
		// 并保存对这个新建实例的软引用
		if (bmp == null) {
			// 传说decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode�?
			// 无需再使用java层的createBitmap，从而节省了java层的空间�?
			bmp = compressImageFromFile(imagePath);
			if (bmp != null) {
				this.addCacheBitmap(bmp, imagePath);
			}
		}
		return bmp;
	}

	/**
	 * 根据自己的需要从网络或本地path下获取，重新获取相应Bitmap对象的实�?
	 */
	public Bitmap getBitmapFromSD(String imagePath, Context context) {
		mContext = context;
		Bitmap bmp = null;
		// 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得�?
		if (hashRefs.containsKey(imagePath)) {
			// MySoftRef ref = (MySoftRef) hashRefs.get(imagePath);
			// bmp = (Bitmap) ref.get();
		}
		// 如果没有软引用，或�?从软引用中得到的实例是null，重新构建一个实例，
		// 并保存对这个新建实例的软引用
		if (bmp == null) {
			// 传说decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode�?
			// 无需再使用java层的createBitmap，从而节省了java层的空间�?
			bmp = compressImageFromSDFile(imagePath);
			if (bmp != null) {
				this.addCacheBitmap(bmp, imagePath);
			}
		}
		return bmp;
	}

	private Bitmap compressImageFromSDFile(String imagePath) {
		try {
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inJustDecodeBounds = true;// 只读�?不读内容
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath, newOpts);

			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			float hh = 800f;//
			float ww = 480f;//
			int be = 1;
			if (w > h && w > ww) {
				be = (int) (newOpts.outWidth / ww);
			} else if (w < h && h > hh) {
				be = (int) (newOpts.outHeight / hh);
			}
			if (be <= 0)
				be = 1;
			// newOpts.inSampleSize = 2;// 设置采样�?

			newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认�?可不�?
			newOpts.inPurgeable = true;// 同时设置才会有效
			newOpts.inInputShareable = true;// 。当系统内存不够时�?图片自动被回�?

			bitmap = BitmapFactory.decodeFile(imagePath, newOpts);
			// InputStream is = mContext.getAssets().open(srcPath);
			// bitmap = BitmapFactory.decodeStream(is);
			// return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
			// 其实是无效的,大家尽管尝试
			return bitmap;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	private void cleanCache() {
		MySoftRef ref = null;
		while ((ref = (MySoftRef) q.poll()) != null) {
			hashRefs.remove(ref._key);
		}
	}

	public void clearGifCache(String key) {
		if (hashRefs.containsKey(key)) {
			hashRefs.remove(key);
		}
	}

	/**
	 * 清除Cache内的全部内容
	 */
	public void clearCache() {
		cleanCache();
		hashRefs.clear();
		System.gc();
		System.runFinalization();
	}

	public Bitmap compressImageFromFile(String srcPath) {
		try {
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inJustDecodeBounds = true;// 只读�?不读内容
			Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			float hh = 800f;//
			float ww = 480f;//
			int be = 1;
			if (w > h && w > ww) {
				be = (int) (newOpts.outWidth / ww);
			} else if (w < h && h > hh) {
				be = (int) (newOpts.outHeight / hh);
			}
			if (be <= 0)
				be = 1;
			newOpts.inSampleSize = be;// 设置采样�?

			newOpts.inPreferredConfig = Config.ARGB_4444;// 该模式是默认�?可不�?
			newOpts.inPurgeable = true;// 同时设置才会有效
			newOpts.inInputShareable = true;// 。当系统内存不够时�?图片自动被回�?

			// bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
			InputStream is = mContext.getAssets().open(srcPath);
			bitmap = BitmapFactory.decodeStream(is);
			// return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
			// 其实是无效的,大家尽管尝试
			return bitmap;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;
		// BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		final int height = options.outHeight;
		final int width = options.outWidth;

		if (height > 800 || width > 480) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / 800f);
			final int widthRatio = Math.round((float) width / 480f);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			options.inSampleSize = heightRatio < widthRatio ? heightRatio
					: widthRatio;
		}

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
}
