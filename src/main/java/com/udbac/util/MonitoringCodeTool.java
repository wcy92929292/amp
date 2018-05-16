package com.udbac.util;

import java.security.MessageDigest;

import org.junit.Test;

import sun.misc.BASE64Encoder;

/**
 * 监测代码生成工具
 * @author LFQ
 * @DATE 2016-04-22
 */
public class MonitoringCodeTool {

	
	/**
	 * 对字符串坚信 SHA256 散列，并进行加密，截取前20位数作为营销代码（加密）
	 * @param original
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("restriction")
	public static String createMic(String original) throws Exception {
		
		byte[] bs = original.getBytes();
		MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
		
		//sha256 散列
		sha256.update(bs);
		
		byte[] bs2 = sha256.digest();
		String encode = new BASE64Encoder().encode(bs2);
		encode = encode.replaceAll("[^a-zA-Z0-9]","");
		//base64 加密,并生成短代码
		return encode.substring(0, encode.length() > 20 ? 20: encode.length() );
	}//end Encodin()
	
	/**  拼接后端链接 
	 * 后端代码生成规则：
		1、链接中无任何[?#&]参数，则在尾部加上?WT.mc_id参数
    	2、链接中仅有[#]参数，则在#之前加上?WT.mc_id参数
    	3、链接中仅有[?]参数，则在尾部加上&WT.mc_id参数链接中同时有[?#]参数，则在#之前添加&WT.mc_id参数
	 **/
	public static String afterUrl(String url,String mic){
	
		int index1 = url.indexOf("?");
		int index2 = url.indexOf("#");
		
		StringBuffer sb = new StringBuffer(30);
		sb.append(url);
		
		//原有的连接中存在WT.mc_id,并且WT.mc_id 在 #之前
		if(url.contains("WT.mc_id") && url.indexOf("WT.mc_id") < index2){
			int start  = url.indexOf("WT.mc_id") + 9;
			int end = url.indexOf("&",start);
			//WT.mc_id 在 URL中后面没有参数
			if(end == -1){
				end = url.indexOf("#",start);
				if(end == -1){
					end = url.length()-1;
				}
			}
			sb.replace(start, end + 1, mic);
		}else if(index1==-1&&index2==-1){
			sb.append("?WT.mc_id=");
			sb.append(mic);
		}else if(index1==-1 && index2 > -1){
			sb.insert(index2,"?WT.mc_id="+mic);
		}else if(index1 > -1 && index2==-1){
			sb.append("&WT.mc_id=");
			sb.append(mic);
		}else{
			if(index1 > index2){
				sb.insert(index2,"?WT.mc_id="+mic);
			}else{
				sb.insert(index2,"&WT.mc_id="+mic);
			}
		}
		return sb.toString();
	}//end setAfterUrl()
	
	
	/**
	 * 生成点击监测代码
	 * 
	 * 点击代码生成规则：
		1、如果不支持点击，不出点击代码
		2、如果媒体名称中包含“新浪微博”
			http://s.trafficjam.cn/m,营销码,mo=__OS__&ns=__IP__&m1=__IMEI__&m2=__IDFA__&m3=__DUID__&m1a=__ANDROIDID__&m2a=__OPENUDID__&m9=__MAC1__&m9b=__MAC__&m1b=__AAID__&m1c=__ANDROIDID1__&m9c=__ODIN__&ts=__TS__&st=__STS__&uid={wb_uid_md5}&uuid=__UUID__
		3、如果（终端类型）terminalType 为  SEM,SEM_PC,SEM_MOBILE  则点击代码为后端代码
		4、如果（终端类型）terminalType 为  APP 或 MOBILE则 媒体名称中包含“咪咕”
			http://s.trafficjam.cn/m,营销码,mo=__OS__&ns=__IP__&m1=__IMEI__&m2=__IDFA__&m3=__DUID__&m1a=__ANDROIDID__&m2a=__OPENUDID__&m9=__MAC1__&m9b=__MAC__&m1b=__AAID__&m1c=__ANDROIDID1__&m9c=__ODIN__&ts=__TS__&st=__STS__&uuid=__UUID__
		5、其它终端类型 则
			http://s.trafficjam.cn/m,营销码,uuid=__UUID__
		6、如果媒体名称中包含 "多盟",则在监测方案添加  &schid=__SEARCH_ID__
	 * 
	 * 	7、优酷：排期表生成代码默认给https://前缀
		
		8、针对MOBILE，APP 端 ,有字符限制的点位：
			mo/m2/m1c/m1a/m9b/m9/m2a/uid/m1/m3/m1b/m9c的原则，插码宏的重要性，从左至右递减，
			
		9、新浪白金点位自即日（2017.6月6日）起将监测域名变更为：https://s.udbac.com。
		10、新浪所有wap端点位（2017.9.6）变更监测域名为：https://s.udbac.com。 变更by liyan
		    监测代码为：mo=__OS__&ns=__IP__&m1=__IMEI__&m2=__IDFA__&m3=__DUID__&m1a=__ANDROIDID__&m2a=__OPENUDID__&m9=__MAC1__&m9b=__MAC__&m1b=__AAID__&m1c=__ANDROIDID1__&m9c=__ODIN__&ts=__TS__&st=__STS__&uuid=__UUID__

	 * @param
	 * 	url:目标链接地址
	 * 	mic:营销短代码
	 * 	mediaName:媒体名称
	 *  pointLocation:投放位置
	 *  putFunction:投放形式
	 * 	supportClick:是否支持点击（是/否）
	 * 	terminalType:终端类型
	 * 
	 * @return 生成的点击代码
	 */
	public static String clickUrl(String url,String mic,String mediaName,String pointLocation ,String putFunction,
			String supportClick,String terminalType){
		
		StringBuffer sb = new StringBuffer(50); 
		
		if("0".equalsIgnoreCase(supportClick) || "否".equalsIgnoreCase(supportClick) || "".equalsIgnoreCase(supportClick)){
			return null;
		}
		
		terminalType = terminalType.toUpperCase();
		
		if("SEM".equals(terminalType) || "SEM_PC".equals(terminalType) || "SEM_MOBILE".equals(terminalType)){
			return afterUrl(url, mic);
		}
		
		// 9.7变更 by liyan
		sb.append(setDNS(mediaName, pointLocation, terminalType)).append("m,");
		
		sb.append(mic);
		
		if(mediaName.contains("新浪微博")){
			sb.append(",mo=__OS__&ns=__IP__&m1=__IMEI__&m2=__IDFA__&m3=__DUID__&m1a=__ANDROIDID__&m2a=__OPENUDID__&m9=__MAC1__&m9b=__MAC__&m1b=__AAID__&m1c=__ANDROIDID1__&m9c=__ODIN__&ts=__TS__&st=__STS__&uid={wb_uid_md5}&uuid=__UUID__");
		}
		else if(mediaName.contains("咪咕") || "APP".equals(terminalType) || "MOBILE".equals(terminalType)){
			sb.append(",mo=__OS__&ns=__IP__&m1=__IMEI__&m2=__IDFA__&m3=__DUID__&m1a=__ANDROIDID__&m2a=__OPENUDID__&m9=__MAC1__&m9b=__MAC__&m1b=__AAID__&m1c=__ANDROIDID1__&m9c=__ODIN__&ts=__TS__&st=__STS__&uuid=__UUID__");
		}
		else if (mediaName.contains("新浪") && "WAP".equals(terminalType) || "wap".equals(terminalType)) {
			sb.append(",mo=__OS__&ns=__IP__&m1=__IMEI__&m2=__IDFA__&m3=__DUID__&m1a=__ANDROIDID__&m2a=__OPENUDID__&m9=__MAC1__&m9b=__MAC__&m1b=__AAID__&m1c=__ANDROIDID1__&m9c=__ODIN__&ts=__TS__&st=__STS__&uuid=__UUID__");
		}else{
			sb.append(",uuid=__UUID__");
		}
		
//		6、如果媒体名称中包含 "多盟",则在监测方案安添加  &schid=__SEARCH_ID__
		if(mediaName.contains("多盟")){
			sb.append("&schid=__SEARCH_ID__");
		}
		
		//百度-原生信息流，截取100字符，2017-05-23
		if(mediaName.equals("百度") && putFunction.contains("原生信息流")){
			return filterCharNum(sb.toString(),100);
		}
		
		return sb.toString();
	}//end clickUrl()
	
	
	/**
	 * 生成曝光监测代码
	 * 
	 * 曝光代码生成规则：
		1、如果不支持曝光，不出曝光代码
		2、如果媒体名称中包含“新浪微博”  则
			http://s.trafficjam.cn/s,营销码,mo=__OS__&ns=__IP__&m1=__IMEI__&m2=__IDFA__&m3=__DUID__&m1a=__ANDROIDID__&m2a=__OPENUDID__&m9=__MAC1__&m9b=__MAC__&m1b=__AAID__&m1c=__ANDROIDID1__&m9c=__ODIN__&ts=__TS__&st=__STS__&uid={wb_uid_md5}&uuid=__UUID__
		3、如果（终端类型）terminalType 为  APP 或 MOBILE 或 媒体名称中包含“咪咕”
			http://s.trafficjam.cn/s,营销码,mo=__OS__&ns=__IP__&m1=__IMEI__&m2=__IDFA__&m3=__DUID__&m1a=__ANDROIDID__&m2a=__OPENUDID__&m9=__MAC1__&m9b=__MAC__&m1b=__AAID__&m1c=__ANDROIDID1__&m9c=__ODIN__&ts=__TS__&st=__STS__&uuid=__UUID__
		4、其它终端类型 则
			http://s.trafficjam.cn/s,营销码,uuid=__UUID__
		5、如果媒体名称中包含 "多盟",则在监测方案安添加  &schid=__SEARCH_ID__
	 * 
	 * 	6、优酷：排期表生成代码默认给https://前缀
		7、针对MOBILE，APP 端 ,有字符限制的点位：
			mo/m2/m1c/m1a/m9b/m9/m2a/uid/m1/m3/m1b/m9c的原则，插码宏的重要性，从左至右递减，
			
		8、新浪白金点位自即日（2017.6月6日）起将监测域名变更为：https://s.udbac.com。
		9、新浪所有wap端点位（2017.9.6）变更监测域名为：https://s.udbac.com。 变更by liyan
    	监测代码为：mo=__OS__&ns=__IP__&m1=__IMEI__&m2=__IDFA__&m3=__DUID__&m1a=__ANDROIDID__&m2a=__OPENUDID__&m9=__MAC1__&m9b=__MAC__&m1b=__AAID__&m1c=__ANDROIDID1__&m9c=__ODIN__&ts=__TS__&st=__STS__&uuid=__UUID__

	 * @param
	 * 	mic:营销短代码
	 * 	mediaName:媒体名称
	 *  pointLocation:投放位置
	 *  putFunction:投放形式
	 * 	supportExposure:是否支持曝光（是/否）
	 * 	terminalType:终端类型
	 * 
	 * @return 生成的曝光监测代码
	 */
	public static String exposureUrl(String mic,String mediaName,String pointLocation,String putFunction,
			String supportExposure,String terminalType){
		
		StringBuffer sb = new StringBuffer(50); 
		
		if("0".equalsIgnoreCase(supportExposure) || "否".equalsIgnoreCase(supportExposure) || "".equalsIgnoreCase(supportExposure)){
			return null;
		}
		
		terminalType = terminalType.toUpperCase();
		
		//9.7变更 by liyan
		//sb.append(setDNS(mediaName, pointLocation)).append("s,");
		sb.append(setDNS(mediaName, pointLocation, terminalType)).append("s,");
		
		sb.append(mic);
		
		if(mediaName.contains("新浪微博")){
			sb.append(",mo=__OS__&ns=__IP__&m1=__IMEI__&m2=__IDFA__&m3=__DUID__&m1a=__ANDROIDID__&m2a=__OPENUDID__&m9=__MAC1__&m9b=__MAC__&m1b=__AAID__&m1c=__ANDROIDID1__&m9c=__ODIN__&ts=__TS__&st=__STS__&uid={wb_uid_md5}&uuid=__UUID__");
		}
		else if("APP".equals(terminalType) || "MOBILE".equals(terminalType) || mediaName.contains("咪咕")){
			sb.append(",mo=__OS__&ns=__IP__&m1=__IMEI__&m2=__IDFA__&m3=__DUID__&m1a=__ANDROIDID__&m2a=__OPENUDID__&m9=__MAC1__&m9b=__MAC__&m1b=__AAID__&m1c=__ANDROIDID1__&m9c=__ODIN__&ts=__TS__&st=__STS__&uuid=__UUID__");
		}
		else if (mediaName.contains("新浪") && "WAP".equals(terminalType) || "wap".equals(terminalType)) {
			sb.append(",mo=__OS__&ns=__IP__&m1=__IMEI__&m2=__IDFA__&m3=__DUID__&m1a=__ANDROIDID__&m2a=__OPENUDID__&m9=__MAC1__&m9b=__MAC__&m1b=__AAID__&m1c=__ANDROIDID1__&m9c=__ODIN__&ts=__TS__&st=__STS__&uuid=__UUID__");
		} else{
			sb.append(",uuid=__UUID__");
		}
		
//		5、如果媒体名称中包含 "多盟",则在监测方案添加  &schid=__SEARCH_ID__
		if(mediaName.contains("多盟")){
			sb.append("&schid=__SEARCH_ID__");
		}
		
		//百度-原生信息流，截取100字符，2017-05-23
		if(mediaName.equals("百度") && putFunction.contains("原生信息流")){
			return filterCharNum(sb.toString(),100);
		}
		
		return sb.toString();
	}//end clickUrl()
	
