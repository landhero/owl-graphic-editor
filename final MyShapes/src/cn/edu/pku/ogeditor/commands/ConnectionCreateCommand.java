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

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.palette.ToolEntry;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.ShapesEditorPaletteRoot;
import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.ConnectionBendpoint;
import cn.edu.pku.ogeditor.model.Shape;



/**
 * A command to create a connection between two shapes.
 * The command can be undone or redone.
 * <p>
 * This command is designed to be used together with a GraphicalNodeEditPolicy.
 * To use this command properly, following steps are necessary:
 * </p>
 * <ol>
 * <li>Create a subclass of GraphicalNodeEditPolicy.</li>
 * <li>Override the <tt>getConnectionCreateCommand(...)</tt> method, 
 * to create a new instance of this class and put it into the CreateConnectionRequest.</li>
 * <li>Override the <tt>getConnectionCompleteCommand(...)</tt>  method,
 * to obtain the Command from the ConnectionRequest, call setTarget(...) to set the
 * target endpoint of the connection and return this command instance.</li>
 * </ol>
 * @see parts.ShapeEditPart#createEditPolicies() for an
 * 			 example of the above procedure.
 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy
 * @author Elias Volanakis
 */
public class ConnectionCreateCommand extends Command {
/** The connection instance. */
private Connection connection;
/** Start endpoint for the connection. */
private final Shape source;
/** Target endpoint for the connection. */
private Shape target;
private String name;


/**
 *	Instantiate a command that can create a connection between two shapes.
 * @param source the source endpoint (a non-null Shape instance)
 * @param lineStyle the desired line style. See Connection#setLineStyle(int) for details
 * @throws IllegalArgumentException if source is null
 * @see Connection#setLineStyle(int)
 */
public ConnectionCreateCommand(Shape source, String lineName) {
	if (source == null) {
		throw new IllegalArgumentException();
	}
	setLabel("connection creation");
	this.source = source;
	this.name = lineName;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#canExecute()
 */
public boolean canExecute() {
	// disallow source -> source connections
	/*if (source.equals(target)) {
		return false;
	}*/
	// return false, if the source -> target connection exists already
	for (Iterator<Connection> iter = source.getSourceConnections().iterator(); iter.hasNext();) {
		Connection conn = (Connection) iter.next();
		if (conn.getTarget().equals(target)) {
			return false;
		}
	}
	Connection parentConnection;
	parentConnection=getParent();
	if(!parentConnection.isRequired()&&!parentConnection.isRoot()) 
		if(source.getParent().getSourceConnections().indexOf(parentConnection)==-1)
			return false;
	return true;
}

private Connection getParent(){
	ToolEntry  selectedTool=ShapesEditor.myselfShapesEditor.paletteViewer.getActiveTool();
	Connection newConnectionParent;
	ShapesEditorPaletteRoot curPaletteRoot = (ShapesEditorPaletteRoot)ShapesEditor.myselfShapesEditor.getPaletteRoot();
	List<?> children=(List<?>) curPaletteRoot.getRequiredConnectionDrawer().getChildren();
	int index=0;
	index=children.indexOf(selectedTool);
	if(index==-1){
		children=(List<?>) curPaletteRoot.getElectiveConnectionDrawer().getChildren();
		index=children.indexOf(selectedTool);
		newConnectionParent=curPaletteRoot.getUplevelAllElectiveConnections().get(index);
	}else newConnectionParent=curPaletteRoot.getUplevelAllRequiredConnections().get(index);
	return newConnectionParent;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#execute()
 */
public void execute() {
	Connection parentConnection;
	parentConnection=getParent();
	//如果这个Connection的target不符合要求，那么不要创建
	if(!parentConnection.isRequired()
			&& !parentConnection.isRoot()
			&& target.getParent().getTargetConnections().indexOf(parentConnection)==-1) 
			return;

	connection = new Connection(source, target);
	if (source == target) {
        //The start and end points of our connection are both at the center of the rectangle,
        //so the two relative dimensions are equal.
        ConnectionBendpoint cbp = new ConnectionBendpoint();
        cbp.setRelativeDimensions(new Dimension(0, -60), new Dimension(0, -60));
        connection.addBendpoint(0, cbp);
        ConnectionBendpoint cbp2 = new ConnectionBendpoint();
        cbp2.setRelativeDimensions(new Dimension(100, -60), new Dimension(100, -60));
        connection.addBendpoint(1, cbp2);
        ConnectionBendpoint cbp3 = new ConnectionBendpoint();
        cbp3.setRelativeDimensions(new Dimension(100, 0), new Dimension(100, 0));
        connection.addBendpoint(2, cbp3);
    }
	connection.setName(name);
	connection.setParent(parentConnection);
	parentConnection.addChild(connection);

}

/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#redo()
 */
public void redo() {
	connection.reconnect();
}

/**
 * Set the target endpoint for the connection.
 * @param target that target endpoint (a non-null Shape instance)
 * @throws IllegalArgumentException if target is null
 */
public void setTarget(Shape target) {
	if (target == null) {
		throw new IllegalArgumentException();
	}
	this.target = target;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#undo()
 */
public void undo() {
	connection.disconnect();
}
}
