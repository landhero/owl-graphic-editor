package cn.edu.pku.ogeditor.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import cn.edu.pku.ogeditor.ShapesEditor;

/**
 * @author Œ‚Ë∫
 *
 */
public class ConceptOutlineView extends ViewPart {


	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		TreeViewer tv=new TreeViewer(parent,SWT.BORDER);
		tv.setContentProvider(new ConceptOutlineContentProvider());
		tv.setLabelProvider(new ConceptOutlineLabelProvider());
		tv.setInput(ShapesEditor.myselfShapesEditor.getDiagram());
		
	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
