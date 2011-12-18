package cn.edu.pku.ogeditor.views;

import java.io.Serializable;

public class SWRLRule implements Serializable{
	private String name;
	private String expression;
	public SWRLRule(String name,String expression)
	{
		this.name = name;
		this.expression = expression;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getExpression() {
		return expression;
	}
	

}
