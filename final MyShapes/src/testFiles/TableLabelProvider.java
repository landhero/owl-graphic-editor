package testFiles;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;


public class TableLabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
//		if (element instanceof ObjectInfo) {
//			ObjectInfo object = (ObjectInfo) element;
//			switch (columnIndex) {
//			case 0:
//				return object.getRfid();
//			case 1:
//				return object.getType();
//			case 2:
//				if(object.isOn())
//					return "On";
//				else
//					return "Off";
//			}
//		}
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
