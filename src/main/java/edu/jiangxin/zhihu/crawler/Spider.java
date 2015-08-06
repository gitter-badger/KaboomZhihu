package edu.jiangxin.zhihu.crawler;

import java.util.ArrayList;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Spider {
	public static void main(String[] args) {

		String url = "http://www.zhihu.com/explore/recommendations";
		// ��ȡ֪���Ƽ���ҳ
		String content = Spider.SendGet(url);

		// ��ȡ�Ƽ�������ϸ����
		ArrayList<Zhihu> myZhihu = Spider.GetRecommendations(content);

		// д���ĵ�
		for (Zhihu zhihu : myZhihu) {
			FileReaderWriter.writeIntoFile(zhihu.writeString(),"D:/֪��_�༭�Ƽ�.txt", true);
		}
	}
	
	//��ȡָ��Urlҳ������
	public static String SendGet(String url) {

		CloseableHttpClient client = HttpClients.createDefault();
		try{
			HttpGet request = new HttpGet(url);
			CloseableHttpResponse resp = client.execute(request);
			
			String result = EntityUtils.toString(resp.getEntity());
			
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				client.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		return null;
	}

	// ��ȡ�Ƽ�������ϸ����url
	public static ArrayList<Zhihu> GetRecommendations(String content) {
		
		ArrayList<Zhihu> results = new ArrayList<Zhihu>();
		Document doc = Jsoup.parse(content);
		Elements items =  doc.getElementsByClass("zm-item");          //�Ƽ�����Ԫ��
		for(Element item:items){
			Element h2TagEle = item.getElementsByTag("h2").first();   //�Ƽ����ݱ���Ԫ��
			Element aTagEl = h2TagEle.getElementsByTag("a").first();  //�Ƽ����ݵ�Url������Ԫ��
			String href = aTagEl.attr("href");                        //�Ƽ�����url
			if(href.contains("question")){                            //ȥ�����淶url
				results.add(new Zhihu(href));
			}
		}
		return results;
	}

}

