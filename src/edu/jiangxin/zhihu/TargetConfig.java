package edu.jiangxin.zhihu;

public class TargetConfig {
	String method;
	String url;
	int operated_num;
	boolean shutdown;
	
	public TargetConfig () {
		this.operated_num = Integer.MAX_VALUE;
		this.shutdown = false;
	};
	
}
