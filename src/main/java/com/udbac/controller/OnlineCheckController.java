package com.udbac.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.udbac.entity.FavoritesInfo;
import com.udbac.entity.OnlineCheckInfo;
import com.udbac.model.UserBean;
import com.udbac.service.FavoritesService;
import com.udbac.service.OnlineCheckService;
import com.udbac.util.FilePathManager;
import com.udbac.util.MailInitBean;
import com.udbac.util.mail;

/**
 * 上线核查
 * 
 * @author han
 *
 */
@Controller
@RequestMapping("/check")
public class OnlineCheckController {

	@Autowired
	OnlineCheckService service;

	@Autowired
	private FilePathManager filePathManager;
	
	@Autowired
	FavoritesService faservice;
	
	/**
	 *查询上线第一天的数据，带有有条件的查询 
	 * @param date 日期
	 * @param mic 短代码
	 * @param state 上线状态
	 * @param cusName 筛选的客户名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list.do", method = RequestMethod.POST)
	public List<OnlineCheckInfo> queryInfo(@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "mic", required = false) String mic,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "cusName", required = false) String cusName,
			@RequestParam(value = "isShowMy", required = false) String isShowMy,
			HttpServletRequest request) {

		List<OnlineCheckInfo> queryInfo = null;
		
		UserBean  user = (UserBean) request.getSession().getAttribute("user");
		
		System.out.println(isShowMy);
		
		if(isShowMy == null || isShowMy.equals("0")){//显示自己的收藏的
			if(user.getROLE_ID().equals("6")){
				queryInfo = service.queryInfo(date, state, mic,user.getCustomerName(),null);
			}else{
				queryInfo = service.queryInfo(date, state, mic,cusName,null);
			}
		}else{
			if(user.getROLE_ID().equals("6")){
				queryInfo = service.queryInfo(date, state, mic,user.getCustomerName(),user.getUSER_NAME());
			}else{
				queryInfo = service.queryInfo(date, state, mic,cusName,user.getUSER_NAME());
			}
		}
		
		return queryInfo;
	}
	
	
	@RequestMapping("mail.do")
	public @ResponseBody String sendMail(@RequestParam("mic") String mic,
			@RequestParam("userName") String userName,
			@RequestParam("toUserName") String toUserName,
			@RequestParam("requireDate") String requireDate,
			@RequestParam("content") String content){
		
		String message = "";
		
		try {
				String smtp = MailInitBean.smtp;// smtp服务器
		        String from = MailInitBean.from;// 邮件显示名称
		        String subject = "点位: "+mic+" 邮件反馈";// 邮件标题
		        String username = MailInitBean.username;// 发件人真实的账户名
		        String password =  MailInitBean.password;// 发件人密码
		        content = "您好，我是"+userName +"<br>"
		        		+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;我反馈的点位是："+mic +"<br>"
		        		+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;反馈的内容是："+ content +"<br>" 
		        		+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;最后希望您反馈的时间是："+requireDate +"<br>" 
		        		+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;感谢配合！"+"<br><br>"
		        		+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='#ccc'>系统邮件，请勿回复！</font>";
		        mail.sendAndCc(smtp, from, toUserName, null, subject, content, username, password);
				message = "1";
			}catch (Exception e) {
				message = "0";
				e.printStackTrace();
			}
		return message;
	}
	
	/**
	 * 单个收藏
	 * @param mic
	 * @param user
	 * @param date
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/save.do")
	public String saveFavorites(@RequestParam("mic") String mic,
			@RequestParam("user") String user,
			@RequestParam("date") String date){
		String message = "";
		
		if(mic != null && user != null && date != null){
			/**
			 * 单个查询
			 */
			FavoritesInfo favorites = faservice.countFavorites(mic, user, date);
			if(favorites == null){
				faservice.addMyFavorites(mic,user,date);
				message = "1";
			}else{
				message = "2";
			}
		}else{
			message = "0";
		}
		return message;
	}
	
	/**
	 * 单个删除
	 * @param mic
	 * @param user
	 * @param date
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delete.do")
	public String deleteFavorites(@RequestParam("mic") String mic,
			@RequestParam("user") String user,
			@RequestParam("date") String date){
		String message = "";
		
		if(mic != null && user != null && date != null){
			faservice.deleteFavorites(mic, user, date);
			message = "1";
		}else{
			message = "0";
		}
		return message;
	}
	

	/**
	 * 批量保存点位
	 * @param mic
	 * @param user
	 * @param date
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/saveMany.do")
	public String saveManyFavorites(@RequestParam("mic") String mic,
			@RequestParam("user") String user,
			@RequestParam("date") String date){
		String message = "";
		String[] split = null;
		Map<String, Object> map = new HashMap<>();
		
		if(mic != null){
			split = mic.split(",");
		}
		
		List<String> miclist = Arrays.asList(split);
		
		map.put("user", user);
		map.put("date", date);
		map.put("list", miclist);
		
		if(mic != null && user != null && date != null){
			
			List<FavoritesInfo> info = faservice.info(map);
			System.out.println(info);
			if (info.size() == 0) {
				faservice.addMyManyFavorites(map);
			}else{
				faservice.deleteManyFavorites(map);
				faservice.addMyManyFavorites(map);
			}
				message = "1";
		}else{
				message = "0";
		}
		return message;
	}
	
	/**
	 * 批量删除点位
	 * @param mic
	 * @param user
	 * @param date
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/deleteMany.do")
	public String deleteManyFavorites(@RequestParam("mic") String mic,
			@RequestParam("user") String user,
			@RequestParam("date") String date){
		String message = "";
		String[] split = null;
		Map<String, Object> map = new HashMap<>();
		
		if(mic != null){
			split = mic.split(",");
		}
		
		List<String> miclist = Arrays.asList(split);
		
		System.out.println(miclist);
		
		if(mic != null && user != null && date != null){
				map.put("user", user);
				map.put("date", date);
				map.put("list", miclist);
				faservice.deleteManyFavorites(map);
				message = "1";
		}else{
				message = "0";
		}
		return message;
	}
	
	/***
	 * 保存曝光核查的图片
	 * @param file
	 * @return
	 */
	private boolean saveExposurePath(HttpServletRequest request, MultipartFile file, String mic,
			String actCode) {
		
		// 判断文件是否为空
		if (!file.isEmpty()) {
			try {
				// 文件命名是当前时间的毫秒数+活动编号+原本文件名
				String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(System.currentTimeMillis())
						+actCode+ StringFilter(file.getOriginalFilename());
				// )
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				StringBuffer fileSB = new StringBuffer(50);
				if(filePathManager.getCheckClickFilePath().isEmpty()){
					File filePath = new File(filePathManager.toString());
					filePath.mkdirs();
				}
				fileSB.append(filePathManager.getCheckClickFilePath());
				fileSB.append(actCode);
				fileSB.append("/");
				fileSB.append(mic);
				fileSB.append("/");
				fileSB.append(sdf.format(new Date()));
				fileSB.append("/");
				fileSB.append(fileName);
				fileSB.append("/");

				File f = new File(fileSB.toString());
				if (!f.exists()) {
					f.mkdirs();
				}
				file.transferTo(f);
				String path = f.toString();
				service.saveExposurePath(path, mic);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/***
	 * 保存点击核查的图片
	 * 
	 * @param file
	 * @param actCode
	 * @return
	 */
	private boolean saveClickPath(HttpServletRequest request, MultipartFile file, String mic,
			String actCode) {
		// 判断文件是否为空
		if (!file.isEmpty()) {
			try {
				// 文件命名是当前时间的毫秒数+活动编号+原本文件名
				String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(System.currentTimeMillis())
						+actCode+ StringFilter(file.getOriginalFilename());
				// )
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				StringBuffer fileSB = new StringBuffer(50);
				fileSB.append(filePathManager.getCheckClickFilePath());
				fileSB.append(actCode);
				fileSB.append("/");
				fileSB.append(mic);
				fileSB.append("/");
				fileSB.append(sdf.format(new Date()));
				fileSB.append("/");
				fileSB.append(fileName);
				fileSB.append("/");

				File f = new File(fileSB.toString());
				if (!f.exists()) {
					f.mkdirs();
				}
				file.transferTo(f);
				String path = f.toString();
				service.saveClickPath(path, mic);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/***
	 * 保存点击核查的图片
	 * 
	 * @param file
	 * @param actCode
	 * @return
	 */
	private boolean saveAfterPath(HttpServletRequest request, MultipartFile file, String mic,
			String actCode) {
		// 判断文件是否为空
		if (!file.isEmpty()) {
			try {
				// 文件命名是当前时间的毫秒数+活动编号+原本文件名
				String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(System.currentTimeMillis())
						+actCode+ StringFilter(file.getOriginalFilename());
				// )
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				StringBuffer fileSB = new StringBuffer(50);
				fileSB.append(filePathManager.getCheckClickFilePath());
				fileSB.append(actCode);
				fileSB.append("/");
				fileSB.append(mic);
				fileSB.append("/");
				fileSB.append(sdf.format(new Date()));
				fileSB.append("/");
				fileSB.append(fileName);
				fileSB.append("/");

				File f = new File(fileSB.toString());
				if (!f.exists()) {
					f.mkdirs();
				}
				file.transferTo(f);
				String path = f.toString();
				service.saveAfterPath(path, mic);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 上传图片
	 * @param files
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/photoUpload.do")
	public String filesUpload(@RequestParam("mic") String mic, @RequestParam("filename") MultipartFile[] files,
			@RequestParam("st") String exposure, //判断是曝光、点击、还是后端按钮
			@RequestParam("actCode") String actCode, //活动编号
			@RequestParam("reupload") boolean reupload, //是否是重新上传的
			@RequestParam(value = "oldSavePath" ,required = false) String oldSavePath, //删除之前的文件
			HttpServletRequest request
			) {

		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];

				System.out.println(mic);
				System.out.println(exposure);
				System.out.println(actCode);
				System.out.println(reupload);
				System.out.println(oldSavePath);
				if(reupload){//判断是重新上传的图片
					deleteDir(oldSavePath);//得到之前的文件路径，删除
				}
				// 判断上传的图片是否是曝光核查图片
				if ("0".equals(exposure)) {
					// 保存文件
					saveExposurePath(request, file, mic, actCode);
				} else if ("1".equals(exposure)) {
					// 保存文件
					saveClickPath(request, file, mic, actCode);
				} else if ("2".equals(exposure)) {
					// 保存文件
					saveAfterPath(request, file, mic, actCode);
				}
			}
		}

		// 重定向
		return "redirect:/page/check/check.html";
	}

	/**
	 * 列出所有上传的图片
	 */
	@RequestMapping("/listAll.do")
	public void list(HttpServletRequest request) {
		String filePath = request.getSession().getServletContext().getRealPath("/") + "/WEB-INF/upload/";
		File uploadDest = new File(filePath);
		String[] fileNames = uploadDest.list();
		for (int i = 0; i < fileNames.length; i++) {
			// 打印出文件名
			System.out.println(fileNames[i]);
		}
	}

	/**
	 * 改变省份的上线状态
	 * 
	 * @param cn_mic 当前的短代码
	 * @param cn_state 当前的状态
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping("/changeState.do")
	public void changeState(@RequestParam(value = "mic", required = false) String mic,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "time", required = false) long time) throws ParseException {
		Date date = new Date(time);
		System.out.println("当前短代码:  "+mic);
		System.out.println("当前上线状态:  "+state);
		service.changeState(mic, state, date);
	}

	/**
	 * 查看图片
	 * 
	 * @param response
	 * @param request
	 * @param fileName
	 * @throws IOException
	 */
	@RequestMapping(value = "/getFile.do")
	public void responseFile(HttpServletResponse response, HttpServletRequest request, String fileName) {
		try {
			fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
			File file = new File(fileName);
			// 设置文件名称
			String filename = file.getName();
			// 设置文件名下载时中文正常
			filename = new String(filename.getBytes("gb2312"), "iso8859-1");
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(fileName));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 定义输出类型
			response.setContentType("application/octet-stream");
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment; filename=" + filename);
			response.addHeader("Content-Length", "" + file.length());
			OutputStream os = new BufferedOutputStream(response.getOutputStream());
			os.write(buffer);
			os.flush();
			os.close();
		} catch (Exception e) {
			System.out.println("图片路径没找到!");
		}
	}

	/**
	 * 读取管道中的流数据
	 */
	@SuppressWarnings("unused")
	private byte[] readStream(InputStream inStream) {
		ByteArrayOutputStream bops = new ByteArrayOutputStream();
		int data = -1;
		try {
			while ((data = inStream.read()) != -1) {
				bops.write(data);
			}
			return bops.toByteArray();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 改变点位状态
	 * @param st_mic
	 * @param new_mic_state
	 */
	@ResponseBody
	@RequestMapping("/micState.do")
	public void setMicState(@RequestParam(value = "st_mic", required = true) String st_mic,
			@RequestParam(value = "new_mic_state", required = false) Integer mic_state,
			@RequestParam(value = "last_state", required = false) Integer last_state) {
		
			service.setMicState(mic_state, st_mic);
	}
	
	/**
	 * 文件名不允许有特殊字符
	 * @param str
	 * @return
	 */
	 public  static  String StringFilter(String   str){      
	   String regEx="[!@#$%^&*]";   
	   Pattern   p   =   Pattern.compile(regEx);      
	   Matcher   m   =   p.matcher(str);      
	   return   m.replaceAll("").trim();      
    }       
	 
	 /**
	  * 递归删除指定的目录下的文件
	  * @param path
	  */
	 public static void deleteDir(String path)
	 {
	 	File file = new File(path);
	 	if (file.exists())
	 	{
	 		if (file.isDirectory())
	 		{
	 			File[] files = file.listFiles();
	 			for (File subFile : files)
	 			{
	 				if (subFile.isDirectory())
	 					deleteDir(subFile.getPath());
	 				else
	 					subFile.delete();
	 			}
	 		}
	 		file.delete();
	 	}
	 }
	 
}
