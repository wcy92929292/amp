package com.udbac.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.udbac.entity.IndexBean;
import com.udbac.model.UserBean;
import com.udbac.service.IndexService;
import com.udbac.util.DecimalTool;
import com.udbac.util.LogUtil;

/****
 * 概览
 * 
 * @author lp
 * @date 2016-04-25
 */
@RestController
@RequestMapping("/index")
public class IndexController {

	private LogUtil logUtil = new LogUtil(IndexController.class);

	@Autowired(required = true)
	private IndexService indexService;

	@RequestMapping(value = "/index.do", method = RequestMethod.POST)
	public @ResponseBody List<Object> getListSite(@RequestParam(value = "sdate") String _sdate,
			@RequestParam(value = "cust") String _cust,HttpServletRequest request) throws UnsupportedEncodingException {
		
		//限制客户登陆的时候，只能查看自己的信息
		Object obj = request.getSession().getAttribute("user");
		if(obj instanceof UserBean){
			UserBean user = (UserBean)obj;
			if("客户".equals(user.getROLE_NAME())){
				_cust = user.getCustomerName();
			}
		}
		
		List<Object> list = new ArrayList<>();
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("sdate", _sdate);
		map.put("cust", _cust);
		
		List<Object> actlist = null;
		int pv = 0;// 曝光PV
		int sumPV = 0; // 曝光PV总计
		int uv = 0;// 曝光UV
		int pv2 = 0;// 点击PV
		String se = "";// 点位是否支持曝光
		String sc = "";// 点位是否支持点击
		long eavg = 0;// 曝光预估
		long cavg = 0;// 点击预估
		Map<String, String> map2 = null;
		String activityCode = "";// 当前活动编号
		String activityCodeNext = "";// 下一个活动编号
		String activityName = "";// 活动名称
		String customerName = "";// 投放单位
		int pnum = 0;// 定义问题数
		try {
			actlist = indexService.QueryList(map);
			if (actlist.toString() != "" && actlist.toString() != null) {
				for (int i = 0; i < actlist.size(); i++) {// 遍历数据，计算问题数
					map2 = new HashMap<String, String>();
					IndexBean ib = (IndexBean) actlist.get(i);
					if (i < actlist.size() - 1) {
						IndexBean ibNext = (IndexBean) actlist.get(i + 1);
						activityCodeNext = ibNext.getActivity_code();
					}
					activityCode = ib.getActivity_code();
					activityName = ib.getActivity_name();
					customerName = ib.getCustomer_name();
					pv = ib.getMonitor_exposure_pv();// 曝光PV
					sumPV = sumPV + pv;
					uv = ib.getMonitor_exposure_uv();// 曝光UV
					pv2 = ib.getMonitor_click_pv();// 点击PV
					se = ib.getSupport_exposure();// 点位是否支持曝光
					sc = ib.getSupport_click();// 点位是否支持点击
					// 曝光预估
					String exp_avg = ib.getExposure_avg().trim();
					if (DecimalTool.isNumeric(exp_avg)) {
						eavg = Long.parseLong(exp_avg);
					} else {
						eavg = 0;
					}
					// 点击预估 
					String clickAvg = ib.getClick_avg().trim();
					if (DecimalTool.isNumeric(clickAvg)) {
						cavg = Long.parseLong(clickAvg);						
					} else {
						cavg = 0;
					}
					if (pv < uv) {// 曝光PV<曝光UV
						pnum += 1;
					}
					if ("1".equals(se) && pv == 0) {// 支持曝光，但是曝光PV为0
						pnum += 1;
					} else if ("0".equals(se) && pv > 10) {// 不支持曝光，但曝光PV>10
						pnum += 1;
					}
					if ("1".equals(sc) && pv2 == 0) {// 支持点击 ，但是点击PV为0
						pnum += 1;
					} else if ("0".equals(sc) && pv2 > 10) {// 不支持点击，但是点击PV>10
						pnum += 1;
					}
					if (pv < (eavg * 0.3)) {// 曝光PV小于曝光预估的30%
						pnum += 1;
					} else if (pv > (eavg * 10)) {// 曝光PV大于曝光预估* 10
						pnum += 1;
					}
					if (pv2 < (cavg * 0.3)) {// 点击PV小于点击预估的30%
						pnum += 1;
					} else if (pv2 > (cavg * 10)) {// 点击PV大于点击预估 * 10
						pnum += 1;
					}
					if ("1".equals(sc) && "1".equals(se) && pv2 > pv) { // 点击和曝光都支持，点击PV大于曝光PV
						pnum += 1;
					}
					if (!activityCode.equals(activityCodeNext) || i == actlist.size() - 1) {
						map2.put("activityCode", activityCode);
						map2.put("activityName", activityName);
						map2.put("customerName", customerName);
						map2.put("pv", "" + sumPV);
						pnum = ib.getException() + pnum;
						map2.put("pnum", "" + pnum);
						sumPV = 0;
						pnum = 0;
						list.add(map2);
					}
				}
				return list;
			}
		} catch (Exception e) {
			logUtil.logErrorExc(e);
			;
			e.printStackTrace();
		}
		return actlist;

	}

}
