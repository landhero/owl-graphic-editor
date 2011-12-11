package cn.edu.pku.ogeditor.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

public class SWRLViewer extends ViewPart implements ISelectionListener {
	
	private static final String RULE_NAME = "Name";
	private static final String RULE_CONTENT = "Expression";
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
		tableRule.setWidth(60);
		tableRule.setText(RULE_CONTENT);

		
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
