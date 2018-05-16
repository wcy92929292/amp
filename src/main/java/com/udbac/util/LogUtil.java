package com.udbac.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/***
 * 公共日志处理
 * 
 * @author lily
 * @date 2016-04-07
 *
 */
public class LogUtil {

	// private static Logger log = Logger.getLogger(LogUtil.class);
	private Log log = null;

	public LogUtil(Class<?> clazz) {
		log = LogFactory.getLog(clazz);
	}

	/****
	 * Message Info日志
	 * 
	 * @param messId
	 *            message编号
	 */
	public void logInfo(String messId) {
		String message = PropUtil.getMessage(messId);
		log.info("InfoLog执行=========MessageID为：" + messId + "，Message内容为：" + message);
	}

	/****
	 * Message Info日志
	 * 
	 * @param messId
	 *            message编号
	 * @param Params
	 *            message参数（数组）
	 */
	public void logInfo(String messId, String[] Params) {
		// String[] par = { "参数1", "参数2", "参数3" };
		String message = PropUtil.getMessage(messId, Params);
		log.info("InfoLog执行=========MessageID为：" + messId + ",Message参数内容为：" + message);
	}

	/****
	 * 普通信息日志
	 * 
	 * @param content
	 *            日志具体内容
	 */
	public void logInfoCon(String content) {
		log.info("InfoLog执行=========" + content);
	}

	/***
	 * Message Error日志
	 * 
	 * @param messId
	 *            message编号
	 */
	public void logError(String messId) {
		String message = PropUtil.getMessage(messId);
		log.error("ErrorLog执行=========MessageID为：" + messId + "，Message内容为：" + message);
	}
	/***
	 * Message Error日志
	 * 
	 * @param messId
	 *            message编号
	 */
	public void logErrorExc(Exception exception) {
		log.error("ErrorLog执行=========Exception内容为：" + exception);
	}
	
	/****
	 * Message Error日志
	 * 
	 * @param messId
	 *            message编号
	 * @param Params
	 *            message参数（数组）
	 */
	public void logError(String messId, String[] Params) {
		// String[] par = { "参数1", "参数2", "参数3" };
		String message = PropUtil.getMessage(messId, Params);
		log.info("InfoLog执行=========MessageID为：" + messId + ",Message参数内容为：" + message);
	}

	/***
	 * Message Debug日志
	 * 
	 * @param messId
	 *            message编号
	 */
	public void logDebug(String messId) {
		String message = PropUtil.getMessage(messId);
		log.error("DebugLog执行=========MessageID为：" + messId + "，Message内容为：" + message);
	}

	/***
	 * 处理调试日志 (开始)
	 */
	public void logDebugSt() {
		log.debug("DebugLog执行=========Start==================");
	}

	/***
	 * 处理调试日志 (结束)
	 */
	public void logDebugEd() {
		log.error("DebugLog执行=========END==================");
	}

 

	/*
	 * public static void main(String[] args) { log.info("测试info"); try { //
	 * PropUtil p=new PropUtil(); int s2 = 1 + 6; logInfo("OLM-EA00001");
	 * log.info("两数相加之和为=======" + s2); log.debug("调试结果为=========" + s2); int s1
	 * = 3 / 0; } catch (Exception e) { log.error("测试error=============" + e); }
	 * }
	 */

}
