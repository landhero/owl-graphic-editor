/*******************************************************************************
?* All rights reserved. This program and the accompanying materials
?* are made available under the terms of the Eclipse Public License v1.0
?* which accompanies this distribution, and is available at
?* http://www.eclipse.org/legal/epl-v10.html
?*
?* Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jface.viewers.TextCellEditor;

import cn.edu.pku.ogeditor.anchor.BorderAnchor;
import cn.edu.pku.ogeditor.commands.ConnectionDeleteCommand;
import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.ConnectionBendpoint;
import cn.edu.pku.ogeditor.model.ModelElement;
import cn.edu.pku.ogeditor.policies.ConnectionBendPointEditPolicy;
import cn.edu.pku.ogeditor.policies.ConnectionDirectEditPolicy;
import cn.edu.pku.ogeditor.policies.ConnectionEndpointEditPolicy;

/**
 * Edit part for Connection model elements.
 * <p>This edit part must implement the PropertyChangeListener interface, 
 * so it can be notified of property changes in the corresponding model element.
 * </p>
 */


public class ConnectionEditPart extends AbstractConnectionEditPart
implements PropertyChangeListener {

	private Label label = null;
	private ConnectionDirectEditManager directManager = null;
	//*********************

	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
	public ConnectionEditPart(){
		super();
	}

	private BorderAnchor sourceAnchor;
	private BorderAnchor targetAnchor;

	public BorderAnchor getSourceAnchor() {
		return sourceAnchor;
	}

	public void setSourceAnchor(BorderAnchor sourceAnchor) {
		this.sourceAnchor = sourceAnchor;
	}

	public BorderAnchor getTargetAnchor() {
		return targetAnchor;
	}

	public void setTargetAnchor(BorderAnchor targetAnchor) {
		this.targetAnchor = targetAnchor;
	}

	public void activate() {
		if (!isActive()) {
			super.activate();
			((ModelElement) getModel()).addPropertyChangeListener(this);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		// Selection handle edit policy. 
		// Makes the connection show a feedback, when selected by the user.
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new ConnectionEndpointEditPolicy());
		// Allows the removal of the connection model element
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy() {
			protected Command getDeleteCommand(GroupRequest request) {
				return new ConnectionDeleteCommand(getCastedModel());
			}
		});
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new ConnectionDirectEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
				new ConnectionBendPointEditPolicy());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		PolylineConnection connection = new PolylineConnection();
		connection.setTargetDecoration(new PolygonDecoration()); // arrow at target endpoint
		label = new Label();
		label.setOpaque(true);
		label.setText(((Connection)this.getModel()).getName());
		label.setBackgroundColor(ColorConstants.lightGray);
		connection.add(label, new MidpointLocator(connection, 0));
		return connection;
	}

	/**
	 * Upon deactivation, detach from the model element as a property change listener.
	 */
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((ModelElement) getModel()).removePropertyChangeListener(this);
		}
	}

	public Connection getCastedModel() {
		return (Connection) getModel();
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getPropertyName();
		if (Connection.LINENAME_PROP.equals(property)){
			label.setText(getCastedModel().getName());
		}
		if (Connection.PROP_BENDPOINT.equals(property)) {
			refreshBendpoints();
		}
	}
	public void refresh(){
		super.refresh();
		refreshBendpoints();
	}
	protected void refreshBendpoints() {
		Connection conn = (Connection) getModel();
		List<?> modelConstraint = conn.getBendpoints();
		List<RelativeBendpoint> figureConstraint = new ArrayList<RelativeBendpoint>();
		for (int i = 0; i < modelConstraint.size(); i++) {
			ConnectionBendpoint cbp = (ConnectionBendpoint) modelConstraint
					.get(i);
			RelativeBendpoint rbp = new RelativeBendpoint(getConnectionFigure());
			rbp.setRelativeDimensions(cbp.getFirstRelativeDimension(), cbp
					.getSecondRelativeDimension());
			rbp.setWeight((i + 1) / ((float) modelConstraint.size() + 1));
			figureConstraint.add(rbp);
		}
		getConnectionFigure().setRoutingConstraint(figureConstraint);
	}
	public void  performRequest(Request req){
		if (req.getType().equals(RequestConstants.REQ_OPEN)){
			performDirectEdit();
			return;
		}
		super.performRequest(req);
	}
	private void performDirectEdit(){
		if (directManager == null){
			directManager = new ConnectionDirectEditManager(this, TextCellEditor.class,
					new ConnectionCellEditorLocator(label));
		}
		directManager.show();
	}
}