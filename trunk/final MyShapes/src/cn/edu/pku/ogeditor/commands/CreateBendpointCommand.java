package cn.edu.pku.ogeditor.commands;

import cn.edu.pku.ogeditor.model.ConnectionBendpoint;



public class CreateBendpointCommand extends BendpointCommand {

	public void execute() {
		ConnectionBendpoint cbp = new ConnectionBendpoint();
		cbp.setRelativeDimensions(d1,d2);
		connection.addBendpoint(index, cbp);
	}

	public void undo() {
		connection.removeBendpoint(index);
	}

}
