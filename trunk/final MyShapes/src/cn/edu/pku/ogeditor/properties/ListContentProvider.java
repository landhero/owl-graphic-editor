package cn.edu.pku.ogeditor.properties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;

import cn.edu.pku.ogeditor.wizards.ObjectsListModel;

public class ListContentProvider implements IStructuredContentProvider,
		PropertyChangeListener {

	private ListViewer viewer;

	private SWRLListModel model;

	public Object[] getElements(Object inputElement) {
		return model.elements();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (ListViewer) viewer;

		if (oldInput instanceof SWRLListModel)
			((SWRLListModel) oldInput).removePropertyChangeListener(this);

		if (newInput instanceof SWRLListModel) {
			this.model = (SWRLListModel) newInput;
			((SWRLListModel) newInput).addPropertyChangeListener(this);
		}

	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (ObjectsListModel.ADD_ELEMENT.equals(evt.getPropertyName()))
			viewer.add(evt.getNewValue());
		if (ObjectsListModel.REMOVE_ELEMENT.equals(evt.getPropertyName()))
			viewer.remove(evt.getNewValue());
	}
}
