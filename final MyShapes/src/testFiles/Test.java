package testFiles;

import iot.client.ManagerDelegate;
import iot.client.ManagerService;
import iot.client.Value;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		// TODO Auto-generated method stub
		ManagerService server = new ManagerService();
		ManagerDelegate md = server.getManagerPort();
//		Manager m = gson.fromJson(md.getManager(), Manager.class);

		Value v = md.getProperty("Air_Condition_1", "Air_Condition_Temperature");
		if(null != v)
			System.out.println(v.getValue());
//		md.setProperty("l1", "isOn", new Boolean(false));
//		
//		v = md.getProperty("l1", "isOn");
//		test = ValueFactory.parseValue(v);
//		if(null != test)
////		Value test = gson.fromJson(v,iot.baseProperty.BooleanValue.class);
//			System.out.println(test.getValue());

	}

}