/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.AutomaticRouter;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.swt.SWT;

import cn.edu.pku.ogeditor.model.ModelElement;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;
import cn.edu.pku.ogeditor.policies.ShapesXYLayoutEditPolicy;

/**
 * EditPart for the a ShapesDiagram instance.
 * <p>This edit part server as the main diagram container, the white area where
 * everything else is in. Also responsible for the container's layout (the
 * way the container rearanges is contents) and the container's capabilities
 * (edit policies).
 * </p>
 * <p>This edit part must implement the PropertyChangeListener interface, 
 * so it can be notified of property changes in the corresponding model element.
 * </p>
 * @author Xueyuan Xing
 * @author Tao Wu
 * @author Hansheng Zhang
 */
public class DiagramEditPart extends AbstractGraphicalEditPart 
implements PropertyChangeListener  {

	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
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
		// disallows the removal of this edit part from its parent
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
		// handles constraint changes (e.g. moving and/or resizing) of model elements
		// and creation of new model elements
		installEditPolicy(EditPolicy.LAYOUT_ROLE,  new ShapesXYLayoutEditPolicy());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		Figure f = new FreeformLayer();
		f.setBorder(new MarginBorder(150));
		f.setLayoutManager(new FreeformLayout());
		return f;
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

	public ShapesDiagram getCastedModel() {
		return (ShapesDiagram) getModel();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List<Shape> getModelChildren() {
		return getCastedModel().getChildren(); // return a list of shapes
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		// these properties are fired when Shapes are added into or removed from 
		// the ShapeDiagram instance and must cause a call of refreshChildren()
		// to update the diagram's contents.
		if (ShapesDiagram.CHILD_ADDED_PROP.equals(prop)
				|| ShapesDiagram.CHILD_REMOVED_PROP.equals(prop)) {
			refreshChildren();
		}
		else if (ShapesDiagram.ALLCHILDREN_RELOCATED_PROP.equals(prop)){
			Figure f = (Figure) getFigure();
			Rectangle rect = new Rectangle(f.getBounds());
			getCastedModel().relocatedAll(rect);
		}
		else if (ShapesDiagram.ID_ROUTER.equals(evt.getPropertyName()))
			refreshVisuals();
	}


	protected void refreshVisuals()
	{
		Animation.markBegin();
		ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
		if ((getViewer().getControl().getStyle() & SWT.MIRRORED ) == 0)
			cLayer.setAntialias(SWT.ON);
		if (getCastedModel().getConnectionRouter().equals(ShapesDiagram.ROUTER_MANUAL)) {
			AutomaticRouter router = new FanRouter();
			router.setNextRouter(new BendpointConnectionRouter());
			cLayer.setConnectionRouter(router);
		} else if (getCastedModel().getConnectionRouter().equals(ShapesDiagram.ROUTER_MANHATTAN))
			cLayer.setConnectionRouter(new ManhattanConnectionRouter());
		else
			cLayer.setConnectionRouter(new ShortestPathConnectionRouter(getFigure()));
		Animation.run(400);
	}

}