package cn.edu.pku.ogeditor.wizards;


import iot.client.Value;
import iot.equipment.Equipment;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class ObjectsListModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ADD_ELEMENT = "addElement";

	public static final String REMOVE_ELEMENT = "removeElement";

	private PropertyChangeSupport delegate;

	private HashMap<String, ArrayList<Equipment>> equipments;
	

	public ObjectsListModel() {
		setEquipments(new HashMap<String, ArrayList<Equipment>>());
		delegate = new PropertyChangeSupport(this);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		delegate.addPropertyChangeListener(listener);
	}

	public void firePropertyChange(PropertyChangeEvent evt) {
		delegate.firePropertyChange(evt);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		delegate.removePropertyChangeListener(listener);
	}

	public void add(String etype, Equipment element) {
		ArrayList<Equipment> list = getEquipments().get(etype);
		if(null == list)
		{
			list = new ArrayList<Equipment>();
		}
		list.add(element);
		getEquipments().put(etype, list);

	}
	public void add(Value v)
	{
		ArrayList<Equipment> list = getEquipments().get(v.getEType());
		if(null == list)
		{
			list = new ArrayList<Equipment>();
		}
		for(Equipment e : list)
		{
			if(e.getName().equals(v.getEquipment()))
			{
				list.remove(e);
				e.setProperty(v.getEType(), v.getProperty(), v.getType(), v.getValue());
				list.add(e);
				getEquipments().put(v.getEType(), list);
				return;
			}
		}
		Equipment e = new Equipment(v.getEquipment());
		e.setProperty(v.getEType(), v.getProperty(), v.getType(), v.getValue());
		list.add(e);
		getEquipments().put(v.getEType(), list);
	}

	public void remove(String etype, Equipment element) {
		ArrayList<Equipment> list = getEquipments().get(etype);
		if(null == list)
		{
			return;
//			list = new ArrayList<Equipment>();
		}
		list.remove(element);
		getEquipments().put(etype, list);
//		if (equipments.remove(element))
//			firePropertyChange(new PropertyChangeEvent(this, REMOVE_ELEMENT,
//					null, element));
	}
	
	public void clear()
	{
		getEquipments().clear();
	}

	public Object[] elements() {
		ArrayList<Value> list = new ArrayList<Value>();
		Iterator<ArrayList<Equipment>> parIter = getEquipments().values().iterator();
		while(parIter.hasNext())
		{
			ArrayList<Equipment> es = parIter.next();

			for(int i = 0 ;i<es.size(); i++)
			{
				Set<Entry<String, Value>> propList = es.get(i).getPropertyList();
				Iterator<Entry<String, Value>> iter = propList.iterator();
				while (iter.hasNext())
				{
					Value v = iter.next().getValue();
					list.add(v);
				}
			}
		}
		return list.toArray();
//		return (ArrayList<Equipment>[]) equipments.values().toArray();
	}

	public HashMap<String, ArrayList<Equipment>> getEquipments() {
		return equipments;
	}

	public void setEquipments(HashMap<String, ArrayList<Equipment>> equipments) {
		this.equipments = equipments;
	}
	public static ObjectsListModel getInstance()
	{
		ObjectsListModel model = new ObjectsListModel();
		Value v = new Value("Light","l1", "isOn", "Boolean", "false");
		model.add(v);
		
		v = new Value("Light","l2", "isOn", "Boolean", "true");
		model.add(v);
		
		v = new Value("Person","xiaomin", "age", "Integer", "21");
		model.add(v);
		
		v = new Value("Air_Condition","a1", "temperature", "Integer", "27");
		model.add(v);
		
		v = new Value("Projector","p1", "discription", "String", "xxy's");
		model.add(v);
		
		v = new Value("Screen","s1", "isDown", "Boolean", "true");
		model.add(v);
		return model;
	}
	
	public static void main(String[] args)
	{
		ObjectsListModel model = ObjectsListModel.getInstance();
		Object[] values = model.elements();
		for(Object o : values)
		{
			Value v = (Value) o;
			System.out.println(v.getEType()+" "+ v.getEquipment()+" " +v.getProperty()+" "+v.getType()+" "+v.getValue());
		}
		model.toStream();
	}
	
	private static byte[] mkBytes( int i)
	{
		byte[] buf4 = new byte[4];
        buf4[0] = (byte) (0xff & i);
        buf4[1] = (byte) ((0xff00 & i) >> 8);
        buf4[2] = (byte) ((0xff0000 & i) >> 16);
        buf4[3] = (byte) ((0xff000000 & i) >> 24);
		return buf4;
	}
	
	public String toStream()
	{
		
		File file = new File("tmp/output.txt");
		
		try {
			FileOutputStream os = new FileOutputStream(file);
			ObjectsListModel model = this;
			Object[] values = model.elements();
			int numOfDevice = values.length;
			//--------界面控制码 2
			os.write(mkBytes(2));
			//--------设备数量
			os.write(mkBytes(numOfDevice));
			for(Object o : values)
			{
				Value v = (Value) o;
//				System.out.println(v.getEType()+" "+ v.getEquipment()+" " +v.getProperty()+" "+v.getType()+" "+v.getValue());
				//一共有三个属性
				os.write(mkBytes(3));
				//----第一对（属性名，属性值）
				String equipmentType = new String("class");
					// 属性名长度 ->5 
				os.write(mkBytes(equipmentType.length()));
					// 属性名         ->class
				for (int i=0; i<equipmentType.length(); i++)
				{
					os.write(equipmentType.charAt(i));
				}
				String etype = v.getEType();
					// 属性值长度 ->
				os.write(mkBytes(etype.length()));
					// 属性值         ->
				for (int i=0; i<etype.length(); i++)
				{
					os.write(etype.charAt(i));
				}
				//----第二对（属性名，属性值）
				String name = new String("name");
					// 属性名长度 ->4
				os.write(mkBytes(name.length()));
					// 属性名         ->name
				for (int i=0; i<name.length(); i++)
				{
					os.write(name.charAt(i));
				}
				String equipmentName = v.getEquipment();
					// 属性值长度 ->
				os.write(mkBytes(equipmentName.length()));
					// 属性值         ->
				for (int i=0; i<equipmentName.length(); i++)
				{
					os.write(equipmentName.charAt(i));
				}
				//----第三对（属性名，属性值）
				String property = v.getProperty();
					// 属性名长度 ->
				os.write(mkBytes(property.length()));
					// 属性名         ->
				for (int i=0; i<property.length(); i++)
				{
					os.write(property.charAt(i));
				}
				String type = v.getType();
				String value = v.getValue();
					// 判断属性值的类型
				if (type.compareTo("Integer") == 0)
				{
					//输入类型
					os.write(mkBytes(-1));
					//输入值
					os.write(mkBytes(Integer.valueOf(value)));
				}
				else if (type.compareTo("Double") == 0)
				{
					//输入类型
					os.write(mkBytes(-2));
					//输入值
					long l = Double.doubleToLongBits(Double.valueOf(value));
					os.write(mkBytes( (int)(l>>>32) ));
					os.write(mkBytes( (int)(l&0xff) ));
				}
				else if (type.compareTo("Boolean") == 0)
				{
					//输入布尔类型
					os.write(mkBytes(-3));
					//输入布尔值
					if (value.compareTo("true") == 0)
					{
						os.write(mkBytes(1));
					}
					else
					{
						os.write(mkBytes(0));
					}
					
				}
				else if (type.compareTo("Float") == 0)
				{
					//输入单精度类型
					os.write( mkBytes(-4));
					//输入值
					int f = Float.floatToIntBits(Float.valueOf(value));
					os.write( mkBytes(f));
				}
				else if (type.compareTo("String") == 0)
				{// 字符串类型 则需要输入字符串长度并输入字符串
					//输入长度
					os.write(mkBytes(value.length()));
					//输入字符串
					for (int i=0; i<value.length(); i++)
					{
						os.write(value.charAt(i));
					}
					
				}
			}
			os.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		return null;
	}
}
