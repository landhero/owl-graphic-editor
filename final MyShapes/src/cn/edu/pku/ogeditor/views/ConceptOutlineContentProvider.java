package cn.edu.pku.ogeditor.views;

//author ���
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;

public class ConceptOutlineContentProvider implements ITreeContentProvider {

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		ShapesDiagram shapesDiagram=(ShapesDiagram)inputElement;
		return shapesDiagram.getChildren().toArray();
	}

	public Object[] getChildren(Object parentElement) {
		// TODO Auto-generated method stub
		Shape shape=(Shape)parentElement;
		List<Shape> list=shape.getChildren();
		if(list==null)return new Object[0];
		return list.toArray();
	}

	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		Shape shape=(Shape)element;
		List<?> list=shape.getChildren();
		return !(list==null||list.isEmpty());
	}

}