package edu.jiangxin.zhihu.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

import edu.jiangxin.zhihu.core.ConfigParser;

public class GetUrlContent {
	
	@SuppressWarnings("deprecation")
	public static String getContent(String url) {
		
		ConfigParser configParser = new ConfigParser();
		configParser.paser();
		
	    CookieStore cookieStore = new BasicCookieStore();
	    
	    File cookieFile = new File(configParser.cookiePath);
        FileReader fr = null;
		try {
			fr = new FileReader(cookieFile);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        BufferedReader bufferedReader = new BufferedReader(fr);
        String line;
        try {
			while((line = bufferedReader.readLine()) != null) {
			    StringTokenizer stringTokenizer = new StringTokenizer(line, ";");
			    while(stringTokenizer.hasMoreTokens()) {
			        String name = stringTokenizer.nextToken();
			        String value = stringTokenizer.nextToken();
			        String domain = stringTokenizer.nextToken();
			        String path = stringTokenizer.nextToken();
			        Date expiry = null;
			        String dt;
			        if(!(dt = stringTokenizer.nextToken()).equals("null")) {
			            expiry = new Date(dt);
			        }

			        boolean isSecure = new Boolean(stringTokenizer.nextToken()).booleanValue();
			       
			        BasicClientCookie cookie = new BasicClientCookie(name, value);
			        cookie.setDomain(domain);
			        cookie.setPath(path);
			        cookie.setExpiryDate(expiry);
			        cookie.setSecure(isSecure);
			        cookieStore.addCookie(cookie);
			    }
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        try {
			bufferedReader.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		try {
			HttpGet request = new HttpGet(url);
			CloseableHttpResponse resp = client.execute(request);
			String result = EntityUtils.toString(resp.getEntity());

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

}
