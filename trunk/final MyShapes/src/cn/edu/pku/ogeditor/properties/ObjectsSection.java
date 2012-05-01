/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.properties;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import cn.edu.pku.ogeditor.dialogs.ObjectEditDialog;
import cn.edu.pku.ogeditor.model.ShapesDiagram;
import cn.edu.pku.ogeditor.parts.DiagramEditPart;
import cn.edu.pku.ogeditor.wizards.ObjectInfo;
import cn.edu.pku.ogeditor.wizards.ObjectsListModel;
import cn.edu.pku.ogeditor.wizards.TableContentProvider;
import cn.edu.pku.ogeditor.wizards.TableLabelProvider;
/**
 * description section in property sheet.
 * @author Xueyuan Xing
 */
public class ObjectsSection extends AbstractPropertySection {

	private static final int LABEL_LENGTH = 200;

	public ObjectsSection() {
		// TODO Auto-generated constructor stub
	}

	private DiagramEditPart dep;
//	private ObjectsListModel objects;
	private TableViewer viewer;

	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		if (!(selection instanceof IStructuredSelection)) {
			return;
		}
		Object input = ((IStructuredSelection) selection).getFirstElement();
		if (input instanceof DiagramEditPart) {
			dep = (DiagramEditPart) input;
		}
	}

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite container = getWidgetFactory()
				.createFlatFormComposite(parent);
		FormData data;
		// composite.setLayout(new FillLayout());

		CLabel listLabel = getWidgetFactory().createCLabel(container, "All detectable objects:"); //$NON-NLS-1$
		
		Table table = new Table(container, SWT.BORDER | SWT.MULTI
				| SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("URIs");
		column1.setWidth(400);
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setWidth(LABEL_LENGTH);
		column2.setText("RFID");
		TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setWidth(LABEL_LENGTH);
		column3.setText("TYPE");
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer = new TableViewer(table);
		viewer.setContentProvider(new TableContentProvider());
		viewer.setLabelProvider(new TableLabelProvider());

//		objects = new ObjectsListModel();
//		viewer.setInput(objects);
//
//		objects.add(new ObjectInfo("http://object1", "001", "light"));
//		objects.add(new ObjectInfo("http://object2", "002", "air-conditioning"));
//		objects.add(new ObjectInfo("http://object3", "003", "screen"));

		Button button = getWidgetFactory().createButton(container,
				"Edit", SWT.PUSH);
		
		data = new FormData();
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(0);
		listLabel.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(listLabel, 0, SWT.LEFT);
		data.right = new FormAttachment(90);
		data.top = new FormAttachment(listLabel, 0, SWT.BOTTOM);
		data.bottom = new FormAttachment(listLabel, 180, SWT.BOTTOM);

		table.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(table);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(table, 0, SWT.TOP);
		button.setLayoutData(data);
		button.addSelectionListener(new ButtonListener());
	}

	public void refresh() {
		ShapesDiagram diagram = (ShapesDiagram) dep.getModel();
		viewer.setInput(diagram.getObjects());
	}

	private class ButtonListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
//			if (isSep)
//				sep.getCastedModel().setDescription(descriptionText.getText());
//			else
//				cep.getCastedModel().setDescription(descriptionText.getText());
//			ShapesEditor.myselfShapesEditor.doSave(null);
//			ShapesEditor.myselfShapesEditor.setDirty(false);
			ObjectEditDialog dialog = new ObjectEditDialog(Display.getDefault().getActiveShell(), ((ShapesDiagram) dep.getModel()).getObjects(), viewer);
			dialog.open();
//			viewer.refresh();
//			if(dialog.open() != ObjectEditDialog.OK)
//				return;
		}	
	}
}
