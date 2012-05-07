/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import cn.edu.pku.ogeditor.model.ShapesDiagram;
/**
 * label provider of hierarchy view
 * @author Xueyuan Xing
 */
public class HierarchyLabelProvider extends LabelProvider {

	public Image getImage(Object element) {
		return null;
	}

	public String getText(Object element) {
		ShapesDiagram diagram = (ShapesDiagram) element;
		return diagram.getOWLName();
	}
}
