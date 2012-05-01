package cn.edu.pku.ogeditor.properties;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import cn.edu.pku.ogeditor.views.SWRLRule;

public class ListLabelProvider implements ILabelProvider {

	public Image getImage(Object element) {
		return null;
	}

	public String getText(Object element) {
		if(element instanceof SWRLRule)
			return ((SWRLRule)element).getExpression();
		return element.toString();
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}
	
	public void dispose() {}
	
	public void addListener(ILabelProviderListener listener) {}
	
	public void removeListener(ILabelProviderListener listener) {}
}
