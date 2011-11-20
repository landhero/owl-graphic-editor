/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;

import cn.edu.pku.ogeditor.model.Connection;

/**
 * Command to create bendpoint of the line
 * @author Tao Wu
 */
public class BendpointCommand extends Command {

	protected int index;
	protected Connection connection;
	protected Dimension d1, d2;

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void redo() {
		execute();
	}

	public void setRelativeDimensions(Dimension dim1, Dimension dim2) {
		d1 = dim1;
		d2 = dim2;
	}

	public void setIndex(int i) {
		index = i;
	}

}
