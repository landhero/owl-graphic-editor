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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;


import cn.edu.pku.ogeditor.commands.ShapeDeleteCommand;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;
import cn.edu.pku.ogeditor.parts.ShapeEditPart;
import cn.edu.pku.ogeditor.parts.ShapeTreeEditPart;

/**
 * This edit policy enables the removal of a Shapes instance from its container. 
 * @see ShapeEditPart#createEditPolicies()
 * @see ShapeTreeEditPart#createEditPolicies()
 * @author Elias Volanakis
 */
public class ShapeComponentEditPolicy extends ComponentEditPolicy {

/* (non-Javadoc)
 * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#createDeleteCommand(org.eclipse.gef.requests.GroupRequest)
 */
protected Command createDeleteCommand(GroupRequest deleteRequest) {
	((ShapeEditPart)getHost()).setTemporarily(false);
	Object parent = getHost().getParent().getModel();
	Object child = getHost().getModel();
	if (parent instanceof ShapesDiagram && child instanceof Shape) {
		return new ShapeDeleteCommand((ShapesDiagram) parent, (Shape) child);
	}
	return super.createDeleteCommand(deleteRequest);
}
}