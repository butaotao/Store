package com.dachen.mediecinelibraryrealizedoctor.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
 
public class FileUtils
{ 
	static FileUtils fileutils;
public static FileUtils getFileUtilsInstance(){
	if (null==fileutils) {
		fileutils = new FileUtils();
		
	}
	return fileutils;
}
    boolean flag = false;// flag为true时，表示文件写完,但还未读完
 
    public synchronized void read(String filePath) throws Exception
    {
        while (!flag)// flag为false 表示文件读完，但是还未写完
        {
            wait();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath)));
 
        String s = "";
 
        while (null != (s = br.readLine()))
        {
            System.out.println(Thread.currentThread().getName());
        }
 
        br.close();
 
        flag = false;
 
        notify();
    }
 
    public synchronized void write(String filePath) throws Exception
    {
        while (flag)// flag为true时，表示文件写完,但还未读完
        {
            wait();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath, true)));
 
        bw.write("Hello World   ");
 
        bw.close();
 
        System.out.println(Thread.currentThread().getName());
 
        flag = true;
 
        notify();
 
    }
}