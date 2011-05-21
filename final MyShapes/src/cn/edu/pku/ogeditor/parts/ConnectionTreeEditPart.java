package cn.edu.pku.ogeditor.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.ModelElement;

public class ConnectionTreeEditPart extends AbstractTreeEditPart implements
		PropertyChangeListener {

	ConnectionTreeEditPart(Connection model){
		super(model);
	}
	public void activate() {
		if (!isActive()) {
			super.activate();
			((ModelElement) getModel()).addPropertyChangeListener(this);
		}
	}
	protected void createEditPolicies() {
		super.createEditPolicies();
		// allow removal of the associated model element
		//installEditPolicy(EditPolicy.COMPONENT_ROLE, new ShapeComponentEditPolicy());
	}
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((ModelElement) getModel()).removePropertyChangeListener(this);
		}
	}
	private Connection getCastedModel() {
		return (Connection) getModel();
	}
	protected String getText() {
		return getCastedModel().getName();
	}
	//@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		refreshVisuals();
	}

}
