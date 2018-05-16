package com.udbac.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件操作工具类
 * @author LFQ
 * @DATE 2016-04-27
 */
public class FileUtil {

	/**
	 * 保存文件
	 * @param is
	 * @param os
	 * @param bsLen
	 */
	public static void saveFile(InputStream is,OutputStream os,int bsLen){
		
		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = new BufferedOutputStream(os);
		
		byte[] bs = new byte[bsLen];
		int len = 0;
		
		try {
			while ((len = bis.read(bs)) != -1) {
				bos.write(bs, 0, len);
			}
			bos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bis != null){
				try {bis.close();} catch (IOException e) {e.printStackTrace();}
			}
			if(bos != null){
				try {bos.close();} catch (IOException e) {e.printStackTrace();}
			}
		}
	}//end saveFile(InputStream,OutputStream)
	
	public static void saveFile(InputStream is,OutputStream os){
		saveFile(is,os,512);
	}
	
	public static void saveFile(String sourcePath,String savePath) throws FileNotFoundException{
		saveFile(sourcePath,sourcePath,512);
	}
	
	public static void saveFile(String sourcePath,String savePath,int bsLen) throws FileNotFoundException{
		saveFile(new FileInputStream(sourcePath),new FileOutputStream(savePath),bsLen);
	}
	
	
	
}
