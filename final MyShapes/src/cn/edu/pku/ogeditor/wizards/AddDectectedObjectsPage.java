package cn.edu.pku.ogeditor.wizards;

import java.util.ArrayList;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class AddDectectedObjectsPage extends WizardPage {
	public final static int LABEL_LENGTH = 150;
	private ObjectsListModel objects;
	private Text urisText;
	private Text rfidText;
	private Text typeText;
	private TableViewer viewer;

	public AddDectectedObjectsPage() {
		super("Add Detectable Objects");
		setTitle("Add Detectable Objects");
		setDescription("Add Detectable Objects");
	}

	public void createControl(Composite parent) {
		// TODO 自动生成方法存根
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new FormLayout());
		setControl(container);

		final Label addL = new Label(container, SWT.NONE);
		addL.setText("Add A Detectable Object:");

		final Label uris = new Label(container, SWT.BORDER);
		uris.setText("URI");
		final Label rfid = new Label(container, SWT.BORDER);
		rfid.setText("RFID");
		final Label type = new Label(container, SWT.BORDER);
		type.setText("TYPE");

		urisText = new Text(container, SWT.BORDER);
		urisText.setText("http://");
		rfidText = new Text(container, SWT.BORDER);
		typeText = new Text(container, SWT.BORDER);
		Button addButton = new Button(container, SWT.NONE);
		addButton.setText("add");
		addButton.addSelectionListener(new AddObjectListener());

		FormData addLData = new FormData();
		addLData.left = new FormAttachment(0);
		addLData.right = new FormAttachment(100);
		addLData.top = new FormAttachment(0);
		addL.setLayoutData(addLData);

		FormData urisData = new FormData();
		urisData.left = new FormAttachment(addL, 0, SWT.LEFT);
		urisData.right = new FormAttachment(addL, 300, SWT.LEFT);
		urisData.top = new FormAttachment(addL, 0, SWT.BOTTOM);
		uris.setLayoutData(urisData);

		FormData rfidData = new FormData();
		rfidData.left = new FormAttachment(uris, 0, SWT.RIGHT);
		rfidData.right = new FormAttachment(uris, LABEL_LENGTH, SWT.RIGHT);
		rfidData.top = new FormAttachment(addL, 0, SWT.BOTTOM);
		rfid.setLayoutData(rfidData);

		FormData typeData = new FormData();
		typeData.left = new FormAttachment(rfid, 0, SWT.RIGHT);
		typeData.right = new FormAttachment(rfid, LABEL_LENGTH, SWT.RIGHT);
		typeData.top = new FormAttachment(addL, 0, SWT.BOTTOM);
		type.setLayoutData(typeData);

		FormData urisTData = new FormData();
		urisTData.left = new FormAttachment(uris, 0, SWT.LEFT);
		urisTData.right = new FormAttachment(uris, 0, SWT.RIGHT);
		urisTData.top = new FormAttachment(uris, 0, SWT.BOTTOM);
		urisText.setLayoutData(urisTData);

		FormData rfidTData = new FormData();
		rfidTData.left = new FormAttachment(rfid, 0, SWT.LEFT);
		rfidTData.right = new FormAttachment(rfid, 0, SWT.RIGHT);
		rfidTData.top = new FormAttachment(rfid, 0, SWT.BOTTOM);
		rfidText.setLayoutData(rfidTData);

		FormData typeTData = new FormData();
		typeTData.left = new FormAttachment(type, 0, SWT.LEFT);
		typeTData.right = new FormAttachment(type, 0, SWT.RIGHT);
		typeTData.top = new FormAttachment(type, 0, SWT.BOTTOM);
		typeText.setLayoutData(typeTData);

		FormData addButtonData = new FormData();
		addButtonData.left = new FormAttachment(typeText, 0, SWT.RIGHT);
		addButtonData.right = new FormAttachment(100);
		addButtonData.top = new FormAttachment(addL, 0, SWT.BOTTOM);
		addButton.setLayoutData(addButtonData);

		final Label objectsListLabel = new Label(container, SWT.NONE);
		objectsListLabel.setText("List of Detectable Objects:");

		Button delButton = new Button(container, SWT.NONE);
		delButton.setText("delete");
		delButton.addSelectionListener(new DelObjectListener());

		Table table = new Table(container, SWT.BORDER | SWT.MULTI
				| SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("URIs");
		column1.setWidth(300);
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

		objects = new ObjectsListModel();
		viewer.setInput(objects);

//		objects.add(new ObjectInfo("http://object1", "001", "light"));
//		objects.add(new ObjectInfo("http://object2", "002", "air-conditioning"));
//		objects.add(new ObjectInfo("http://object3", "003", "screen"));

		FormData listData = new FormData();
		listData.left = new FormAttachment(addL, 0, SWT.LEFT);
		listData.right = new FormAttachment(typeText, 0, SWT.RIGHT);
		listData.top = new FormAttachment(typeText, 30, SWT.BOTTOM);
		objectsListLabel.setLayoutData(listData);

		FormData tableData = new FormData();
		tableData.left = new FormAttachment(addL, 0, SWT.LEFT);
		tableData.right = new FormAttachment(typeText, 0, SWT.RIGHT);
		tableData.top = new FormAttachment(objectsListLabel, 0, SWT.BOTTOM);
		tableData.bottom = new FormAttachment(100);
		table.setLayoutData(tableData);

		FormData delButtonData = new FormData();
		delButtonData.left = new FormAttachment(table, 0, SWT.RIGHT);
		delButtonData.right = new FormAttachment(100);
		delButtonData.top = new FormAttachment(objectsListLabel, 0, SWT.BOTTOM);
		delButton.setLayoutData(delButtonData);

		// checkboxTableViewer =
		// CheckboxTableViewer.newCheckList(container, SWT.BORDER);
		// checkboxTableViewer.setContentProvider(
		// new MasterContentProvider());
		// checkboxTableViewer.setLabelProvider(
		// new TableViewerLabelProvider());
		// final Table table = checkboxTableViewer.getTable();
		// final FormData formData = new FormData();
		// formData.bottom = new FormAttachment(100, 0);
		// formData.right = new FormAttachment(100, 0);
		// formData.top = new FormAttachment(0, 0);
		// formData.left = new FormAttachment(0, 0);
		// table.setLayoutData(formData);
		// table.setHeaderVisible(true);
		//
		// final TableColumn tableColumn =
		// new TableColumn(table, SWT.NONE);
		// tableColumn.setWidth(200);
		// tableColumn.setText("列表名称");
		//
		// final TableColumn tableColumn_1 =
		// new TableColumn(table, SWT.NONE);
		// tableColumn_1.setWidth(250);
		// tableColumn_1.setText("列表描述");
	}

	/**
	 * Update the content before becoming visible.
	 */
	public void setVisible(boolean visible) {
		// if (visible) {
		// AddressCategory category = ((NewAddressItemWizardPage)
		// getPreviousPage())
		// .getSelectedAddressCategory();
		// String peopleName = ((NewAddressItemWizardPage) getPreviousPage())
		// .getSelectedName();
		// item = new AddressItem(peopleName, category);
		// input = new SimpleFormEditorInput(item.getName());
		// checkboxTableViewer.setInput(input);
		// checkboxTableViewer.setAllChecked(true);
		// }
		super.setVisible(visible);
	}

	public ObjectsListModel getObjects() {
		return objects;
	}

	public void setObjects(ObjectsListModel objects) {
		this.objects = objects;
	}
	
	private class AddObjectListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
//			ObjectInfo object = new ObjectInfo(urisText.getText(), rfidText.getText(), typeText.getText());
//			objects.add(object);
//			refreshTexts();
		}
	}
	
	private class DelObjectListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
//			int[] indices = viewer.getTable().getSelectionIndices();
//			ArrayList<ObjectInfo> dels = new ArrayList<ObjectInfo>();
//			for(int index : indices)
//			{
//				ObjectInfo object = (ObjectInfo) viewer.getElementAt(index);
//				dels.add(object);
////				objects.remove(object);
//			}
//			objects.removeAll(dels);
//			viewer.refresh();
		}
	}
	private void refreshTexts() {
		urisText.setText("http://");
		rfidText.setText("");
		typeText.setText("");
	}
}


