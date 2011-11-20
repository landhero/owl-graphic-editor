/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.properties;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.parts.ConnectionEditPart;
import cn.edu.pku.ogeditor.parts.ShapeEditPart;
/**
 * description section in property sheet.
 * @author Xueyuan Xing
 */
public class DescriptionSection extends AbstractPropertySection {

	public DescriptionSection() {
		// TODO Auto-generated constructor stub
	}

	private Text descriptionText;

	private ShapeEditPart sep;
	private ConnectionEditPart cep;
	private boolean isSep;
	private ModifyListener listener = new ModifyListener() {

		public void modifyText(ModifyEvent arg0) {
			if(!ShapesEditor.myselfShapesEditor.isDirty())
			{
				ShapesEditor.myselfShapesEditor.setDirty(true);
			}

		}
	};

	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		if (!(selection instanceof IStructuredSelection)) {
			return;
		}
		Object input = ((IStructuredSelection) selection).getFirstElement();
		if (input instanceof ShapeEditPart) {
			isSep = true;
			sep = (ShapeEditPart) input;
		} else if (input instanceof ConnectionEditPart) {
			isSep = false;
			cep = (ConnectionEditPart) input;
		}
	}

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);
		FormData data;
		// composite.setLayout(new FillLayout());

		descriptionText = getWidgetFactory()
				.createText(
						composite,
						"",
						SWT.WRAP | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL
								| SWT.V_SCROLL); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		data.height = 100;
		descriptionText.setLayoutData(data);
		descriptionText.addModifyListener(listener);
		//
		// CLabel labelLabel = getWidgetFactory()
		//		.createCLabel(composite, "description:"); //$NON-NLS-1$
		// data = new FormData();
		// data.left = new FormAttachment(0, 0);
		// data.right = new FormAttachment(descriptionText,
		// -ITabbedPropertyConstants.HSPACE);
		// data.top = new FormAttachment(descriptionText, 0, SWT.CENTER);
		// labelLabel.setLayoutData(data);
		Button button = getWidgetFactory().createButton(composite,
				IDialogConstants.OK_LABEL, SWT.PUSH);
		data = new FormData();
		// data.left = new FormAttachment(0,ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(descriptionText,
				ITabbedPropertyConstants.VSPACE);
		data.width = STANDARD_LABEL_WIDTH;
		button.setLayoutData(data);
		button.addSelectionListener(new ButtonListener());
	}

	public void refresh() {
		descriptionText.removeModifyListener(listener);
		descriptionText.setText(isSep ? sep.getCastedModel().getDescription()
				: cep.getCastedModel().getDescription());
		descriptionText.addModifyListener(listener);
	}

	private class ButtonListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (isSep)
				sep.getCastedModel().setDescription(descriptionText.getText());
			else
				cep.getCastedModel().setDescription(descriptionText.getText());
			ShapesEditor.myselfShapesEditor.doSave(null);
			ShapesEditor.myselfShapesEditor.setDirty(false);
		}
		
	}
}
