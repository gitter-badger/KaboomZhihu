package edu.jiangxin.zhihu.core;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
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
		
		Logger logger = Logger.getRootLogger();
		DOMConfigurator.configure("log4j.xml");
		
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.zhihu.com");
		
		driver.manage().window().maximize(); //maximize the window
		
		CookieWrapper cookieWrapper = new CookieWrapper();
		cookieWrapper.setCookieList(configParser.cookiePath);
		List<Cookie> cookieList = cookieWrapper.getCookieList();
		for(Cookie cookie : cookieList) {
			driver.manage().addCookie(cookie);
		}

		
		for(int i=0; i<configParser.targets.size(); i++) {
			String method = configParser.targets.get(i).method;
			
			driver.get(configParser.targets.get(i).url);
			
			//int followees_num = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[1]/a[1]/strong")).getText());
			int operated_num = 0; //�Ѿ���ע����ȡ����ע����Ŀ

			List<WebElement> follow = driver.findElements(By.className("zg-btn-follow"));
			List<WebElement> unfollow = driver.findElements(By.className("zg-btn-unfollow"));
			
			//https://bitbucket.org/snippets/jiangxincode/Ro6XE
			
			int duplicate = 0;
			int sum_follow_unfollow = follow.size() + unfollow.size();
			
			/**
			 * ����������ι�������û�б仯��ֹͣѭ�����÷������ö�λҳ���ϵ�һЩ�ο�ֵ����Щֵ��̫�ö�λ��������Ӧ������ޣ����Ҷ��ڳ���ִ�����Ƿ��ע��Ҫ��������ۣ�
			 * �����ķ����ǣ�
			 * String followNumber = driver.findElement(By.tagName("strong")).getText(); //��λ������
			 * while(follow.size() + unfollow.size() < Integer.parseInt(followNumber)-1) { //����ִ���߿����ڹ�ע��֮��
			 */
			while(duplicate < 5) {
				if((method.equals("follow")) && (follow.size() > configParser.targets.get(i).operated_num)) {
					break;
				} else if((method.equals("unfollow")) && (unfollow.size() > configParser.targets.get(i).operated_num)) {
					break;
				}
				
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
					try {
						Thread.sleep(1000); //��ֹ���ع���
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
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
				logger.warn("[Follow:" + follow.size() + "] " + "[Unfollow:" + unfollow.size() + "] " + "[Duplicate:" + duplicate + "]");
			}
			
			
			if(method.equals("follow")) {
				for(WebElement we : follow) {
					we.click();
					operated_num ++;
					if(operated_num >= configParser.targets.get(i).operated_num) {
						break;
					}
				}
			} else if(method.equals("unfollow")) {
				for(WebElement we : unfollow) {
					we.click();
					operated_num ++;
					if(operated_num >= configParser.targets.get(i).operated_num) {
						break;
					}
				}
			}
			
			if(configParser.targets.get(i).shutdown) {
				
				try {
					//Runtime.getRuntime().exit(1);
					Runtime.getRuntime().exec("shutdown -s");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		driver.close();
	}

}
