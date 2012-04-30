package cn.edu.pku.ogeditor.wizards;

public class User {

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
