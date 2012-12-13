package iot.client;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for value complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="value">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="equipment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="property" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="eType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "value", propOrder = { "equipment", "property", "type",
		"value", "eType" })
public class Value implements Serializable{

	protected String equipment;
	protected String property;
	protected String type;
	protected String value;
	protected String eType;

	public Value(String et, String e, String p, String t,
			String v) {
		// TODO Auto-generated constructor stub
		setEType(et);
		setEquipment(e);
		setProperty(p);
		setType(t);
		setValue(v);
	}

	public Value() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Gets the value of the equipment property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEquipment() {
		return equipment;
	}

	/**
	 * Sets the value of the equipment property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEquipment(String value) {
		this.equipment = value;
	}

	/**
	 * Gets the value of the property property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Sets the value of the property property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setProperty(String value) {
		this.property = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setType(String value) {
		this.type = value;
	}

	/**
	 * Gets the value of the value property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the value of the eType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEType() {
		return eType;
	}

	/**
	 * Sets the value of the eType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEType(String value) {
		this.eType = value;
	}

}
