package cn.edu.pku.ogeditor.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.model.ShapesDiagram;

public class HierarchyView extends ViewPart {

	private TreeViewer viewer;
	private TreeEditor ce;

	public HierarchyView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub

		viewer = new TreeViewer (parent, SWT.SINGLE);
		final Tree tree = viewer.getTree();
		ce = new TreeEditor(tree);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		//tree.addSelectionListener(new RenameListener());
		viewer.setLabelProvider(new HierarchyLabelProvider());
		viewer.setContentProvider(new HierarchyContentProvider());
		List<ShapesDiagram> diagrams = new ArrayList<ShapesDiagram>();
		diagrams.add(ShapesEditor.myselfShapesEditor.getDiagram());
		viewer.setInput(diagrams.toArray());
		viewer.addSelectionChangedListener(new LevelChangeListener());
		viewer.addDoubleClickListener(new RenameListener());
		getSite().setSelectionProvider(viewer);
		Menu menu = new Menu(parent);
		tree.setMenu(menu);
		MenuItem menuItem1 = new MenuItem(menu, SWT.NONE);
		menuItem1.setText("Add Lower Level Ontology");
		menuItem1.addSelectionListener(new AddChildListener());
		MenuItem menuItem2 = new MenuItem(menu, SWT.NONE);
		menuItem2.setText("Remove Ontology");
		menuItem2.addSelectionListener(new RemoveOntologyListener());
		MenuItem menuItem3 = new MenuItem(menu, SWT.NONE);
		menuItem3.setText("Rename");
		menuItem3.addSelectionListener(new RenameListener());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private class LevelChangeListener implements ISelectionChangedListener
	{
		public void selectionChanged(SelectionChangedEvent event) {
			// TODO Auto-generated method stub
			TreeSelection curSelection = (TreeSelection) viewer.getSelection();
			final ShapesDiagram curDiagram = (ShapesDiagram) curSelection.getFirstElement();
			if(curDiagram == null)
				return; 
			ShapesEditor.myselfShapesEditor.refreshModel(curDiagram);
		}
	}

	private class AddChildListener extends SelectionAdapter
	{
		public void widgetSelected(SelectionEvent event){
			//System.out.println(viewer.getSelection().toString());
			TreeSelection curSelection = (TreeSelection) viewer.getSelection();
			final ShapesDiagram curDiagram = (ShapesDiagram) curSelection.getFirstElement();
			if(curDiagram == null)
				return;
			final Shell tempShell = new Shell();
			tempShell.setText("Create Lower Level Ontology");
			tempShell.setSize(350, 120);
			Label label = new Label(tempShell, SWT.NONE);
			label.setText("Please input the name of the new Ontology");
			label.setLocation(5, 5);
			label.setSize(350,15);
			final Text text = new Text(tempShell, SWT.NONE);
			text.setLocation(5, 30);
			text.setSize(200, 13);
			Button button = new Button(tempShell, SWT.NONE);
			button.setLocation(5, 53);
			button.setSize(100, 25);
			button.setText("OK");
			button.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
					String childName = text.getText();
					ShapesDiagram childDiagram = new ShapesDiagram();
					childDiagram.setName(childName);
					curDiagram.addLowerLevelDiagram(childDiagram);
					viewer.refresh(curDiagram);
					ShapesEditor.myselfShapesEditor.setDirty(true);
					tempShell.dispose();
				}
			});
			tempShell.open();
		}
	}
	
	private class RemoveOntologyListener extends SelectionAdapter
	{
		public void widgetSelected(SelectionEvent event){
			//System.out.println(viewer.getSelection().toString());
			TreeSelection curSelection = (TreeSelection) viewer.getSelection();
			final ShapesDiagram curDiagram = (ShapesDiagram) curSelection.getFirstElement();
			if(curDiagram == null)
				return;
			ShapesDiagram curDiagramFather = curDiagram.getFather();
			if(curDiagramFather == null)
				return;
			curDiagramFather.removeLowerLevelDiagram(curDiagram);
			viewer.refresh(curDiagramFather);
			ShapesEditor.myselfShapesEditor.refreshModel(curDiagramFather);
			ShapesEditor.myselfShapesEditor.setDirty(true);
		}
	}
	
	private class RenameListener extends SelectionAdapter implements IDoubleClickListener
	{
		public void widgetSelected(SelectionEvent event){
			TreeSelection curSelection = (TreeSelection) viewer.getSelection();
			final TreeItem item = viewer.getTree().getSelection()[0];
			final ShapesDiagram curDiagram = (ShapesDiagram) curSelection.getFirstElement();
			if (curSelection != null) {

				boolean showBorder = true;
				final Composite composite = new Composite (viewer.getTree(), SWT.NONE);
				if (showBorder) composite.setBackground (ColorConstants.black);
				final Text text = new Text (composite, SWT.NONE);
				final int inset = showBorder ? 1 : 0;
				composite.addListener (SWT.Resize, new Listener () {
					public void handleEvent (Event e) {
						Rectangle rect = composite.getClientArea ();
						text.setBounds (rect.x + inset, rect.y + inset, rect.width - inset * 2, rect.height - inset * 2);
					}
				});
				Listener textListener = new Listener () {
					public void handleEvent (final Event e) {
						switch (e.type) {
						case SWT.FocusOut:
							item.setText (text.getText ());
							composite.dispose ();
							break;
						case SWT.Verify:
							String newText = text.getText ();
							String leftText = newText.substring (0, e.start);
							String rightText = newText.substring (e.end, newText.length ());
							GC gc = new GC (text);
							Point size = gc.textExtent (leftText + e.text + rightText);
							gc.dispose ();
							size = text.computeSize (size.x, SWT.DEFAULT);
							ce.horizontalAlignment = SWT.LEFT;
							Rectangle itemRect = item.getBounds (), rect = viewer.getTree().getClientArea ();
							ce.minimumWidth = Math.max (size.x, itemRect.width) + inset * 2;
							int left = itemRect.x, right = rect.x + rect.width;
							ce.minimumWidth = Math.min (ce.minimumWidth, right - left);
							ce.minimumHeight = size.y + inset * 2;
							ce.layout ();
							ShapesEditor.myselfShapesEditor.setDirty(true);
							break;
						case SWT.Traverse:
							switch (e.detail) {
							case SWT.TRAVERSE_RETURN:
								item.setText (text.getText());
								curDiagram.setName(text.getText());
								//FALL THROUGH
							case SWT.TRAVERSE_ESCAPE:
								composite.dispose ();
								e.doit = false;
							}
							break;
						}
					}
				};
				text.addListener (SWT.FocusOut, textListener);
				text.addListener (SWT.Traverse, textListener);
				text.addListener (SWT.Verify, textListener);
				ce.setEditor (composite, item);
				text.setText (item.getText());
				text.selectAll ();
				text.setFocus ();
			}
		}

		public void doubleClick(DoubleClickEvent event) {
			// TODO Auto-generated method stub
			widgetSelected(null);
		}
	}
}

