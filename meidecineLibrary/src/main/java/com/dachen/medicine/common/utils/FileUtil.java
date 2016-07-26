package com.dachen.medicine.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import android.os.Environment;
import android.text.TextUtils;

import com.dachen.medicine.entity.User;

public class FileUtil {

	/*
	 * private static final int TYPE_IMAGE = 1; private static final int
	 * TYPE_ADUIO = 2; private static final int TYPE_VIDEO = 3;
	 *//**
	 * {@link #TYPE_IMAGE}<br/>
	 * {@link #TYPE_ADUIO}<br/>
	 * {@link #TYPE_VIDEO} <br/>
	 * 
	 * @param type
	 * @return
	 */
	/*
	 * private static String getPublicFilePath(int type) { String fileDir =
	 * null; String fileSuffix = null; switch (type) { case TYPE_ADUIO: fileDir
	 * = DApplication.getUniqueInstance().mVoicesDir; fileSuffix = ".mp3";
	 * break; case TYPE_VIDEO: fileDir =
	 * DApplication.getUniqueInstance().mVideosDir; fileSuffix = ".mp4"; break;
	 * case TYPE_IMAGE: fileDir = DApplication.getUniqueInstance().mPicturesDir;
	 * fileSuffix = ".jpg"; break; } if (fileDir == null) { return null; } File
	 * file = new File(fileDir); if (!file.exists()) { if (!file.mkdirs()) {
	 * return null; } } return fileDir + File.separator +
	 * UUID.randomUUID().toString().replaceAll("-", "") + fileSuffix; }
	 *//**
	 * {@link #TYPE_ADUIO}<br/>
	 * {@link #TYPE_VIDEO} <br/>
	 * 
	 * @param type
	 * @return
	 */
	/*
	 * private static String getPrivateFilePath(int type, String userId) {
	 * String fileDir = null; String fileSuffix = null; switch (type) { case
	 * TYPE_ADUIO: fileDir = DApplication.getUniqueInstance().mAppDir +
	 * File.separator + userId + File.separator + Environment.DIRECTORY_MUSIC;
	 * fileSuffix = ".mp3"; break; case TYPE_VIDEO: fileDir =
	 * DApplication.getUniqueInstance().mAppDir + File.separator + userId +
	 * File.separator + Environment.DIRECTORY_MOVIES; fileSuffix = ".mp4";
	 * break; } if (fileDir == null) { return null; } File file = new
	 * File(fileDir); if (!file.exists()) { if (!file.mkdirs()) { return null; }
	 * } return fileDir + File.separator +
	 * UUID.randomUUID().toString().replaceAll("-", "") + fileSuffix; }
	 * 
	 * public static String getRandomImageFilePath() { return
	 * getPublicFilePath(TYPE_IMAGE); }
	 * 
	 * public static String getRandomAudioFilePath() { User user =
	 * DApplication.getUniqueInstance().mLoginUser; if (user != null &&
	 * !TextUtils.isEmpty(user.getUserId())) { return
	 * getPrivateFilePath(TYPE_ADUIO, user.getUserId()); } else { return
	 * getPublicFilePath(TYPE_ADUIO); } }
	 * 
	 * public static String getRandomAudioAmrFilePath() { User user =
	 * DApplication.getUniqueInstance().mLoginUser; String filePath = null; if
	 * (user != null && !TextUtils.isEmpty(user.getUserId())) { filePath =
	 * getPrivateFilePath(TYPE_ADUIO, user.getUserId()); } else { filePath =
	 * getPublicFilePath(TYPE_ADUIO); } if (!TextUtils.isEmpty(filePath)) {
	 * return filePath.replace(".mp3", ".amr"); } else { return null; } }
	 * 
	 * public static String getRandomVideoFilePath() { User user =
	 * DApplication.getUniqueInstance().mLoginUser; if (user != null &&
	 * !TextUtils.isEmpty(user.getUserId())) { return
	 * getPrivateFilePath(TYPE_VIDEO, user.getUserId()); } else { return
	 * getPublicFilePath(TYPE_VIDEO); } }
	 * 
	 * //
	 * ///////////////////////////////////////////////////////////////////////
	 * ////////////////////////////
	 * 
	 * public static void createFileDir(String fileDir) { File fd = new
	 * File(fileDir); if (!fd.exists()) { fd.mkdirs(); } }
	 *//**
	 * 
	 * @param fullName
	 */
	/*
	 * public static void delFile(String fullName) { File file = new
	 * File(fullName); if (file.exists()) { if (file.isFile()) { try {
	 * file.delete(); } catch (Exception e) { e.printStackTrace(); } } } }
	 *//**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 /sdcard/data/
	 */
	/*
	 * public static void delAllFile(String path) { File file = new File(path);
	 * if (!file.exists()) { return; } if (!file.isDirectory()) { return; }
	 * String[] tempList = file.list(); File temp = null; for (int i = 0; i <
	 * tempList.length; i++) { System.out.println(path + tempList[i]); if
	 * (path.endsWith(File.separator)) { temp = new File(path + tempList[i]); }
	 * else { temp = new File(path + File.separator + tempList[i]); } if
	 * (temp.isFile()) { temp.delete(); } if (temp.isDirectory()) {
	 * delAllFile(path + "/" + tempList[i]); // 先删除文件夹里面的文件 delFolder(path + "/"
	 * + tempList[i]); // 再删除空文件夹 } } }
	 *//**
	 * 删除文件夹
	 * 
	 * @param filePathAndName
	 *            String 文件夹路径及名称 如/sdcard/data/
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	/*
	 * public static void delFolder(String folderPath) { try {
	 * delAllFile(folderPath); // 删除完里面所有内容 String filePath = folderPath;
	 * filePath = filePath.toString(); java.io.File myFilePath = new
	 * java.io.File(filePath); myFilePath.delete(); // 删除空文件夹 } catch (Exception
	 * e) { System.out.println("删除文件夹操作出错"); e.printStackTrace(); } }
	 */
	private static final int TYPE_IMAGE = 1;
	private static final int TYPE_ADUIO = 2;
	private static final int TYPE_VIDEO = 3;

