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
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import cn.edu.pku.ogeditor.parts.DiagramEditPart;
import cn.edu.pku.ogeditor.views.SWRLRule;
/**
 * description section in property sheet.
 * @author Xueyuan Xing
 */
public class SWRLSection extends AbstractPropertySection {

	public SWRLSection() {
		// TODO Auto-generated constructor stub
	}

	private DiagramEditPart dep;

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
		CLabel addL = getWidgetFactory().createCLabel(container, "Add a SWRL Rule"); //$NON-NLS-1$
//		addL.setText("Add a SWRL Rule");
		Text ruleText = new Text(container, SWT.BORDER);
		Button addButton = new Button(container, SWT.NONE);
		addButton.setText("add");
		
		final Label listL = new Label(container, SWT.NONE);
		listL.setText("List of  SWRL Rules");
		Button delButton = new Button(container, SWT.NONE);
		delButton.setText("delete");
		
		ListViewer viewer = new ListViewer(container, SWT.BORDER);

		viewer.setContentProvider(new ListContentProvider());
		viewer.setLabelProvider(new ListLabelProvider());
		
		SWRLListModel input = new SWRLListModel();
		viewer.setInput(input);
		
		input.add(new SWRLRule("Room(?r) … isOccupied(?r, true) … Room_Temperature(?r, ?t) … swrlb:greaterThan(?t, 30.0) … Air_Condition(?x) …  isIn(?x, ?r) ★  isOn(?x, true)"));
		input.add(new SWRLRule("Room(?r) … Room_PersonNum(?r, ?n) … swrlb:lessThan(?n, 4) … Air_Condition(?x) … isIn(?x, ?r) … isOn(?x, true) ★  Air_Condition_Temperature(?x, 27)"));
		input.add(new SWRLRule("Room(?r) … Room_PersonNum(?r, ?n) … swrlb:greaterThan(?n, 3) … Air_Condition(?x) … isIn(?x, ?r) … isOn(?x, true) ★  Air_Condition_Temperature(?x, 26)"));
		
		FormData addLData = new FormData();
		addLData.left = new FormAttachment(0);
		addLData.right = new FormAttachment(100);
		addLData.top = new FormAttachment(0);
		addL.setLayoutData(addLData);
		
		FormData ruleTData = new FormData();
		ruleTData.left = new FormAttachment(addL, 0, SWT.LEFT);
		ruleTData.right = new FormAttachment(90);
		ruleTData.top = new FormAttachment(addL, 0, SWT.BOTTOM);
		ruleText.setLayoutData(ruleTData);
		
		FormData addButtonData = new FormData();
		addButtonData.left = new FormAttachment(ruleText, 0, SWT.RIGHT);
		addButtonData.right = new FormAttachment(addL, 0, SWT.RIGHT);
		addButtonData.top = new FormAttachment(addL, 0, SWT.BOTTOM);
		addButton.setLayoutData(addButtonData);

		FormData listLData = new FormData();
		listLData.left = new FormAttachment(addL, 0, SWT.LEFT);
		listLData.right = new FormAttachment(addL, 0, SWT.RIGHT);
		listLData.top = new FormAttachment(ruleText, 30, SWT.BOTTOM);
		listL.setLayoutData(listLData);
		
		FormData listData = new FormData();
		listData.left = new FormAttachment(addL, 0, SWT.LEFT);
		listData.right = new FormAttachment(ruleText, 0, SWT.RIGHT);
		listData.top = new FormAttachment(listL, 0, SWT.BOTTOM);
		listData.bottom = new FormAttachment(listL, 100, SWT.BOTTOM);
		viewer.getList().setLayoutData(listData);
		
		FormData delButtonData = new FormData();
		delButtonData.left = new FormAttachment(addButton, 0, SWT.LEFT);
		delButtonData.right = new FormAttachment(addButton, 0, SWT.RIGHT);
		delButtonData.top = new FormAttachment(listL, 0, SWT.BOTTOM);
		delButton.setLayoutData(delButtonData);

	}

	public void refresh() {
//		descriptionText.removeModifyListener(listener);
//		descriptionText.setText(isSep ? sep.getCastedModel().getDescription()
//				: cep.getCastedModel().getDescription());
//		descriptionText.addModifyListener(listener);
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
		}
		
	}
}
