package com.udbac.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.entity.TbAmpBasicMediaInfo;
import com.udbac.model.UserBean;
import com.udbac.service.MediaService;

/**
 * 媒体Controller
 * @author LFQ
 *
 */
@Controller
@RequestMapping("/media")
public class MediaController {
	
	@Autowired
	private MediaService mediaService;
	/**
	 * 获取所有媒体信息
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Collection<TbAmpBasicMediaInfo> list(){
		Map<String, TbAmpBasicMediaInfo> medias = mediaService.getMedias(false);
		return medias.values();
	}//获取所有媒体信息
	
	/**
	 * 映射媒体名称
	 * @return
	 */
	@RequestMapping("mapMedia.do")
	@ResponseBody
	public String mapMedia(@RequestParam("newMediaName")String newMediaName,
			@RequestParam("mediaInputVal")String mediaInputVal,
			HttpServletResponse response, HttpServletRequest request){
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String userName=user.getUSER_NAME();
		//System.out.println(newMediaName + "==" +mediaInputVal);
		if("".equals(mediaInputVal) || "".equals(newMediaName) || mediaInputVal == null || newMediaName == null){
			return "0";
		}
		String mes = mediaService.mapMedia(newMediaName, mediaInputVal,userName);
		if("".equals(mes)){
			return "1";
		}
		
		return mes;
//		return "1";
	}
	
	
	
	/**
	 * 新增媒体
	 * @return
	 */
	@RequestMapping("addMedia.do")
	@ResponseBody
	public String addMedia(@RequestParam("mediaName")String mediaName,
			@RequestParam("mediaType")String mediaType,
			HttpServletResponse response, HttpServletRequest request){
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		String userName=user.getUSER_NAME();
		
		String mes = mediaService.addMedia(mediaName, mediaType,userName);
		return mes;
	
	}
	//checkMedia
	/**
	 * 新增媒体
	 * @return
	 */
	@RequestMapping("checkMedia.do")
	@ResponseBody
	public Integer checkMedia(@RequestParam("mediaInputVal")String mediaName){
		
		Integer mes = mediaService.checkMedia(mediaName);
		return mes;
	
	}

}
