/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;

/**
 * Factory that maps model elements to TreeEditParts. TreeEditParts are used in
 * the outline view of the ShapesEditor.
 * 
 * @author Xueyuan Xing
 * @author Tao Wu
 */
public class ShapesTreeEditPartFactory implements EditPartFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see 
	 * org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 * java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof Shape) {
			return new ShapeTreeEditPart((Shape) model);
		}
		if (model instanceof ShapesDiagram) {
			return new DiagramTreeEditPart((ShapesDiagram) model);
		}
		if (model instanceof Connection) {
			return new ConnectionTreeEditPart((Connection) model);
		}
		return null; // will not show an entry for the corresponding model
						// instance
	}

}
