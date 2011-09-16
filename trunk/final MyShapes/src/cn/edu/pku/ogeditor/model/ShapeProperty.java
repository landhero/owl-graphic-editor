package cn.edu.pku.ogeditor.model;

import java.io.Serializable;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.XSD;

public class ShapeProperty implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final String BOOLEAN_TYPE = "boolean";
	public static final String FLOAT_TYPE = "float";
	public static final String INT_TYPE = "int";
	public static final String STRING_TYPE = "string";
	public static final String ANY_TYPE = "any";
	public static final String DATE_TYPE = "date";
	public static final String DATETIME_TYPE = "datetime";
	public static final String TIME_TYPE = "time";
	
	private String name;
	private String type;
	private String value;
	
	private static final String[] TYPES = { BOOLEAN_TYPE,
		FLOAT_TYPE, INT_TYPE, STRING_TYPE,
		ANY_TYPE, DATE_TYPE, DATETIME_TYPE,
		TIME_TYPE};
	private static final Resource[] XSD_TYPES = { XSD.xboolean,
		XSD.xfloat, XSD.xint, XSD.xstring,
		XSD.anyURI, XSD.date, XSD.dateTime,
		XSD.time};
	public static String[] getAllTypes()
	{
		return TYPES;
	}
	public ShapeProperty()
	{
		name = new String();
		type = new String();
		value = new String();
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
//
//	public boolean addValue(String value) {
//		if(!containValue(value))
//		{
//			values.add(value);
//			return true;
//		}
//		else
//		{
//			//弹出个对话框之类
//			return false;
//		}
//	}
//
//	public void deleteValue(String value) {
//		if(containValue(value))
//			values.remove(value);
//		else
//		{
//			//弹出个对话框之类
//		}
//	}
//	
//	private boolean containValue(String value) {
//		for(int i =0;i<values.size();i++)
//		{
//			if(values.get(i).equals(value))
//				return true;
//		}
//		return false;
//	}

//	public List<String> getValues() {
//		return values;
//	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public static int getSelectedIndex(String string) {
		for (int i = 0; i < TYPES.length; i++) {
			if(TYPES[i].equals(string))
				return i;
		}
		return 0;
	}
//	public void deleteAllValues() {
//		values = new ArrayList<String>();
//		
//	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public String getValue() {
		// TODO Auto-generated method stub
		return value;
	}
	public void deleteValue() {
		this.value = "";
		
	}
	public static Resource type2XSDType(String type)
	{
		return XSD_TYPES[getSelectedIndex(type)];
	}
}