	/**
	 * 删除URL中的WT.mc_id参数
	 * @param args
	 */
	public static String removeUrlWt(String url) {
		
		StringBuffer sb = new StringBuffer(50);
		sb.append(url);
		
		int start; //# &
		int end;
		int tmp;
		
		while(sb.toString().contains("?WT.mc_id=") || sb.toString().contains("&WT.mc_id=")){
			start = (tmp = sb.indexOf("?WT.mc_id=")) == -1 ? (tmp = sb.indexOf("&WT.mc_id=") + 1) : tmp + 1;
			
			end = (tmp = sb.indexOf("#",start)) == -1 ? url.length() : tmp;
			end = (tmp = sb.indexOf("&",start)) == -1 ? end : (tmp < end ? tmp + 1 : end );
			sb.replace(start, end, "");
		}
		
		for(int i =0;i<3;i++){
			//去掉最末尾的?
			if((tmp = sb.lastIndexOf("?")) == sb.length() - 1){
				sb.replace(tmp, sb.length(), "");
			}
			
			//去点没有参数是否的"#
			if((tmp = sb.lastIndexOf("#")) == sb.length() - 1){
				sb.replace(tmp, sb.length(), "");
			}
			
			//去点没有参数是否的&
			if((tmp = sb.lastIndexOf("&")) == sb.length() - 1){
				sb.replace(tmp, sb.length(), "");
			}
			
			//去掉&#相邻 中的  &
			if((tmp = sb.indexOf("&#")) >= 0){
				sb.replace(tmp, tmp + 1, "");
			}
		}
		
		return sb.toString();
	}//end removeUrlWt()
	
	
//	http://s.trafficjam.cn/m,w1z2ASCPnDLCf7PaZTGm,
//	mo=__OS__
//	&ns=__IP__
//	&m1=__IMEI__
//	&m2=__IDFA__
//	&m3=__DUID__
//	&m1a=__ANDROIDID__
//	&m2a=__OPENUDID__
//	&m9=__MAC1__
//	&m9b=__MAC__
//	&m1b=__AAID__
//	&m1c=__ANDROIDID1__
//	&m9c=__ODIN__
//	&ts=__TS__
//	&st=__STS__
//	&uuid=__UUID__
	//可去参数配置表
	private static final String[] params = {"&st=__STS__","&ts=__TS__","&m9c=__ODIN__","&m1b=__AAID__","&m3=__DUID__",
		"&m1=__IMEI__","&uuid=__UUID__","&m2a=__OPENUDID__","&m9=__MAC1__","&m9b=__MAC__","&m1a=__ANDROIDID__",
		"&m1c=__ANDROIDID1__","&m2=__IDFA__"}; 
	
