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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
/**
 * A container for multiple shapes.
 * This is the "root" of the model data structure.
 * @author Elias Volanakis
 */
public class ShapesDiagram extends ModelElement {

	public static String ID_ROUTER = "router";	//$NON-NLS-1$
	public static Integer ROUTER_MANUAL = new Integer(0);
	public static Integer ROUTER_MANHATTAN = new Integer(1);
	public static Integer ROUTER_SHORTEST_PATH = new Integer(2);

	public static String PropertyDescriptor_LogicDiagram_Manual="Manual";
	public static String PropertyDescriptor_LogicDiagram_Manhattan="Manhattan";
	public static String PropertyDescriptor_LogicDiagram_ShortestPath="Shortest Path";
	protected Integer connectionRouter = null;
	/** Property ID to use when a child is added to this diagram. */
	public static final String CHILD_ADDED_PROP = "ShapesDiagram.ChildAdded";
	/** Property ID to use when a child is removed from this diagram. */
	public static final String CHILD_REMOVED_PROP = "ShapesDiagram.ChildRemoved";
	public static final String ALLCHILDREN_RELOCATED_PROP = "ShapesDiagram.AllChildrenRelocated";
	private static final long serialVersionUID = 1;

	private String name;
	private ShapesDiagram father;
	private List<Shape> shapes;
	private List<ShapesDiagram> lowerLevelDiagrams;
	private List<String> allShapesNames;
	private List<String> allConnectionsNames;

