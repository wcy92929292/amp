package com.udbac.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udbac.dao.MediaDao;
import com.udbac.entity.TbAmpBasicMediaInfo;
import com.udbac.service.MediaService;
import com.udbac.util.FilePathManager;

/**
 * 媒体服务类
 * @author LFQ
 * @DATE 2016-04-25
 */
@Service
public class MediaServiceImpl implements MediaService{
	
	//标准媒体信息
	private static	Map<String,TbAmpBasicMediaInfo> medias = new HashMap<>();
	
	@Autowired
	private FilePathManager fpm;
	
	@Autowired
	private MediaDao mediaDao;
	
	//泛化媒体与标准媒体名称关系映射一一对应
	private static Map<String,String> generalization = new HashMap<>();
	
	//泛化媒体与标准媒体名称属性文件关系映射
	private Map<String,String> mediaProperties = new HashMap<>(); 
	
	/**
	 * 获取所有媒体列表
	 * @return	
	 */
	@Override
	public  Map<String,TbAmpBasicMediaInfo> getMedias(boolean isFlush){
		
		synchronized(MediaServiceImpl.class){
			//如果媒体信息列表为空，则到数据中取得媒体信息列表
			if(medias.size() == 0 || isFlush){
				setGeneralization();
				
				List<TbAmpBasicMediaInfo> mediaList = getDBMedia();
				TbAmpBasicMediaInfo mediaInfo = null;
				
				for (int i = 0; i < mediaList.size(); i++) {
					mediaInfo = mediaList.get(i);
					medias.put(mediaInfo.getMediaName(), mediaInfo);
				}
			}//end if
		}
		
		return medias;
	}//end getMedias
	
	/**
	 * 映射媒体信息
	 * @return
	 */
	public String mapMedia(String newMediaName,String mediaName, String userName){
		getMedias(true);
		synchronized(this.getClass()){
	//		通过媒体ID找到标准媒体名称
			Set<String> keyset = medias.keySet();
			Iterator<String> keys = keyset.iterator();
			String key;
			TbAmpBasicMediaInfo media = null;
			while (keys.hasNext()) {
				key = keys.next();
				media = medias.get(key);
				if(String.valueOf(media.getMediaName()).equals(mediaName)){
					break;
				}
			}
			//未找到标准名称
			if(media == null){
				return "未匹配标准名称";
			}
			//判断是否已经存在泛化关系
			if(generalization.get(newMediaName) != null){
				return "1";
			}
			
			//将新泛化名称已经标准媒体名称映射
			generalization.put(newMediaName, media.getMediaName());
			if(mediaProperties.get(media.getMediaName())==null){
				mediaProperties.put(media.getMediaName(), newMediaName);
			}else{
			mediaProperties.put(media.getMediaName(), mediaProperties.get(media.getMediaName()) + ";" + newMediaName);
			}
			saveGeneralization();
	//		getMedias(true);
			
			return "1";
		}
	}//end mapMedia
	
	/**
	 * 保存泛化媒体信息
	 */
	private void saveGeneralization(){
		
		BufferedWriter bw = null;
		String key;
		StringBuffer lineData = new StringBuffer(30);
		
		try {
			bw = new BufferedWriter(new FileWriter(fpm.getMediaFilePath()));
			
			Set<String> keySet = mediaProperties.keySet();
			Iterator<String> iterator = keySet.iterator();
			
			while (iterator.hasNext()) {
				key = iterator.next();
				
				lineData.append(key);
				lineData.append("==");
				lineData.append(mediaProperties.get(key));
				
				bw.write(lineData.toString());
				bw.newLine();
				
				lineData.delete(0, lineData.length());
			}
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}//end saveGeneralization
	
	
	
	
	/**
	 * 获取DB中的媒体信息
	 * @return
	 */
	public List<TbAmpBasicMediaInfo> getDBMedia(){
		return mediaDao.getMedias();
	}
	
	public Map<String,String> getGeneralization(){
		return generalization;
	}
	
	/**
	 * 读取媒体映射关系文件，将映射关系存入到Map中
	 */
	public void setGeneralization(){
		String mediaFilePath = fpm.getMediaFilePath();
		String[] values;
		String[] keys;
		String lineData;
		int i;
		BufferedReader br = null;
		
		try {
			FileInputStream fls = new FileInputStream(mediaFilePath);
			InputStreamReader isr = new InputStreamReader(fls, "UTF-8");
			br = new BufferedReader(isr);
			
			while((lineData = br.readLine()) != null){
				values = lineData.split("==");
				if(values.length != 2){
					continue;
				}
				keys = values[1].split(";");
				
				//保存原始关系映射
				mediaProperties.put(values[0],values[1]);
				
				//将要泛化名称作为主键，标准名称作为值
				for (i = 0; i < keys.length; i++) {
					generalization.put(keys[i],values[0]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}//end setGeneralization

	@Override
	public String addMedia(String mediaName, String mediaType, String userName) {
		String flag = "0";
		Integer count =mediaDao.getCount( mediaName);
		if(count==0){
			try {
				TbAmpBasicMediaInfo mediaInfo = new TbAmpBasicMediaInfo();
				mediaInfo.setMediaName(mediaName);
				mediaInfo.setMediaType(mediaType.trim());
				mediaInfo.setAddUser(userName);
		    	mediaDao.insertMedia(mediaInfo);
		    	flag="1";
			} catch (Exception e) {
				flag="0";
			}
			
		}else{
			flag="2";
		}
		
		return flag;
	}

	@Override
	public Integer checkMedia(String mediaName) {
		return 	mediaDao.getCount( mediaName);
	}

	
}
