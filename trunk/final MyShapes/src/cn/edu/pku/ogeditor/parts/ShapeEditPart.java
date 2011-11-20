/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *******************************************************************************/
package cn.edu.pku.ogeditor.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import cn.edu.pku.ogeditor.anchor.BorderAnchor;
import cn.edu.pku.ogeditor.anchor.EllipseBorderAnchor;
import cn.edu.pku.ogeditor.figures.ShapeFigure;
import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.ModelElement;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.policies.ShapeComponentEditPolicy;
import cn.edu.pku.ogeditor.policies.ShapeDirectEditPolicy;
import cn.edu.pku.ogeditor.policies.ShapeNodeEditPolicy;

/**
 * EditPart used for Shape instances (more specific for EllipticalShape and
 * RectangularShape instances).
 * <p>
 * This edit part must implement the PropertyChangeListener interface, so it can
 * be notified of property changes in the corresponding model element.
 * </p>
 * 
 * @author Xueyuan Xing
 */
public class ShapeEditPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener, NodeEditPart {

	// private ConnectionAnchor anchor;
	private ShapeDirectEditManager directmanager = null;

	/**
	 * Upon activation, attach to the model element as a property change
	 * listener.
	 */
	public void activate() {
		if (!isActive()) {
			super.activate();
			((ModelElement) getModel()).addPropertyChangeListener(this);
			//refreshVisibility();
		}
	}
//
//	private void refreshVisibility() {
//		getCastedModel().setVisible(getCastedModel().isVisible());
//	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new ShapeDirectEditPolicy());
		// allow removal of the associated model element
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new ShapeComponentEditPolicy());
		// allow the creation of connections and
		// and the reconnection of connections between Shape instances
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new ShapeNodeEditPolicy());
	}

	protected IFigure createFigure() {
		return new ShapeFigure(getCastedModel().getName(), getCastedModel()
				.getParent().getName(), getCastedModel().getColor());
	}

	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((ModelElement) getModel()).removePropertyChangeListener(this);
		}
	}

	public Shape getCastedModel() {
		return (Shape) getModel();
	}

	public Shape getModelFromOut() {
		return getCastedModel();
	}

	protected List<Connection> getModelSourceConnections() {
		return getCastedModel().getSourceConnections();
	}

	protected List<?> getModelTargetConnections() {
		return getCastedModel().getTargetConnections();
	}

	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		cn.edu.pku.ogeditor.parts.ConnectionEditPart con = (cn.edu.pku.ogeditor.parts.ConnectionEditPart) connection;
		BorderAnchor anchor = con.getSourceAnchor();
		if (anchor == null || anchor.getOwner() != getFigure()) {
			if (getModel() instanceof Shape)
				anchor = new EllipseBorderAnchor(
						((ShapeFigure) getFigure()).getEllipseFigure());
			else
				throw new IllegalArgumentException("unexpected model");

			Connection conModel = (Connection) con.getModel();
			anchor.setAngle(conModel.getSourceAngle());
			con.setSourceAnchor(anchor);
		}
		return anchor;
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		if (request instanceof ReconnectRequest) {
			ReconnectRequest r = (ReconnectRequest) request;
			cn.edu.pku.ogeditor.parts.ConnectionEditPart con = (cn.edu.pku.ogeditor.parts.ConnectionEditPart) r
					.getConnectionEditPart();
			Connection conModel = (Connection) con.getModel();
			BorderAnchor anchor = con.getSourceAnchor();
			GraphicalEditPart part = (GraphicalEditPart) r.getTarget();
			if (anchor == null || anchor.getOwner() != part.getFigure()) {
				if (getModel() instanceof Shape)
					anchor = new EllipseBorderAnchor(
							((ShapeFigure) getFigure()).getEllipseFigure());
				else
					throw new IllegalArgumentException("unexpected model");

				anchor.setAngle(conModel.getSourceAngle());
				con.setSourceAnchor(anchor);
			}

			Point loc = r.getLocation();
			Rectangle rect = Rectangle.SINGLETON;
			rect.setBounds(getFigure().getBounds());
			getFigure().translateToAbsolute(rect);
			Point ref = rect.getCenter();
			double dx = loc.x - ref.x;
			double dy = loc.y - ref.y;
			anchor.setAngle(Math.atan2(dy, dx));
			conModel.setSourceAngle(anchor.getAngle());
			return anchor;
		} else {
			if (getModel() instanceof Shape)
				return new EllipseBorderAnchor(
						((ShapeFigure) getFigure()).getEllipseFigure());
			else
				throw new IllegalArgumentException("unexpected model");
		}
	}

	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		cn.edu.pku.ogeditor.parts.ConnectionEditPart con = (cn.edu.pku.ogeditor.parts.ConnectionEditPart) connection;
		BorderAnchor anchor = con.getTargetAnchor();
		if (anchor == null || anchor.getOwner() != getFigure()) {
			if (getModel() instanceof Shape)
				anchor = new EllipseBorderAnchor(
						((ShapeFigure) getFigure()).getEllipseFigure());
			else
				throw new IllegalArgumentException("unexpected model");

			Connection conModel = (Connection) con.getModel();
			anchor.setAngle(conModel.getTargetAngle());
			con.setTargetAnchor(anchor);
		}
		return anchor;
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		if (request instanceof ReconnectRequest) {
			ReconnectRequest r = (ReconnectRequest) request;
			cn.edu.pku.ogeditor.parts.ConnectionEditPart con = (cn.edu.pku.ogeditor.parts.ConnectionEditPart) r
					.getConnectionEditPart();
			Connection conModel = (Connection) con.getModel();
			BorderAnchor anchor = con.getTargetAnchor();
			GraphicalEditPart part = (GraphicalEditPart) r.getTarget();
			if (anchor == null || anchor.getOwner() != part.getFigure()) {
				if (getModel() instanceof Shape)
					anchor = new EllipseBorderAnchor(
							((ShapeFigure) getFigure()).getEllipseFigure());
				else
					throw new IllegalArgumentException("unexpected model");

				anchor.setAngle(conModel.getTargetAngle());
				con.setTargetAnchor(anchor);
			}

			Point loc = r.getLocation();
			Rectangle rect = Rectangle.SINGLETON;
			rect.setBounds(getFigure().getBounds());
			getFigure().translateToAbsolute(rect);
			Point ref = rect.getCenter();
			double dx = loc.x - ref.x;
			double dy = loc.y - ref.y;
			anchor.setAngle(Math.atan2(dy, dx));
			conModel.setTargetAngle(anchor.getAngle());
			return anchor;
		} else {
			if (getModel() instanceof Shape)
				return new EllipseBorderAnchor(
						((ShapeFigure) getFigure()).getEllipseFigure());
			else
				throw new IllegalArgumentException("unexpected model");
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (Shape.SIZE_PROP.equals(prop) || Shape.LOCATION_PROP.equals(prop)) {
			refreshVisuals();
		} else if (Shape.SOURCE_CONNECTIONS_PROP.equals(prop)) {
			refreshSourceConnections();
		} else if (Shape.TARGET_CONNECTIONS_PROP.equals(prop)) {
			refreshTargetConnections();
		} else if (Shape.NAME_PROP.equals(prop)) {
			((ShapeFigure) getFigure()).getLabel().setText(
					getCastedModel().getName());
		} else if (Shape.COLOR_PROP.equals(prop)) {
			// 加一个对话框
			boolean confirm = MessageDialog.openConfirm(Display.getDefault()
					.getActiveShell(), "提示", "确实要改变该类及其所有子类颜色吗？");
			if (confirm)
				((ShapeFigure) getFigure()).getEllipseFigure()
						.setBackgroundColor(
								new Color(null, getCastedModel().getColor()));

			// getCastedModel().refreshChildrenColor();
		} else if (Shape.VISIBLE_PROP.equals(prop)) {
			((ShapeFigure) getFigure()).setVisible((Boolean) evt.getNewValue());
		}
	}

	protected void refreshVisuals() {
		Rectangle bounds = new Rectangle(getCastedModel().getLocation(),
				getCastedModel().getSize());
		// setTemporarily(isTemporarily());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), bounds);
		getFigure().setVisible(getCastedModel().isVisible());

	}

	public void performRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			performDirectEdit();
		} else
			super.performRequest(req);
	}

	private void performDirectEdit() {
		if (directmanager == null) {
			directmanager = new ShapeDirectEditManager(this,
					TextCellEditor.class, new ShapeCellEditorLocator(
							(ShapeFigure) getFigure()));
		}
		// if(getCastedModel().isTemporarily())
		// setTemporarily(false);
		// else
		directmanager.show();
	}
}