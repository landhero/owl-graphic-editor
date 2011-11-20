/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.pku.ogeditor.model.Connection;

/**
 * command used to delete a relation between two concepts
 * @author Xueyuan Xing
 * @author Tao Wu
 */
public class ConnectionDeleteCommand extends Command {

/** Connection instance to disconnect. */
private final Connection connection;

/** 
 * Create a command that will disconnect a connection from its endpoints.
 * @param conn the connection instance to disconnect (non-null)
 * @throws IllegalArgumentException if conn is null
 */ 
public ConnectionDeleteCommand(Connection conn) {
	if (conn == null) {
		throw new IllegalArgumentException();
	}
	setLabel("connection deletion");
	this.connection = conn;
}


/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#execute()
 */
public void execute() {
	connection.disconnect();
	connection.getSource().getDiagram().removeConnection(connection);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.commands.Command#undo()
 */
public void undo() {
	connection.reconnect();
	//connection.getSource().getDiagram().addConnection(connection);
}
}
