package com.udbac.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.ddf.EscherColorRef.SysIndexProcedure;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.udbac.dao.AdMonitorModelDao;
import com.udbac.dao.MobieMarketDao;
import com.udbac.dao.TbMaEffectDataMapper;
import com.udbac.model.AdMonitorModel;
import com.udbac.model.DetailTerminalSales;
import com.udbac.model.MonitorUpdateModel;
import com.udbac.util.CSVFileUtil;

@Controller
@RequestMapping("/admonitor")
@SuppressWarnings(value = { "all" })
/****
 * 广告监测查询
 */
public class AdMonitorController {

	@Autowired(required = true)
	private AdMonitorModelDao adMonitorModelDao;// 营销活动dao

	private SimpleDateFormat dfpm = new SimpleDateFormat("yyyyMMddHH");
	private SimpleDateFormat dfdb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat dfpw = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat dfpd = new SimpleDateFormat("yyyyMMdd");

	// 导入用的map
	private Map<String, AdMonitorModel> insertMap;
	private boolean file1flag = false;
	private boolean file2flag = false;
	private boolean file3flag = false;
	private boolean file4flag = false;

	/***
	 * 上传文件(四个文件版本) 最新
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/fileUploadv4.do", method = RequestMethod.POST)
	public @ResponseBody void fileUploadv4(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().flush();
		
		if ("1".equals(multipartRequest.getParameter("file"))) {
			file1flag = importVisitFile(multipartRequest, response);
		} else if ("2".equals(multipartRequest.getParameter("file"))) {
			file2flag = importVisitorFile2(multipartRequest, response);
		} else if ("3".equals(multipartRequest.getParameter("file"))) {
			file3flag = importClickFile(multipartRequest, response);
		} else {
			if (!importBounceFile3(multipartRequest, response)) {
				return;
			}
			file4flag = true;
		}
		procedurev2(response, multipartRequest);

	}

	private boolean importBounceFile3(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws IOException, ParseException {
		String[] firstLine = null;
		String[] line = null;
		String content = null;
		String date = multipartRequest.getParameter("date");
		String date2 = null;
		String format = multipartRequest.getParameter("format");

		for (String parts : date.split("-")) {
			date2 += parts + "/";
		}
		date2 = date2.substring(0, date2.length() - 1);
		// 获取多个file
		for (Iterator it = multipartRequest.getFileNames(); it.hasNext();) {
			String key = (String) it.next();
			MultipartFile upFile = multipartRequest.getFile(key);
			if (upFile.getOriginalFilename().length() > 0) {
				// String fileName = upFile.getOriginalFilename();
				BufferedReader br = new BufferedReader(new InputStreamReader(upFile.getInputStream(), "UTF-8"));
				if (br.ready()) {
					for (int i = 0; i < 3; i++) {
						br.readLine();
					}
					firstLine = br.readLine().split(",", -1);
					if (firstLine.length > 1 
							&& (firstLine[1].substring(1, 11).equals(date)
							|| firstLine[1].substring(1, 11).equals(date2))
							) {
						for (int i = 0; i < 4; i++) {
							br.readLine();
						}
						while (br.ready()) { //读取数据
							content = br.readLine();
							line = CSVFileUtil.fromCSVLinetoArray(content);
							if (content == "" || line.length < 1 || "".equals(line[0])) {
								return true;
							}

							String mic;
							if (line[1] != null && line[1].startsWith("\"")) {
								mic = line[1].substring(1, line[1].length() - 1);
							} else {
								if(line[1].length()<=24 &&line[1].length()>=20 && !line[1].contains("?")&& !line[1].contains("_")&& !line[1].contains("-")&& !line[1].contains("=")&&!line[1].contains("2015")&&!line[1].contains("2016")&&!line[1].contains("2017")&&!line[1].contains("2018")){
									if(line[1].toLowerCase()==line[1] ||line[1].toUpperCase()==line[1]){
										mic = adMonitorModelDao.checkMic(line[1])==null ? line[1]:adMonitorModelDao.checkMic(line[1]);
									}else{
										mic = line[1];
									}
								}else{
									mic = line[1];
								}
							}
							AdMonitorModel model = insertMap.get(mic);
							if (model != null) {
								if ("1".equals(format)) {
									model.setBounce_times(Integer.parseInt(line[2]));// 跳出仿次
								} else {
									model.setBounce_times(Integer.parseInt(line[4]));// 跳出仿次
								}
								
							} else {
								// response.getWriter().print("not_match");
								continue;
							}

						}

					} else {
						response.getWriter().print("wrong_date");
						return false;
					}
				}

			}
		}
		return true;
	}

	private boolean importClickFile(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws IOException {
		String[] firstLine = null;
		String[] line = null;
		String content = null;
		String date = multipartRequest.getParameter("date");
		String date2 = null;
		for (String parts : date.split("-")) {
			date2 += parts + "/";
		}
		date2 = date2.substring(0, date2.length() - 1);

		// 获取多个file
		for (Iterator it = multipartRequest.getFileNames(); it.hasNext();) {
			String key = (String) it.next();
			MultipartFile upFile = multipartRequest.getFile(key);
			if (upFile.getOriginalFilename().length() > 0) {
				// String fileName = upFile.getOriginalFilename();
				BufferedReader br = new BufferedReader(new InputStreamReader(upFile.getInputStream(), "UTF-8"));
				if (br.ready()) {
					for (int i = 0; i < 3; i++) {
						br.readLine();
					}
					firstLine = br.readLine().split(",", -1);
					if (firstLine.length > 1 && (firstLine[1].substring(1, 11).equals(date)
							|| firstLine[1].substring(1, 11).equals(date2))) {
						for (int i = 0; i < 4; i++) {
							br.readLine();
						}
						while (br.ready()) {
							content = br.readLine();
							line = CSVFileUtil.fromCSVLinetoArray(content);

							if (content == "" || line.length < 1 || "".equals(line[0])) {
								response.getWriter().print("success");
								response.getWriter().flush();
								response.getWriter().close();
								return true;
							}
							AdMonitorModel model = null;

							String mic;
							if (line[1] != null && line[1].startsWith("\"")) {
								mic = line[1].substring(1, line[1].length() - 1);
							} else {
								if(line[1].length()<=24 &&line[1].length()>=20 && !line[1].contains("?")&& !line[1].contains("_")&& !line[1].contains("-")&& !line[1].contains("=")&&!line[1].contains("2015")&&!line[1].contains("2016")&&!line[1].contains("2017")&&!line[1].contains("2018")){
									if(line[1].toLowerCase()==line[1] ||line[1].toUpperCase()==line[1]){
										mic = adMonitorModelDao.checkMic(line[1])==null ? line[1]:adMonitorModelDao.checkMic(line[1]);
									}else{
										mic = line[1];
									}
								}else{
									mic = line[1];
								}
							}

							model = insertMap.get(mic);
							if (model != null) {
								try {
									model.setClk(Integer.parseInt(line[2]));// 点击数

								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								continue;
							}
						}

					} else {
						response.getWriter().print("wrong_date");
						return false;
					}
				}

			}
		}
		return true;
	}

	private boolean importVisitorFile2(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws IOException {
		String[] firstLine = null;
		String[] line = null;
		String content = null;
		String date = multipartRequest.getParameter("date");
		String date2 = null;
		for (String parts : date.split("-")) {
			date2 += parts + "/";
		}
		date2 = date2.substring(0, date2.length() - 1);

		// 获取多个file
		for (Iterator it = multipartRequest.getFileNames(); it.hasNext();) {
			String key = (String) it.next();
			MultipartFile upFile = multipartRequest.getFile(key);
			if (upFile.getOriginalFilename().length() > 0) {
				// String fileName = upFile.getOriginalFilename();
				BufferedReader br = new BufferedReader(new InputStreamReader(upFile.getInputStream(), "UTF-8"));
				if (br.ready()) {
					for (int i = 0; i < 3; i++) {
						br.readLine();
					}
					firstLine = br.readLine().split(",", -1);
					if (firstLine.length > 1 && (firstLine[1].substring(1, 11).equals(date)
							|| firstLine[1].substring(1, 11).equals(date2))) {
						for (int i = 0; i < 4; i++) {
							br.readLine();
						}
						while (br.ready()) {
							content = br.readLine();
							line = CSVFileUtil.fromCSVLinetoArray(content);

							if (content == "" || line.length < 1 || "".equals(line[0])) {
								response.getWriter().print("success");
								response.getWriter().flush();
								response.getWriter().close();
								return true;
							}
							AdMonitorModel model = null;

							String mic;
							if (line[1] != null && line[1].startsWith("\"")) {
								mic = line[1].substring(1, line[1].length() - 1);
							} else {
								if(line[1].length()<=24 &&line[1].length()>=20 && !line[1].contains("?")&& !line[1].contains("_")&& !line[1].contains("-")&& !line[1].contains("=")&&!line[1].contains("2015")&&!line[1].contains("2016")&&!line[1].contains("2017")&&!line[1].contains("2018")){
									if(line[1].toLowerCase()==line[1] ||line[1].toUpperCase()==line[1]){
										mic = adMonitorModelDao.checkMic(line[1])==null ? line[1]:adMonitorModelDao.checkMic(line[1]);
									}else{
										mic = line[1];
									}
								}else{
									mic = line[1];
								}
							}

							model = insertMap.get(mic);
							if (model != null) {
								try {
									model.setUv(Integer.parseInt(line[2]));// 仿客

								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								continue;
							}
						}

					} else {
						response.getWriter().print("wrong_date");
						return false;
					}
				}

			}
		}
		return true;
	}

	private boolean importVisitFile(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws IOException, ParseException {
		String[] firstLine = null;
		String[] line = null;
		String content = null;
		String date = multipartRequest.getParameter("date");
		insertMap = new HashMap<String, AdMonitorModel>();
		String date2 = null;
		for (String parts : date.split("-")) {
			date2 += parts + "/";
		}
		date2 = date2.substring(0, date2.length() - 1);
		// 获取多个file
		for (Iterator it = multipartRequest.getFileNames(); it.hasNext();) {
			String key = (String) it.next();
			MultipartFile upFile = multipartRequest.getFile(key);
			if (upFile.getOriginalFilename().length() > 0) {
				// String fileName = upFile.getOriginalFilename();
				BufferedReader br = new BufferedReader(new InputStreamReader(upFile.getInputStream(), "UTF-8"));
				if (br.ready()) {
					for (int i = 0; i < 3; i++) {
						br.readLine();
					}
					firstLine = br.readLine().split(",", -1);
					if (firstLine.length > 1 && (firstLine[1].substring(1, 11).equals(date)
							|| firstLine[1].substring(1, 11).equals(date2))) {
						for (int i = 0; i < 4; i++) {
							br.readLine();
						}
						while (br.ready()) {
							content = br.readLine();
							line = CSVFileUtil.fromCSVLinetoArray(content);

							if (content == "" || line.length < 1 || "".equals(line[0])) {
								response.getWriter().print("success");
								response.getWriter().flush();
								response.getWriter().close();
								return true;
							}
							AdMonitorModel model = new AdMonitorModel();

							String mic;
							if (line[1] != null && line[1].startsWith("\"")) {
								mic = line[1].substring(1, line[1].length() - 1);
							} else {
								if(line[1].length()<=24 &&line[1].length()>=20 && !line[1].contains("?")&& !line[1].contains("_")&& !line[1].contains("-")&& !line[1].contains("=")&&!line[1].contains("2015")&&!line[1].contains("2016")&&!line[1].contains("2017")&&!line[1].contains("2018")){
									if(line[1].toLowerCase()==line[1] ||line[1].toUpperCase()==line[1]){
										mic = adMonitorModelDao.checkMic(line[1])==null ? line[1]:adMonitorModelDao.checkMic(line[1]);
									}else{
										mic = line[1];
									}
								}else{
									mic = line[1];
								}
							}
//							mic = adMonitorModelDao.checkMic(line[1])==null ? line[1]:adMonitorModelDao.checkMic(line[1]);
							model.setMic(mic);

							model.setVv(Integer.parseInt(line[2]));// 仿次
							model.setPv(Integer.parseInt(line[4]));// 浏览次数

							if (null != line[2] && (!"".equals(line[5]))) {
								model.setTime_spent(Double.parseDouble(line[5]));// 访问时长viewtimeFloat
							} else {
								model.setTime_spent(Double.parseDouble("0"));// 访问时长viewtimeFloat
							}

							model.setDaytime(new java.sql.Date(dfpw.parse(date).getTime()));// 创建时间

							if (mic != null && mic.length() < 50) {
								insertMap.put(mic, model);
							}

						}

					} else {
						response.getWriter().print("wrong_date");
						return false;
					}
				}

			}
		}
		return true;
	}

	/***
	 * 上传文件(四个文件版本) 新
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/fileUploadv3.do", method = RequestMethod.POST)
	public @ResponseBody void fileUploadv3(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().flush();
		if ("1".equals(multipartRequest.getParameter("file"))) {
			file1flag = importTimeFile(multipartRequest, response);
		} else if ("2".equals(multipartRequest.getParameter("file"))) {
			file2flag = importVisitorFile(multipartRequest, response);
		} else if ("3".equals(multipartRequest.getParameter("file"))) {
			file3flag = importVisitPVFile(multipartRequest, response);
		} else {
			if (!importBounceFile2(multipartRequest, response)) {
				return;
			}
			file4flag = true;
		}
		procedurev2(response, multipartRequest);

	}

	/***
	 * 上传文件(四个文件版本)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/fileUploadv2.do", method = RequestMethod.POST)
	public @ResponseBody void fileUploadv2(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().flush();
		if ("1".equals(multipartRequest.getParameter("file"))) {
			file1flag = importTimeFile(multipartRequest, response);
		} else if ("2".equals(multipartRequest.getParameter("file"))) {
			file2flag = importVisitorFile(multipartRequest, response);
		} else if ("3".equals(multipartRequest.getParameter("file"))) {
			file3flag = importVisitPVFile(multipartRequest, response);
		} else {
			if (!importBounceFile(multipartRequest, response)) {
				return;
			}
			file4flag = true;
		}
		procedurev2(response, multipartRequest);

	}

	private boolean importVisitPVFile(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws IOException, ParseException {
		String[] firstLine = null;
		String[] line = null;
		String content = null;
		String date = multipartRequest.getParameter("date");
		String date2 = null;
		for (String parts : date.split("-")) {
			date2 += parts + "/";
		}
		date2 = date2.substring(0, date2.length() - 1);

		// 获取多个file
		for (Iterator it = multipartRequest.getFileNames(); it.hasNext();) {
			String key = (String) it.next();
			MultipartFile upFile = multipartRequest.getFile(key);
			if (upFile.getOriginalFilename().length() > 0) {
				// String fileName = upFile.getOriginalFilename();
				BufferedReader br = new BufferedReader(new InputStreamReader(upFile.getInputStream(), "UTF-8"));
				if (br.ready()) {
					for (int i = 0; i < 3; i++) {
						br.readLine();
					}
					firstLine = br.readLine().split(",", -1);
					if (firstLine.length > 1 && (firstLine[1].substring(1, 11).equals(date)
							|| firstLine[1].substring(1, 11).equals(date2))) {
						for (int i = 0; i < 4; i++) {
							br.readLine();
						}
						while (br.ready()) {
							content = br.readLine();
							line = CSVFileUtil.fromCSVLinetoArray(content);

							if (content == "" || line.length < 1 || "".equals(line[0])) {
								response.getWriter().print("success");
								response.getWriter().flush();
								response.getWriter().close();
								return true;
							}
							AdMonitorModel model = null;

							String mic;
							if (line[1] != null && line[1].startsWith("\"")) {
								mic = line[1].substring(1, line[1].length() - 1);
							} else {
								mic = line[1];
							}

							model = insertMap.get(mic);
							if (model != null) {
								try {
									model.setVv(Integer.parseInt(line[3]));// 仿次
									model.setPv(Integer.parseInt(line[2]));// 浏览次数
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								continue;
							}
						}

					} else {
						response.getWriter().print("wrong_date");
						return false;
					}
				}

			}
		}
		return true;
	}

	private boolean importVisitorFile(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws IOException, ParseException {
		String[] firstLine = null;
		String[] line = null;
		String content = null;
		String date = multipartRequest.getParameter("date");
		String date2 = null;
		for (String parts : date.split("-")) {
			date2 += parts + "/";
		}
		date2 = date2.substring(0, date2.length() - 1);
		// 获取多个file
		for (Iterator it = multipartRequest.getFileNames(); it.hasNext();) {
			String key = (String) it.next();
			MultipartFile upFile = multipartRequest.getFile(key);
			if (upFile.getOriginalFilename().length() > 0) {
				// String fileName = upFile.getOriginalFilename();
				BufferedReader br = new BufferedReader(new InputStreamReader(upFile.getInputStream(), "UTF-8"));
				if (br.ready()) {
					for (int i = 0; i < 3; i++) {
						br.readLine();
					}
					firstLine = br.readLine().split(",", -1);
					if (firstLine.length > 1 && (firstLine[1].substring(1, 11).equals(date)
							|| firstLine[1].substring(1, 11).equals(date2))) {
						for (int i = 0; i < 4; i++) {
							br.readLine();
						}
						while (br.ready()) {
							content = br.readLine();
							line = CSVFileUtil.fromCSVLinetoArray(content);

							if (content == "" || line.length < 1 || "".equals(line[0])) {
								response.getWriter().print("success");
								response.getWriter().flush();
								response.getWriter().close();
								return true;
							}
							AdMonitorModel model = null;

							String mic;
							if (line[1] != null && line[1].startsWith("\"")) {
								mic = line[1].substring(1, line[1].length() - 1);
							} else {
								mic = line[1];
							}

							model = insertMap.get(mic);
							if (model != null) {
								try {
									model.setUv(Integer.parseInt(line[2]));// 访客
									// model.setVisitRate(line[3]);// 访客rate
									// ******跳出率没涉及到********
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								continue;
							}
							// model.setVisit(Integer.parseInt(line[4]));// 仿次
							// model.setPv(Integer.parseInt(line[5]));// 浏览次数
						}

					} else {
						response.getWriter().print("wrong_date");
						return false;
					}
				}

			}
		}
		return true;
	}

	private boolean importTimeFile(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws IOException, ParseException {
		String[] firstLine = null;
		String[] line = null;
		String content = null;
		String date = multipartRequest.getParameter("date");
		insertMap = new HashMap<String, AdMonitorModel>();
		String date2 = null;
		for (String parts : date.split("-")) {
			date2 += parts + "/";
		}
		date2 = date2.substring(0, date2.length() - 1);
		// 获取多个file
		for (Iterator it = multipartRequest.getFileNames(); it.hasNext();) {
			String key = (String) it.next();
			MultipartFile upFile = multipartRequest.getFile(key);
			if (upFile.getOriginalFilename().length() > 0) {
				// String fileName = upFile.getOriginalFilename();
				BufferedReader br = new BufferedReader(new InputStreamReader(upFile.getInputStream(), "UTF-8"));
				if (br.ready()) {
					for (int i = 0; i < 3; i++) {
						br.readLine();
					}
					firstLine = br.readLine().split(",", -1);
					if (firstLine.length > 1 && (firstLine[1].substring(1, 11).equals(date)
							|| firstLine[1].substring(1, 11).equals(date2))) {
						for (int i = 0; i < 4; i++) {
							br.readLine();
						}
						while (br.ready()) {
							content = br.readLine();
							line = CSVFileUtil.fromCSVLinetoArray(content);

							if (content == "" || line.length < 1 || "".equals(line[0])) {
								response.getWriter().print("success");
								response.getWriter().flush();
								response.getWriter().close();
								return true;
							}
							AdMonitorModel model = new AdMonitorModel();

							String mic;
							if (line[1] != null && line[1].startsWith("\"")) {
								mic = line[1].substring(1, line[1].length() - 1);
							} else {
								mic = line[1];
							}

							model.setMic(mic);

							if (null != line[2] && (!"".equals(line[2]))) {
								model.setTime_spent(Double.parseDouble(line[2]));// 访问时长viewtimeFloat
							} else {
								model.setTime_spent(Double.parseDouble("0"));// 访问时长viewtimeFloat
							}

							model.setDaytime(new java.sql.Date(dfpw.parse(date).getTime()));// 创建时间
							if (mic != null && mic.length() < 50) {
								insertMap.put(mic, model);
							}

						}

					} else {
						response.getWriter().print("wrong_date");
						return false;
					}
				}

			}
		}
		return true;

	}

	private boolean importBounceFile2(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws IOException, ParseException {
		String[] firstLine = null;
		String[] line = null;
		String content = null;
		String date = multipartRequest.getParameter("date");
		String date2 = "";
		for (String parts : date.split("-")) {
			date2 += parts + "/";
		}
		date2 = date2.substring(0, date2.length() - 1);
		System.out.println(date2);
		// 获取多个file
		for (Iterator it = multipartRequest.getFileNames(); it.hasNext();) {
			String key = (String) it.next();
			MultipartFile upFile = multipartRequest.getFile(key);
			if (upFile.getOriginalFilename().length() > 0) {
				// String fileName = upFile.getOriginalFilename();
				BufferedReader br = new BufferedReader(new InputStreamReader(upFile.getInputStream(), "UTF-8"));
				if (br.ready()) {
					for (int i = 0; i < 3; i++) {
						br.readLine();
					}
					firstLine = br.readLine().split(",", -1);
					if (firstLine.length > 1 && (firstLine[1].substring(1, 11).equals(date)
							|| firstLine[1].substring(1, 11).equals(date2))) {
						for (int i = 0; i < 4; i++) {
							br.readLine();
						}
						while (br.ready()) {
							content = br.readLine();
							line = CSVFileUtil.fromCSVLinetoArray(content);
							if (content == "" || line.length < 1 || "".equals(line[0])) {
								return true;
							}

							String mic;
							if (line[1] != null && line[1].startsWith("\"")) {
								mic = line[1].substring(1, line[1].length() - 1);
							} else {
								mic = line[1];
							}

							// model.setMic(mic);
							// model.setVisitor(Integer.parseInt(line[2]));//访客
							// model.setVisitRate(Double.parseDouble(line[6]));//访客rate
							// model.setVisit(Integer.parseInt(line[4]));//仿次
							// model.setPv(Integer.parseInt(line[5]));//浏览次数
							// model.setViewtimeFloat(Double.parseDouble(line[6]));//访问时长viewtimeFloat
							// model.setCreate_date((Date)
							// dfpw.parse(date));//创建时间
							// insertMap.put(mic, model);

							AdMonitorModel model = insertMap.get(mic);
							if (model != null) {
								model.setBounce_times(Integer.parseInt(line[2]));
							} else {
								// response.getWriter().print("not_match");
								continue;
							}

						}

					} else {
						response.getWriter().print("wrong_date");
						return false;
					}
				}

			}
		}
		return true;
	}

	private void procedurev2(HttpServletResponse response, HttpServletRequest request) throws IOException {
		if (file1flag && file2flag && file3flag && file4flag) {
			String dateStr = request.getParameter("date");
			List<AdMonitorModel> insertList = mapTransitionList(insertMap);
//			adMonitorModelDao.deleteTimedata();
//			adMonitorModelDao.deleteBouncedata();

//			System.out.println("数据输出=========== \r" + insertList);
			 adMonitorModelDao.insertTimedata(insertList);
			 
			 adMonitorModelDao.summaryData();

			// ****** 未涉及到注释 *******
			// adMonitorModelDao.insertBouncedata(insertList);
			//
			// adMonitorModelDao.callFunction(dateStr);

			file1flag = false;
			file2flag = false;
			file3flag = false;
			file4flag = false;

			response.getWriter().print("success");
		}

	}

	private static List<AdMonitorModel> mapTransitionList(Map<String, AdMonitorModel> map) {
		List<AdMonitorModel> list = new ArrayList<AdMonitorModel>();
		Iterator iter = map.entrySet().iterator(); // 获得map的Iterator
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			list.add((AdMonitorModel) entry.getValue());
		}
		return list;
	}

	private boolean importBounceFile(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws IOException, ParseException {
		String[] firstLine = null;
		String[] line = null;
		String content = null;
		String date = multipartRequest.getParameter("date");
		String date2 = null;
		for (String parts : date.split("-")) {
			date2 += parts + "/";
		}
		date2 = date2.substring(0, date2.length() - 1);
		// 获取多个file
		for (Iterator it = multipartRequest.getFileNames(); it.hasNext();) {
			String key = (String) it.next();
			MultipartFile upFile = multipartRequest.getFile(key);
			if (upFile.getOriginalFilename().length() > 0) {
				// String fileName = upFile.getOriginalFilename();
				BufferedReader br = new BufferedReader(new InputStreamReader(upFile.getInputStream(), "UTF-8"));
				if (br.ready()) {
					for (int i = 0; i < 3; i++) {
						br.readLine();
					}
					firstLine = br.readLine().split(",", -1);
					if (firstLine.length > 1 && (firstLine[1].substring(1, 11).equals(date)
							|| firstLine[1].substring(1, 11).equals(date2))) {
						for (int i = 0; i < 4; i++) {
							br.readLine();
						}
						while (br.ready()) {
							content = br.readLine();
							line = CSVFileUtil.fromCSVLinetoArray(content);
							if (content == "" || line.length < 1 || "".equals(line[0])) {
								return true;
							}

							String mic;
							if (line[1] != null && line[1].startsWith("\"")) {
								mic = line[1].substring(1, line[1].length() - 1);
							} else {
								mic = line[1];
							}

							AdMonitorModel model = insertMap.get(mic);
							if (model != null) {
								model.setBounce_times(Integer.parseInt(line[4]));
							} else {
								// response.getWriter().print("not_match");
								continue;
							}

						}

					} else {
						response.getWriter().print("wrong_date");
						return false;
					}
				}

			}
		}
		return true;
	}

}
