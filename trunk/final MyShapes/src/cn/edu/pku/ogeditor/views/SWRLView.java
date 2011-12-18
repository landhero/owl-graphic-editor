package cn.edu.pku.ogeditor.views;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import cn.edu.pku.ogeditor.ShapesEditor;

public class SWRLView extends ViewPart implements ISelectionListener {
	
	public static final String RULE_NAME = "Name";
	public static final String RULE_CONTENT = "Expression";
	private TableViewer viewer;

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPartControl(Composite parent) {

		viewer = new TableViewer(parent, SWT.CHECK);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableColumn tableName = new TableColumn(table, SWT.NONE);
		tableName.setWidth(60);
		tableName.setText(RULE_NAME);
		TableColumn tableRule = new TableColumn(table, SWT.NONE);
		tableRule.setWidth(150);
		tableRule.setText(RULE_CONTENT);
		
		viewer.setContentProvider(new SWRLContentProvider());
		viewer.setLabelProvider(new SWRLLabelProvider());
		ShapesEditor editor = null;
		if (null != (editor = getShapesEditor())) {
			//editor.getDiagram().addRule(new SWRLRule("testname1", "testexpression1"));
			viewer.setInput(editor);
		}
		
		CellEditor[] cellEditors = new CellEditor[2];
		cellEditors[0] = new TextCellEditor(table);
		cellEditors[1] = new TextCellEditor(table);
		viewer.setCellEditors(cellEditors);
		SWRLCellModifier modifier = new SWRLCellModifier(viewer);
		viewer.setCellModifier(modifier);
		
		//table.getSelection()[0].getChecked();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private ShapesEditor getShapesEditor() {
		// TODO Auto-generated method stub
		IEditorPart curEditor = getSite().getPage().getActiveEditor();
		if (curEditor instanceof ShapesEditor)
			return (ShapesEditor) curEditor;
		else
			return null;
	}
}
class SWRLCellModifier implements ICellModifier
{
	private TableViewer vi;
	public SWRLCellModifier(TableViewer v)
	{
		vi = v;
	}
	@Override
	public boolean canModify(Object element, String property) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		SWRLRule rule = (SWRLRule) element;
		if(property.equals(SWRLView.RULE_NAME))
		{
			return rule.getName();
		}
		else if(property.equals(SWRLView.RULE_CONTENT))
		{
			return rule.getExpression();
		}
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		TableItem item = (TableItem)element;
		SWRLRule rule = (SWRLRule) item.getData();
		if(property.equals(SWRLView.RULE_NAME))
		{
			rule.setName((String) value);
		}
		else if(property.equals(SWRLView.RULE_CONTENT))
		{
			rule.setExpression((String) value);
		}
		vi.update(rule, null);
	}
}

class SWRLContentProvider implements IStructuredContentProvider
{

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		if(inputElement instanceof ShapesEditor)
			return ((ShapesEditor)inputElement).getDiagram().getRootDiagram().getRules().toArray();
		else 
			return new Object[0];
	}
}

class SWRLLabelProvider extends LabelProvider implements ITableLabelProvider
{

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof SWRLRule)
		{
			SWRLRule rule = (SWRLRule) element;
			switch (columnIndex)
			{
			case 0:
				return rule.getName();
			case 1:
				return rule.getExpression();
			}
		}
		return null;
	}
}
