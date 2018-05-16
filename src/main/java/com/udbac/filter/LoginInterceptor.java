package com.udbac.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登陆过滤器
 * @author LFQ
 * @date	2016-05-09
 */
public class LoginInterceptor implements HandlerInterceptor{


	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
//		 	String requestUri = request.getRequestURI();  
//	        String contextPath = request.getContextPath();  
//	        String url = requestUri.substring(contextPath.length()); 
//		
//	       System.out.println(requestUri);
//	       System.out.println(contextPath);
//	       System.out.println(url);
		
		Object user = request.getSession().getAttribute("user");
		if(user == null){
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
