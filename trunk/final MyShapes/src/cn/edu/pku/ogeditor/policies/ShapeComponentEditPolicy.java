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
package cn.edu.pku.ogeditor.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cn.edu.pku.ogeditor.commands.ShapeDeleteCommand;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;
import cn.edu.pku.ogeditor.parts.ShapeEditPart;
import cn.edu.pku.ogeditor.parts.ShapeTreeEditPart;

/**
 * This edit policy enables the removal of a Shapes instance from its container.
 * 
 * @see ShapeEditPart#createEditPolicies()
 * @see ShapeTreeEditPart#createEditPolicies()
 * @author Elias Volanakis
 */
public class ShapeComponentEditPolicy extends ComponentEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(
	 * org.eclipse.gef.requests.GroupRequest)
	 */
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		((ShapeEditPart) getHost()).setTemporarily(false);
		Object parent = getHost().getParent().getModel();
		Object child = getHost().getModel();
		if (parent instanceof ShapesDiagram && child instanceof Shape) {
			return childrenDeleteCommand((ShapesDiagram) parent, (Shape) child);
		}
		return super.createDeleteCommand(deleteRequest);
	}

	private Command childrenDeleteCommand(ShapesDiagram parentShapesDiagram,Shape parentShape) {
		Command returnedCommand=new CompoundCommand();
		if (parentShape.getChildren().size() > 0) {
			ArrayList<Shape> children = parentShape.getChildren();
			List<ShapesDiagram> childShapesDiagramList = parentShapesDiagram.getLowerLevelDiagrams();
			
			/*
			for (int j = 0; j < childShapesDiagramList.size(); j++) {
				if (childShapesDiagramList.get(j).getChildren().indexOf(children.get(0)) != -1) {
					returnedCommand = childrenDeleteCommand(childShapesDiagramList.get(j), children.get(0));
					break;
				}
			}
			*/
			for (int i = 0; i < children.size(); i++) {
				for (int j = 0; j < childShapesDiagramList.size(); j++) {
					if (childShapesDiagramList.get(j).getChildren().indexOf(children.get(i)) != -1) {
						returnedCommand = returnedCommand.chain(childrenDeleteCommand(childShapesDiagramList.get(j),children.get(i)));
						break;
					}
				}
			}
			returnedCommand = returnedCommand.chain(new ShapeDeleteCommand(
					parentShapesDiagram, parentShape));
		} else {
			returnedCommand = new ShapeDeleteCommand(parentShapesDiagram, parentShape);
		}
		return returnedCommand;
	}
}