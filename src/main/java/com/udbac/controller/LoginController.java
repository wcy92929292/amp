package com.udbac.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.udbac.dao.LoginDao;
import com.udbac.model.UserBean;
import com.udbac.util.LogUtil;

/****
 * 
 * 用户登录
 * 
 * @author lp
 * @date 2016-04-06
 *
 */
@RestController
@RequestMapping("/amp")
public class LoginController {

	private LogUtil logUtil = new LogUtil(LoginController.class);
	@Autowired(required = true)
	private LoginDao loginDao;

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> userLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String,Object> map =new HashMap<>();
		HttpSession session = request.getSession();
		String checkCode = request.getParameter("check_code");
		String cc = (String) session.getAttribute("code");
		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		String code =checkCode.toUpperCase();//转为大写匹配
		if (cc != "" && !cc.equals(code)) {
			map.put("message", "验证码错误");
			//response.getWriter().print("验证码错误");
		} else {
			UserBean user = loginDao.getUserByUserName(username);
			if (user != null && user.getPASSWORD().equals(pwd)) {
				// 登录成功,数据存入Session
				session.setAttribute("user", user);
	              Cookie cookie = new Cookie("userName", user.getUSER_NAME());
	              //存1天
	              cookie.setMaxAge(60 * 60 * 24);
	              //cookie有效路径是网站根目录
	              cookie.setPath("/amp");
	              //向客户端写入
	              response.addCookie(cookie);
	              map.put("userId",user.getUSER_ID());
	              map.put("roleName", user.getROLE_NAME());
				// 判断用户角色，根据用户角色信息返回页面
				logUtil.logInfoCon("登录成功>>>>>>>>>>>>>" + user.getREAL_NAME() + user.getROLE_NAME());
				if (user.getROLE_NAME().contains("监测中心") || user.getROLE_NAME().contains("接口人")) {
					map.put("message", "index");
					//response.getWriter().print("index");
				}else if("客户".equals(user.getROLE_NAME())){
					map.put("message", "customer");
					//response.getWriter().print("customer");
				} else {
					map.put("message", "todo");
					//response.getWriter().print("todo");
				}
			} else {
				logUtil.logError("用户名或密码错误==>>>>>>>>>>>>>" + username + pwd);
				map.put("message", "用户名或密码错误");
				//response.getWriter().print("用户名或密码错误");
			}
		}
		return map;
	}
	/****
	 * 根据用户名查询数据库中是否有数据
	 * @param _username
	 * @return
	 * @author lp
	 * @throws Exception
	 * @date 2016-04-08
	 */
	
	@RequestMapping(value = "/QueryUserName.do",  method = RequestMethod.POST)
	public @ResponseBody Integer queryUsername(
			@RequestParam(value = "username") String  _username
			)throws Exception {
		Integer count = loginDao.QueryUserName(_username);
		try {
			if(count>0){
				return 1;
			}
		} catch (Exception e) {
			logUtil.logError(e.getMessage());
		}
		logUtil.logInfoCon(_username+"--用户名不存在");
		return 0;
		
	}
	/****
	 * 登录验证码生成
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @date 2016-04-06
	 * @user lp
	 */
	
	  private int width = 80;//定义图片的width
	  private int height = 28;//定义图片的height
	  private int codeCount = 4;//定义图片上显示验证码的个数
	  private int xx = 15;
	  private int fontHeight = 18;
	  private int codeY = 16;
	  char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
	      'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
	      'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	@RequestMapping(value = "login/checkcode.do", method = RequestMethod.GET)
	public @ResponseBody void checkcode(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
	

		    // 定义图像buffer
		    BufferedImage buffImg = new BufferedImage(width, height,
		       BufferedImage.TYPE_INT_RGB);
		    Graphics gd = buffImg.getGraphics();
		    // 创建一个随机数生成器类
		    Random random = new Random();
		    // 将图像填充为白色
		    gd.setColor(Color.WHITE);
		    gd.fillRect(0, 0, width, height);

		    // 创建字体，字体的大小应该根据图片的高度来定。
		    Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
		    // 设置字体。
		    gd.setFont(font);

		    // 画边框。
		    gd.setColor(Color.BLACK);
		    gd.drawRect(0, 0, width - 1, height - 1);

		    // 随机产生20条干扰线，使图象中的认证码不易被其它程序探测到。
		    gd.setColor(Color.BLACK);
		    for (int i = 0; i < 20; i++) {
		      int x = random.nextInt(width);
		      int y = random.nextInt(height);
		      int xl = random.nextInt(12);
		      int yl = random.nextInt(12);
		      gd.drawLine(x, y, x + xl, y + yl);
		    }

		    // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		    StringBuffer randomCode = new StringBuffer();
		    int red = 0, green = 0, blue = 0;

		    // 随机产生codeCount数字的验证码。
		    for (int i = 0; i < codeCount; i++) {
		      // 得到随机产生的验证码数字。
		      String code = String.valueOf(codeSequence[random.nextInt(36)]);
		      // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
		      red = random.nextInt(255);
		      green = random.nextInt(255);
		      blue = random.nextInt(255);

		      // 用随机产生的颜色将验证码绘制到图像中。
		      gd.setColor(new Color(red, green, blue));
		      gd.drawString(code, (i + 1) * xx, codeY);

		      // 将产生的四个随机数组合在一起。
		      randomCode.append(code);
		    }
		    // 将四位数字的验证码保存到Session中。
		    HttpSession session = req.getSession();
		    session.setAttribute("code", randomCode.toString());
		    System.out.println(randomCode.toString());

		    // 禁止图像缓存。
		    resp.setHeader("Pragma", "no-cache");
		    resp.setHeader("Cache-Control", "no-cache");
		    resp.setDateHeader("Expires", 0);

		    resp.setContentType("image/jpeg");

		    // 将图像输出到Servlet输出流中。
		    ServletOutputStream sos = resp.getOutputStream();
		    ImageIO.write(buffImg, "jpeg", sos);
		    sos.close();
		  
	}

	/***
	 * 用户退出页面请求
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/loginout.do", method = RequestMethod.POST)
	public @ResponseBody void userLoginOut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// response.sendRedirect("detail.do");
		System.out.println("退出页面请求");
		HttpSession session = request.getSession();
		session.invalidate();
		response.getWriter().print("success");
	}

	public static String toStringHex(String s) {
		System.out.println(s);
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword);// UTF-16le:Not

			return Base64.encodeBase64String(baKeyword);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	/**
	 * 判断session是否已经登录
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping("isLogin.do")
	public void  isLogin(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession();
		UserBean user = (UserBean)session.getAttribute("user");
		response.getWriter().write(user == null ? "null" :user.getREAL_NAME());
	}
	
	
}
