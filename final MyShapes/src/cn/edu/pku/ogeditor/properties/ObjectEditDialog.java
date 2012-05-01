package cn.edu.pku.ogeditor.properties;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import cn.edu.pku.ogeditor.wizards.ObjectInfo;
import cn.edu.pku.ogeditor.wizards.ObjectsListModel;
import cn.edu.pku.ogeditor.wizards.TableContentProvider;
import cn.edu.pku.ogeditor.wizards.TableLabelProvider;

public class ObjectEditDialog extends Dialog{

	private static final int LABEL_LENGTH = 200;
	private ObjectsListModel objects;
	
	protected ObjectEditDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());
		
		final Label addL = new Label(container, SWT.NONE);
		addL.setText("Add A Detectable Object:");

		final Label uris = new Label(container, SWT.BORDER);
		uris.setText("URIs");
		final Label rfid = new Label(container, SWT.BORDER);
		rfid.setText("RFID");
		final Label type = new Label(container, SWT.BORDER);
		type.setText("TYPE");

		Text urisText = new Text(container, SWT.BORDER);
		urisText.setText("http://");
		Text rfidText = new Text(container, SWT.BORDER);
		Text typeText = new Text(container, SWT.BORDER);
		Button addButton = new Button(container, SWT.NONE);
		addButton.setText("add");

		FormData addLData = new FormData();
		addLData.left = new FormAttachment(0);
		addLData.right = new FormAttachment(100);
		addLData.top = new FormAttachment(0);
		addL.setLayoutData(addLData);

		FormData urisData = new FormData();
		urisData.left = new FormAttachment(addL, 0, SWT.LEFT);
		urisData.right = new FormAttachment(addL, LABEL_LENGTH, SWT.LEFT);
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

		Table table = new Table(container, SWT.BORDER | SWT.MULTI
				| SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("URIs");
		column1.setWidth(LABEL_LENGTH);
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setWidth(LABEL_LENGTH);
		column2.setText("RFID");
		TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setWidth(LABEL_LENGTH);
		column3.setText("TYPE");
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableViewer viewer = new TableViewer(table);
		viewer.setContentProvider(new TableContentProvider());
		viewer.setLabelProvider(new TableLabelProvider());

		objects = new ObjectsListModel();
		viewer.setInput(objects);

		objects.add(new ObjectInfo("http://object1", "001", "light"));
		objects.add(new ObjectInfo("http://object2", "002", "air-conditioning"));
		objects.add(new ObjectInfo("http://object3", "003", "screen"));

		FormData listData = new FormData();
		listData.left = new FormAttachment(addL, 0, SWT.LEFT);
		listData.right = new FormAttachment(typeText, 0, SWT.RIGHT);
		listData.top = new FormAttachment(typeText, 30, SWT.BOTTOM);
		objectsListLabel.setLayoutData(listData);

		FormData tableData = new FormData();
		tableData.left = new FormAttachment(addL, 0, SWT.LEFT);
		tableData.right = new FormAttachment(typeText, 0, SWT.RIGHT);
		tableData.top = new FormAttachment(objectsListLabel, 0, SWT.BOTTOM);
		tableData.bottom = new FormAttachment(objectsListLabel, 300, SWT.BOTTOM);
		table.setLayoutData(tableData);

		FormData delButtonData = new FormData();
		delButtonData.left = new FormAttachment(table, 0, SWT.RIGHT);
		delButtonData.right = new FormAttachment(100);
		delButtonData.top = new FormAttachment(objectsListLabel, 0, SWT.BOTTOM);
		delButton.setLayoutData(delButtonData);
		return container;
	}
	
	protected void configureShell(Shell newShell){
		super.configureShell(newShell);
		newShell.setText("Edit Detectable Objects List");
	}


}
