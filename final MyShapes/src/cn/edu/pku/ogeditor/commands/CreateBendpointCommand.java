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
 * command used to create bend point
 * 
 * @author Tao Wu
 */
public class CreateBendpointCommand extends BendpointCommand {

	public void execute() {
		ConnectionBendpoint cbp = new ConnectionBendpoint();
		cbp.setRelativeDimensions(d1, d2);
		connection.addBendpoint(index, cbp);
	}

	public void undo() {
		connection.removeBendpoint(index);
	}

}
