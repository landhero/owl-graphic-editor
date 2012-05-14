/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.properties;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import cn.edu.pku.ogeditor.model.ShapesDiagram;
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
	private ListViewer viewer;
	private Text ruleText;
	private SWRLListModel rulesModel;

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
		Display display = Display.getDefault();

		Font titleFont = new Font(display, "Arial", 12, SWT.BOLD);
		Font textFont = new Font(display, "Cambria", 12, SWT.NORMAL);
		
		CLabel addL = getWidgetFactory().createCLabel(container, "Add a SWRL Rule"); //$NON-NLS-1$
		addL.setFont(titleFont);
		ruleText = new Text(container, SWT.BORDER);
		ruleText.setFont(textFont);
		Button addButton = new Button(container, SWT.NONE);
		addButton.setText("Add");
		addButton.setFont(titleFont);
		addButton.addSelectionListener(new AddRuleListener());
		
		CLabel listL = getWidgetFactory().createCLabel(container, "List of  SWRL Rules"); //$NON-NLS-1$
		listL.setFont(titleFont);

		Button delButton = new Button(container, SWT.NONE);
		delButton.setText("Delete");
		delButton.setFont(titleFont);
		delButton.addSelectionListener(new DelRuleListener());
		
		viewer = new ListViewer(container, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getList().setFont(textFont);
		viewer.setContentProvider(new ListContentProvider());
		viewer.setLabelProvider(new ListLabelProvider());
		
//		SWRLListModel input = new SWRLListModel();
//		viewer.setInput(input);
//		
//		input.add(new SWRLRule("Room(?r) … isOccupied(?r, true) … Room_Temperature(?r, ?t) … swrlb:greaterThan(?t, 30.0) … Air_Condition(?x) …  isIn(?x, ?r) ★  isOn(?x, true)"));
//		input.add(new SWRLRule("Room(?r) … Room_PersonNum(?r, ?n) … swrlb:lessThan(?n, 4) … Air_Condition(?x) … isIn(?x, ?r) … isOn(?x, true) ★  Air_Condition_Temperature(?x, 27)"));
//		input.add(new SWRLRule("Room(?r) … Room_PersonNum(?r, ?n) … swrlb:greaterThan(?n, 3) … Air_Condition(?x) … isIn(?x, ?r) … isOn(?x, true) ★  Air_Condition_Temperature(?x, 26)"));
		
		data = new FormData();
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(0);
		addL.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(addL, 0, SWT.LEFT);
		data.right = new FormAttachment(90);
		data.top = new FormAttachment(addL, 0, SWT.BOTTOM);
		ruleText.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(ruleText, 0, SWT.RIGHT);
		data.right = new FormAttachment(addL, 0, SWT.RIGHT);
		data.top = new FormAttachment(addL, 0, SWT.BOTTOM);
		addButton.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(addL, 0, SWT.LEFT);
		data.right = new FormAttachment(addL, 0, SWT.RIGHT);
		data.top = new FormAttachment(ruleText, 5, SWT.BOTTOM);
		listL.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(addL, 0, SWT.LEFT);
		data.right = new FormAttachment(ruleText, 0, SWT.RIGHT);
//		data.right = new FormAttachment(addL, 940, SWT.LEFT);
		data.top = new FormAttachment(listL, 0, SWT.BOTTOM);
		data.bottom = new FormAttachment(listL, 200, SWT.BOTTOM);
		viewer.getList().setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(addButton, 0, SWT.LEFT);
		data.right = new FormAttachment(addButton, 0, SWT.RIGHT);
		data.top = new FormAttachment(listL, 0, SWT.BOTTOM);
		delButton.setLayoutData(data);

	}

	public void refresh() {
		ShapesDiagram diagram = (ShapesDiagram) dep.getModel();
		rulesModel = diagram.getRootDiagram().getRules();
		viewer.setInput(rulesModel);
	}

	private class AddRuleListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			SWRLRule rule = new SWRLRule(ruleText.getText());
			rulesModel.add(rule);
			refreshTexts();
		}

	}
	
	private class DelRuleListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			int[] indices = viewer.getList().getSelectionIndices();
			ArrayList<SWRLRule> dels = new ArrayList<SWRLRule>();
			for(int index : indices)
			{
				SWRLRule rule = (SWRLRule) viewer.getElementAt(index);
				dels.add(rule);
//				objects.remove(object);
			}
			rulesModel.removeAll(dels);
			viewer.refresh();
		}
	}

	private void refreshTexts() {
		ruleText.setText("");
	}
}
