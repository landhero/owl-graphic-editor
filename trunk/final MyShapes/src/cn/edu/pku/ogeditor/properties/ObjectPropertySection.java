package cn.edu.pku.ogeditor.properties;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.parts.ConnectionEditPart;

public class ObjectPropertySection extends AbstractPropertySection {

	private Button addDomainButton;
	private Button deleteDomainButton;
	private List domainList;
	private List rangeList;
	private Button addRangeButton;
	private Button deleteRangeButton;
	private ConnectionEditPart cep;

	public ObjectPropertySection() {
		// TODO Auto-generated constructor stub
	}
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		if(!(selection instanceof IStructuredSelection))
		{
			return;
		}
		Object input = ((IStructuredSelection) selection).getFirstElement();
		if(input instanceof ConnectionEditPart)
			cep = (ConnectionEditPart) input;
	}
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
		.createFlatFormComposite(parent);

		FormData data;
		composite.setBackground(ColorConstants.lightGray);

		CLabel domainLabel = getWidgetFactory().createCLabel(composite, "Domain");
		CLabel rangeLabel = getWidgetFactory().createCLabel(composite, "Range"); 
		addDomainButton = getWidgetFactory().createButton(composite, "Add", SWT.PUSH);
		deleteDomainButton = getWidgetFactory().createButton(composite, "Delete", SWT.PUSH);
		addRangeButton = getWidgetFactory().createButton(composite, "Add", SWT.PUSH);
		deleteRangeButton = getWidgetFactory().createButton(composite, "Delete", SWT.PUSH);
		
		domainList = getWidgetFactory().createList(composite, SWT.SINGLE);
		rangeList = getWidgetFactory().createList(composite, SWT.SINGLE);
		
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(addDomainButton,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		domainLabel.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(deleteDomainButton,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		addDomainButton.setLayoutData(data);
		
		data = new FormData();
		data.right = new FormAttachment(rangeLabel,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		deleteDomainButton.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(addRangeButton,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		rangeLabel.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(deleteRangeButton,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		addRangeButton.setLayoutData(data);
		
		data = new FormData();
		data.right = new FormAttachment(100,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		deleteRangeButton.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(rangeLabel,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(domainLabel, 
				ITabbedPropertyConstants.VSPACE);
		data.bottom = new FormAttachment(100,0);
		domainList.setLayoutData(data);
		
		
		data = new FormData();
		data.left = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(100,0);
		data.top = new FormAttachment(rangeLabel, 
				ITabbedPropertyConstants.VSPACE);
		data.height = 100;
		rangeList.setLayoutData(data);
	}
	public void refresh() 
	{
		domainList.removeAll();
		rangeList.removeAll();
		java.util.List<Shape> domain = cep.getCastedModel().getDomain();
		java.util.List<Shape> range = cep.getCastedModel().getRange();
		for (int i = 0; i < domain.size(); i++) {
			domainList.add(domain.get(i).getName());
		}
		for (int i = 0; i < range.size(); i++) {
			rangeList.add(range.get(i).getName());
		}
	}
}
