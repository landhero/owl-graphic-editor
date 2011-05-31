package cn.edu.pku.ogeditor.properties;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class DataPropertySection extends AbstractPropertySection {

	private Button addButton;
	private Button deleteButton;
	private List propertyList;
	private CCombo rangeBox;
	private String[] types = {"boolean","float","int","string","any","date","dateTime","time"};
	private List allowedValueList;
	public DataPropertySection() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
		.createFlatFormComposite(parent);

		FormData data;
		composite.setBackground(ColorConstants.lightGray);

		CLabel nameLabel = getWidgetFactory().createCLabel(composite, "DataProperty"); 
		addButton = getWidgetFactory().createButton(composite, "Add", SWT.PUSH);
		deleteButton = getWidgetFactory().createButton(composite, "Delete", SWT.PUSH);
		CLabel rangeLabel = getWidgetFactory().createCLabel(composite, "Range"); 
		propertyList = getWidgetFactory().createList(composite, SWT.SINGLE);
		rangeBox = getWidgetFactory().createCCombo(composite, SWT.READ_ONLY);
		rangeBox.setItems(types);
		allowedValueList = getWidgetFactory().createList(composite, SWT.SINGLE);
		
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(addButton,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		nameLabel.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(deleteButton,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		addButton.setLayoutData(data);
		
		data = new FormData();
		data.right = new FormAttachment(rangeLabel,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		deleteButton.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(100,0);
		data.top = new FormAttachment(0, 0);
		rangeLabel.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(rangeLabel,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(nameLabel, 
				ITabbedPropertyConstants.VSPACE);
		data.bottom = new FormAttachment(100,0);
		propertyList.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(100,0);
		data.top = new FormAttachment(rangeLabel, 
				ITabbedPropertyConstants.VSPACE);
		rangeBox.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(100,0);
		data.top = new FormAttachment(rangeBox, 
				ITabbedPropertyConstants.VSPACE);
		allowedValueList.setLayoutData(data);
	}
}
