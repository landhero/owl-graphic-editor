package cn.edu.pku.ogeditor.properties;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import cn.edu.pku.ogeditor.parts.ConnectionEditPart;
import cn.edu.pku.ogeditor.parts.ShapeEditPart;

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
			if(isSep)
				sep.getCastedModel().setDescription(descriptionText.getText());
			else 
				cep.getCastedModel().setDescription(descriptionText.getText())	;
		}
	};

	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		if(!(selection instanceof IStructuredSelection))
		{
			return;
		}
		Object input = ((IStructuredSelection) selection).getFirstElement();
		if(input instanceof ShapeEditPart)
		{
			isSep = true;
			sep = (ShapeEditPart) input;
		}	
		else if(input instanceof ConnectionEditPart)
		{
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
		//composite.setLayout(new FillLayout());

		descriptionText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 100);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		descriptionText.setLayoutData(data);
		descriptionText.addModifyListener(listener);
		
		CLabel labelLabel = getWidgetFactory()
		.createCLabel(composite, "description:"); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(descriptionText,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(descriptionText, 0, SWT.CENTER);
		labelLabel.setLayoutData(data);

	}

	public void refresh() {
		descriptionText.removeModifyListener(listener);
		descriptionText.setText(isSep?
				sep.getCastedModel().getDescription() : cep.getCastedModel().getDescription());
		descriptionText.addModifyListener(listener);
	}
}
