package cn.edu.pku.ogeditor.properties;

import java.util.ArrayList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import cn.edu.pku.ogeditor.model.ShapeProperty;
import cn.edu.pku.ogeditor.parts.ShapeEditPart;

public class DataPropertySection extends AbstractPropertySection {

	private Button addPropertyButton;
	private Button deletePropertyButton;
	private List propertyList;
	private CCombo rangeBox;
	private String[] types = ShapeProperty.getAllTypes();//{"boolean","float","int","string","any","date","dateTime","time"};
	private List allowedValueList;
	private ShapeEditPart sep;
	private Button addValueButton;
	private Button deleteValueButton;
	private ShapeProperty curProp;
	//private int lastRangeIndex;
	public DataPropertySection() {
		// TODO Auto-generated constructor stub
	}
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		if(!(selection instanceof IStructuredSelection))
		{
			return;
		}
		Object input = ((IStructuredSelection) selection).getFirstElement();
		if(input instanceof ShapeEditPart)
			sep = (ShapeEditPart) input;
	}
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
		.createFlatFormComposite(parent);

		FormData data;
		composite.setBackground(ColorConstants.lightGray);

		CLabel nameLabel = getWidgetFactory().createCLabel(composite, "DataProperty"); 
		addPropertyButton = getWidgetFactory().createButton(composite, "Add", SWT.PUSH);
		deletePropertyButton = getWidgetFactory().createButton(composite, "Delete", SWT.PUSH);
		CLabel rangeLabel = getWidgetFactory().createCLabel(composite, "Range"); 
		propertyList = getWidgetFactory().createList(composite, SWT.SINGLE);
		rangeBox = getWidgetFactory().createCCombo(composite, SWT.READ_ONLY);
		rangeBox.setItems(types);
		CLabel valueLabel = getWidgetFactory().createCLabel(composite, "Allowed Values"); 
		addValueButton = getWidgetFactory().createButton(composite, "Add", SWT.PUSH);
		deleteValueButton = getWidgetFactory().createButton(composite, "Delete", SWT.PUSH);
		allowedValueList = getWidgetFactory().createList(composite, SWT.SINGLE);

		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(addPropertyButton,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		nameLabel.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(deletePropertyButton,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		addPropertyButton.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(rangeLabel,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		deletePropertyButton.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(rangeBox,
				-ITabbedPropertyConstants.HSPACE);
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
		//		data.left = new FormAttachment(rangeLabel, 
		//				ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(100,0);
		data.top = new FormAttachment(0,0);
		rangeBox.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		//		data.right = new FormAttachment(rangeBox,
		//				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(rangeLabel, 
				ITabbedPropertyConstants.VSPACE);
		valueLabel.setLayoutData(data);

		data = new FormData();
		//data.left = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(deleteValueButton,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(rangeBox, 
				ITabbedPropertyConstants.VSPACE);
		addValueButton.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(100,0);
		data.top = new FormAttachment(rangeBox, 
				ITabbedPropertyConstants.VSPACE);
		deleteValueButton.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.right = new FormAttachment(100,0);
		data.top = new FormAttachment(addValueButton, 
				ITabbedPropertyConstants.VSPACE);
		allowedValueList.setLayoutData(data);

		addPropertyButton.addSelectionListener(new AddPropertyListener());
		deletePropertyButton.addSelectionListener(new DeletePropertyListener());
		propertyList.addSelectionListener(new PropertyListener());
		rangeBox.setEnabled(false);
		rangeBox.addSelectionListener(new RangeBoxListener());
		addValueButton.addSelectionListener(new AddValueListener());
		deleteValueButton.addSelectionListener(new DeleteValueListener());
	}
	public void refresh() {
		java.util.List<ShapeProperty> pt = sep.getCastedModel().getProperties();
		java.util.List<String> ptNames = new ArrayList<String>();
		for (int i = 0; i < pt.size(); i++) {
			ptNames.add(pt.get(i).getName());
		}
		propertyList.setItems((String[])ptNames.toArray(new String[0]));

		if(pt.size() != 0)
		{
			curProp = pt.get(0);
			//curProp = prop;
			propertyList.select(0);
			rangeBox.setEnabled(true);
			rangeBox.select(ShapeProperty.getSelectedIndex(curProp.getType()));
			//lastRangeIndex = ShapeProperty.getSelectedIndex(curProp.getType());
			//java.util.List<String> ptValues = new ArrayList<String>();
			for (int i = 0; i < curProp.getValues().size(); i++) {
				allowedValueList.add(curProp.getValues().get(i));
				//ptValues.add(curProp.getValues().get(i));
			}
			//allowedValueList.setItems((String[]) ptValues.toArray(new String[0]));
		}
	}

	private class AddPropertyListener implements SelectionListener
	{
		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			InputDialog dialog =
				new InputDialog(Display.getCurrent().getActiveShell(), "Create a new property",
						"Enter a name for the new Property:",
						null, null);
			if (dialog.open() == InputDialog.OK)
			{
				String propName = dialog.getValue().trim();
				ShapeProperty newProp = new ShapeProperty();
				newProp.setName(propName);
				newProp.setType(ShapeProperty.ANY_TYPE);
				if(sep.getCastedModel().addProperty(newProp))
				{
					propertyList.add(propName);
//					propertyList.setSelection(propertyList.getItemCount()-1);
//					curProp = newProp;
				}
				else 
				{
					//弹出个对话框之类的
				}
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}	
	}

	private class DeletePropertyListener implements SelectionListener
	{
		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			boolean confirm = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "提示", "确实要删除该属性吗？");
			if(confirm)
			{
				String propName = propertyList.getSelection()[0];
				ShapeProperty prop = sep.getCastedModel().getProperty(propName);
				sep.getCastedModel().deleteProperty(prop);
				propertyList.remove(propertyList.getSelection()[0]);
				propertyList.deselectAll();
				curProp = null;
				rangeBox.deselectAll();
				rangeBox.setEnabled(false);
				allowedValueList.removeAll();
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}	
	}

	private class PropertyListener implements SelectionListener
	{
		@Override
		public void widgetSelected(SelectionEvent e) {
			if(propertyList.getSelection().length > 0)
			{
				String propName = propertyList.getSelection()[0];
				curProp = sep.getCastedModel().getProperty(propName);
				//curProp = prop;
				rangeBox.setEnabled(true);
				rangeBox.select(ShapeProperty.getSelectedIndex(curProp.getType()));
				if(curProp.getType().equals(ShapeProperty.ANY_TYPE))
				{
					addValueButton.setEnabled(false);
					deleteValueButton.setEnabled(false);
				}
				else
				{
					addValueButton.setEnabled(true);
					deleteValueButton.setEnabled(true);
				}
				//java.util.List<String> ptValues = new ArrayList<String>();
				allowedValueList.removeAll();
				for (int i = 0; i < curProp.getValues().size(); i++) {
					allowedValueList.add(curProp.getValues().get(i));
					//ptValues.add(curProp.getValues().get(i));
				}
				//allowedValueList.setItems((String[]) ptValues.toArray(new String[0]));
			}
			else
				rangeBox.setEnabled(false);
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}	
	}
	private class RangeBoxListener implements SelectionListener
	{
		@Override
		public void widgetSelected(SelectionEvent e) {
			//String pre = rangeBox.getText();
			int i = rangeBox.getSelectionIndex();
//			if(pre.equals(rangeBox.getItem(i)))
//				return;
			allowedValueList.removeAll();
			curProp.deleteAllValues();
			if(ShapeProperty.getAllTypes()[i].equals(ShapeProperty.ANY_TYPE))
			{
				addValueButton.setEnabled(false);
				deleteValueButton.setEnabled(false);
			}
			else 
			{
				addValueButton.setEnabled(true);
				deleteValueButton.setEnabled(true);
				curProp.setType(ShapeProperty.getAllTypes()[i]);
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}	
	}

	private class AddValueListener implements SelectionListener
	{
		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			InputDialog dialog =
				new InputDialog(Display.getCurrent().getActiveShell(), "Input",
						"Enter a new "+rangeBox.getText()+" literal",
						null, null);
			String type = rangeBox.getText();
			if (dialog.open() == InputDialog.OK)
			{
				String value = dialog.getValue().trim();
				if(type.equals(ShapeProperty.BOOLEAN_TYPE))
				{
					Boolean v = Boolean.valueOf(value);
					if(curProp.addValue(v.toString()))
						allowedValueList.add(v.toString());
					else
					{
						//弹出个对话框之类的
					}
				}
				else if(type.equals(ShapeProperty.INT_TYPE))
				{
					Integer v = Integer.valueOf(value);
					if(curProp.addValue(v.toString()))
						allowedValueList.add(v.toString());
					else
					{
						//弹出个对话框之类的
					}
				}
				else if(type.equals(ShapeProperty.FLOAT_TYPE))
				{
					Float v = Float.valueOf(value);
					if(curProp.addValue(v.toString()))
						allowedValueList.add(v.toString());
					else
					{
						//弹出个对话框之类的
					}
				}
				else if(type.equals(ShapeProperty.STRING_TYPE)
						||type.equals(ShapeProperty.DATE_TYPE)
						||type.equals(ShapeProperty.DATETIME_TYPE)
						||type.equals(ShapeProperty.TIME_TYPE))
				{
					String v = value;
					if(curProp.addValue(v.toString()))
						allowedValueList.add(v.toString());
					else
					{
						//弹出个对话框之类的
					}
				}

			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}
	}

	private class DeleteValueListener implements SelectionListener
	{
		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			boolean confirm = MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "提示", "确实要删除该值吗？");
			if (confirm)
			{
				curProp.deleteValue(allowedValueList.getSelection()[0]);
				allowedValueList.remove(allowedValueList.getSelection()[0]);
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}
	}
}