	/**
	 * 缩减字符串字符长度限制。
	 * 
	 * 8、针对MOBILE，APP 端 ,有字符限制的点位：
		ODIN、STS、DUID、AAID，可以删,
		mo/m2/m1c/m1a/m9b/m9/m2a/uid/m1/m3/m1b/m9c的原则，插码宏的重要性，从左至右递减，
	 * @param url
	 * @return
	 */
	public static String filterCharNum(String url,Integer maxCharNum){
		
		for(int i=0;i<params.length;i++){
			if(url.length() < maxCharNum){
				break;
			}else{
				url = url.replace(params[i],"");
			}
		}
		return url;
	}//end filterCharNum()
	
	
	/**
	 * 设置监测地址域名 
	 * @param mediaName		媒体名称
	 * @param pointLocation	投放位置
	 * @param terminalType	终端
	 * @return
	 */
	public static String setDNS(String mediaName,String pointLocation, String terminalType){
		if(mediaName != null && mediaName.contains("优酷")){
			return "https://s.trafficjam.cn/";
		}
		else if(mediaName != null && mediaName.contains("新浪") 
				&& pointLocation != null && pointLocation.contains("白金")){
			return "https://s.udbac.com/";
		} else if (mediaName != null && mediaName.contains("新浪")
				&& ("WAP".equals(terminalType) || "wap".equals(terminalType))) {
			return "https://s.udbac.com/";
		}
		else{
			return "http://s.trafficjam.cn/";
		}
	}//end setDNS
	
