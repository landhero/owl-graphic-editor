package iot.client;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "ManagerDelegate", targetNamespace = "http://manager.iot/")
public interface ManagerDelegate {

	/**
	 * 
	 * @param arg0
	 * @throws MongoException_Exception
	 * @throws UnknownHostException_Exception
	 */
	@WebMethod
	@RequestWrapper(localName = "main", targetNamespace = "http://manager.iot/", className = "iot.client.Main")
	@ResponseWrapper(localName = "mainResponse", targetNamespace = "http://manager.iot/", className = "iot.client.MainResponse")
	public void main(
			@WebParam(name = "arg0", targetNamespace = "") List<String> arg0)
			throws MongoException_Exception, UnknownHostException_Exception;

	/**
	 * 
	 * @param arg2
	 * @param arg1
	 * @param arg0
	 */
	@WebMethod
	@RequestWrapper(localName = "setProperty", targetNamespace = "http://manager.iot/", className = "iot.client.SetProperty")
	@ResponseWrapper(localName = "setPropertyResponse", targetNamespace = "http://manager.iot/", className = "iot.client.SetPropertyResponse")
	public void setProperty(
			@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1,
			@WebParam(name = "arg2", targetNamespace = "") String arg2);

	/**
	 * 
	 * @param arg1
	 * @param arg0
	 * @return returns iot.client.Value
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getProperty", targetNamespace = "http://manager.iot/", className = "iot.client.GetProperty")
	@ResponseWrapper(localName = "getPropertyResponse", targetNamespace = "http://manager.iot/", className = "iot.client.GetPropertyResponse")
	public Value getProperty(
			@WebParam(name = "arg0", targetNamespace = "") String arg0,
			@WebParam(name = "arg1", targetNamespace = "") String arg1);

	/**
     * 
     */
	@WebMethod
	@RequestWrapper(localName = "initDB", targetNamespace = "http://manager.iot/", className = "iot.client.InitDB")
	@ResponseWrapper(localName = "initDBResponse", targetNamespace = "http://manager.iot/", className = "iot.client.InitDBResponse")
	public void initDB();

	/**
     * 
     */
	@WebMethod
	@RequestWrapper(localName = "cleanDB", targetNamespace = "http://manager.iot/", className = "iot.client.CleanDB")
	@ResponseWrapper(localName = "cleanDBResponse", targetNamespace = "http://manager.iot/", className = "iot.client.CleanDBResponse")
	public void cleanDB();

	/**
	 * 
	 * @return returns java.util.List<iot.client.Value>
	 */
	@WebMethod
	@WebResult(targetNamespace = "")
	@RequestWrapper(localName = "getAllProperties", targetNamespace = "http://manager.iot/", className = "iot.client.GetAllProperties")
	@ResponseWrapper(localName = "getAllPropertiesResponse", targetNamespace = "http://manager.iot/", className = "iot.client.GetAllPropertiesResponse")
	public List<Value> getAllProperties();

}