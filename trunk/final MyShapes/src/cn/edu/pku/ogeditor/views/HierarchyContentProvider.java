package cn.edu.pku.ogeditor.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

import cn.edu.pku.ogeditor.model.ShapesDiagram;

public class HierarchyContentProvider extends ArrayContentProvider implements
ITreeContentProvider {
	public Object[] getChildren(Object parentElement) {
		ShapesDiagram diagram = (ShapesDiagram) parentElement;
		return diagram.getLowerLevelDiagrams().toArray();
	}

	public Object getParent(Object element) {
		ShapesDiagram diagram = (ShapesDiagram) element;
		return diagram.getFather();
	}

	public boolean hasChildren(Object element) {
		ShapesDiagram diagram = (ShapesDiagram) element;
		return diagram.getLowerLevelDiagrams()!= null && diagram.getLowerLevelDiagrams().size() > 0;
	}
}
