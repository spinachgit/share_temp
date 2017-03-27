package com.tag.framework.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SftpUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(SftpUtil.class);

	public static void upload(String host, int port, String username, String password, InputStream inputStream, String remoteDirectory, String remoteFileName) throws Exception {
		Sftp sftp = new Sftp();
		sftp.getConnect(host, port, username, password);
		sftp.upload(inputStream, remoteDirectory, remoteFileName);
		sftp.disconnect();
	}

	public static void upload(String host, int port, String username, String password, String localFilePathName, String remoteDirectory, String remoteFileName) throws Exception {
		Sftp sftp = new Sftp();
		sftp.getConnect(host, port, username, password);
		File file = new File(localFilePathName);
		sftp.upload(new FileInputStream(file), remoteDirectory, remoteFileName);
		sftp.disconnect();
	}

	public static void uploadBatch(String host, int port, String username, String password, List<String> localFilePathNameList, List<String> remoteDirectoryList, List<String> remoteFileNameList) throws Exception {
		if ((localFilePathNameList.size() != remoteDirectoryList.size()) || (localFilePathNameList.size() != remoteFileNameList.size())) {
			throw new RuntimeException("参数错误！本地文件list 和 远端目录list 以及 远端文件名 list 大小不一致。");
		}

		Sftp sftp = new Sftp();
		sftp.getConnect(host, port, username, password);

		for (int i = 0; i < localFilePathNameList.size(); i++) {
			String localFilePathName = (String) localFilePathNameList.get(i);
			String remoteDirectory = (String) remoteDirectoryList.get(i);
			String remoteFileName = (String) remoteFileNameList.get(i);
			File file = new File(localFilePathName);
			sftp.upload(new FileInputStream(file), remoteDirectory, remoteFileName);
		}

		sftp.disconnect();
	}

	public static void download(String host, int port, String username, String password, String remoteDirectory, String remoteFileName, String localDirectorys, String localFileName) throws Exception {
		Sftp sftp = new Sftp();
		sftp.getConnect(host, port, username, password);
		sftp.download(remoteDirectory, remoteFileName, localDirectorys, localFileName);
		sftp.disconnect();
	}

	public static void delete(String host, int port, String username, String password, String remoteDirectory, String remoteFileName) throws Exception {
		Sftp sftp = new Sftp();
		sftp.getConnect(host, port, username, password);
		sftp.delete(remoteDirectory, remoteFileName);
		sftp.disconnect();
	}

	public static void main(String[] args) throws Exception {
		test();
	}

	private static void test() throws Exception {
		long b = System.currentTimeMillis();

		String host = "172.51.96.192";
		short port = 22;
		String username = "root";
		String password = "Bluemobi@2014";

		String localFilePathName = "D://sftp.JPG";

		String remoteDirectory = "/tmp/ftp/a/b/c/d/";
		String remoteFileName = "remoteFile_1.JPG";
		String localDirectorys = "D://aaa2/b/c/d/e/";
		String localFileName = "localFile_1.JPG";

		upload(host, port, username, password, localFilePathName, remoteDirectory, remoteFileName);

		download(host, port, username, password, remoteDirectory, remoteFileName, localDirectorys, localFileName);

		long e = System.currentTimeMillis();

		System.err.println("总耗时：" + (e - b) / 1000.0D + "秒");
	}

	private static void testUploadBatch() throws Exception {
		long b = System.currentTimeMillis();

		String host = "172.51.96.192";
		short port = 22;
		String username = "root";
		String password = "Bluemobi@2014";

		String localDirectorys = "D://aaa2/b/c/d/e/";
		String localFileName = "localFile_1.JPG";

		List localFilePathNameList = new ArrayList();
		List remoteDirectoryList = new ArrayList();
		List remoteFileNameList = new ArrayList();

		for (int i = 1; i <= 10; i++) {
			String localFilePathName = "D://sftp_" + i + ".JPG";
			String remoteDirectory = "/tmp/ftp/" + i % 4 + "/";
			String remoteFileName = "remoteFile_" + i + ".JPG";

			localFilePathNameList.add(localFilePathName);
			remoteDirectoryList.add(remoteDirectory);
			remoteFileNameList.add(remoteFileName);
		}

		uploadBatch(host, port, username, password, localFilePathNameList, remoteDirectoryList, remoteFileNameList);

		long e = System.currentTimeMillis();

		System.err.println("总耗时：" + (e - b) / 1000.0D + "秒");
	}

	private static class Sftp {
		private static final Logger LOGGER = LoggerFactory.getLogger(Sftp.class);

		private Session session = null;
		private Channel channel = null;
		private ChannelSftp sftp = null;

		private void getConnect(String host, int port, String username, String password) throws Exception {
			LOGGER.debug("开始创建sftp连接...");
			JSch jsch = new JSch();
			this.session = jsch.getSession(username, host, port);
			this.session.setPassword(password);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			this.session.setConfig(config);

			this.session.connect();

			this.channel = this.session.openChannel("sftp");

			this.channel.connect();

			this.sftp = ((ChannelSftp) this.channel);

			LOGGER.debug("创建sftp连接结束...");
		}

		private void upload(InputStream inputStream, String remoteDirectory, String remoteFileName) throws Exception {
			this.sftp.cd("/");
			try {
				if ((!remoteDirectory.equals("")) && (remoteDirectory.trim() != "")) {
					String[] dd = remoteDirectory.split("/");
					for (String directory : dd)
						if ((directory != null) && (!"".equals(directory.trim()))) {
							try {
								this.sftp.cd(directory);
							} catch (SftpException sException) {
								if (2 == sException.id) {
									LOGGER.info("创建目录【{}】", new Object[] { directory });
									this.sftp.mkdir(directory);
									this.sftp.cd(directory);
								}
							}
						}
				}
				this.sftp.put(inputStream, remoteFileName);
			} catch (Exception e) {
				throw new Exception(e.getMessage(), e);
			}
		}

		private void download(String remoteDirectory, String remoteFileName, String localDirectorys, String localFileName) throws Exception {
			try {
				this.sftp.cd(remoteDirectory);
				File file = new File(localDirectorys);
				if (!file.exists()) {
					file.mkdirs();
				}
				this.sftp.get(remoteFileName, new FileOutputStream(new File(localDirectorys, localFileName)));
			} catch (Exception e) {
				throw new Exception(e.getMessage(), e);
			} finally {
				disconnect();
			}
		}

		private void delete(String directory, String deleteFile) throws Exception {
			try {
				this.sftp.cd(directory);
				this.sftp.rm(deleteFile);
			} catch (Exception e) {
				throw new Exception(e.getMessage(), e);
			} finally {
				disconnect();
			}
		}

		private void disconnect() throws Exception {
			if (this.sftp != null) {
				this.sftp.disconnect();
				this.sftp.exit();
				this.sftp = null;
			}
			if (this.channel != null) {
				this.channel.disconnect();
				this.channel = null;
			}
			if (this.session != null) {
				this.session.disconnect();
				this.session = null;
			}
		}
	}
}