package cn.edu.pku.ogeditor.properties;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import cn.edu.pku.ogeditor.views.SWRLRule;

public class SWRLListModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ADD_ELEMENT = "addElement";

	public static final String REMOVE_ELEMENT = "removeElement";

	private PropertyChangeSupport delegate;

	private Vector<SWRLRule> rules;

	public SWRLListModel() {
		rules = new Vector<SWRLRule>();
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

	public void add(SWRLRule element) {
		if (rules.add(element))
			firePropertyChange(new PropertyChangeEvent(this, ADD_ELEMENT, null,
					element));
	}

	public void remove(SWRLRule element) {
		if (rules.remove(element))
			firePropertyChange(new PropertyChangeEvent(this, REMOVE_ELEMENT,
					null, element));
	}

	public Object[] elements() {
		return rules.toArray();
	}

	public void removeAll(ArrayList<SWRLRule> dels) {
		rules.removeAll(dels);	
	}
}
