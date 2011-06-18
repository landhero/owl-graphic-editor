/*******************************************************************************
 * Copyright (c) 2004, 2005 Elias Volanakis and others.
?* All rights reserved. This program and the accompanying materials
?* are made available under the terms of the Eclipse Public License v1.0
?* which accompanies this distribution, and is available at
?* http://www.eclipse.org/legal/epl-v10.html
?*
?* Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import cn.edu.pku.ogeditor.ShapesPlugin;

/**
 * Abstract prototype of a shape.
 * Has a size (width and height), a location (x and y position) and a list of incoming
 * and outgoing connections. Use subclasses to instantiate a specific shape.
 * @see cn.edu.pku.ogeditor.model.EllipseShape
 * @see org.eclipse.gef.examples.shapes.model.Shape
 */
public class Shape extends ModelElement {

	private static IPropertyDescriptor[] descriptors;
	private static final Image RECTANGLE_ICON = createImage("icons/ellipse16.gif");
	private static final long serialVersionUID = 1;

	public  static final String NAME_PROP = "Shape.Name";
	public static final String LOCATION_PROP = "Shape.Location";
	public static final String SIZE_PROP = "Shape.Size";
	public static final String SOURCE_CONNECTIONS_PROP = "Shape.SourceConn";
	public static final String TARGET_CONNECTIONS_PROP = "Shape.TargetConn";


	/** Location of this shape. */
	private Point location = new Point(0, 0);
	/** Size of this shape. */
	private Dimension size = new Dimension(100, 60);

	private ShapesDiagram diagram;
	private String name;
	private Shape parent;
	private ArrayList<Shape> children;
	private List<Connection> sourceConnections;
	private List<Connection> targetConnections;

	private String description;
	private boolean isRoot=false;
	private boolean temporarily=false;
	private boolean isSeparator= false;

	public Shape()
	{
		super();
		name = new String();
		description = new String();
		children=new ArrayList<Shape>();
		sourceConnections = new ArrayList<Connection>();
		targetConnections = new ArrayList<Connection>();
	}
	public boolean isTemporarily() {
		return temporarily;
	}

	public void setTemporarily(boolean temporarily) {
		this.temporarily = temporarily;
	}

	public boolean isRoot() {
		return isRoot;
	}
	public void setRoot(boolean root) {
		this.isRoot = root;
	}

	public void setDiagram(ShapesDiagram diagram) {
		this.diagram = diagram;
	}

	public ShapesDiagram getDiagram() {
		return diagram;
	}

