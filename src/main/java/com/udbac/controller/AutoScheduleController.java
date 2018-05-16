package com.udbac.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.udbac.entity.AutoScheduleInfo;
import com.udbac.service.AutoScheduleService;

@RequestMapping("/auto")
@Controller
public class AutoScheduleController {

	@Autowired
	AutoScheduleService service;

	@ResponseBody
	@RequestMapping("/schedule.do")
	public List<AutoScheduleInfo> getList(@RequestParam("sdate") String sdate, @RequestParam("edate") String edate,
			HttpServletResponse response ) {

		response.setContentType("text/plain;charset=UTF-8");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date startDate = null;
		Date endDate = null;
		
		List<AutoScheduleInfo> all = null;
		try {
			if ("".equals(sdate) || null == sdate || "".equals(edate) || null == edate) {
				startDate = new Date();
				endDate = new Date();
			} else {
				startDate = sdf.parse(sdate);
				endDate = sdf.parse(edate);
			}
			
			System.out.println(startDate);
			System.out.println(endDate);
			all = service.listAll(startDate, endDate);
			
			System.out.println(all);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return all;
	}
}
