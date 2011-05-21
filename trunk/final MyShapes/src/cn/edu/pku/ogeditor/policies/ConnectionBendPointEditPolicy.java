package cn.edu.pku.ogeditor.policies;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import cn.edu.pku.ogeditor.commands.BendpointCommand;
import cn.edu.pku.ogeditor.commands.CreateBendpointCommand;
import cn.edu.pku.ogeditor.commands.DeleteBendpointCommand;
import cn.edu.pku.ogeditor.commands.MoveBendpointCommand;



public class ConnectionBendPointEditPolicy extends BendpointEditPolicy {

	protected Command getCreateBendpointCommand(BendpointRequest request) {
		CreateBendpointCommand cmd = new CreateBendpointCommand();
		Point p = request.getLocation();
		Connection conn = getConnection();

		conn.translateToRelative(p);

		Point ref1 = getConnection().getSourceAnchor().getReferencePoint();
		Point ref2 = getConnection().getTargetAnchor().getReferencePoint();

		conn.translateToRelative(ref1);
		conn.translateToRelative(ref2);

		cmd.setRelativeDimensions(p.getDifference(ref1), p.getDifference(ref2));
		cmd.setConnection((cn.edu.pku.ogeditor.model.Connection) request.getSource()
				.getModel());
		cmd.setIndex(request.getIndex());
		return cmd;
	}

	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		BendpointCommand cmd = new DeleteBendpointCommand();
		cmd.setConnection((cn.edu.pku.ogeditor.model.Connection) request.getSource().getModel());
		cmd.setIndex(request.getIndex());
		return cmd;
	}

	protected Command getMoveBendpointCommand(BendpointRequest request) {
		MoveBendpointCommand cmd = new MoveBendpointCommand();
		Point p = request.getLocation();
		Connection conn = getConnection();

		conn.translateToRelative(p);

		Point ref1 = getConnection().getSourceAnchor().getReferencePoint();
		Point ref2 = getConnection().getTargetAnchor().getReferencePoint();

		conn.translateToRelative(ref1);
		conn.translateToRelative(ref2);

		cmd.setRelativeDimensions(p.getDifference(ref1), p.getDifference(ref2));
		cmd.setConnection((cn.edu.pku.ogeditor.model.Connection) request.getSource()
				.getModel());
		cmd.setIndex(request.getIndex());
		return cmd;
	}

}
