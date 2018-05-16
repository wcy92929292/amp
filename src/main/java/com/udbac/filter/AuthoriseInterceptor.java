package com.udbac.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.udbac.model.UserBean;

/**
 * 权限过滤器
 * @author LFQ
 * @date	2016-07-19
 */
public class AuthoriseInterceptor implements HandlerInterceptor{


	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
	    //判断是否登录
		Object obj = request.getSession().getAttribute("user");
		if(obj == null){
			response.getWriter().write("401");
			return false;
		}
		
		UserBean user = (UserBean)obj;
		
		//接口人
		if(!user.getROLE_NAME().contains("接口人") && !user.getROLE_NAME().contains("前端支撑")){
			response.getWriter().write("401");
			return false;
		}
		
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
