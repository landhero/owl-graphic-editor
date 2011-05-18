package cn.edu.pku.ogeditor.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.model.ShapesDiagram;

public class HierarchyView extends ViewPart {

	private TreeViewer viewer;

	public HierarchyView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub

		viewer = new TreeViewer (parent, SWT.SINGLE);
		final Tree tree = viewer.getTree();
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		viewer.setLabelProvider(new HierarchyLabelProvider());
		viewer.setContentProvider(new HierarchyContentProvider());
		List<ShapesDiagram> diagrams = new ArrayList<ShapesDiagram>();
		diagrams.add(ShapesEditor.myselfShapesEditor.getDiagram());
		viewer.setInput(diagrams.toArray());
		viewer.addSelectionChangedListener(new LevelChangeListener());
		getSite().setSelectionProvider(viewer);
		Menu menu = new Menu(parent);
		tree.setMenu(menu);
		MenuItem menuItem1 = new MenuItem(menu, SWT.NONE);
		menuItem1.setText("Add Lower Level Ontology");
		menuItem1.addSelectionListener(new AddChildListener());
		MenuItem menuItem2 = new MenuItem(menu, SWT.NONE);
		menuItem2.setText("Remove Ontology");
		menuItem2.addSelectionListener(new RemoveChildListener());
		MenuItem menuItem3 = new MenuItem(menu, SWT.NONE);
		menuItem3.setText("Rename");
		menuItem3.addSelectionListener(new RenameChildListener());
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private class LevelChangeListener implements ISelectionChangedListener
	{
		@Override
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
					curDiagram.addLowLevelDiagram(childDiagram);
					viewer.refresh(curDiagram);
					ShapesEditor.myselfShapesEditor.setDirty(true);
					tempShell.dispose();
				}
			});
			tempShell.open();
		}
	}
	private class RemoveChildListener extends SelectionAdapter
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
			curDiagramFather.removeLowLevelDiagram(curDiagram);
			viewer.refresh(curDiagramFather);
			ShapesEditor.myselfShapesEditor.refreshModel(curDiagramFather);
			ShapesEditor.myselfShapesEditor.setDirty(true);
		}
	}
	private class RenameChildListener extends SelectionAdapter
	{
		public void widgetSelected(SelectionEvent event){
			//System.out.println(viewer.getSelection().toString());
			TreeSelection curSelection = (TreeSelection) viewer.getSelection();
			viewer.editElement(curSelection.getFirstElement(),0);
//			final ShapesDiagram curDiagram = (ShapesDiagram) curSelection.getFirstElement();
//			if(curDiagram == null)
//				return;
//			ShapesDiagram curDiagramFather = curDiagram.getFather();
//			if(curDiagramFather == null)
//				return;
//			curDiagramFather.removeLowLevelDiagram(curDiagram);
//			viewer.refresh(curDiagramFather);
//			ShapesEditor.myselfShapesEditor.refreshModel(curDiagramFather);
//			ShapesEditor.myselfShapesEditor.setDirty(true);
		}
	}
}

