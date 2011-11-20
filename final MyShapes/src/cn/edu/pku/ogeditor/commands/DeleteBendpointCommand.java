/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.commands;

import cn.edu.pku.ogeditor.model.ConnectionBendpoint;

/**
 * command used to delete bend point
 * 
 * @author Tao Wu
 */
public class DeleteBendpointCommand extends BendpointCommand{
	private ConnectionBendpoint deletedBendpoint;
	public void execute() {
		deletedBendpoint = (ConnectionBendpoint)connection.getBendpoints().get(index);
		connection.removeBendpoint(index);
	}

	public void undo() {
		connection.addBendpoint(index, deletedBendpoint);
	}

}
