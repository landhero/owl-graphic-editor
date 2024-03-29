/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * model of the relation
 * @author Xueyuan Xing
 * @author Tao Wu
 */
public class Connection extends ModelElement {
	public static final String LINENAME_PROP = "Connection.Name";
	public static final String LINESTYLE_REQUIRED = "required";
	public static final String LINESTYLE_ELECTIVE = "elective";
	public static final String LINESTYLE_PROP = "Connection.Style";
	public static final String VISIBLE_PROP = "Connection.Visible";
	private static final IPropertyDescriptor[] descriptors;
	private static final long serialVersionUID = 1;
	private boolean isRoot=false;
	/** True, if the connection is attached to its endpoints. */ 
	private boolean isConnected;
	protected List<ConnectionBendpoint> bendpoints;
	final public static String BENDPOINT_PROP = "Connection.Bendpoint";
	
	private boolean isVisible = true;

	/** Connection's source endpoint. */
	private Shape source;
	/** Connection's target endpoint. */
	private Shape target;
	private Connection parent;
	private ArrayList<Connection> children;
	private String name = null;
	private boolean required;
	private boolean isSeparator = false;
	private String description = new String();
	private List<Shape> domain = new ArrayList<Shape>();
	private List<Shape> range =  new ArrayList<Shape>();

	static {
		descriptors = new IPropertyDescriptor[] {
				new TextPropertyDescriptor(LINENAME_PROP, "Name"),
				new ComboBoxPropertyDescriptor(LINESTYLE_PROP, LINESTYLE_PROP, 
						new String[] {LINESTYLE_REQUIRED,LINESTYLE_ELECTIVE})};
	}
	public Connection(String string){
		children=new ArrayList<Connection>();
		bendpoints = new ArrayList<ConnectionBendpoint>();
		if(!string.equals("ConnectionRoot")
				&&!string.equals("Seperator") && !string.equals("subClassOf"))
			System.err.println("Construct an incorrect connection!");
		setRoot(true);
	}
	public Connection(Shape source, Shape target, String name) {
		children=new ArrayList<Connection>();
		bendpoints = new ArrayList<ConnectionBendpoint>();
		this.name = name;
		reconnect(source, target);
	}
	
	public Connection(Shape source, Shape target, String name, double sourceAngle,
			double targetAngle) {
		children=new ArrayList<Connection>();
		bendpoints = new ArrayList<ConnectionBendpoint>();
		this.name = name;
		setSourceAngle(sourceAngle);
		setTargetAngle(targetAngle);
		reconnect(source, target);
	}
	public void addBendpoint(int index, ConnectionBendpoint point) {
		getBendpoints().add(index, point);
		firePropertyChange(BENDPOINT_PROP, null, null);
	}
	/**
	 * 为了在更新两个dimension后能发送事件，在MoveBendpointCommand要在用这个方法设置新坐标，
	 * 而不是直接用BendPoint里的方法。
	 */
	public void setBendpointRelativeDimensions(int index, Dimension d1,
			Dimension d2) {
		ConnectionBendpoint cbp = (ConnectionBendpoint) getBendpoints().get(
				index);
		cbp.setRelativeDimensions(d1, d2);
		firePropertyChange(BENDPOINT_PROP, null, null);
	}

	public void removeBendpoint(int index) {
		getBendpoints().remove(index);
		firePropertyChange(BENDPOINT_PROP, null, null);
	}
	public List<ConnectionBendpoint> getBendpoints() {
		return bendpoints;
	}

	public void setBendpoints(List<ConnectionBendpoint> bendpoints) {
		this.bendpoints = bendpoints;
	}

	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	/*
	 * for anchor
	 */
	private double sourceAngle = Double.MAX_VALUE;
	private double targetAngle = Double.MAX_VALUE;
	

	public double getSourceAngle() {
		return sourceAngle;
	}

	public void setSourceAngle(double sourceAngle) {
		this.sourceAngle = sourceAngle;
	}

	public double getTargetAngle() {
		return targetAngle;
	}

	public void setTargetAngle(double targetAngle) {
		this.targetAngle = targetAngle;
	}

	/**
	 * Reconnect to a different source and/or target shape.
	 * The connection will disconnect from its current attachments and reconnect to 
	 * the new source and target. 
	 * @param newSource a new source endpoint for this connection (non null)
	 * @param newTarget a new target endpoint for this connection (non null)
	 * @throws IllegalArgumentException if any of the paramers are null or newSource == newTarget
	 */
	public void reconnect(Shape newSource, Shape newTarget) {
		if (newSource == null || newTarget == null ) {
			throw new IllegalArgumentException();
		}
		disconnect();
		this.source = newSource;
		this.target = newTarget;
		reconnect();
	}
	public void disconnect() {
		if (isConnected) {
			source.removeSourceConnection(this);
			target.removeTargetConnection(this);
			isConnected = false;
			source.getDiagram().removeConnection(this);
		}
	}
	public void reconnect() {
		if (!isConnected) {
			source.addSourceConnection(this);
			target.addTargetConnection(this);
			isConnected = true;
			source.getDiagram().addConnection(this);
		}
	}

	/**
	 * Returns the descriptor for the lineStyle property
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return descriptors;
	}

	/**
	 * Returns the lineStyle as String for the Property Sheet
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		if (id.equals(LINENAME_PROP)){
			return name;
		}
		else if (id.equals(LINESTYLE_PROP)){
			if(isRequired())
				return new Integer(0);
			else 
				return new Integer(1);
		}
		return super.getPropertyValue(id);
	}

	/**
	 * Returns the source endpoint of this connection.
	 * @return a non-null Shape instance
	 */
	public Shape getSource() {
		return source;
	}

	/**
	 * Returns the target endpoint of this connection.
	 * @return a non-null Shape instance
	 */
	public Shape getTarget() {
		return target;
	}

	public void setName(String name){
		if(name != null)
		{
			this.name = name;
			firePropertyChange(LINENAME_PROP, null, name);
		}
	}
	/**
	 * Sets the lineStyle based on the String provided by the PropertySheet
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		if (id.equals(LINENAME_PROP)){
			setName((String) value);
		}
		if (id.equals(LINESTYLE_PROP)){
			if(new Integer(0).equals(value))
				setRequired(true);
			else
				setRequired(false);
		}
		super.setPropertyValue(id, value);
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	public Connection getParent() {
		return parent;
	}
	public ArrayList<Connection> getChildren() {
		return children;
	}
	public void setParent(Connection parent) {
		this.parent = parent;
		this.setRequired(parent.isRequired());

	}

	public void addChild(Connection child){
		if(!children.contains(child))
			children.add(child);
	}
	public boolean isRoot() {
		return isRoot;
	}
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
	public void setSeparator(boolean isSeperator) {
		this.isSeparator = isSeperator;
	}
	public boolean isSeparator() {
		return isSeparator;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setDomain(List<Shape> domain) {
		this.domain = domain;
	}
	public List<Shape> getDomain() {
		return domain;
	}
	public void setRange(List<Shape> range) {
		this.range = range;
	}
	public List<Shape> getRange() {
		return range;
	}
	public void setVisible(boolean visible) {
		if(visible == true)
		{
			if(!(source.isVisible() && target.isVisible()))
				return;
		}
		isVisible = visible;
		firePropertyChange(VISIBLE_PROP, null, visible);

	}
	public boolean isVisible()
	{
		return isVisible;
	}
}
