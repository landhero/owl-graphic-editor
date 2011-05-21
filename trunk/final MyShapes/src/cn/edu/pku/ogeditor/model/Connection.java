/*******************************************************************************
 * Copyright (c) 2004, 2005 Elias Volanakis and others.
?* All rights reserved. This program and the accompanying materials
?* are made available under the terms of the Eclipse Public License v1.0
?* which accompanies this distribution, and is available at
?* http://www.eclipse.org/legal/epl-v10.html
?*
?* Contributors:
?*????Elias Volanakis - initial API and implementation
?*******************************************************************************/
package cn.edu.pku.ogeditor.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;


public class Connection extends ModelElement {
	public static final String LINENAME_PROP = "LineName";
	private static final IPropertyDescriptor[] descriptors;
	private static final long serialVersionUID = 1;
	private boolean isRoot=false;
	/** True, if the connection is attached to its endpoints. */ 
	private boolean isConnected;
	protected List<ConnectionBendpoint> bendpoints = new ArrayList<ConnectionBendpoint>();
	final public static String PROP_BENDPOINT = "BENDPOINT";

	/** Connection's source endpoint. */
	private Shape source;
	/** Connection's target endpoint. */
	private Shape target;
	private Connection parent;
	private ArrayList<Connection> children=new ArrayList<Connection>();
	private String name="NewConnection";
	private boolean required;

	static {
		descriptors = new IPropertyDescriptor[] {
				new TextPropertyDescriptor(LINENAME_PROP, "Name")};
	}

	/** 
	 * Create a (solid) connection between two distinct shapes.
	 * @param source a source endpoint for this connection (non null)
	 * @param target a target endpoint for this connection (non null)
	 * @param name 
	 * @throws IllegalArgumentException if any of the parameters are null or source == target
	 * @see #setLineStyle(int) 
	 */
	public Connection(Shape source, Shape target) {
		reconnect(source, target);
	}
	public void addBendpoint(int index, ConnectionBendpoint point) {
		getBendpoints().add(index, point);
		firePropertyChange(PROP_BENDPOINT, null, null);
	}
	/**
	 * zhanghao: 为了在更新两个dimension后能发送事件，在MoveBendpointCommand要在用这个方法设置新坐标，
	 * 而不是直接用BendPoint里的方法。
	 */
	public void setBendpointRelativeDimensions(int index, Dimension d1,
			Dimension d2) {
		ConnectionBendpoint cbp = (ConnectionBendpoint) getBendpoints().get(
				index);
		cbp.setRelativeDimensions(d1, d2);
		firePropertyChange(PROP_BENDPOINT, null, null);
	}

	public void removeBendpoint(int index) {
		getBendpoints().remove(index);
		firePropertyChange(PROP_BENDPOINT, null, null);
	}
	public List<ConnectionBendpoint> getBendpoints() {
		return bendpoints;
	}

	public void setBendpoints(List<ConnectionBendpoint> bendpoints) {
		this.bendpoints = bendpoints;
	}
	public Connection(String string){
		if(!string.equals("ElectiveRoot")&&!string.equals("RequiredRoot"))
			System.err.println("Construct an incorrect connection!");
		setRoot(true);
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
	 * Disconnect this connection from the shapes it is attached to.
	 */
	public void disconnect() {
		if (isConnected) {
			source.removeConnection(this);
			target.removeConnection(this);
			isConnected = false;
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

	/** 
	 * Reconnect this connection. 
	 * The connection will reconnect with the shapes it was previously attached to.
	 */  
	public void reconnect() {
		if (!isConnected) {
			source.addSourceConnection(this);
			target.addTargetConnection(this);
			isConnected = true;
		}
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
	public void setName(String name){
		this.name = name;
		firePropertyChange(LINENAME_PROP, null, name);
	}
	/**
	 * Sets the lineStyle based on the String provided by the PropertySheet
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		if (id.equals(LINENAME_PROP)){
			setName((String) value);
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
	public void setChildren(ArrayList<Connection> children) {
		this.children = children;
	}
	public void addChild(Connection child){
		children.add(child);
	}
	public boolean isRoot() {
		return isRoot;
	}
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
}