	/**
	 * {@link #TYPE_IMAGE}<br/>
	 * {@link #TYPE_ADUIO}<br/>
	 * {@link #TYPE_VIDEO} <br/>
	 * 
	 * @param type
	 * @return
	 */
	private static String getPublicFilePath(int type) {
		String fileDir = null;
		String fileSuffix = null;
		switch (type) {
		case TYPE_ADUIO:
			fileDir = MedicineApplication.mVoicesDir;
			fileSuffix = ".mp3";
			break;
		case TYPE_VIDEO:
			fileDir = MedicineApplication.mVideosDir;
			fileSuffix = ".mp4";
			break;
		case TYPE_IMAGE:
			fileDir = MedicineApplication.mPicturesDir;
			fileSuffix = ".jpg";
			break;
		}
		if (fileDir == null) {
			return null;
		}
		File file = new File(fileDir);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				return null;
			}
		}
		return fileDir + File.separator + UUID.randomUUID().toString().replaceAll("-", "") + fileSuffix;
	}

	/**
	 * {@link #TYPE_ADUIO}<br/>
	 * {@link #TYPE_VIDEO} <br/>
	 * 
	 * @param type
	 * @return
	 */
	private static String getPrivateFilePath(int type, String userId) {
		String fileDir = null;
		String fileSuffix = null;
		switch (type) {
		case TYPE_ADUIO:
			fileDir = MedicineApplication.mAppDir + File.separator + userId + File.separator + Environment.DIRECTORY_MUSIC;
			fileSuffix = ".mp3";
			break;
		case TYPE_VIDEO:
			fileDir = MedicineApplication.mAppDir + File.separator + userId + File.separator + Environment.DIRECTORY_MOVIES;
			fileSuffix = ".mp4";
			break;
		}
		if (fileDir == null) {
			return null;
		}
		File file = new File(fileDir);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				return null;
			}
		}
		return fileDir + File.separator + UUID.randomUUID().toString().replaceAll("-", "") + fileSuffix;
	}

	public static String getRandomImageFilePath() {
		return getPublicFilePath(TYPE_IMAGE);
	}

	public static String getRandomVideoFilePath() {
		User user = MedicineApplication.mLoginUser;
		if (user != null && !TextUtils.isEmpty(user.getUserId())) {
			return getPrivateFilePath(TYPE_VIDEO, user.getUserId());
		} else {
			return getPublicFilePath(TYPE_VIDEO);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////

	public static void createFileDir(String fileDir) {
		File fd = new File(fileDir);
		if (!fd.exists()) {
			fd.mkdirs();
		}
	}

	/**
	 * 
	 * @param fullName
	 */
	public static void delFile(String fullName) {
		File file = new File(fullName);
		if (file.exists()) {
			if (file.isFile()) {
				try {
					file.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 /sdcard/data/
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			System.out.println(path + tempList[i]);
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]); // 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]); // 再删除空文件夹
			}
		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @param filePathAndName
	 *            String 文件夹路径及名称 如/sdcard/data/
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			System.out.println("删除文件夹操作出错");
			e.printStackTrace();
		}
	}
	 /**
     * 读取SD卡中文本文件
     * 
     * @param fileName
     * @return
     */
    public static String readSDFileTxt(String fileName)
    {
        File file = new File(fileName);
        if (file.exists())
        {
            return readFileData(file);
        }
        return "";
    }
    
    public static String readFileData(String fileName)
    {
        File file = new File(fileName);
        return readFileData(file);
    }
    
    public static String readFileData(File file)
    {
        String charset = "UTF-8";
        byte[] first3Bytes = new byte[3];
        boolean checked = false;
        BufferedInputStream bis = null;
        try
        {
            bis = new BufferedInputStream(new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
            {
                //文件编码为 ANSI
            }
            else if (first3Bytes[0] == -44 && first3Bytes[1] == -38
                    && first3Bytes[2] == 49)
            {
                //gbk
                charset = "GBK";
            }
            else if (first3Bytes[0] == -50 && first3Bytes[1] == -46
                    && first3Bytes[2] == -59)
            {
                //gb2312，gb18030
                charset = "GBK";
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        if ("GBK".equals(charset))
        {
            
            try
            {
                br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file.getPath()), charset));
                String line = "";
                while ((line = br.readLine()) != null)
                {
                    sb.append(line);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (br != null)
                {
                    try
                    {
                        br.close();
                        br = null;
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        else
        {
            try
            {
                br = new BufferedReader(new FileReader(file));
                String line = "";
                while ((line = br.readLine()) != null)
                {
                    sb.append(line);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (br != null)
                {
                    try
                    {
                        br.close();
                        br = null;
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return  sb.toString() ;
    }
}