	public static void main(String[] args) throws Exception {
//		String url = "http://music.migu.cn/?loc=A1Z1Y1L5N1&locno=0&WT.mc_id=PZgGAniD5jxqhuTKmjZi#/album/1004383537/P7Z1Y1L3N1/1/001002C";
//		url = removeUrlWt(url);
//		System.out.println(url);
		
//		url = afterUrl(url, "1111111111111111");
//		System.out.println(url);

//		System.out.println(createMic("1231adfqw123-"));
//		http://s.trafficjam.cn/s,t0NiT3gcns1XPgkFMkiW,mo=__OS__&ns=__IP__&m1=__IMEI__&m2=__IDFA__&m3=__DUID__&m1a=__ANDROIDID__&m2a=__OPENUDID__&m9=__MAC1__&m9b=__MAC__&m1b=__AAID__&m1c=__ANDROIDID1__&m9c=__ODIN__&ts=__TS__&st=__STS__
		
		/*String url = "http://s.trafficjam.cn/m,w1z2ASCPnDLCf7PaZTGm,mo=__OS__&ns=__IP__&m1=__IMEI__&m2=__IDFA__&m3=__DUID__&m1a=__ANDROIDID__&m2a=__OPENUDID__&m9=__MAC1__&m9b=__MAC__&m1b=__AAID__&m1c=__ANDROIDID1__&m9c=__ODIN__&ts=__TS__&st=__STS__&uuid=__UUID__";
		url = filterCharNum(url,100);
		System.out.println(url.length());
		System.out.println(url);*/
		String url = "http://music.migu.cn/?loc=A1Z1Y1L5N1&locno=0&WT.mc_id=PZgGAniD5jxqhuTKmjZi#/album/1004383537/P7Z1Y1L3N1/1/001002C";
		String mic = "isQrS1ZmquCr8tUASjgD";
		String mediaName = "咪咕";
		String pointLocation = "";
		String putFunction = "";
		String supportClick = "是";
		String terminalType = "WAP";
		System.out.println(clickUrl(url,mic,mediaName,pointLocation ,putFunction,
				supportClick,terminalType));
		System.out.println(exposureUrl(mic,mediaName,pointLocation,putFunction,
				supportClick,terminalType));
	}
}
