/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.dialogs;

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
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;

/**
 * the dialog used to configure conceptfilter
 * @author Xueyuan Xing
 */
public class ConceptFilterDialog extends Dialog {
	private CheckboxTreeViewer tv;
	private ShapesDiagram diagram;

	public ConceptFilterDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		diagram = ShapesEditor.myselfShapesEditor.getDiagram();
	}

	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		super.configureShell(newShell);
	    newShell.setText("Concept Filter");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		// TODO Auto-generated method stub
		container.setLayout(new GridLayout(1, false));
		// Add a checkbox to toggle whether the labels preserve case
		Button upperCase = new Button(container, SWT.CHECK);
		upperCase.setText("&Upper case");
		//preserveCase.setSelection(true);
		tv = new CheckboxTreeViewer(container);
		tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		tv.setContentProvider(new ConceptFilterTreeContentProvider());
		tv.setLabelProvider(new ConceptFilterTreeLabelProvider());
		tv.setInput(diagram); // pass a non-null that will be ignored
		ArrayList<Shape> visibleShapes = new ArrayList<Shape>();
		List<Shape> allShapes = diagram.getAllShapesNames();
		for (int i = 0; i < allShapes.size(); i++) {
			if (allShapes.get(i).isVisible()) {
				visibleShapes.add(allShapes.get(i));
			}
		}
		tv.setCheckedElements(visibleShapes.toArray());

		// When user checks the checkbox, toggle the preserve case attribute
		// of the label provider
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
			List<Shape> shapes = diagram.getAllShapesNames();
			for (int i = 0; i < shapes.size(); i++) {
				Shape curShape = shapes.get(i);
				if (tv.getChecked(curShape)) {
					curShape.setVisible(true);
				}
				else
				{
					curShape.setVisible(false);
				}
			}
		}
		super.buttonPressed(buttonId);
	}
}

/**
 * This class provides the content for the tree in FileTree
 */

class ConceptFilterTreeContentProvider implements ITreeContentProvider {
	public ShapesDiagram diagram = ShapesEditor.myselfShapesEditor.getDiagram();

	/**
	 * Gets the children of the specified object
	 * 
	 * @param arg0
	 *            the parent object
	 * @return Object[]
	 */
	public Object[] getChildren(Object shape) {
		// Return the files and subdirectories in this directory
		// return ((Shape)shape).getChildren().toArray();
		return null;
	}

	/**
	 * Gets the parent of the specified object
	 * 
	 * @param shape
	 *            the object
	 * @return Object
	 */
	public Object getParent(Object shape) {
		// Return this file's parent file
		// return ((Shape) shape).getParent();
		return null;
	}

	/**
	 * Returns whether the passed object has children
	 * 
	 * @param shape
	 *            the parent object
	 * @return boolean
	 */
	public boolean hasChildren(Object shape) {
		// Get the children
		// Object[] obj = getChildren(shape);

		// Return whether the parent has children
		// return obj == null ? false : obj.length > 0;
		return false;
	}

	/**
	 * Gets the root element(s) of the tree
	 * 
	 * @param diagram
	 *            the input data
	 * @return Object[]
	 */
	public Object[] getElements(Object diagram) {
		// These are the root elements of the tree
		// We don't care what arg0 is, because we just want all
		// the root nodes in the file system
		return ((ShapesDiagram) diagram).getAllShapesNames().toArray();
	}

	/**
	 * Disposes any created resources
	 */
	public void dispose() {
		// Nothing to dispose
	}

	/**
	 * Called when the input changes
	 * 
	 * @param arg0
	 *            the viewer
	 * @param arg1
	 *            the old input
	 * @param arg2
	 *            the new input
	 */
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// Nothing to change
	}
}

/**
 * This class provides the labels for the file tree
 */

class ConceptFilterTreeLabelProvider implements ILabelProvider {
	// The listeners
	private List<ILabelProviderListener> listeners;
	private Image image;
	// Label provider state: preserve case of file names/directories
	boolean upperCase;

	/**
	 * Constructs a FileTreeLabelProvider
	 */
	public ConceptFilterTreeLabelProvider() {
		// Create the list to hold the listeners
		listeners = new ArrayList<ILabelProviderListener>();
		image = ImageDescriptor.createFromFile(ShapesPlugin.class,"icons/ellipse16.gif").createImage(); //new Image(null, new FileInputStream("../icons/ellipse16.gif"));
	}

	/**
	 * Sets the preserve case attribute
	 * 
	 * @param preserveCase
	 *            the preserve case attribute
	 */
	public void setPreserveCase(boolean preserveCase) {
		this.upperCase = preserveCase;

		// Since this attribute affects how the labels are computed,
		// notify all the listeners of the change.
		LabelProviderChangedEvent event = new LabelProviderChangedEvent(this);
		for (int i = 0, n = listeners.size(); i < n; i++) {
			ILabelProviderListener ilpl = (ILabelProviderListener) listeners
					.get(i);
			ilpl.labelProviderChanged(event);
		}
	}

	/**
	 * Gets the image to display for a node in the tree
	 * 
	 * @param shape
	 *            the node
	 * @return Image
	 */
	public Image getImage(Object shape) {
		// �Ժ����չ
		return image;
	}

	/**
	 * Gets the text to display for a node in the tree
	 * 
	 * @param shape
	 *            the node
	 * @return String
	 */
	public String getText(Object shape) {
		// Get the name of the file
		String text = ((Shape) shape).getName();
		// Check the case settings before returning the text
		return upperCase ? text.toUpperCase() : text;
	}

	/**
	 * Adds a listener to this label provider
	 * 
	 * @param arg0
	 *            the listener
	 */
	public void addListener(ILabelProviderListener arg0) {
		listeners.add(arg0);
	}

	//
	// /**
	// * Called when this LabelProvider is being disposed
	// */
	public void dispose() {
		// // Dispose the images
		// if (dir != null)
		// dir.dispose();
		// if (file != null)
		// file.dispose();
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

	/**
	 * Removes the listener
	 * 
	 * @param arg0
	 *            the listener to remove
	 */
	public void removeListener(ILabelProviderListener arg0) {
		listeners.remove(arg0);
	}

}