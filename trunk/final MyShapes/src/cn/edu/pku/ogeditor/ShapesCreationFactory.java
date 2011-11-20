/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor;

import org.eclipse.gef.requests.CreationFactory;

import cn.edu.pku.ogeditor.model.Shape;

/**
 * Implement a CreationFactory
 * 
 * @author Xueyuan Xing
 */
public class ShapesCreationFactory implements CreationFactory {

	private String shapeName;

	public ShapesCreationFactory(String shapeName) {
		this.shapeName = shapeName;
	}

	public Object getNewObject() {
		try {
			Shape newShape = Shape.class.newInstance();
			if (newShape.setName(shapeName + newShape.hashCode()))
				return newShape;
			else
				return null;
		} catch (Exception exc) {
			return null;
		}
	}

	public Object getObjectType() {
		return Shape.class;
	}

}
