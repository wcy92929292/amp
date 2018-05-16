package com.udbac.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MonitorConfig {
	
	private static Properties configfile = null;

	static {
		InputStream in = MonitorConfig.class.getClassLoader()
				.getResourceAsStream("monitor.properties");
		configfile = new Properties();
		try {
			configfile.load(in);
			in.close();
		} catch (IOException e) {
			System.out.println("No config.properties defined error");
		}
	}

	public String getPropertiesByKey(String _key) {
		return configfile.getProperty(_key);
	}


}
