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
/**
 * A container for multiple shapes.
 * This is the "root" of the model data structure.
 * @author Elias Volanakis
 */
public class ShapesDiagram extends ModelElement {

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

	public ShapesDiagram(){
		shapes=new ArrayList<Shape>();
		lowerLevelDiagrams = new ArrayList<ShapesDiagram>();
		setName("New Ontology");
		setFather(null);

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
	public void addLowLevelDiagram(ShapesDiagram childDiagram) {
		// TODO Auto-generated method stub
		if(lowerLevelDiagrams.add(childDiagram))
			childDiagram.setFather(this);
		else 
			System.err.println("Can't add to lowerLevelDiagrams");
	}

	public void removeLowLevelDiagram(ShapesDiagram childDiagram) {
		// TODO Auto-generated method stub
		if(lowerLevelDiagrams.remove(childDiagram))
			childDiagram.setFather(null);
		else 
			System.err.println("Can't add to lowerLevelDiagrams");
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


}