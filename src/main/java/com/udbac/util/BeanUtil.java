package com.udbac.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Bean工具类
 * @author LFQ
 */
public class BeanUtil {
	
	/**
	 * 使用对象导航注入属性
	 * @param rootObj	要被注入的实例对象
	 * @param value		注入的值
	 * @param fieldName	对象属性名称
	 * @return
	 * @throws Exception
	 * 
	 * eg:	setProperties(instance, "湖北移动", "media.mediaName");
	 */
	public static Object setProperties(Object rootObj,Object value,String fieldName) throws Exception{
		return setProperties(rootObj, value, 0, fieldName.split("\\."));
	}
	
	/**
	 * 使用对象导航注入属性
	 * @param rootObj	要被注入的实例对象
	 * @param value		注入的值
	 * @param index		对象属性导航的位置，从0起始
	 * @param fieldName	对象属性数组
	 * @return
	 * @throws Exception
	 * 
	 * eg:  setProperties(instance, "湖北移动",0, "media.mediaName".split("\\."));
	 */
	public static Object setProperties(Object rootObj,Object value,int index,String ... fieldName) throws Exception{
		
		if("".equals(fieldName[index])){
			return rootObj;
		}
		
		Class<? extends Object> parentClass = rootObj.getClass();
		//根据属性名称，获取属性
		Field field = parentClass.getDeclaredField(fieldName[index]);
		
		//获取属性的类型
		Class<?> type = field.getType();
		
		//属性名称首字母大写
		String fieldStarts = String.valueOf(field.getName().charAt(0)).toUpperCase() + field.getName().substring(1);
		
		//获取当前属性值
		Method getMethod = parentClass.getMethod("get" + fieldStarts);
		Object object = getMethod.invoke(rootObj);
		if(object == null){
			try{object = type.newInstance();}
			catch(Exception e){
				try{object = type.getConstructor(value.getClass()).newInstance(value);}catch(Exception e1){}
			}
		}
		
		//设置属性值
		Object properties = value;
		if(fieldName.length > index + 1){
			properties = setProperties(object,value,++index,fieldName);
		}
		
		properties = type.equals(Integer.class) ? Integer.parseInt((String)properties) : 
			type.equals(Double.class) ? Double.parseDouble((String)properties) : 
			type.equals(Long.class) ? Long.parseLong((String)properties) : 
			type.equals(Byte.class) ? Byte.parseByte((String)properties) :
			type.equals(Float.class) ? Float.parseFloat((String)properties) : 
			type.equals(Short.class) ? Short.parseShort((String)properties) : 
			type.equals(Boolean.class) ? Boolean.parseBoolean((String)properties) : properties;
		
		//获取属性的的SET函数
		Method setMethod = parentClass.getMethod("set" + fieldStarts, type);
	
		//将属性值注入
		setMethod.invoke(rootObj, properties);
	
		return rootObj;
	}//end setProperties

	
	/**
	 * 调用某个对象的方法
	 * @param obj	对象
	 * @param clazz	对象的类型
	 * @param methodName	方法名称
	 * @param parameterTypes	参数类型列表
	 * @param args		参数值
	 * @return	返回值
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Object invokeDeclaredMethod(Object obj,Class<?> clazz,String methodName,Class<?> []parameterTypes,Object...args
			) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
		method.setAccessible(true);
		
		return method.invoke(obj, args);
	}//end invokeDeclaredMethod
	
}
