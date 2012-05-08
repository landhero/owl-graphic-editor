/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.commands;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.ShapesEditorPaletteRoot;
import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.ConnectionBendpoint;
import cn.edu.pku.ogeditor.model.Shape;

/**
 * command used to add a relation between two concepts
 * 
 * @author Xueyuan Xing
 * @author Tao Wu
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
	 * Instantiate a command that can create a connection between two shapes.
	 * 
	 * @param source
	 *            the source endpoint (a non-null Shape instance)
	 * @param lineStyle
	 *            the desired line style. See Connection#setLineStyle(int) for
	 *            details
	 * @throws IllegalArgumentException
	 *             if source is null
	 * @see Connection#setLineStyle(int)
	 */
	public ConnectionCreateCommand(Shape source, String name) {
		if (source == null) {
			throw new IllegalArgumentException();
		}
		setLabel("connection creation");
		this.source = source;
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		Connection parentConnection;
		parentConnection = getParent();
		if (!parentConnection.isRoot())
			if (!source.getParent().containSourceConnectionName(
					parentConnection))
				return false;
		return true;
	}

	private Connection getParent() {
		ToolEntry selectedTool = ShapesEditor.myselfShapesEditor.paletteViewer
				.getActiveTool();
		Connection newConnectionParent;
		ShapesEditorPaletteRoot curPaletteRoot = (ShapesEditorPaletteRoot) ShapesEditor.myselfShapesEditor
				.getPaletteRoot();
		List<?> children = (List<?>) curPaletteRoot.getConnectionDrawer()
				.getChildren();
		int index = 0;
		index = children.indexOf(selectedTool);
		newConnectionParent = curPaletteRoot.getAllUpperLevelConnections().get(
				index);
		return newConnectionParent;
	}

	public void execute() {
		if (existSameConnection()) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Error", "The \"" + name + "\" Relation has existed!");
			return;
		}
		boolean connectionExist = false;
		Connection parentConnection;
		parentConnection = getParent();
		// 如果这个Connection的target不符合要求，那么不要创建
		if (!parentConnection.isRoot() // 如果是最顶层本体则可以随意创建
				&& !target.getParent().containTargetConnectionName(
						parentConnection))
			return;

		for (Iterator<Connection> iter = source.getSourceConnections()
				.iterator(); iter.hasNext();) {
			Connection conn = (Connection) iter.next();
			if (conn.getTarget().equals(target)) {
				connectionExist = true;
			}
		}
		for (Iterator<Connection> iter = target.getSourceConnections()
				.iterator(); iter.hasNext();) {
			Connection conn = (Connection) iter.next();
			if (conn.getTarget().equals(source)) {
				connectionExist = true;
			}
		}
		if (connectionExist)
			connection = new Connection(source, target, name, Math.PI
					* (Math.random() * 2 - 1), Math.PI
					* (Math.random() * 2 - 1));
		else
			connection = new Connection(source, target, name);

		if (source == target) {
			// The start and end points of our connection are both at the center
			// of the rectangle,
			// so the two relative dimensions are equal.
			ConnectionBendpoint cbp = new ConnectionBendpoint();
			cbp.setRelativeDimensions(new Dimension(0, -60), new Dimension(0,
					-60));
			connection.addBendpoint(0, cbp);
			ConnectionBendpoint cbp2 = new ConnectionBendpoint();
			cbp2.setRelativeDimensions(new Dimension(100, -60), new Dimension(
					100, -60));
			connection.addBendpoint(1, cbp2);
			ConnectionBendpoint cbp3 = new ConnectionBendpoint();
			cbp3.setRelativeDimensions(new Dimension(100, 0), new Dimension(
					100, 0));
			connection.addBendpoint(2, cbp3);
		}
		connection.setName(name);
		connection.setParent(parentConnection);
		parentConnection.addChild(connection);
		source.getDiagram().addConnection(connection);

	}

	private boolean existSameConnection() {
		List<Connection> connections = source.getSourceConnections();
		for (int i = 0; i < connections.size(); i++) {
			Connection curConnection = connections.get(i);
			if (curConnection.getTarget() == target
					&& connections.get(i).getName().equals(name))
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		connection.reconnect();
	}

	/**
	 * Set the target endpoint for the connection.
	 * 
	 * @param target
	 *            that target endpoint (a non-null Shape instance)
	 * @throws IllegalArgumentException
	 *             if target is null
	 */
	public void setTarget(Shape target) {
		if (target == null) {
			throw new IllegalArgumentException();
		}
		this.target = target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		connection.disconnect();
	}
}
