/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.policies;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import cn.edu.pku.ogeditor.commands.ConnectionCreateCommand;
import cn.edu.pku.ogeditor.commands.ConnectionReconnectCommand;
import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.parts.DiagramEditPart;
/**
 * edit policy to regard the shape as connection node
 * @author Xueyuan Xing
 */
public class ShapeNodeEditPolicy extends GraphicalNodeEditPolicy 
{

	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		ConnectionCreateCommand cmd 
		= (ConnectionCreateCommand) request.getStartCommand();
		cmd.setTarget((Shape) getHost().getModel());
		return cmd;
	}

	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		Shape source = (Shape) getHost().getModel();
		String name  = ((String) request.getNewObjectType());
		ConnectionCreateCommand cmd = new ConnectionCreateCommand(source ,name);
		request.setStartCommand(cmd);
		return cmd;
	}

	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		Connection conn = (Connection) request.getConnectionEditPart().getModel();
		Shape newSource = (Shape) getHost().getModel();
		ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn,(DiagramEditPart) this.getHost().getParent());
		cmd.setNewSource(newSource);
		return cmd;
	}

	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		Connection conn = (Connection) request.getConnectionEditPart().getModel();
		Shape newTarget = (Shape) getHost().getModel();
		ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn,(DiagramEditPart) this.getHost().getParent());
		cmd.setNewTarget(newTarget);
		return cmd;
	}
}