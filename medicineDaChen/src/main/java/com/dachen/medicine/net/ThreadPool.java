package com.dachen.medicine.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @描述： @线程池工具类
 * @作者： @蒋诗朋
 * @创建时间： @2014-11-27
 */

public class ThreadPool {
	private ExecutorService pool;
	private static ThreadPool instance;
	private final int MAX_THREAD_COUNT = 3;

	public ThreadPool() {
		pool = Executors.newFixedThreadPool(MAX_THREAD_COUNT); // 最多并发三个线程，其它等待
	}

	public static ThreadPool getInstance() {
		if (instance == null) {
			instance = new ThreadPool();
		}
		return instance;
	}

	public void execThread(Runnable runnable) {
		pool.execute(runnable);
	}
}