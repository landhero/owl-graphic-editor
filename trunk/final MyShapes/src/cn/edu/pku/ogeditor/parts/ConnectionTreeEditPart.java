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

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.ModelElement;
/**
 * @author Xueyuan Xing
 * @author Tao Wu
 */
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
