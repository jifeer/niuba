package com.etyero.tool;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * httpclient工具类
 * 
 * @author lijialong
 * */
public class DoRequest {
	public static CloseableHttpClient httpClient = null;
	public static HttpClientContext context = null;
	public static CookieStore cookieStore = null;
	public static RequestConfig requestConfig = null;
	private static final Logger logger = LoggerFactory.getLogger(DoRequest.class);

	static {
		init();
	}

	private static void init() {
		try {
			
			//=====忽略证书信任问题 start=====
			HostnameVerifier hv = new HostnameVerifier() {  
		        public boolean verify(String urlHostName, SSLSession session) {  
		            System.out.println("Warning: URL Host: " + urlHostName + " vs. "  
		                               + session.getPeerHost());  
		            return true;  
		        }  
		    };  
			trustAllHttpsCertificates();
			//=====忽略证书信任问题 end=====
			
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
			context = HttpClientContext.create();
			cookieStore = new BasicCookieStore();
			// 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）
			requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)
					.setConnectionRequestTimeout(60000).build();
			// 设置默认跳转以及存储cookie
			httpClient = HttpClientBuilder.create().setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
					.setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)
					.setDefaultCookieStore(cookieStore).build();
		} catch (Exception e) {
			logger.info("httpClient初始化失败----{}",e);
		}
		
	}

	/**
	 * 执行get请求
	 * 
	 * @author lijialong
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private static JSONObject doGet(String url) {
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("device", "F50FF0BD-94F5-49FE-8F2F-F2595598784F");
		httpget.setHeader("X-App-Info","1.6.0/appstore/ios");
		httpget.setHeader("User-Agent","finbtc/1.6.0 (iPhone; iOS 11.2.2; Scale/2.00)");
		JSONObject json = null;
		try {
			CloseableHttpResponse response = httpClient.execute(httpget, context);
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				json = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
			} else {
				logger.info("http响应异常，请求：{}----状态码：{}",url,status);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpget.releaseConnection();
		}
		return json;
	}

	/**
	 * 执行post方法请求
	 * 
	 * @author lijialong
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private static JSONObject doPost(String url, String parameters) {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
		httpPost.setHeader("device", "F50FF0BD-94F5-49FE-8F2F-F2595598784F");
		httpPost.setHeader("X-App-Info","1.6.0/appstore/ios");
		httpPost.setHeader("User-Agent","finbtc/1.6.0 (iPhone; iOS 11.2.2; Scale/2.00)");
		httpPost.setEntity(new StringEntity(parameters, Charset.forName("UTF-8")));
		JSONObject json = null;
		try {
			CloseableHttpResponse response = httpClient.execute(httpPost, context);
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				json = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
			} else {
				logger.info("http响应异常，请求：{}----状态码：{}",url,status);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
		}
		return json;
	}
	
	public static String getResponse(String url){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		URI uri = URI.create(url);
		HttpGet get = new HttpGet(uri);
		String result = null;
		try {
			CloseableHttpResponse response = httpClient.execute(get);
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
			} else {
				logger.info("http响应异常，请求：{}----状态码：{}",url,status);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			get.releaseConnection();
		}
		return result;
	}

	/**
	 * 执行请求
	 * 
	 * @author lijialong
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static JSONObject doRequest(String method, String url, String parameters) {
		JSONObject json = null;
		if (method.equals("get")) {
			json = doGet(url);
		} else {
			json = doPost(url, parameters);
		}
		return json;
	}

	/**
	 * 打印当前cookie
	 * 
	 */
	public static void printCookies() {
		System.out.println("headers:");
		cookieStore = context.getCookieStore();
		List<Cookie> cookies = cookieStore.getCookies();
		for (Cookie cookie : cookies) {
			System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());
		}
	}
	
	
	// ======忽略https证书信任问题，解决sun.security.provider.certpath.SunCertPathBuilderException问题
	// ======start======
	private static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
		// ======end======
	}
}
