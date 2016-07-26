package com.dachen.medicine.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.util.Log;

import com.dachen.medicine.config.FusionConfig;

/**
 * log类
 * 
 * @author butaotao 2015-6-26 09:05
 * @update @yuankangle 2015-7-1
 */
public class LogUtils {
	private static LogUtils blog;
	// 袁康乐的log
	private static final String YUAN = "@YuanKL@ ";
	// 系统log
	private static final String SYSTEM = "@System@ ";
	// 卜涛涛的log
	private static final String BURT = "@BuTT@ ";
	// 彭健峰的log
	private static final String PENG = "@PengJF@ ";
	// 李彦利的log
	private static final String LI = "@LiYL@ ";
	// 冷国星
	private static final String LENG = "@LengGX@ ";
	// 鲁伟
	private static final String LUWEI = "@LUWEI@";

	public static final String V = "VERBOSE";
	public static final String D = "DEBUG";
	public static final String I = "INFO";
	public static final String W = "WARN";
	public static final String E = "ERROR";
	private FileOutputStream out = null;
	private ConcurrentLinkedQueue<String> queue;

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	public static LogUtils getLogger() {
		if (blog == null) {
			blog = new LogUtils();
		}
		return blog;
	}

	private void log(String name, String level, String content) {
		if (queue == null)
			queue = new ConcurrentLinkedQueue<String>();
		if (V.equals(level) && MedicineApplication.isDebug) {
			Log.v(name, getFunctionName() + "\r\n" + content);
		} else if (D.equals(level) && MedicineApplication.isDebug) {
			Log.d(name, getFunctionName() + "\r\n" + content);
		} else if (I.equals(level) && MedicineApplication.isDebug) {
			Log.i(name, getFunctionName() + "\r\n" + content);
		} else if (W.equals(level) && MedicineApplication.isDebug) {
			Log.w(name, getFunctionName() + "\r\n" + content);
		} else if (E.equals(level) && MedicineApplication.isDebug) {
			Log.e(name, getFunctionName() + "\r\n" + content);
		} else if (MedicineApplication.isDebug) {
			Log.d(name, getFunctionName() + "\r\n" + content);
		}
		/*if (MedicineApplication.isDebug) {
			try {
				out = new FileOutputStream(new File(FusionConfig.LOGPATH), true);
				queue.add(name + " [" + getFunctionName() + "]\r\n[" + content
						+ "]\r\n");
				out.write(queue.poll().getBytes("UTF-8"));// 监听线程，不断从queue中读数据写入到文件中
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}*/

	}

	private static int countDuplicates(StackTraceElement[] currentStack,
			StackTraceElement[] parentStack) {
		int duplicates = 0;
		int parentIndex = parentStack.length;
		for (int i = currentStack.length; --i >= 0 && --parentIndex >= 0;) {
			StackTraceElement parentFrame = parentStack[parentIndex];
			if (parentFrame.equals(currentStack[i])) {
				duplicates++;
			} else {
				break;
			}
		}
		return duplicates;
	}

	public static void systemLog(Object content) {
		try {
			if (content instanceof Throwable)
				getLogger()
						.log(SYSTEM, E, printStackTrace((Throwable) content));
			else
				getLogger().log(SYSTEM, E, content + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String printStackTrace(Throwable ex) throws Exception {
		final StringBuilder sb = new StringBuilder();
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		return sb.toString();
	}

	/**
	 * Purpose:Mark user one
	 * 
	 * @return
	 */
	public static void burtLog(String level, String content) {
		getLogger().log(BURT, level, content);
	}

	public static void burtLog(String content) {
		burtLog(E, content);
	}

	/**
	 * Purpose:Mark user two
	 * 
	 * @return
	 */
	public static void yuanLog(String level, String content) {
		getLogger().log(YUAN, level, content);
	}

	public static void yuanLog(String content) {
		yuanLog(D, content);
	}

	public static void pengLog(String level, String content) {
		getLogger().log(PENG, level, content);
	}

	public static void pengLog(String content) {
		pengLog(D, content);
	}

	public static void liLog(String level, String content) {
		getLogger().log(LI, level, content);
	}

	public static void liLog(String content) {
		liLog(D, content);
	}

	public static void lengLog(String level, String content) {
		getLogger().log(LENG, level, content);
	}

	public static void luWeiLog(String level, String content) {
		getLogger().log(LUWEI, level, content);
	}

	/**
	 * Get The Current Function Name
	 * 
	 * @return
	 */
	private String getFunctionName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null) {
			return null;
		}
		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(this.getClass().getName())) {
				continue;
			}
			return TimeUtils.getTime() + "[ "
					+ Thread.currentThread().getName() + ": "
					+ st.getClassName() + ":" + st.getLineNumber() + " "
					+ st.getMethodName() + " ]";
		}
		return null;
	}

	/**
	 * 初始化往SD卡记入log的类
	 * 
	 * @author butaotao 增加方法 2015-6-26 09:05
	 */
	// public static void initLogConfig(){
	// //删除原来生成的log
	// File fileUpload = new File(FusionConfig.LOGPATH);
	// if (fileUpload.exists()) {
	// fileUpload.delete();
	// }
	// LogConfigurator logConfigurator = new LogConfigurator();
	// logConfigurator.setFileName(FusionConfig.LOGPATH);
	// logConfigurator.setRootLevel(Level.DEBUG);
	// logConfigurator.setLevel("org.apache", Level.ERROR);
	// logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
	// logConfigurator.setMaxFileSize(1024 * 1024 * 5);
	// logConfigurator.setImmediateFlush(true);
	// logConfigurator.configure();
	// logRecordToSDcard = Logger.getLogger(AllApplication.class);
	// }

	// 获取输出日志所在代码的类名方法名和行数
	private static String getStackInfo() {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		int stacksLen = stacks.length;
		for (int i = 0; i < stacksLen; i++) {
			if (stacks[i].getClassName().equals(Log.class.getName())
					&& !stacks[i].getMethodName().equals("getStackInfo")) {
				sb.append("类名: ").append(stacks[i + 1].getClassName())
						.append("; 方法: ").append(stacks[i + 1].getMethodName())
						.append("; 行数: ").append(stacks[i + 1].getLineNumber());
				break;
			}
		}
		return sb.toString();
	}

	class DealFile implements Runnable {
		private FileOutputStream out;
		private ConcurrentLinkedQueue<String> queue;

		public DealFile() {
		}

		public DealFile(FileOutputStream out,
				ConcurrentLinkedQueue<String> queue) {
			this.out = out;
			this.queue = queue;
		}

		public void run() {
			while (true) {// 循环监听
				if (!queue.isEmpty()) {
					try {
						out.write(queue.poll().getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getAbsoluteTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		return sdf.format(new Date());
	}

}
