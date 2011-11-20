/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.views;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.model.ShapesDiagram;
/**
 * hierachy view of ontology
 * @author Xueyuan Xing
 */
public class HierarchyView extends ViewPart implements ISelectionListener {

	private TreeViewer viewer;
	private TreeEditor ce;

	public HierarchyView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub

		viewer = new TreeViewer(parent, SWT.SINGLE);
		final Tree tree = viewer.getTree();
		ce = new TreeEditor(tree);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		viewer.setContentProvider(new HierarchyContentProvider());
		viewer.setLabelProvider(new HierarchyLabelProvider());
		ShapesEditor editor = null;
		if ((editor = getShapesEditor()) != null) {
			// List<ShapesDiagram> diagrams = new ArrayList<ShapesDiagram>();
			// diagrams.add(editor.getDiagram().getRootDiagram());
			viewer.setInput(editor);
		}
		viewer.addSelectionChangedListener(new LevelChangeListener());
		viewer.addDoubleClickListener(new RenameListener());
		// getSite().setSelectionProvider(viewer);
		getSite().getPage().addSelectionListener(this);
		Menu menu = new Menu(parent);
		tree.setMenu(menu);
		MenuItem menuItem1 = new MenuItem(menu, SWT.NONE);
		menuItem1.setText("Add Lower Level Ontology");
		menuItem1.addSelectionListener(new AddChildListener());
		MenuItem menuItem2 = new MenuItem(menu, SWT.NONE);
		menuItem2.setText("Remove Ontology");
		menuItem2.addSelectionListener(new RemoveOntologyListener());
		MenuItem menuItem3 = new MenuItem(menu, SWT.NONE);
		menuItem3.setText("Rename");
		menuItem3.addSelectionListener(new RenameListener());
	}

	private ShapesEditor getShapesEditor() {
		// TODO Auto-generated method stub
		IEditorPart curEditor = getSite().getPage().getActiveEditor();
		if (curEditor instanceof ShapesEditor)
			return (ShapesEditor) curEditor;
		else
			return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private class LevelChangeListener implements ISelectionChangedListener {
		public void selectionChanged(SelectionChangedEvent event) {
			// TODO Auto-generated method stub
			TreeSelection curSelection = (TreeSelection) viewer.getSelection();
			final ShapesDiagram curDiagram = (ShapesDiagram) curSelection
					.getFirstElement();
			if (curDiagram == null)
				return;
			ShapesEditor.myselfShapesEditor.refreshModel(curDiagram);
		}
	}

	private class AddChildListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent event) {

			TreeSelection curSelection = (TreeSelection) viewer.getSelection();
			final ShapesDiagram curDiagram = (ShapesDiagram) curSelection
					.getFirstElement();
			if (curDiagram == null)
				return;
			Shell shell = viewer.getControl().getShell();
			InputDialog dialog = new InputDialog(shell,
					"Create a Lower Level Ontology",
					"Enter a name for the new Ontology:", null, null);
			if (dialog.open() == InputDialog.OK) {
				String childName = dialog.getValue().trim();
				ShapesDiagram childDiagram = new ShapesDiagram();
				childDiagram.setName(childName);
				childDiagram.setFather(curDiagram);
				curDiagram.addLowerLevelDiagram(childDiagram);
				viewer.refresh(curDiagram);
				ShapesEditor.myselfShapesEditor.setDirty(true);
			}
		}
	}

	private class RemoveOntologyListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent event) {
			TreeSelection curSelection = (TreeSelection) viewer.getSelection();
			final ShapesDiagram curDiagram = (ShapesDiagram) curSelection
					.getFirstElement();
			if (curDiagram == null)
				return;
			ShapesDiagram curDiagramFather = curDiagram.getFather();
			if (curDiagramFather == null)
				return;
			curDiagramFather.removeLowerLevelDiagram(curDiagram);
			viewer.refresh(curDiagramFather);
			ShapesEditor.myselfShapesEditor.refreshModel(curDiagramFather);
			ShapesEditor.myselfShapesEditor.setDirty(true);
		}
	}

	private class RenameListener extends SelectionAdapter implements
			IDoubleClickListener {
		public void widgetSelected(SelectionEvent event) {
			TreeSelection curSelection = (TreeSelection) viewer.getSelection();
			final TreeItem item = viewer.getTree().getSelection()[0];
			final ShapesDiagram curDiagram = (ShapesDiagram) curSelection
					.getFirstElement();
			if (curSelection != null) {

				boolean showBorder = true;
				final Composite composite = new Composite(viewer.getTree(),
						SWT.NONE);
				if (showBorder)
					composite.setBackground(ColorConstants.black);
				final Text text = new Text(composite, SWT.NONE);
				final int inset = showBorder ? 1 : 0;
				composite.addListener(SWT.Resize, new Listener() {
					public void handleEvent(Event e) {
						Rectangle rect = composite.getClientArea();
						text.setBounds(rect.x + inset, rect.y + inset,
								rect.width - inset * 2, rect.height - inset * 2);
					}
				});
				Listener textListener = new Listener() {
					public void handleEvent(final Event e) {
						switch (e.type) {
						case SWT.FocusOut:
							item.setText(text.getText());
							composite.dispose();
							break;
						case SWT.Verify:
							String newText = text.getText();
							String leftText = newText.substring(0, e.start);
							String rightText = newText.substring(e.end,
									newText.length());
							GC gc = new GC(text);
							Point size = gc.textExtent(leftText + e.text
									+ rightText);
							gc.dispose();
							size = text.computeSize(size.x, SWT.DEFAULT);
							ce.horizontalAlignment = SWT.LEFT;
							Rectangle itemRect = item.getBounds(),
							rect = viewer.getTree().getClientArea();
							ce.minimumWidth = Math.max(size.x, itemRect.width)
									+ inset * 2;
							int left = itemRect.x,
							right = rect.x + rect.width;
							ce.minimumWidth = Math.min(ce.minimumWidth, right
									- left);
							ce.minimumHeight = size.y + inset * 2;
							ce.layout();
							ShapesEditor.myselfShapesEditor.setDirty(true);
							break;
						case SWT.Traverse:
							switch (e.detail) {
							case SWT.TRAVERSE_RETURN:
								item.setText(text.getText());
								curDiagram.setName(text.getText());
								// FALL THROUGH
							case SWT.TRAVERSE_ESCAPE:
								composite.dispose();
								e.doit = false;
							}
							break;
						}
					}
				};
				text.addListener(SWT.FocusOut, textListener);
				text.addListener(SWT.Traverse, textListener);
				text.addListener(SWT.Verify, textListener);
				ce.setEditor(composite, item);
				text.setText(item.getText());
				text.selectAll();
				text.setFocus();
			}
		}

		public void doubleClick(DoubleClickEvent event) {
			// TODO Auto-generated method stub
			widgetSelected(null);
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		if (part instanceof IEditorPart) {
			ShapesEditor editor = null;
			if ((editor = getShapesEditor()) != null) {
				viewer.setInput(editor);
			} else
				viewer.setInput(null);
		}
	}
}
