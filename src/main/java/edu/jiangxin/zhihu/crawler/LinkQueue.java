/**
 * ��������Ҫ���ݽṹ���ඨ��
 * ���ߣ�����
 **/
package edu.jiangxin.zhihu.crawler;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Queue;

public class LinkQueue {

	// �ѷ��ʵ� url ���ϣ�֮������ΪSet����Ҫ��֤����������Ԫ�ز��ظ�
	private static Set<String> visitedUrl = new HashSet<String>();
	// �����ʵ� url ����
	private static Queue<String> unVisitedUrl = new PriorityQueue<String>();

	// ��ӵ����ʹ���URL������
	public static void addVisitedUrl(String url) {
		visitedUrl.add(url);
		System.out.println("����visitedUrl�����й��У�" + LinkQueue.getVisitedUrlNum() + "��Ԫ��"); // not
																							// very
																							// bad
	}

	// �Ƴ����ʹ���URL
	public static void removeVisitedUrl(String url) {
		visitedUrl.remove(url);
	}

	// ����Ѿ����ʵ�URL��Ŀ
	public static int getVisitedUrlNum() {
		return visitedUrl.size();
	}

	// ���URL����
	public static Queue<String> getUnVisitedUrl() {
		return unVisitedUrl;
	}

	// δ���ʵ�URL������
	public static Object unVisitedUrlDeQueue() {
		String visitUrl = null;
		visitUrl = unVisitedUrl.poll();
		System.out.println(visitUrl + "�뿪unVisitedUrl����"); // bad

		return visitUrl;
	}

	// ��֤ÿ�� url ֻ������һ��
	public static void addUnvisitedUrl(String url) {

		if (url != null && !url.trim().equals("") && !visitedUrl.contains(url) && !unVisitedUrl.contains(url)) {
			unVisitedUrl.add(url);
			System.out.println(url + "����unVisitedUrl����"); // not very bad
			System.out.println("����unVisitedUrl�����й���:" + LinkQueue.getUnVisitedUrlNum() + "��Ԫ��"); // not
																									// very
																									// bad

		}

	}

	public static int getUnVisitedUrlNum() {
		return unVisitedUrl.size();
	}

	// �ж�δ���ʵ�URL�������Ƿ�Ϊ��
	public static boolean unVisitedUrlsEmpty() {
		return unVisitedUrl.isEmpty();
	}

}
