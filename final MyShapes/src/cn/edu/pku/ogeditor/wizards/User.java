package cn.edu.pku.ogeditor.wizards;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String uri;

	private String rfid;
	private String type;
	
	public User(String uri, String name, String type) {
		this.setUri(uri);
		this.setRfid(name);
		this.type = type;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public void setRfid(String rfid) {
		this.rfid = rfid;
	}

	public String getRfid() {
		return rfid;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
