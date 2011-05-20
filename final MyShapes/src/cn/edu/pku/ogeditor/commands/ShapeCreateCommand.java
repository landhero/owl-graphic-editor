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
package cn.edu.pku.ogeditor.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.ShapesEditorPaletteRoot;
import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;

/**
 * A command to add a Shape to a ShapeDiagram.
 * The command can be undone or redone.
 * @author Elias Volanakis
 */
public class ShapeCreateCommand 
	extends Command 
{
	
/** The new shape. */ 
private Shape newShape;
/** ShapeDiagram to add to. */
private final ShapesDiagram parent;
/** The bounds of the new Shape. */
private Rectangle bounds;

/**
 * Create a command that will add a new Shape to a ShapesDiagram.
 * @param newShape the new Shape that is to be added
 * @param parent the ShapesDiagram that will hold the new element
 * @param bounds the bounds of the new shape; the size can be (-1, -1) if not known
 * @throws IllegalArgumentException if any parameter is null, or the request
 * 						  does not provide a new Shape instance
 */
public ShapeCreateCommand(Shape newShape, ShapesDiagram parent, Rectangle bounds) {
	this.newShape = newShape;
	PaletteViewer paletteViewer=ShapesEditor.myselfShapesEditor.paletteViewer;
	ToolEntry  selected=paletteViewer.getActiveTool();
	ShapesEditorPaletteRoot curPaletteRoot = (ShapesEditorPaletteRoot)ShapesEditor.myselfShapesEditor.getPaletteRoot();
	List<?> children=curPaletteRoot.getShapeDrawer().getChildren();
	int index=children.indexOf(selected);
	Shape newShapeParent=curPaletteRoot.getUplevelAllShapes().get(index);
	this.newShape.setParent(newShapeParent);
	newShapeParent.getChildren().add(newShape);
	this.parent = parent;
	this.bounds = bounds;
	
	setLabel("shape creation");
}

/**
 * Can execute if all the necessary information has been provided. 
 * @see org.eclipse.gef.commands.Command#canExecute()
 */
public boolean canExecute() {
	return newShape != null && parent != null && bounds != null;
}
/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#execute()
 */
public void execute() {
	newShape.setLocation(bounds.getLocation());
	Dimension size = bounds.getSize();
	if (size.width > 0 && size.height > 0)
		newShape.setSize(size);
	redo();
	createRequiredShape(newShape);
}
private ArrayList<Shape> requiredShapes = new ArrayList<Shape>();
public void createRequiredShape(Shape shape) {
	if(shape.isRoot())return;
	if (shape == newShape)requiredShapes.add(shape);
	List<Connection> sourceConnection=shape.getParent().getSourceConnections();
	Shape defaultShape;
	for(int i=0;i<sourceConnection.size();i++){
		if(!sourceConnection.get(i).isRequired())continue;
		Shape parentShape=sourceConnection.get(i).getTarget();
		int j;
		for(j=0;j<requiredShapes.size();j++){
			if(requiredShapes.get(j).getParent()==parentShape)break;
		}
		if(j==requiredShapes.size())//如果parentShape的子类还没有被创建
		{ 
		defaultShape=new Shape();
		requiredShapes.add(defaultShape);
		defaultShape.setParent(parentShape);
		parentShape.getChildren().add(defaultShape);
		defaultShape.setName("缺省 "+parentShape.getName());
		defaultShape.setTemporarily(true);
		Point location;
		location=new Point(bounds.getLocation());
		location.x+=300*((new Random()).nextDouble()-0.5);
		location.y+=300*((new Random()).nextDouble()-0.5);
		defaultShape.setLocation(location);
		Dimension size = bounds.getSize();
		if (size.width > 0 && size.height > 0)
			defaultShape.setSize(size);
		parent.addChild(defaultShape);
		createRequiredShape(defaultShape);
		}
		else defaultShape=requiredShapes.get(j);//如果parentShape的子类已经被创建，那么用它来建立新的Connection
		Connection defaultConnection=new Connection(shape, defaultShape);
		defaultConnection.setName("缺省 "+sourceConnection.get(i).getName());
		defaultConnection.setParent(sourceConnection.get(i));
		sourceConnection.get(i).getChildren().add(defaultConnection);
		defaultConnection.reconnect();
		
	}
}


/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#redo()
 */
public void redo() {
	parent.addChild(newShape);
	

	
}

/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#undo()
 */
public void undo() {
	parent.removeChild(newShape);
}
	
}