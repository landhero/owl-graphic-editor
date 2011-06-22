package cn.edu.pku.ogeditor.properties;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class ConnectionBasicSection extends AbstractPropertySection {
//
//    private Text nameText;
//
//    private ConnectionEditPart cep;
//    public ConnectionBasicSection() {
//		// TODO Auto-generated constructor stub
//	}
//    private ModifyListener listener = new ModifyListener() {
//
//        public void modifyText(ModifyEvent arg0) {
//        	cep.getCastedModel().setName(nameText.getText());
//        }
//    };
//
//    public void setInput(IWorkbenchPart part, ISelection selection) {
//        super.setInput(part, selection);
//        if(!(selection instanceof IStructuredSelection))
//        {
//        	return;
//        }
//        Object input = ((IStructuredSelection) selection).getFirstElement();
//        if(input instanceof ConnectionEditPart)
//        	cep = (ConnectionEditPart) input;
//    }
//
//    public void createControls(Composite parent,
//            TabbedPropertySheetPage aTabbedPropertySheetPage) {
//        super.createControls(parent, aTabbedPropertySheetPage);
//        Composite composite = getWidgetFactory()
//            .createFlatFormComposite(parent);
//        FormData data;
//
//        nameText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
//        data = new FormData();
//        data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
//        data.right = new FormAttachment(100, 0);
//        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
//        nameText.setLayoutData(data);
//        nameText.addModifyListener(listener);
//
//        CLabel labelLabel = getWidgetFactory()
//            .createCLabel(composite, "name:"); //$NON-NLS-1$
//        data = new FormData();
//        data.left = new FormAttachment(0, 0);
//        data.right = new FormAttachment(nameText,
//            -ITabbedPropertyConstants.HSPACE);
//        data.top = new FormAttachment(nameText, 0, SWT.CENTER);
//        labelLabel.setLayoutData(data);
//    }
//
//    public void refresh() {
//        nameText.removeModifyListener(listener);
//        nameText.setText(cep.getCastedModel().getName());
//        nameText.addModifyListener(listener);
//    }


	private PropertySheetPage page;

	public void createControls(Composite parent,
			final TabbedPropertySheetPage atabbedPropertySheetPage) {
		super.createControls(parent, atabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
			.createFlatFormComposite(parent);
		page = new PropertySheetPage();

		page.createControl(composite);
		FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		page.getControl().setLayoutData(data);
		
		page.getControl().addControlListener(new ControlAdapter() {

			public void controlResized(ControlEvent e) {
				atabbedPropertySheetPage.resizeScrolledComposite();
			}
		});
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ISection#setInput(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		page.selectionChanged(part, selection);
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ISection#dispose()
	 */
	public void dispose() {
		super.dispose();

		if (page != null) {
			page.dispose();
			page = null;
		}

	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ISection#refresh()
	 */
	public void refresh() {
		page.refresh();
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ISection#shouldUseExtraSpace()
	 */
	public boolean shouldUseExtraSpace() {
		return true;
	}

}
