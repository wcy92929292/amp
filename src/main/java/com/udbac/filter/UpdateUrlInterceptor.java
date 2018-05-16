package com.udbac.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.udbac.model.UserBean;

/**
 * 登陆过滤器
 * @author LFQ
 * @date	2016-05-29
 */
public class UpdateUrlInterceptor implements HandlerInterceptor{


	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String requestUri = request.getRequestURI();  
	       
	    String parameter = request.getParameter("operation");
	       
	    //判断是否登录
		Object obj = request.getSession().getAttribute("user");
		if(obj == null){
			response.getWriter().write("401");
			return false;
		}
		
		UserBean user = (UserBean)obj;
		
		if(requestUri.contains("findUrlByActCode.do")){
			//如果是接口人，则有权利执行新增操作   或者  是后端人员，则有权利执行审核操作
			if(!("0".equals(parameter) && user.getROLE_NAME().contains("接口人")) && 
					!("1".equals(parameter) && user.getROLE_NAME().contains("后端支撑"))){
				response.getWriter().write("401");
				return false;
			}
	    }else if(requestUri.contains("newUrl.do") || requestUri.contains("update.do")){
	    	if(!user.getROLE_NAME().contains("接口人")){
	    		return false;
	    	}
	    }else if(requestUri.contains("checkUrl.do")){
	    	if(!user.getROLE_NAME().contains("后端支撑")){
	    		return false;
	    	}
	    }else{
	    	response.getWriter().write("404");
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
