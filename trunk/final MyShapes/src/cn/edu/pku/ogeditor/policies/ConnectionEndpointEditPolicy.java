/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.policies;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.GraphicalEditPart;
/**
 * edit policy to change the viewer of relation
 * @author Xueyuan Xing
 */
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
