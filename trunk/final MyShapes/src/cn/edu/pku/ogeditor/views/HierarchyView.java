package cn.edu.pku.ogeditor.views;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.ShapesEditorPaletteFactory;
import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.EllipseShape;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.parts.ShapesEditPartFactory;

public class HierarchyView extends ViewPart {

	private Table table;
	public void createPartControl(Composite parent) {
		table=new Table(parent,SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableColumn col1=new TableColumn(table,SWT.NONE);
		col1.setText("Levels");
		col1.setWidth(200);
		int cengshu = 0;
		if(ShapesEditor.myselfShapesEditor != null)
		{
			cengshu = ShapesEditor.myselfShapesEditor.getDiagram().getShapesList().size();
			for(int index=0;index<cengshu;index++){
				TableItem item=new TableItem(table,0);
				item.setText("Level "+(index+1));
			}
		}

		table.addSelectionListener(new LevelChangeListener());
		
		Composite composite=new Composite(parent,SWT.NONE);
		composite.setLayout(new RowLayout());
		Button addButton=new Button(composite,SWT.NONE);
		addButton.setText("Add One Level");
		addButton.addSelectionListener(new AddLevelListener(cengshu));

		Button deleteButton=new Button(composite,SWT.NONE);
		deleteButton.setText("Delete One Level");
		deleteButton.addSelectionListener(new RemoveLevelListener(cengshu));


	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	private class AddLevelListener extends SelectionAdapter {
		int i;

		private AddLevelListener(int cengshu) {
			i = cengshu;
		}

		public void widgetSelected(SelectionEvent e){
			i++;
			TableItem item=new TableItem(table,0);
			item.setText("Level "+i);
			ShapesEditor.myselfShapesEditor.getDiagram().addShapes(i);
		}
	}

	//not completed
	private class RemoveLevelListener extends SelectionAdapter {
		private RemoveLevelListener(int cengshu) {
		}
	}

	private class LevelChangeListener implements SelectionListener
	{

		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			int selectedIndex=table.getSelectionIndex();
			ShapesEditor.myselfShapesEditor.getDiagram().changeShapes(selectedIndex);
			ShapesEditPartFactory.diagramEditPart.refresh();
			if(selectedIndex==0){
				ShapesEditorPaletteFactory.RemoveShapeTool();
				ShapesEditorPaletteFactory.RemoveRequiredConnectionTool();
				ShapesEditorPaletteFactory.RemoveElectiveConnectionTool();
				Shape rootShape=new EllipseShape();

				rootShape.setName("Thing");
				ShapesEditorPaletteFactory.AddShapeTool(rootShape);

				Connection requiredRootConnection=new Connection("RequiredRoot");//注意，以后可能要修改@吴韬
				requiredRootConnection.setName("RequiredRelation");
				requiredRootConnection.setRequired(true);
				ShapesEditorPaletteFactory.AddRequiredConnectionTool(requiredRootConnection);

				Connection electiveRootConnection=new Connection("ElectiveRoot");//注意，以后可能要修改@吴韬
				electiveRootConnection.setName("ElectiveRelation");
				electiveRootConnection.setRequired(false);
				ShapesEditorPaletteFactory.AddElectiveConnectionTool(electiveRootConnection);

			}
			else{
				ShapesEditorPaletteFactory.RemoveShapeTool();
				ShapesEditorPaletteFactory.RemoveRequiredConnectionTool();
				ShapesEditorPaletteFactory.RemoveElectiveConnectionTool();
				ArrayList<Shape> shapes=(ArrayList<Shape>) ShapesEditor.myselfShapesEditor.getDiagram().getShapesList().get(selectedIndex-1);
				Connection tempConnection;
				for(int i=0;i<shapes.size();i++){
					Shape shapeTemp=(Shape)shapes.get(i);
					ShapesEditorPaletteFactory.AddShapeTool(shapeTemp);
					for(int j=0;j<shapeTemp.getSourceConnections().size();j++){
						tempConnection=(Connection)shapeTemp.getSourceConnections().get(j);
						if(tempConnection.isRequired())
							ShapesEditorPaletteFactory.AddRequiredConnectionTool(tempConnection);
						else 
							ShapesEditorPaletteFactory.AddElectiveConnectionTool(tempConnection);
					}
				}
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}
	}
}
