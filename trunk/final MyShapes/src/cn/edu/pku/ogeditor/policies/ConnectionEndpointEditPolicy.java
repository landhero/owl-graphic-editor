package cn.edu.pku.ogeditor.policies;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.GraphicalEditPart;

public class ConnectionEndpointEditPolicy extends
		org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy {
	protected void addSelectionHandles(){
		super.addSelectionHandles();
		getConnectionFigure().setLineWidth(2);
	}

	protected PolylineConnection getConnectionFigure(){
		return (PolylineConnection)((GraphicalEditPart)getHost()).getFigure();
	}

	protected void removeSelectionHandles(){
		super.removeSelectionHandles();
		getConnectionFigure().setLineWidth(0);
	}
}
