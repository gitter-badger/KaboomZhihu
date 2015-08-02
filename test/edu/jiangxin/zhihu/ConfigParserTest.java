package edu.jiangxin.zhihu;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ConfigParserTest {
	
	ConfigParser configParser;

	@Before
	public void setUp() {
		configParser = new ConfigParser("resource/ConfigParserTest.xml");
		configParser.paser();
	}
	
	//http://www.zhihu.com/topic/19627873/followers //���ջ�����й�ע
	//http://www.zhihu.com/question/19644659/followers //����������й�ע
	//http://www.zhihu.com/people/YenvY/followees //ĳ�˹�ע����
	//http://www.zhihu.com/people/YenvY/followers //��עĳ�˵���
	//http://www.zhihu.com/people/jiangxinnju/followees //�ҹ�ע����
	//http://www.zhihu.com/people/jiangxinnju/followers //��ע�ҵ���
	//http://www.zhihu.com/people/qu-yiming/columns/followed //ĳ�˹�ע��ר��
	//http://www.zhihu.com/people/qu-yiming/topics //ĳ�˹�ע�Ļ���
	
	
	@Test
	public void testParser01() {
		assertEquals("tmp\\Cookie\\cookie.txt", configParser.cookiePath);
		assertEquals("dfjlsdajfsfvn", configParser.username);
		assertEquals("vkasdfjlsd", configParser.password);
		assertEquals(6,configParser.targets.size());
		assertEquals("unfollow", configParser.targets.get(0).method);
		assertEquals(Integer.MAX_VALUE, configParser.targets.get(0).operated_num);
		assertEquals(false, configParser.targets.get(0).shutdown);
	}
	
	@Test
	public void testPaser02() {
		assertEquals(Kind.SOMETOPIC_FOLLOWERS, configParser.targets.get(0).kind);
		assertEquals(Kind.SOMEQUESTION_FOLLOWERS, configParser.targets.get(1).kind);
		assertEquals(Kind.SOMEPEOPLE_FOLLOWEES, configParser.targets.get(2).kind);
		assertEquals(Kind.SOMEPEOPLE_FOLLOWERS, configParser.targets.get(3).kind);
		assertEquals(Kind.SOMEPEOPLE_COLUMNS, configParser.targets.get(4).kind);
		assertEquals(Kind.SOMEPEOPLE_TOPICS, configParser.targets.get(5).kind);
	}

}
