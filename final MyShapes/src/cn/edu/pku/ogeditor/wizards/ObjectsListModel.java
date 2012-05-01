package cn.edu.pku.ogeditor.wizards;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Vector;

public class ObjectsListModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ADD_ELEMENT = "addElement";

	public static final String REMOVE_ELEMENT = "removeElement";

	private PropertyChangeSupport delegate;

	private Vector<ObjectInfo> objects;

	public ObjectsListModel() {
		objects = new Vector<ObjectInfo>();
		delegate = new PropertyChangeSupport(this);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		delegate.addPropertyChangeListener(listener);
	}

	public void firePropertyChange(PropertyChangeEvent evt) {
		delegate.firePropertyChange(evt);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		delegate.removePropertyChangeListener(listener);
	}

	public void add(ObjectInfo element) {
		if (objects.add(element))
			firePropertyChange(new PropertyChangeEvent(this, ADD_ELEMENT, null,
					element));
	}

	public void remove(ObjectInfo element) {
		if (objects.remove(element))
			firePropertyChange(new PropertyChangeEvent(this, REMOVE_ELEMENT,
					null, element));
	}

	public Object[] elements() {
		return objects.toArray();
	}
}
