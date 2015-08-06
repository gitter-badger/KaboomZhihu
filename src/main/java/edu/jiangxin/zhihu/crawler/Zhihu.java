package edu.jiangxin.zhihu.crawler;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Zhihu {
	public String question;// ����
	public String questionDescription;// ��������
	public String zhihuUrl;// ���ӵ�ַ
	public ArrayList<String> answers;// �ش�

	// ��ȡ֪������
	public Zhihu(String url) {
		question = "";
		questionDescription = "";
		zhihuUrl = "";
		answers = new ArrayList<String>();

		if (getRealUrl(url)) {
			
			// ��ȡ�Ƽ�������ϸҳ��
			String content = Spider.SendGet(zhihuUrl);
			if(content != null){
				Document doc = Jsoup.parse(content);
				// ��ȡ���⣬���û�����������
				question = doc.title();
				
				// ������Ϣ����
				Element despElement = doc.getElementById("zh-question-detail");
				if(despElement != null){
					questionDescription = despElement.text();
				}
				// ���
				Elements ansItems = doc.getElementsByClass("zm-item-answer");
				for(Element ansItem:ansItems){
					Element textElement = ansItem.getElementsByClass("zm-item-rich-text").first();
					if(despElement != null){
						answers.add(textElement.text());
					}
				}
			}else{
				System.out.println("content is null");
			}
		}
	}

	// 
	boolean getRealUrl(String url) {
		// ��http://www.zhihu.com/question/22355264/answer/21102139
		// ת��Ϊhttp://www.zhihu.com/question/22355264
		Pattern pattern = Pattern.compile("question/(.*?)/");
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			zhihuUrl = "http://www.zhihu.com/question/" + matcher.group(1);
		} else {
			return false;
		}
		return true;
	}

	public String writeString() {
		// ��htmlҳ��ת��Ϊ�ַ���
		String result = "";
		result += "���⣺" + question + "\r\n";
		result += "������" + questionDescription + "\r\n";
		result += "���ӣ�" + zhihuUrl + "\r\n\r\n";
		for (int i = 0; i < answers.size(); i++) {
			result += "�ش�" + i + "��" + answers.get(i) + "\r\n\r\n\r\n";
		}
		result += "\r\n\r\n\r\n\r\n\r\n\r\n";
		// �滻html���з��������ַ�
		result = result.replaceAll("<br>", "\r\n");
		result = result.replaceAll("<.*?>", "");
		return result;
	}

	@Override
	public String toString() {
		String result = "";
		result += "���⣺" + question + "\n";
		result += "������" + questionDescription + "\n";
		result += "���ӣ�" + zhihuUrl + "\n";
		result += "�ش�" + answers.size() + "\n";
		return result;
	}
}

