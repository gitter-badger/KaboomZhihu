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

import edu.jiangxin.zhihu.core.ConfigParser;

public class Spider {

	// ��ȡָ��Urlҳ������
	public static String SendGet(String url) {

		CloseableHttpClient client = HttpClients.createDefault();
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

	// ��ȡ�Ƽ�������ϸ����url
	public static ArrayList<GetRecommendationList> GetRecommendations(String content) {

		ArrayList<GetRecommendationList> results = new ArrayList<GetRecommendationList>();
		Document doc = Jsoup.parse(content);
		Elements items = doc.getElementsByClass("zm-item"); // �Ƽ�����Ԫ��
		for (Element item : items) {
			String href = item.getElementsByTag("h2").first().getElementsByTag("a").first().attr("href"); // �Ƽ�����url
			if (href.contains("question")) { // ȥ�����淶url
				results.add(new GetRecommendationList(href));
			}
		}
		return results;
	}

	public static void main(String[] args) {

		ConfigParser configParser = new ConfigParser();
		configParser.paser();
		for (int i = 0; i < configParser.targets.size(); i++) {
			if (configParser.targets.get(i).method.equals("recommendations")) {
				String savePath = configParser.targets.get(i).path;
				String content = Spider.SendGet("http://www.zhihu.com/explore/recommendations"); // ��ȡ֪���Ƽ���ҳ
				ArrayList<GetRecommendationList> list = Spider.GetRecommendations(content); // ��ȡ�Ƽ�������ϸ����
				for (GetRecommendationList zhihu : list) { // д���ĵ�
					
					FileReaderWriter.writeIntoFile(zhihu.writeString(), savePath, true);
				}
			}
		}

	}

}
