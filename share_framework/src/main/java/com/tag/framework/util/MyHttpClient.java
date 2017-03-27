package com.tag.framework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class MyHttpClient {
	public static final int CONNECT_TIMEOUT = 3000;
	public static final int READ_TIMEOUT = 3000;

	public static String httpGetGo(String getUrl, Map<String, String> parameterMap) throws Exception {
		String param = UrlService.getSortedUrl(parameterMap);
		getUrl = getUrl + "?" + param;

		String result = httpGetGo(getUrl);
		System.out.println("getUrl=====" + getUrl);
		return result;
	}

	public static String httpGetGo(String getUrl) throws Exception {
		StringBuffer readOneLineBuff = new StringBuffer();
		String content = "";
		URL url = new URL(getUrl);
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(3000);
		conn.setReadTimeout(3000);
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
		String line = "";
		while ((line = reader.readLine()) != null) {
			readOneLineBuff.append(line);
		}
		content = readOneLineBuff.toString();
		reader.close();
		return content;
	}

	public static String httpPostGo(String postUrl, String params) throws Exception {
		StringBuffer readOneLineBuff = new StringBuffer();
		String content = "";

		if (!postUrl.substring(postUrl.length() - 1).equals("?")) {
			postUrl = postUrl + "?";
		}
		System.out.println(postUrl);
		URL url = new URL(postUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(3000);
		conn.setReadTimeout(3000);
		conn.setDoOutput(true);

		byte[] bypes = params.getBytes("utf-8");
		conn.getOutputStream().write(bypes);

		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
		String line = "";
		while ((line = reader.readLine()) != null) {
			readOneLineBuff.append(line);
		}
		content = readOneLineBuff.toString();
		reader.close();
		return content;
	}

	public static String safeHttpPostGo(String url, Map map) {
		boolean requireHttps = false;
		if (url.startsWith("https")) {
			requireHttps = true;
		}
		HttpClient httpclient = new DefaultHttpClient();

		httpclient.getParams().setParameter("http.connection.timeout", Integer.valueOf(3000));

		httpclient.getParams().setParameter("http.socket.timeout", Integer.valueOf(3000));

		HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		String printStr;
		if (requireHttps) {
			httpclient = wrapClient(httpclient);
			SchemeRegistry registry = new SchemeRegistry();
			SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
			socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
			registry.register(new Scheme("https", socketFactory, 443));
			SingleClientConnManager mgr = new SingleClientConnManager(httpclient.getParams(), registry);
			DefaultHttpClient httpClient = new DefaultHttpClient(mgr, httpclient.getParams());
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
			printStr = url;
		}
		HttpPost httpPost = new HttpPost(url);
		List qparams = new ArrayList();
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			String val = entry.getValue().toString();
			qparams.add(new BasicNameValuePair(key, val));
		}

		printStr = "";
		for (int i = 0; i < qparams.size(); i++) {
			printStr = printStr + "&" + ((NameValuePair) qparams.get(i)).getName() + "=" + ((NameValuePair) qparams.get(i)).getValue();
		}

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(qparams, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String responseStr = null;
		try {
			ResponseHandler handler = new ResponseHandler() {
				public byte[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						return EntityUtils.toByteArray(entity);
					}
					return null;
				}
			};
			byte[] response = (byte[]) httpclient.execute(httpPost, handler);
			return new String(response, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static HttpClient wrapClient(HttpClient base) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = base.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			return new DefaultHttpClient(ccm, base.getParams());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		String str = safeHttpPostGo("http://192.168.18.68:8080/handPayWeb/xiaomi/xiaomiServlet", new HashMap());

		System.out.println(str);
	}
}