/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import cn.edu.pku.ogeditor.MultiPageEditor;
import cn.edu.pku.ogeditor.ShapesEditor;
/**
 * the outline view of the concepts
 * @author Tao Wu
 */
public class ConceptOutlineView extends ViewPart implements ISelectionListener {

	private TreeViewer tv;

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		tv = new TreeViewer(parent, SWT.BORDER);
		tv.setContentProvider(new ConceptOutlineContentProvider());
		tv.setLabelProvider(new ConceptOutlineLabelProvider());
		ShapesEditor editor = null;
		if ((editor = getShapesEditor()) != null) {
			tv.setInput(editor.getDiagram());
		}
		getSite().getPage().addSelectionListener(this);

	}

	private ShapesEditor getShapesEditor() {
		// TODO Auto-generated method stub
		IEditorPart curEditor = getSite().getPage().getActiveEditor();
		if (curEditor instanceof MultiPageEditor)
			return ((MultiPageEditor) curEditor).getShapesEditor();
		else
			return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		if (part instanceof IEditorPart) {
			ShapesEditor editor = null;
			if ((editor = getShapesEditor()) != null) {
				tv.setInput(editor.getDiagram());
			} else
				tv.setInput(null);
		}
	}
}
