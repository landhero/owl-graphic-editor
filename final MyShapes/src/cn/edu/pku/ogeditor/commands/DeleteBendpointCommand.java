package cn.edu.pku.ogeditor.commands;

import org.eclipse.draw2d.Bendpoint;

import cn.edu.pku.ogeditor.model.ConnectionBendpoint;


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