	public ShapesDiagram(){
		shapes=new ArrayList<Shape>();
		lowerLevelDiagrams = new ArrayList<ShapesDiagram>();
		setName("New Ontology");
		setFather(null);

	}
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if(getClass().equals(ShapesDiagram.class)){
			ComboBoxPropertyDescriptor cbd = new ComboBoxPropertyDescriptor(
					ID_ROUTER, 
					"Connection Router",
					new String[]{
							PropertyDescriptor_LogicDiagram_Manual,
							PropertyDescriptor_LogicDiagram_Manhattan,
							PropertyDescriptor_LogicDiagram_ShortestPath});
			cbd.setLabelProvider(new ConnectionRouterLabelProvider());
			return new IPropertyDescriptor[]{cbd};
		}
		return super.getPropertyDescriptors();
	}

	public void setPropertyValue(Object id, Object value){
		if(ID_ROUTER.equals(id))
			setConnectionRouter((Integer)value);
		else super.setPropertyValue(id,value);
	}

	public Object getPropertyValue(Object propName) {
		if(propName.equals(ID_ROUTER))
			return connectionRouter;
		return super.getPropertyValue(propName);
	}
	public void setConnectionRouter(Integer router){
		Integer oldConnectionRouter = connectionRouter;
		connectionRouter = router;
		firePropertyChange(ID_ROUTER, oldConnectionRouter, connectionRouter);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setFather(ShapesDiagram father) {
		this.father = father;
	}
	public ShapesDiagram getFather() {
		return father;
	}

	public void setLowerLevelDiagrams(List<ShapesDiagram> lowLevelDiagrams) {
		this.lowerLevelDiagrams = lowLevelDiagrams;
	}
	public List<ShapesDiagram> getLowerLevelDiagrams() {
		return lowerLevelDiagrams;
	}
	public void addLowerLevelDiagram(ShapesDiagram childDiagram) {
		// TODO Auto-generated method stub
		if(lowerLevelDiagrams.add(childDiagram))
			childDiagram.setFather(this);
		else 
			System.err.println("Can't add to lowerLevelDiagrams");
	}

	public void removeLowerLevelDiagram(ShapesDiagram childDiagram) {
		// TODO Auto-generated method stub
		//删除一个diagram时其shapes的父子关系是否被删除，这个问吴韬
		if(lowerLevelDiagrams.remove(childDiagram))
			childDiagram.setFather(null);
		else 
			System.err.println("Can't remove Diagram "+childDiagram.getName());
	}

	public void addShapeName(String name) {
		if(!ContainShapeName(name))
		{
			allShapesNames.add(name);
		}
	}
	private boolean ContainShapeName(String name) {
		// TODO Auto-generated method stub
		for(int i=0;i<allShapesNames.size();i++)
		{
			if(allShapesNames.get(i).equals(name))
			{
				return true;
			}
		}
		return false;
	}
	public List<String> getAllShapesNames() {
		return allShapesNames;
	}
	public void addConnectionName(String name) {
		if(!ContainConnectionName(name))
		{
			allConnectionsNames.add(name);
		}
	}
	private boolean ContainConnectionName(String name) {
		// TODO Auto-generated method stub
		for(int i=0;i<allConnectionsNames.size();i++)
		{
			if(allConnectionsNames.get(i).equals(name))
			{
				return true;
			}
		}
		return false;
	}
	public List<String> getAllConnectionsNames() {
		return allConnectionsNames;
	}
	public ShapesDiagram getRootDiagram()
	{
		ShapesDiagram root = this;
		while(root.getFather() != null)
			root = root.getFather();
		return root;

	}
	/** Return a List of Shapes in this diagram.  The returned List should not be modified. */
	public List<Shape> getChildren() {
		return shapes;
	}

	/** 
	 * Add a shape to this diagram.
	 * @param s a non-null shape instance
	 * @return true, if the shape was added, false otherwise
	 */
	public boolean addChild(Shape s) {
		if (s != null && shapes.add(s)) {
			firePropertyChange(CHILD_ADDED_PROP, null, s);
			return true;
		}
		return false;
	}

	/**
	 * Remove a shape from this diagram.
	 * @param s a non-null shape instance;
	 * @return true, if the shape was removed, false otherwise
	 */
	public boolean removeChild(Shape s) {
		if (s != null && shapes.remove(s)) {
			firePropertyChange(CHILD_REMOVED_PROP, null, s);
			return true;
		}
		return false;
	}

	public void fireRelocate(){
		firePropertyChange(ALLCHILDREN_RELOCATED_PROP, null, null);
	}
	public void relocatedAll(Rectangle rect){
		int size = shapes.size();
		int sides[][] = new int[size][size];
		boolean used[] = new boolean[size];
		int allocated[] = new int[size];
		int degree[] = new int [size];
		int unity[]  = new int [size];
		Point points[] = new Point[size];
		int center[] = new int[size];
		int i, j, k;
		for (i=0; i<size; i++){
			used[i] = false;
			degree[i] = 0;
			for (j=0; j<size; j++){
				sides[i][j] = 0;
			}
			points[i] = new Point();
			if (i==0){
				center[0] = -1;
				points[0].x = rect.x + 25;
				points[0].y =  rect.y + 25+100*2;
			}
			else{
				if ((i+4)/5 == 1)
					center[i] = 0;
				else center[i] = ((i+4)/5-2)*5 + 1; //the former center point;
				points[i].x = ((i+4)/5)*200 + 25 + rect.x;
				int y = points[0].y;
				switch(i%5){
				case 1: points[i].y = y; break;
				case 2: points[i].y = y-100; break;
				case 3: points[i].y = y+100; break;
				case 4: points[i].y = y-200; break;
				case 0: points[i].y = y+200; break;
				default:
				}	
			}
		}
		for (i=0; i<size; i++){
			List<?> list = ((Shape)shapes.get(i)).getSourceConnections();
			int tsize = list.size();
			for (j=0; j<tsize; j++){
				int temp  = shapes.indexOf(((Connection)list.get(j)).getTarget());
				sides[i][temp] = 0;
			}
			degree[i] = tsize;
			list = ((Shape)shapes.get(i)).getTargetConnections();
			degree[i] += list.size();
		}
		for (i=0; i<size; i++){
			System.err.println(i+":"+center[i]);
		}
		int max = -1;
		int choose = -1;
		for (i=0; i<size; i++){
			if (max < degree[i]){
				max = degree[i];
				choose = i;
			}
		}
		((Shape)shapes.get(choose)).setLocation(points[0]);
		used[choose] = true;
		allocated[0] = choose;
		for (i=1; i<size; i++){
			for (j=0; j<size; j++){
				if (used[j] == true){	//init
					unity[j] = -100000000;
					continue;
				}
				else	unity[j] = 0;   //good
				unity[j] += (sides[allocated[center[i]]][j] + sides[j][allocated[center[i]]])*100;
				for (k=0; k<i; k++){
					unity[j] -= (sides[allocated[k]][j] + sides[j][allocated[k]])*50;
				}
				switch (i%5){
				case 1: 
					unity[j] += degree[j];
					for (k=0; k<i; k++){
						unity[j] -= sides[allocated[k]][j] + sides[j][allocated[k]];
					}
					break;
				case 2:
				case 3:
					k = ((i+4)/5-1)*5 + 1;
					unity[j] += (sides[allocated[k]][j] + sides[j][allocated[k]])*100;
					break;
				case 4:
				case 0:
					unity[j] += (sides[allocated[i-2]][j] + sides[j][allocated[i-2]])*100;
					if (i>5){
						unity[j] += (sides[allocated[i-5]][j] + sides[j][allocated[i-5]])*100;
						unity[j] += (sides[allocated[i-7]][j] + sides[j][allocated[i-7]])*100;
					}
					break;
				default:
				}
			}
			max = -1000000;
			choose = -1;
			for (j=0; j<size; j++){
				if (max<unity[j]){
					max = unity[j];
					choose = j;
				}
			}
			((Shape)shapes.get(choose)).setLocation(points[i]);
			used[choose] = true;
			allocated[i] = choose;
		}
	}

	private class ConnectionRouterLabelProvider 
	extends org.eclipse.jface.viewers.LabelProvider{

		public ConnectionRouterLabelProvider(){
			super();
		}
		public String getText(Object element){
			if(element instanceof Integer){
				Integer integer = (Integer)element;
				if(ROUTER_MANUAL.intValue()==integer.intValue())
					return PropertyDescriptor_LogicDiagram_Manual;
				if(ROUTER_MANHATTAN.intValue()==integer.intValue())
					return PropertyDescriptor_LogicDiagram_Manhattan;
				if(ROUTER_SHORTEST_PATH.intValue()==integer.intValue())
					return PropertyDescriptor_LogicDiagram_ShortestPath;
			}
			return super.getText(element);
		}
	}

	public Integer getConnectionRouter(){
		if(connectionRouter==null)
			connectionRouter = ROUTER_MANUAL;
		return connectionRouter;
	}
}