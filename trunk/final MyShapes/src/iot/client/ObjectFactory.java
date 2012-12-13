package iot.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the iot.client package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _GetAllProperties_QNAME = new QName(
			"http://manager.iot/", "getAllProperties");
	private final static QName _InitDB_QNAME = new QName("http://manager.iot/",
			"initDB");
	private final static QName _CleanDBResponse_QNAME = new QName(
			"http://manager.iot/", "cleanDBResponse");
	private final static QName _InitDBResponse_QNAME = new QName(
			"http://manager.iot/", "initDBResponse");
	private final static QName _GetProperty_QNAME = new QName(
			"http://manager.iot/", "getProperty");
	private final static QName _CleanDB_QNAME = new QName(
			"http://manager.iot/", "cleanDB");
	private final static QName _SetPropertyResponse_QNAME = new QName(
			"http://manager.iot/", "setPropertyResponse");
	private final static QName _MongoException_QNAME = new QName(
			"http://manager.iot/", "MongoException");
	private final static QName _Main_QNAME = new QName("http://manager.iot/",
			"main");
	private final static QName _GetAllPropertiesResponse_QNAME = new QName(
			"http://manager.iot/", "getAllPropertiesResponse");
	private final static QName _MainResponse_QNAME = new QName(
			"http://manager.iot/", "mainResponse");
	private final static QName _SetProperty_QNAME = new QName(
			"http://manager.iot/", "setProperty");
	private final static QName _GetPropertyResponse_QNAME = new QName(
			"http://manager.iot/", "getPropertyResponse");
	private final static QName _UnknownHostException_QNAME = new QName(
			"http://manager.iot/", "UnknownHostException");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: iot.client
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link Main }
	 * 
	 */
	public Main createMain() {
		return new Main();
	}

	/**
	 * Create an instance of {@link MongoException }
	 * 
	 */
	public MongoException createMongoException() {
		return new MongoException();
	}

	/**
	 * Create an instance of {@link SetProperty }
	 * 
	 */
	public SetProperty createSetProperty() {
		return new SetProperty();
	}

	/**
	 * Create an instance of {@link Value }
	 * 
	 */
	public Value createValue() {
		return new Value();
	}

	/**
	 * Create an instance of {@link MainResponse }
	 * 
	 */
	public MainResponse createMainResponse() {
		return new MainResponse();
	}

	/**
	 * Create an instance of {@link InitDB }
	 * 
	 */
	public InitDB createInitDB() {
		return new InitDB();
	}

	/**
	 * Create an instance of {@link UnknownHostException }
	 * 
	 */
	public UnknownHostException createUnknownHostException() {
		return new UnknownHostException();
	}

	/**
	 * Create an instance of {@link CleanDB }
	 * 
	 */
	public CleanDB createCleanDB() {
		return new CleanDB();
	}

	/**
	 * Create an instance of {@link GetAllProperties }
	 * 
	 */
	public GetAllProperties createGetAllProperties() {
		return new GetAllProperties();
	}

	/**
	 * Create an instance of {@link CleanDBResponse }
	 * 
	 */
	public CleanDBResponse createCleanDBResponse() {
		return new CleanDBResponse();
	}

	/**
	 * Create an instance of {@link GetAllPropertiesResponse }
	 * 
	 */
	public GetAllPropertiesResponse createGetAllPropertiesResponse() {
		return new GetAllPropertiesResponse();
	}

	/**
	 * Create an instance of {@link InitDBResponse }
	 * 
	 */
	public InitDBResponse createInitDBResponse() {
		return new InitDBResponse();
	}

	/**
	 * Create an instance of {@link GetProperty }
	 * 
	 */
	public GetProperty createGetProperty() {
		return new GetProperty();
	}

	/**
	 * Create an instance of {@link GetPropertyResponse }
	 * 
	 */
	public GetPropertyResponse createGetPropertyResponse() {
		return new GetPropertyResponse();
	}

	/**
	 * Create an instance of {@link SetPropertyResponse }
	 * 
	 */
	public SetPropertyResponse createSetPropertyResponse() {
		return new SetPropertyResponse();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetAllProperties }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://manager.iot/", name = "getAllProperties")
	public JAXBElement<GetAllProperties> createGetAllProperties(
			GetAllProperties value) {
		return new JAXBElement<GetAllProperties>(_GetAllProperties_QNAME,
				GetAllProperties.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link InitDB }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://manager.iot/", name = "initDB")
	public JAXBElement<InitDB> createInitDB(InitDB value) {
		return new JAXBElement<InitDB>(_InitDB_QNAME, InitDB.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link CleanDBResponse }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://manager.iot/", name = "cleanDBResponse")
	public JAXBElement<CleanDBResponse> createCleanDBResponse(
			CleanDBResponse value) {
		return new JAXBElement<CleanDBResponse>(_CleanDBResponse_QNAME,
				CleanDBResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link InitDBResponse }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://manager.iot/", name = "initDBResponse")
	public JAXBElement<InitDBResponse> createInitDBResponse(InitDBResponse value) {
		return new JAXBElement<InitDBResponse>(_InitDBResponse_QNAME,
				InitDBResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetProperty }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://manager.iot/", name = "getProperty")
	public JAXBElement<GetProperty> createGetProperty(GetProperty value) {
		return new JAXBElement<GetProperty>(_GetProperty_QNAME,
				GetProperty.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link CleanDB }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://manager.iot/", name = "cleanDB")
	public JAXBElement<CleanDB> createCleanDB(CleanDB value) {
		return new JAXBElement<CleanDB>(_CleanDB_QNAME, CleanDB.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link SetPropertyResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://manager.iot/", name = "setPropertyResponse")
	public JAXBElement<SetPropertyResponse> createSetPropertyResponse(
			SetPropertyResponse value) {
		return new JAXBElement<SetPropertyResponse>(_SetPropertyResponse_QNAME,
				SetPropertyResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link MongoException }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://manager.iot/", name = "MongoException")
	public JAXBElement<MongoException> createMongoException(MongoException value) {
		return new JAXBElement<MongoException>(_MongoException_QNAME,
				MongoException.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Main }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://manager.iot/", name = "main")
	public JAXBElement<Main> createMain(Main value) {
		return new JAXBElement<Main>(_Main_QNAME, Main.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetAllPropertiesResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://manager.iot/", name = "getAllPropertiesResponse")
	public JAXBElement<GetAllPropertiesResponse> createGetAllPropertiesResponse(
			GetAllPropertiesResponse value) {
		return new JAXBElement<GetAllPropertiesResponse>(
				_GetAllPropertiesResponse_QNAME,
				GetAllPropertiesResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link MainResponse }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://manager.iot/", name = "mainResponse")
	public JAXBElement<MainResponse> createMainResponse(MainResponse value) {
		return new JAXBElement<MainResponse>(_MainResponse_QNAME,
				MainResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SetProperty }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://manager.iot/", name = "setProperty")
	public JAXBElement<SetProperty> createSetProperty(SetProperty value) {
		return new JAXBElement<SetProperty>(_SetProperty_QNAME,
				SetProperty.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetPropertyResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://manager.iot/", name = "getPropertyResponse")
	public JAXBElement<GetPropertyResponse> createGetPropertyResponse(
			GetPropertyResponse value) {
		return new JAXBElement<GetPropertyResponse>(_GetPropertyResponse_QNAME,
				GetPropertyResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link UnknownHostException }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://manager.iot/", name = "UnknownHostException")
	public JAXBElement<UnknownHostException> createUnknownHostException(
			UnknownHostException value) {
		return new JAXBElement<UnknownHostException>(
				_UnknownHostException_QNAME, UnknownHostException.class, null,
				value);
	}

}