	public ArrayList<Shape> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Shape> children) {
		this.children = children;
	}

	public void addChild(Shape child)
	{
		if(!children.contains(child))
			children.add(child);		
	}

	public void removeChild(Shape child) {
		// TODO Auto-generated method stub
		if(children.contains(child))
			children.remove(child);
	}

	public Shape getParent() {
		return parent;
	}

	public void setParent(Shape parent) {
		this.parent = parent;
	}

	public void setSeparator(boolean isSeperator) {
		this.isSeparator = isSeperator;
	}

	public boolean isSeparator() {
		return isSeparator;
	}

	static {
		descriptors = new IPropertyDescriptor[] { 
				new TextPropertyDescriptor(NAME_PROP, "Name"),
		};
	} // static

	protected static Image createImage(String name) {
		InputStream stream = ShapesPlugin.class.getResourceAsStream(name);
		Image image = new Image(null, stream);
		try {
			stream.close();
		} catch (IOException ioe) {
		}
		return image;
	}
	/**
	 * Add an outgoing connection to this shape.
	 * @param conn a non-null connection instance
	 * @throws IllegalArgumentException if the connection is null or has not distinct endpoints
	 */
	void addSourceConnection(Connection conn) {
		if (conn == null ) {
			throw new IllegalArgumentException();
		}
		if (conn.getSource() == this) {
			//if(!sourceConnections.contains(conn))
				sourceConnections.add(conn);
			firePropertyChange(SOURCE_CONNECTIONS_PROP, null, conn);
		} 
	}
	/**
	 * Add an incoming connection to this shape.
	 * @param conn a non-null connection instance
	 * @throws IllegalArgumentException if the connection is null or has not distinct endpoints
	 */
	void addTargetConnection(Connection conn) {
		if (conn == null ) {
			throw new IllegalArgumentException();
		}
		if (conn.getTarget() == this) {
			//if(!targetConnections.contains(conn))
				targetConnections.add(conn);
			firePropertyChange(TARGET_CONNECTIONS_PROP, null, conn);
		}
	}

	/**
	 * Return a pictogram (small icon) describing this model element.
	 * Children should override this method and return an appropriate Image.
	 * @return a 16x16 Image or null
	 */
	public Image getIcon() {
		return RECTANGLE_ICON;
	}

	public String toString() {
		return this.getName();
	}

	/**
	 * Return the Location of this shape.
	 * @return a non-null location instance
	 */
	public Point getLocation() {
		return location.getCopy();
	}

	/**
	 * Returns an array of IPropertyDescriptors for this shape.
	 * <p>The returned array is used to fill the property view, when the edit-part corresponding
	 * to this model element is selected.</p>
	 * @see #descriptors
	 * @see #getPropertyValue(Object)
	 * @see #setPropertyValue(Object, Object)
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return descriptors;
	}

	/**
	 * Return the Size of this shape.
	 * @return a non-null Dimension instance
	 */
	public Dimension getSize() {
		return size.getCopy();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * Return a List of outgoing Connections.
	 */
	public List<Connection> getSourceConnections() {
		return new ArrayList<Connection>(sourceConnections);
	}

	/**
	 * Return a List of incoming Connections.
	 */
	public List<Connection> getTargetConnections() {
		return new ArrayList<Connection>(targetConnections);
	}

	/**
	 * Remove an incoming or outgoing connection from this shape.
	 * @param conn a non-null connection instance
	 * @throws IllegalArgumentException if the parameter is null
	 */
	void removeSourceConnection(Connection conn) {
		if (conn == null) {
			throw new IllegalArgumentException();
		}
		if (conn.getSource() == this
				&& sourceConnections.contains(conn)) {
			sourceConnections.remove(conn);
			firePropertyChange(SOURCE_CONNECTIONS_PROP, null, conn);
		}
	}
	void removeTargetConnection(Connection conn) {
		if (conn == null) {
			throw new IllegalArgumentException();
		}
		if (conn.getTarget() == this
				&& targetConnections.contains(conn)) {
			targetConnections.remove(conn);
			firePropertyChange(TARGET_CONNECTIONS_PROP, null, conn);
		}
	}

	/**
	 * Set the Location of this shape.
	 * @param newLocation a non-null Point instance
	 * @throws IllegalArgumentException if the parameter is null
	 */
	public void setLocation(Point newLocation) {
		if (newLocation == null) {
			throw new IllegalArgumentException();
		}
		location.setLocation(newLocation);
		firePropertyChange(LOCATION_PROP, null, location);
	}

	/**
	 * Set the Size of this shape.
	 * Will not modify the size if newSize is null.
	 * @param newSize a non-null Dimension instance or null
	 */
	public void setSize(Dimension newSize) {
		if (newSize != null) {
			size.setSize(newSize);
			firePropertyChange(SIZE_PROP, null, size);
		}
	}

	public boolean setName(String newName) {
		if (newName == null)
			return false;
		//		if(getDiagram() == null)
		//		{
		name = newName;
		firePropertyChange(NAME_PROP, null, name);
		return  true;
		//		}
		//		else if(getDiagram().ContainShapeName(newName))
		//		{
		//			//弹出个对话框之类
		//			return false;
		//		}
		//		else
		//		{
		//			name = newName;
		//			getDiagram().addShapeName(this);
		//			firePropertyChange(NAME_PROP, null, name);
		//			return true;
		//		}
	}

	public void setDescription(String newDes) {
		if (newDes != null) {
			description = newDes;
		}
	}

	/**
	 * Return the property value for the given propertyId, or null.
	 * <p>The property view uses the IDs from the IPropertyDescriptors array 
	 * to obtain the value of the corresponding properties.</p>
	 * @see #descriptors
	 * @see #getPropertyDescriptors()
	 */
	public Object getPropertyValue(Object propertyId) {
		if (NAME_PROP.equals(propertyId)) {
			return name;
		}
		return super.getPropertyValue(propertyId);
	}

	/**
	 * Set the property value for the given property id.
	 * If no matching id is found, the call is forwarded to the superclass.
	 * <p>The property view uses the IDs from the IPropertyDescriptors array to set the values
	 * of the corresponding properties.</p>
	 * @see #descriptors
	 * @see #getPropertyDescriptors()
	 */
	public void setPropertyValue(Object propertyId, Object value) {
		if (NAME_PROP.equals(propertyId)) {
			setName((String)value);
		}
		else {
			super.setPropertyValue(propertyId, value);
		}
	}
}
