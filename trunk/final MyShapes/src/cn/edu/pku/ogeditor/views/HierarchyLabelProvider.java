package cn.edu.pku.ogeditor.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import cn.edu.pku.ogeditor.model.ShapesDiagram;

public class HierarchyLabelProvider extends LabelProvider {

	public Image getImage(Object element) {
		return null;
	}

	public String getText(Object element) {
		ShapesDiagram diagram = (ShapesDiagram) element;
		return diagram.getName();
	}
}
