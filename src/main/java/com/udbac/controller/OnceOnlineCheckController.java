package com.udbac.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.udbac.entity.OnlineCheckInfo;
import com.udbac.model.UserBean;
import com.udbac.service.OnceOnlineCheckService;
import com.udbac.util.FilePathManager;

/**
 * 上线核查
 * 
 * @author han
 *
 */
@Controller
@RequestMapping("/onceCheck")
public class OnceOnlineCheckController {

	@Autowired
	OnceOnlineCheckService service;

	@Autowired
	private FilePathManager filePathManager;

	/**
	 *查询已经上线的数据 
	 * @param select_date 选择的日期 
	 * @param cusName 客户名称
	 * @param mic 短代码
	 * @param state 上线状态
	 * @param response 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list.do")
	public List<OnlineCheckInfo> queryInfo(
			@RequestParam(value = "select_date", required = false) String select_date,
			@RequestParam(value = "cusName", required = false) String cusName,
			@RequestParam(value = "mic", required = false) String mic,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "isShowMyTab", required = false) String isShowMyTab,
			HttpServletRequest request) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<OnlineCheckInfo> queryInfo = null;
		
		if (select_date == null || select_date == "") {
			select_date = sdf.format(new Date());
		}
		
		System.out.println("========tab======"+isShowMyTab);
		
		UserBean  user = (UserBean) request.getSession().getAttribute("user");
		if(isShowMyTab == null || isShowMyTab.equals("0")){//显示自己的收藏的
			//判断session中的当前用户角色，为客户时取这个用户所在的客户名称
			if(user.getROLE_ID().equals("6")){
				queryInfo = service.queryOnlineInfo( select_date,user.getCustomerName(),mic,state,null);
			}else{
				queryInfo = service.queryOnlineInfo( select_date,cusName,mic,state,null);
			}
		}else{
			//判断session中的当前用户角色，为客户时取这个用户所在的客户名称
			if(user.getROLE_ID().equals("6")){
				queryInfo = service.queryOnlineInfo( select_date,user.getCustomerName(),mic,state,user.getUSER_NAME());
			}else{
				queryInfo = service.queryOnlineInfo( select_date,cusName,mic,state,user.getUSER_NAME());
			}
		}
		
		
		return queryInfo;
	}

	/***
	 * 保存曝光核查的图片
	 * @param file
	 * @return
	 */
	private boolean saveExposurePath(HttpServletRequest request, MultipartFile file, String mic, String exposure) {

		// 判断文件是否为空
		if (!file.isEmpty()) {
			try {
				// 文件命名是当前时间的毫秒数+活动编号+原本文件名
				String fileName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(System.currentTimeMillis())
						+ "-" + "曝光核查" + "-" + mic + "-" + file.getOriginalFilename();
				String filePath = filePathManager.getCheckClickFilePath() + fileName;
				System.out.println("文件的保存路径----->" + filePath);
				File saveDir = new File(filePath);
				if (!saveDir.getParentFile().exists())
					saveDir.getParentFile().mkdirs();
				// 转存文件
				file.transferTo(saveDir);
				// 存取曝光核查图片的地址
				service.saveExposurePath(filePath, mic);
				System.out.println("曝光核查URL存取成功!");
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
	 * @return
	 */
	private boolean saveClickPath(HttpServletRequest request, MultipartFile file, String mic, String click) {

		// 判断文件是否为空
		if (!file.isEmpty()) {
			try {
				// 文件命名是当前时间的毫秒数+活动编号+原本文件名
				String fileName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(System.currentTimeMillis())
						+ "-" + "点击核查" + "-" + mic + "-" + file.getOriginalFilename();
				String filePath = filePathManager.getCheckClickFilePath() + fileName;
				System.out.println("文件的保存路径----->" + filePath);
				File saveDir = new File(filePath);
				if (!saveDir.getParentFile().exists())
					saveDir.getParentFile().mkdirs();
				// 转存文件
				file.transferTo(saveDir);
				// 存取曝光核查图片的地址
				service.saveClickPath(filePath, mic);
				System.out.println("点击核查URL存取成功!");
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
	 * @return
	 */
	private boolean saveAfterPath(HttpServletRequest request, MultipartFile file, String mic, String click) {

		// 判断文件是否为空
		if (!file.isEmpty()) {
			try {
				// 文件命名是当前时间的毫秒数+活动编号+原本文件名
				String fileName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS").format(System.currentTimeMillis())
						+ "-" + "后端核查" + "-" + mic + "-" + file.getOriginalFilename();
				// 保存的文件路径(如果用的是Tomcat服务器，文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\upload\\文件夹中
				// )
				String filePath = filePathManager.getCheckClickFilePath() + fileName;
				File saveDir = new File(filePath);
				if (!saveDir.getParentFile().exists())
					saveDir.getParentFile().mkdirs();
				// 转存文件
				file.transferTo(saveDir);
				// 存取曝光核查图片的地址
				service.saveAfterPath(filePath, mic);
				System.out.println("后端核查URL存取成功!");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 上传图片
	 *
	 * @param files
	 * @param request
	 * @return
	 */
	@RequestMapping("/photoUpload.do")
	public String filesUpload(@RequestParam("mic") String mic, @RequestParam("myfiles") MultipartFile[] files,
			@RequestParam("st") String exposure, HttpServletRequest request) {
		System.out.println("mic=" + mic + "  " + "exposure=" + exposure);
		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				// 判断上传的图片是否是曝光核查图片
				if (exposure.equals("曝光核查")) {
					// 保存文件
					saveExposurePath(request, file, mic, exposure);
				} else if (exposure.equals("点击核查")) {
					// 保存文件
					saveClickPath(request, file, mic, exposure);
				} else if (exposure.equals("后端核查")) {
					// 保存文件
					saveAfterPath(request, file, mic, exposure);
				}
				System.out.println("---ok,upload success!---");
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
	 * 改变上线状态(通过一个短代码和时间关联来更改状态)
	 * @param mic 
	 * @param state
	 * @param time
	 */
	@ResponseBody
	@RequestMapping("/changeState.do")
	public void changeState(@RequestParam(value = "mic", required = false) String mic,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "time", required = false) long time) {
		
			Date date = new Date(time);
			
//			System.out.println(mic +"  "+ state +"  "+time);
			
			service.changeState(mic,state,date);
	}

	/**
	 * 查看图片
	 * 
	 * @param response
	 * @param request
	 * @param fileName
	 */
	@RequestMapping(value = "/getFile.do")
	public void responseFile(HttpServletResponse response, HttpServletRequest request, String fileName) {
		try {
			OutputStream os = response.getOutputStream();
			File file = new File(fileName);
			if (file != null && file.exists()) {
				FileInputStream fips = new FileInputStream(file);
				byte[] btImg = readStream(fips);
				os.write(btImg);
			}
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 读取管道中的流数据
	 */
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
	 * 
	 * @param st_mic
	 * @param new_mic_state
	 */
	@ResponseBody
	@RequestMapping("/micState.do")
	public void setMicState(@RequestParam(value = "st_mic", required = true) String st_mic,
			@RequestParam(value = "new_mic_state", required = false) Integer mic_state,
			@RequestParam(value = "last_state", required = false) Integer last_state) {

		System.out.println("+++++++++++++++++++++++++++"+mic_state+st_mic);
		service.setMicState(mic_state, st_mic);
		System.out.println(st_mic + "------------------------改变之后的点位状态----------------" + mic_state);

	}

}
