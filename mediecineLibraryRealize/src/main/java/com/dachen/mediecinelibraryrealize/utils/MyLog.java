package com.dachen.mediecinelibraryrealize.utils;

public class MyLog {
	private static boolean flag = true;

	public static void log(String msg) {
		if (flag) {

			System.out.println("========" + msg);
		}
	}
}
