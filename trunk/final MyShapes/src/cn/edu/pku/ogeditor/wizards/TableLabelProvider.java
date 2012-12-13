package cn.edu.pku.ogeditor.wizards;

import iot.client.Value;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;


public class TableLabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof Value) {
			Value v = (Value) element;
			switch (columnIndex) {
			case 0:
				return v.getEquipment();
			case 1:
				return v.getProperty();
			case 2:
				return v.getValue();
			case 3:
				return v.getType();
			case 4:
				return v.getEType();
			}
		}
		return null;
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void removeListener(ILabelProviderListener listener) {
	}
}
