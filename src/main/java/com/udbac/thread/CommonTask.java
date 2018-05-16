package com.udbac.thread;

import java.lang.reflect.Method;
import java.util.TimerTask;

public class CommonTask<T> extends TimerTask {
	
	private T target;
	private String methodName;
	private Class<?>[] params;
	private Object[] paramsValues;
	private Method method;
	
	CommonTask(T target,String methodName,Class<?>[] params,Object[] paramsValues){
		this.target = target;
		this.methodName = methodName;
		this.params = params;
		this.paramsValues = paramsValues;
		bind();
	}
//	urlCheckThread.check();
	
	private void bind(){
		try {
			Class<? extends Object> clazz = target.getClass();
			this.method = clazz.getMethod(methodName, params);
			method.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			System.out.println(method);
			System.out.println(target);
			method.invoke(target, paramsValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
