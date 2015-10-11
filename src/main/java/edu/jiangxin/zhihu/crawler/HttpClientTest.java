package edu.jiangxin.zhihu.crawler;

import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
 
/**
 * Created by gavin on 15-7-23.
 */
public class HttpClientTest {
 
    public static void main(String[] args)
    {
        //����һ��HttpClient
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        try {
            //����һ��get������������_xsrf��Ϣ
        HttpGet get = new HttpGet("http://www.zhihu.com/");
            //��ȡ_xsrf
            CloseableHttpResponse response = httpClient.execute(get,new BasicHttpContext());
            setCookie(response);
            String responseHtml = EntityUtils.toString(response.getEntity());
            String xsrfValue = responseHtml.split("<input type=\"hidden\" name=\"_xsrf\" value=\"")[1].split("\"/>")[0];
            System.out.println("xsrfValue:" + xsrfValue);
            response.close();
             
            //����post����
            List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
            valuePairs.add(new BasicNameValuePair("_xsrf", xsrfValue));
            valuePairs.add(new BasicNameValuePair("email", "jiangxinnju@163.com"));
            valuePairs.add(new BasicNameValuePair("password", "621224jx"));
            valuePairs.add(new BasicNameValuePair("remember_me", "true"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);
             
            //����һ��post����
            HttpPost post = new HttpPost("http://www.zhihu.com/login/email");
            post.setHeader("Cookie", " cap_id=\"YjA5MjE0YzYyNGQ2NDY5NWJhMmFhN2YyY2EwODIwZjQ=|1437610072|e7cc307c0d2fe2ee84fd3ceb7f83d298156e37e0\"; ");
 
            //ע��post����
            post.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(post);
            //��ӡ��¼�Ƿ�ɹ���Ϣ
            printResponse(httpResponse);
 
            //����һ��get�����������Ե�¼cookie�Ƿ��õ�
            HttpGet g = new HttpGet("http://www.zhihu.com/question/followers");
            //�õ�post���󷵻ص�cookie��Ϣ
            String c = setCookie(httpResponse);
            //��cookieע�뵽get����ͷ����
            g.setHeader("Cookie",c);
            CloseableHttpResponse r = httpClient.execute(g);
            String content = EntityUtils.toString(r.getEntity());
            System.out.println(content);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    public static void printResponse(HttpResponse httpResponse)
            throws ParseException, IOException {
        // ��ȡ��Ӧ��Ϣʵ��
        HttpEntity entity = httpResponse.getEntity();
        // ��Ӧ״̬
        System.out.println("status:" + httpResponse.getStatusLine());
        System.out.println("headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        while (iterator.hasNext()) {
            System.out.println("\t" + iterator.next());
        }
        // �ж���Ӧʵ���Ƿ�Ϊ��
        if (entity != null) {
            String responseString = EntityUtils.toString(entity);
            System.out.println("response length:" + responseString.length());
            System.out.println("response content:"
                    + responseString.replace("\r\n", ""));
        }
    }
 
    public static Map<String,String> cookieMap = new HashMap<String, String>(64);
    //����Ӧ��Ϣ�л�ȡcookie
    public static String setCookie(HttpResponse httpResponse)
    {
        System.out.println("----setCookieStore");
        Header headers[] = httpResponse.getHeaders("Set-Cookie");
        if (headers == null || headers.length==0)
        {
            System.out.println("----there are no cookies");
            return null;
        }
        String cookie = "";
        for (int i = 0; i < headers.length; i++) {
            cookie += headers[i].getValue();
            if(i != headers.length-1)
            {
                cookie += ";";
            }
        }
 
        String cookies[] = cookie.split(";");
        for (String c : cookies)
        {
            c = c.trim();
            if(cookieMap.containsKey(c.split("=")[0]))
            {
                cookieMap.remove(c.split("=")[0]);
            }
            cookieMap.put(c.split("=")[0], c.split("=").length == 1 ? "":(c.split("=").length ==2?c.split("=")[1]:c.split("=",2)[1]));
        }
        System.out.println("----setCookieStore success");
        String cookiesTmp = "";
        for (String key :cookieMap.keySet())
        {
            cookiesTmp +=key+"="+cookieMap.get(key)+";";
        }
 
        return cookiesTmp.substring(0,cookiesTmp.length()-2);
    }
}