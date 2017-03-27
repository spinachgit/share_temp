package com.tag.framework.conf;

import com.tag.framework.model.conf.AppConfig;
import com.tag.framework.service.AppConfigService;
import com.tag.framework.util.ConfPathUtil;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreConfig {
	private static final Logger initConfigLog = LoggerFactory.getLogger("InitConfigLog");

	private static String confPath = ConfPathUtil.getConfPath();

	public static AppConfig appConfig = AppConfigService.initAppConfig();

	public static Element getRootElement(String fileName) {
		Document doc = null;
		try {
			SAXBuilder sb = new SAXBuilder();
			String fileFullName = confPath + fileName;
			initConfigLog.info("读取xml文件：【{}】", new Object[] { fileFullName });
			doc = sb.build(new File(fileFullName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Element root = doc.getRootElement();
		return root;
	}

	public static void main(String[] args) {
		test();
	}

	public static void test() {
		String p = CoreConfig.class.getResource("/").getPath();
		String p2 = CoreConfig.class.getResource("").getPath();

		String p4 = CoreConfig.class.getClassLoader().getResource("").getPath();
		String p5 = Thread.currentThread().getContextClassLoader().getResource("").getPath();

		String p6 = System.getProperty("user.dir") + System.getProperty("file.separator");

		System.out.println("p===" + p);
		System.out.println("p2===" + p2);

		System.out.println("p4===" + p4);
		System.out.println("p5===" + p5);
		System.out.println("p6===" + p6);
	}
}