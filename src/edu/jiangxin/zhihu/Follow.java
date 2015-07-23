package edu.jiangxin.zhihu;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Follow {

	public static void main(String[] args) {

		ConfigParser configParser = new ConfigParser();
		configParser.paser();
		
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.zhihu.com");
		
		driver.manage().window().maximize(); //maximize the window
		
		CookieWrapper cookieWrapper = new CookieWrapper();
		cookieWrapper.setCookieList(configParser.cookiePath);
		List<Cookie> cookieList = cookieWrapper.getCookieList();
		for(Cookie cookie : cookieList) {
			driver.manage().addCookie(cookie);
		}

		//driver.get("http://www.zhihu.com/topic/19627873/followers"); //���ջ�����й�ע
		//driver.get("http://www.zhihu.com/question/19644659/followers"); //����������й�ע
		//driver.get("http://www.zhihu.com/people/YenvY/followees"); //ĳ�˹�ע����
		//driver.get("http://www.zhihu.com/people/YenvY/followers"); //��עĳ�˵���
		//driver.get("http://www.zhihu.com/people/jiangxinnju/followees"); //�ҹ�ע����
		//driver.get("http://www.zhihu.com/people/jiangxinnju/followers"); //��ע�ҵ���
		//driver.get("http://www.zhihu.com/people/qu-yiming/columns/followed"); //ĳ�˹�ע��ר��
		//driver.get("http://www.zhihu.com/people/qu-yiming/topics"); //ĳ�˹�ע�Ļ���
		driver.get(configParser.targets.get(0).website);
		
		
		List<WebElement> follow = driver.findElements(By.className("zg-btn-follow"));
		List<WebElement> unfollow = driver.findElements(By.className("zg-btn-unfollow"));
		
		int duplicate = 0;
		int sum_follow_unfollow = follow.size() + unfollow.size();
		
		/**
		 * ����������ι�������û�б仯��ֹͣѭ�����÷������ö�λҳ���ϵ�һЩ�ο�ֵ����Щֵ��̫�ö�λ��������Ӧ������ޣ����Ҷ��ڳ���ִ�����Ƿ��ע��Ҫ��������ۣ�
		 * �����ķ����ǣ�
		 * String followNumber = driver.findElement(By.tagName("strong")).getText(); //��λ������
		 * while(follow.size() + unfollow.size() < Integer.parseInt(followNumber)-1) { //����ִ���߿����ڹ�ע��֮��
		 */
		while(duplicate < 3) {
			
			WebElement loadMore;
			
			try {
				loadMore = driver.findElement(By.id("zh-load-more")); //��������ڸ�Ԫ�ػ��׳��쳣��������ֱ�ӷ���null
			} catch (NoSuchElementException e) {
				loadMore = null;
			}
			if((loadMore != null) && loadMore.isDisplayed()) {
				loadMore.click();
			} else {
				String js="var q=document.documentElement.scrollTop=document.body.scrollHeight";
				((JavascriptExecutor)driver).executeScript(js);
				//((HasInputDevices) driver).getKeyboard().sendKeys(Keys.PAGE_DOWN); // inefficiency
			}
			follow = driver.findElements(By.className("zg-btn-follow"));
			unfollow = driver.findElements(By.className("zg-btn-unfollow"));
			if(follow.size() + unfollow.size() > sum_follow_unfollow) {
				duplicate = 0;
				sum_follow_unfollow = follow.size() + unfollow.size();
			} else {
				duplicate++;
			}
			System.out.println(follow.size() + " " + unfollow.size() + " " + duplicate);
		}
		
		String method = configParser.targets.get(0).method;
		if(method.equals("follow")) {
			for(WebElement we : follow) {
				we.click();
			}
		} else if(method.equals("unfollow")) {
			for(WebElement we : unfollow) {
				we.click();
			}
		}
		driver.close();
	}

}
