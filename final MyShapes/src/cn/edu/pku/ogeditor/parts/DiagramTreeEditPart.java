/*******************************************************************************
 * Copyright (c) 2004, 2005 Elias Volanakis and others.
?* All rights reserved. This program and the accompanying materials
?* are made available under the terms of the Eclipse Public License v1.0
?* which accompanies this distribution, and is available at
?* http://www.eclipse.org/legal/epl-v10.html
?*
?* Contributors:
?*????Elias Volanakis - initial API and implementation
?*******************************************************************************/
package cn.edu.pku.ogeditor.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.pku.ogeditor.model.Shape;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import cn.edu.pku.ogeditor.model.ModelElement;
import cn.edu.pku.ogeditor.model.ShapesDiagram;


/**
 * TreeEditPart for a ShapesDiagram instance.
 * This is used in the Outline View of the ShapesEditor.
 * <p>This edit part must implement the PropertyChangeListener interface, 
 * so it can be notified of property changes in the corresponding model element.
 * </p>
 * 
 */
class DiagramTreeEditPart extends AbstractTreeEditPart
implements PropertyChangeListener {

	/**
	 * Create a new instance of this edit part using the given model element.
	 * 
	 * @param model
	 *            a non-null ShapesDiagram instance
	 */
	DiagramTreeEditPart(ShapesDiagram model) {
		super(model);
	}

	/**
	 * Upon activation, attach to the model element as a property change
	 * listener.
	 */
	public void activate() {
		if (!isActive()) {
			super.activate();
			((ModelElement) getModel()).addPropertyChangeListener(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * parts.ShapeTreeEditPart#createEditPolicies
	 * ()
	 */
	protected void createEditPolicies() {
		// If this editpart is the root content of the viewer, then disallow
		// removal
		if (getParent() instanceof RootEditPart) {
			installEditPolicy(EditPolicy.COMPONENT_ROLE,new RootComponentEditPolicy());
		}
	}

	/**
	 * Upon deactivation, detach from the model element as a property change
	 * listener.
	 */
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((ModelElement) getModel()).removePropertyChangeListener(this);
		}
	}

	private ShapesDiagram getCastedModel() {
		return (ShapesDiagram) getModel();
	}

	/**
	 * Convenience method that returns the EditPart corresponding to a given
	 * child.
	 * 
	 * @param child
	 *            a model element instance
	 * @return the corresponding EditPart or null
	 */
	private EditPart getEditPartForChild(Object child) {
		return (EditPart) getViewer().getEditPartRegistry().get(child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List<?> getModelChildren() {
		return getCastedModel().getChildren(); // a list of shapes
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (ShapesDiagram.CHILD_ADDED_PROP.equals(prop)) {
			// add a child to this edit part
			// causes an additional entry to appear in the tree of the outline
			// view
			addChild(createChild(evt.getNewValue()), -1);
		} else if (ShapesDiagram.CHILD_REMOVED_PROP.equals(prop)) {
			// remove a child from this edit part
			// causes the corresponding edit part to disappear from the tree in
			// the outline view
			removeChild(getEditPartForChild(evt.getNewValue()));
		} else {
			refreshVisuals();
		}
	}

	protected void refreshChildren() {

		ShapesDiagram shapesDiagram = getCastedModel();

		int currentCengciIndex;
		List<ArrayList<Shape>> ShapesList = shapesDiagram.getShapesList();
		currentCengciIndex = ShapesList.indexOf(shapesDiagram.getChildren());
		int size;
		size = ShapesList.size();
		for (int i = size - 1; i >= 0; i--) {// 如果i从0往上增加原则上也没有问题，只不过增加的顺序在outline里面显示出来就是倒过来的
			shapesDiagram.changeShapes(i);
			_refreshChildren();
		}
		for (int i = 0; i < getChildren().size(); i++) {
			EditPart editPart = (EditPart) getChildren().get(i);
			addChildVisual(editPart, i);
			editPart.refresh();
		}
		shapesDiagram.changeShapes(currentCengciIndex);

	}

	private void _refreshChildren() {
		int i;
		EditPart editPart;
		Object model;

		Map<Object, EditPart> modelToEditPart = new HashMap<Object, EditPart>();
		List<?> children = getChildren();

		for (i = 0; i < children.size(); i++) {
			editPart = (EditPart) children.get(i);
			modelToEditPart.put(editPart.getModel(), editPart);
		}

		List<?> modelObjects = getModelChildren();

		for (i = 0; i < modelObjects.size(); i++) {
			model = modelObjects.get(i);

			// Do a quick check to see if editPart[i] == model[i]
			if (i < children.size()
					&& ((EditPart) children.get(i)).getModel() == model)
				continue;

			// Look to see if the EditPart is already around but in the wrong
			// location
			editPart = (EditPart) modelToEditPart.get(model);

			if (editPart != null)
				reorderChild(editPart, i);
			else {
				// An editpart for this model doesn't exist yet. Create and
				// insert one.
				editPart = createChild(model);
				_addChild(editPart, i);
			}
		}
		
		// 调用每一个EditPart的setParent()这样就把它们连成了一棵单向的树

	}
	protected void _addChild(EditPart child, int index) {
		Assert.isNotNull(child);
		if (index == -1)
			index = getChildren().size();
		if (children == null)
			children = new ArrayList<EditPart>(2);

		children.add(index, child);
		child.setParent(this);
		child.addNotify();

		if (isActive())
			child.activate();
		fireChildAdded(child, index);
	}

	protected void addChildVisual(EditPart childEditPart, int index) {
		int i;
		EditPart editPart;
		Map<Shape, EditPart> modelToEditPart = new HashMap<Shape, EditPart>();
		List<?> children = getChildren();
		for (i = 0; i < children.size(); i++) {
			editPart = (EditPart) children.get(i);
			modelToEditPart.put((Shape) editPart.getModel(), editPart);
		}
		TreeItem item;
		Widget widget;
		Shape parentShape = ((Shape) childEditPart.getModel()).getParent();
		if (parentShape.getParent() == null) {
			widget = getWidget();
			item = new TreeItem((Tree) widget, 0);

		} else {
			widget = ((TreeEditPart) modelToEditPart.get(parentShape)).getWidget();
			item = new TreeItem((TreeItem) widget, 0);
		}
		((TreeEditPart) childEditPart).setWidget(item);
	}
}
