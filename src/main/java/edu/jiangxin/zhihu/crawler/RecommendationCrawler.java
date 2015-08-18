package edu.jiangxin.zhihu.crawler;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.jiangxin.zhihu.core.ConfigParser;

public class RecommendationCrawler {

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
				String content = GetUrlContent.getContent("http://www.zhihu.com/explore/recommendations"); // ��ȡ֪���Ƽ���ҳ
				ArrayList<GetRecommendationList> list = RecommendationCrawler.GetRecommendations(content); // ��ȡ�Ƽ�������ϸ����
				for (GetRecommendationList zhihu : list) { // д���ĵ�
					
					FileReaderWriter.writeIntoFile(zhihu.writeString(), savePath, true);
				}
			}
		}
	}

}
