package cn.edu.pku.ogeditor.wizards;

import iot.client.Value;
import iot.equipment.Equipment;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;


public class TableContentProvider implements IStructuredContentProvider,
		PropertyChangeListener {

	private TableViewer viewer;

	private ObjectsListModel model;

	public Object[] getElements(Object inputElement) {
		return model.elements();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TableViewer) viewer;

		if (oldInput instanceof ObjectsListModel)
			((ObjectsListModel) oldInput).removePropertyChangeListener(this);

		if (newInput instanceof ObjectsListModel) {
			this.model = (ObjectsListModel) newInput;
			((ObjectsListModel) newInput).addPropertyChangeListener(this);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (ObjectsListModel.ADD_ELEMENT.equals(evt.getPropertyName()))
			viewer.add(evt.getNewValue());
		if (ObjectsListModel.REMOVE_ELEMENT.equals(evt.getPropertyName()))
			viewer.remove(evt.getNewValue());
	}
}
