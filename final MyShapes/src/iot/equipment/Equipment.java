package iot.equipment;

import iot.client.Value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Equipment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String BOOLEAN_TYPE = "Boolean";
	public static final String INTEGER_TYPE = "Integer";
	public static final String STRING_TYPE = "String";
	public static final String DOUBLE_TYPE = "Double";
	public static final String FLOAT_TYPE = "Float";

	private String name;
	private HashMap<String, Value> properties;

	Equipment() {
	}

	public Equipment(String name) {
		setName(name);
		properties = new HashMap<String, Value>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProperty(String et, String p, String t, String value) {
		Value v = properties.get(p);
		v = new Value();
		v.setEType(et);
		v.setEquipment(name);
		v.setProperty(p);
		v.setType(t);
		v.setValue(value);
		properties.put(p, v);
	}

	public void removeProperty(String vname) {
		properties.remove(vname);
	}

	public Value getProperty(String vname) {
		return properties.get(vname);
	}

	public Set<Entry<String, Value>> getPropertyList() {
		return properties.entrySet();
	}
}
