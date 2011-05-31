package cn.edu.pku.ogeditor.properties;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import cn.edu.pku.ogeditor.parts.ShapeEditPart;

public class ShapeBasicSection extends AbstractPropertySection {

    private Text nameText;

    private ShapeEditPart sep;
    public ShapeBasicSection() {
		// TODO Auto-generated constructor stub
	}
    private ModifyListener listener = new ModifyListener() {

        public void modifyText(ModifyEvent arg0) {
        	sep.getCastedModel().setName(nameText.getText());
        }
    };

    public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        System.out.println(selection);
        if(!(selection instanceof IStructuredSelection))
        {
        	return;
        }
        Object input = ((IStructuredSelection) selection).getFirstElement();
        if(input instanceof ShapeEditPart)
        	sep = (ShapeEditPart) input;
    }

    public void createControls(Composite parent,
            TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = getWidgetFactory()
            .createFlatFormComposite(parent);
        FormData data;

        nameText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        nameText.setLayoutData(data);
        nameText.addModifyListener(listener);

        CLabel labelLabel = getWidgetFactory()
            .createCLabel(composite, "name:"); //$NON-NLS-1$
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(nameText,
            -ITabbedPropertyConstants.HSPACE);
        data.top = new FormAttachment(nameText, 0, SWT.CENTER);
        labelLabel.setLayoutData(data);
    }

    public void refresh() {
        nameText.removeModifyListener(listener);
        nameText.setText(sep.getCastedModel().getName());
        nameText.addModifyListener(listener);
    }

}
