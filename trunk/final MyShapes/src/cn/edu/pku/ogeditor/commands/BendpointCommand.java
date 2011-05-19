/**
 * Copied from GEF Logic example.
 */
package cn.edu.pku.ogeditor.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;

import cn.edu.pku.ogeditor.model.Connection;


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
