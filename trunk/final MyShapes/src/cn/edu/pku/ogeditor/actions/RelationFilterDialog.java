package cn.edu.pku.ogeditor.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.ShapesPlugin;
import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.ShapesDiagram;

public class RelationFilterDialog extends Dialog {

	private CheckboxTreeViewer tv;
	private ShapesDiagram diagram;

	protected RelationFilterDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		diagram = ShapesEditor.myselfShapesEditor.getDiagram();
	}

	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		super.configureShell(newShell);
		newShell.setText("Relation Filter");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		Button upperCase = new Button(container, SWT.CHECK);
		upperCase.setText("&Upper case");
		tv = new CheckboxTreeViewer(container);
		tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		tv.setContentProvider(new RelationFilterTreeContentProvider());
		tv.setLabelProvider(new RelationFilterTreeLabelProvider());
		tv.setInput(diagram); // pass a non-null that will be ignored
		ArrayList<Connection> visibleConnections = new ArrayList<Connection>();
		List<Connection> allConnections = diagram.getAllConnectionsNames();
		for (int i = 0; i < allConnections.size(); i++) {
			if (allConnections.get(i).isVisible()) {
				visibleConnections.add(allConnections.get(i));
			}
		}
		tv.setCheckedElements(visibleConnections.toArray());

		upperCase.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				boolean preserveCase = ((Button) event.widget).getSelection();
				RelationFilterTreeLabelProvider ftlp = (RelationFilterTreeLabelProvider) tv
						.getLabelProvider();
				ftlp.setPreserveCase(preserveCase);
			}
		});

		// When user checks a checkbox in the tree, check all its children
		tv.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				// If the item is checked . . .
				if (event.getChecked()) {
					// . . . check all its children
					tv.setSubtreeChecked(event.getElement(), true);
				}
			}
		});
		return container;
	}

	protected int getShellStyle() {

		return super.getShellStyle() | SWT.RESIZE | SWT.MAX;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		// TODO Auto-generated method stub
		// super.createButtonsForButtonBar(parent);
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			List<Connection> connections = diagram.getAllConnectionsNames();
			for (int i = 0; i < connections.size(); i++) {
				Connection curConnection = connections.get(i);
				if (tv.getChecked(curConnection)) {
					diagram.setConnectionsVisible(curConnection, true);
				}
			}
			for (int i = 0; i < connections.size(); i++) {
				Connection curConnection = connections.get(i);
				if (!tv.getChecked(curConnection)) {
					diagram.setConnectionsVisible(curConnection, false);
				}
			}
		}
		super.buttonPressed(buttonId);
	}
}

/**
 * This class provides the content for the tree in FileTree
 */
class RelationFilterTreeContentProvider implements ITreeContentProvider {
	public ShapesDiagram diagram = ShapesEditor.myselfShapesEditor.getDiagram();

	public Object[] getChildren(Object connection) {
		return null;
	}

	public Object getParent(Object connection) {
		return null;
	}

	public boolean hasChildren(Object connection) {
		return false;
	}

	public Object[] getElements(Object diagram) {
		return ((ShapesDiagram) diagram).getAllConnectionsNames().toArray();
	}

	/**
	 * Disposes any created resources
	 */
	public void dispose() {
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}
}

/**
 * This class provides the labels for the file tree
 */

class RelationFilterTreeLabelProvider implements ILabelProvider {
	private List<ILabelProviderListener> listeners;
	private Image image;
	boolean upperCase;

	public RelationFilterTreeLabelProvider() {
		listeners = new ArrayList<ILabelProviderListener>();
		image = ImageDescriptor.createFromFile(ShapesPlugin.class,
				"icons/connection_common.gif").createImage(); 
	}

	public void setPreserveCase(boolean preserveCase) {
		this.upperCase = preserveCase;
		LabelProviderChangedEvent event = new LabelProviderChangedEvent(this);
		for (int i = 0, n = listeners.size(); i < n; i++) {
			ILabelProviderListener ilpl = (ILabelProviderListener) listeners
					.get(i);
			ilpl.labelProviderChanged(event);
		}
	}

	public Image getImage(Object connection) {
		return image;
	}

	public String getText(Object connection) {
		String text = ((Connection) connection).getName();
		return upperCase ? text.toUpperCase() : text;
	}

	public void addListener(ILabelProviderListener arg0) {
		listeners.add(arg0);
	}

	public void dispose() {
		if (image != null)
			image.dispose();
	}

	/**
	 * Returns whether changes to the specified property on the specified
	 * element would affect the label for the element
	 * 
	 * @param arg0
	 *            the element
	 * @param arg1
	 *            the property
	 * @return boolean
	 */
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	public void removeListener(ILabelProviderListener arg0) {
		listeners.remove(arg0);
	}
}