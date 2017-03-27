package com.spinach.web.constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseConstants {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseConstants.class);
	private static String fileName = "WEB-INF/etc/conf/core/app-core.properties";
	private static Properties properties = getProperties(fileName);

	public static final String app_temp_path = properties.getProperty("app.temp.path");
	public static final String app_server_domain = properties.getProperty("app.server.domain");
	public static final String app_server_www = properties.getProperty("app.server.www");
	public static final String app_server_name = properties.getProperty("app.server.name");
	
	public static Properties getProperties(String fileName) {
		LOGGER.info("开始读取文件【{}】...", new Object[] { fileName });
		InputStream is = BaseConstants.class.getClassLoader().getResourceAsStream(fileName);
		Properties properties = new Properties();
		try {
			properties.load(new InputStreamReader(is, "UTF-8"));
			if (is != null)
				is.close();
		} catch (IOException e) {
			LOGGER.error("Exception:【{}】" + e);
		}
		LOGGER.info("读取文件【{}】结束...", new Object[] { fileName });
		return properties;
	}
	
	public static void main(String[] args) {
		System.out.println(app_server_name);
	}
}
