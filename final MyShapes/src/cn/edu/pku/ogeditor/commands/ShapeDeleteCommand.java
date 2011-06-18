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

import org.eclipse.gef.commands.Command;

import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;

public class ShapeDeleteCommand extends Command {
	/** Shape to remove. */
	private final Shape child;

	/** ShapeDiagram to remove from. */
	private final ShapesDiagram parentDiagram;
	/** Holds a copy of the outgoing connections of child. */
	private List<Connection> sourceConnections;
	/** Holds a copy of the incoming connections of child. */
	private List<Connection> targetConnections;
	/** True, if child was removed from its parent. */
	private boolean wasRemoved;

	/**
	 * Create a command that will remove the shape from its parent.
	 * 
	 * @param parent
	 *            the ShapesDiagram containing the child
	 * @param child
	 *            the Shape to remove
	 * @throws IllegalArgumentException
	 *             if any parameter is null
	 */
	public ShapeDeleteCommand(ShapesDiagram parentDiagram, Shape child) {
		if (parentDiagram == null || child == null) {
			throw new IllegalArgumentException();
		}
		setLabel("shape deletion");
		this.parentDiagram = parentDiagram;
		this.child = child;
	}

	/**
	 * Reconnects a List of Connections with their previous endpoints.
	 * 
	 * @param connections
	 *            a non-null List of connections
	 */
	private void addConnections(List<Connection> connections) {
		for (Iterator<Connection> iter = connections.iterator(); iter.hasNext();) {
			Connection conn = (Connection) iter.next();
			conn.reconnect();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean canUndo() {
		return wasRemoved;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		// store a copy of incoming & outgoing connections before proceeding
		sourceConnections = child.getSourceConnections();
		targetConnections = child.getTargetConnections();
		redo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		// remove the child and disconnect its connections
		wasRemoved = parentDiagram.removeChild(child);
		if (wasRemoved) {
			child.getParent().removeChild(child);
			removeConnections(sourceConnections);
			removeConnections(targetConnections);
		}
	}

	/**
	 * Disconnects a List of Connections from their endpoints.
	 * 
	 * @param connections
	 *            a non-null List of connections
	 */
	private void removeConnections(List<Connection> connections) {
		for (Iterator<Connection> iter = connections.iterator(); iter.hasNext();) {
			Connection conn = (Connection) iter.next();
			conn.disconnect();
		}
	}

	public void undo() {
		// add the child and reconnect its connections
		if (parentDiagram.addChild(child)) {
			addConnections(sourceConnections);
			addConnections(targetConnections);
		}
	}
}