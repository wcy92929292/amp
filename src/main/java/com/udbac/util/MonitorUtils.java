package com.udbac.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.udbac.model.ExpolureModel;

public class MonitorUtils {

	private String url;
	private String username;
	private String password;

	private Connection conn = null;
	private MonitorConfig config = null;

	public MonitorUtils() {
		// TODO Auto-generated constructor stub
		config = new MonitorConfig();
		url = config.getPropertiesByKey("jdbc.url");
		username = config.getPropertiesByKey("jdbc.username");
		password = config.getPropertiesByKey("jdbc.password");
	}

	public MonitorUtils(String _url, String _username, String _password) {
		// TODO Auto-generated constructor stub
		this.url = _url;
		this.username = _username;
		this.password = _password;

	}
	
	public void updateMonitor()
	{
		
		String sql=config.getPropertiesByKey("update_exposure");
		
		System.out.println("sum sql ---->"+sql);
		this.execute(sql);
	}

	/**
	 * 插入小时数据
	 * @param sql
	 * @param sqllist
	 */
	public void insertDB(List<String> sqlList) {
		Statement stmt = null;
//		ResultSet rs = null;
//		String[] result = null;
		
		
		try {
			
			conn = DriverManager.getConnection(url, username, password);
			ExpolureModel model = null;
			stmt = conn.createStatement();
			
			for (int i = 0; i < sqlList.size(); i++) {
				
			
				stmt.executeUpdate(sqlList.get(i));
				
//				if(i%100 == 0){
//					
//				}
				
			}
			
			// conn.commit();
			System.out.println("import sql ok....");

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			

			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public void execute(String sql) {
		Connection conn = null;
		Statement stmt = null;
		try {

			conn = DriverManager.getConnection(url, username, password);
			conn.setAutoCommit(false);    
			stmt = conn.createStatement();
			System.out.println("execute the sql   >>>>>>> " + sql);
			stmt.execute(sql);
			conn.commit();   

		} catch (Exception e) {
			System.out.println("the execute error >>>>>>> " + e.getMessage());
		} finally {

			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
