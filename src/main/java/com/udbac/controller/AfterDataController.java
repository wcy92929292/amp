package com.udbac.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.entity.AfterData;
import com.udbac.service.AfterDataService;

@Controller
@RequestMapping("/after1")
public class AfterDataController {

	@Autowired
	AfterDataService service;

	@ResponseBody
	@RequestMapping("/list.do")
	public String list(@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate, @RequestParam(value = "mic", required = true) String mic) throws IOException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String[] split = mic.split("\n");
		List<String> miclist = Arrays.asList(split);

		List<AfterData> list = null;
		try {
			list = service.list(sdf.parse(startDate), sdf.parse(endDate), miclist);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf1= new SimpleDateFormat("yyyyMMddHHmmss");
		return exportCsv("/data/export/backend"+sdf1.format(new Date())+".csv", list);
	}

	public static String exportCsv(String savePath, List<AfterData> dataList) throws IOException {
		
		File file=new File(savePath);
		if(!file.exists()){
			file.createNewFile();
		}

		FileOutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			out = new FileOutputStream(savePath);
			osw = new OutputStreamWriter(out);
			bw = new BufferedWriter(osw);
			bw.write("daytime,");
			bw.write("mic,");
			bw.write("visit,");
			bw.write("visitor,");
			bw.write("click,");
			bw.write("pageview,");
			bw.write("bounce_times,");
			bw.write("timeSpent,");
			bw.newLine();
			
			if (dataList != null && !dataList.isEmpty()) {
				for (AfterData data : dataList) {
					bw.write(data.toString());
					bw.newLine();
				}
			}
		} catch (Exception e) {
		} finally {
			if (bw != null) {
				try {
					bw.close();
					bw = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (osw != null) {
				try {
					osw.close();
					osw = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return savePath;
	}

}
