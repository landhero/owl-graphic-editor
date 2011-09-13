package cn.edu.pku.ogeditor.views;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.model.ShapesDiagram;

public class HierarchyContentProvider implements ITreeContentProvider {
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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		if(inputElement instanceof ShapesEditor)
			return new Object[]{
				((ShapesEditor)inputElement).getDiagram().getRootDiagram()
		};
		else 
			return new Object[0];
	}
}
