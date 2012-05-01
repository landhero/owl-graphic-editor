//package cn.edu.pku.ogeditor.views;
//
//import org.eclipse.jface.dialogs.TitleAreaDialog;
//import org.eclipse.jface.viewers.DoubleClickEvent;
//import org.eclipse.jface.viewers.IDoubleClickListener;
//import org.eclipse.jface.viewers.ILabelProviderListener;
//import org.eclipse.jface.viewers.ISelection;
//import org.eclipse.jface.viewers.IStructuredContentProvider;
//import org.eclipse.jface.viewers.ITableLabelProvider;
//import org.eclipse.jface.viewers.LabelProvider;
//import org.eclipse.jface.viewers.StructuredSelection;
//import org.eclipse.jface.viewers.TableViewer;
//import org.eclipse.jface.viewers.Viewer;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.FocusAdapter;
//import org.eclipse.swt.events.FocusEvent;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.graphics.Image;
//import org.eclipse.swt.layout.FormAttachment;
//import org.eclipse.swt.layout.FormData;
//import org.eclipse.swt.layout.FormLayout;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Menu;
//import org.eclipse.swt.widgets.MenuItem;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Table;
//import org.eclipse.swt.widgets.TableColumn;
//import org.eclipse.swt.widgets.Text;
//import org.eclipse.ui.IEditorPart;
//import org.eclipse.ui.ISelectionListener;
//import org.eclipse.ui.IWorkbenchPart;
//import org.eclipse.ui.part.ViewPart;
//import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
//
//import cn.edu.pku.ogeditor.ShapesEditor;
//
//public class SWRLView extends ViewPart implements ISelectionListener {
//
//	public static final String RULE_NAME = "Name";
//	public static final String RULE_CONTENT = "Expression";
//	private static final String ADD_RULE_MENU = "Add a new rule";
//	private static final String REMOVE_RULE_MENU = "Remove rule";
//	private static final String EDIT_RULE_MENU = "edit rule";
//	private TableViewer viewer;
//
//	@Override
//	public void createPartControl(Composite parent) {
//
//		viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL
//				| SWT.V_SCROLL | SWT.FULL_SELECTION);
//		Table table = viewer.getTable();
//		table.setHeaderVisible(true);
//		table.setLinesVisible(true);
//		TableColumn tableName = new TableColumn(table, SWT.NONE);
//		tableName.setWidth(60);
//		tableName.setText(RULE_NAME);
//		TableColumn tableRule = new TableColumn(table, SWT.NONE);
//		tableRule.setWidth(150);
//		tableRule.setText(RULE_CONTENT);
//
//		viewer.setContentProvider(new SWRLContentProvider());
//		viewer.setLabelProvider(new SWRLLabelProvider());
//		ShapesEditor editor = null;
//		if (null != (editor = getShapesEditor())) {
//			// editor.getDiagram().addRule(new SWRLRule("testname1",
//			// "testexpression1"));
//			viewer.setInput(editor);
//		}
//		// viewer.addSelectionChangedListener(new LevelChangeListener());
//		viewer.addDoubleClickListener(new RenameListener());
//		// getSite().setSelectionProvider(viewer);
//		getSite().getPage().addSelectionListener(this);
//		Menu menu = new Menu(parent);
//		table.setMenu(menu);
//		MenuItem menuItem1 = new MenuItem(menu, SWT.NONE);
//		menuItem1.setText(ADD_RULE_MENU);
//		menuItem1.addSelectionListener(new AddRuleListener());
//		MenuItem menuItem2 = new MenuItem(menu, SWT.NONE);
//		menuItem2.setText(REMOVE_RULE_MENU);
//		menuItem2.addSelectionListener(new RemoveOntologyListener());
//		MenuItem menuItem3 = new MenuItem(menu, SWT.NONE);
//		menuItem3.setText(EDIT_RULE_MENU);
//		menuItem3.addSelectionListener(new RenameListener());
//
//		// CellEditor[] cellEditors = new CellEditor[2];
//		// cellEditors[0] = new TextCellEditor(table);
//		// cellEditors[1] = new TextCellEditor(table);
//		// viewer.setColumnProperties(new String[] {"name","expression"});
//		// viewer.setCellEditors(cellEditors);
//		// SWRLCellModifier modifier = new SWRLCellModifier(viewer);
//		// viewer.setCellModifier(modifier);
//
//		// table.getSelection()[0].getChecked();
//	}
//
//	@Override
//	public void setFocus() {
//		// TODO Auto-generated method stub
//
//	}
//
//	private ShapesEditor getShapesEditor() {
//		// TODO Auto-generated method stub
//		IEditorPart curEditor = getSite().getPage().getActiveEditor();
//		if (curEditor instanceof ShapesEditor)
//			return (ShapesEditor) curEditor;
//		else
//			return null;
//	}
//
//	public class RuleDialog extends TitleAreaDialog {
//
//		private String sname;
//		private String sexpression;
//		private Text name;
//		private Text expression;
//
//		public RuleDialog(Shell parentShell) {
//			super(parentShell);
//			setSname(new String());
//			setSexpression(new String());
//		}
//
//		public RuleDialog(Shell shell, String n, String e) {
//			super(shell);
//			setSname(n);
//			setSexpression(e);
//		}
//
//		protected Control createContents(Composite parent) {
//			super.createContents(parent);
//			this.getShell().setText("SWRL Rule");// 设置对话框标题栏
//			setTitle("Edit Rule");
//			setMessage("Please input the name and the expression of the rule");
//			return parent;
//		}
//
//		protected Control createDialogArea(Composite parent) {
//			super.createDialogArea(parent);
//			Composite composite = new Composite(parent, SWT.NONE);
//			//composite.setLayout(new GridLayout(2, true));
//			composite.setLayout(new FormLayout());
//			FormData data;
//			Label nameLabel = new Label(composite, SWT.NONE);
//			nameLabel.setText("rule name：");
//			name = (new Text(composite, SWT.BORDER));
//			name.setText(getSname());
//			name.addFocusListener(new FocusAdapter() {
//
//				@Override
//				public void focusLost(FocusEvent e) {
//					// TODO Auto-generated method stub
//					super.focusLost(e);
//					sname = name.getText();
//				}
//			});
//			Label expressionLabel = new Label(composite, SWT.NONE);
//			expressionLabel.setText("rule expression：");
//			expression = (new Text(composite, SWT.BORDER));
//			expression.setText(getSexpression());
//			expression.addFocusListener(new FocusAdapter() {
//
//				@Override
//				public void focusLost(FocusEvent e) {
//					// TODO Auto-generated method stub
//					super.focusLost(e);
//					sexpression = expression.getText();
//				}
//			});
//			data = new FormData();
//			data.left = new FormAttachment(0, 0);
//			//data.right = new FormAttachment(name, -ITabbedPropertyConstants.HSPACE);
//			data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
//			data.width = 100;
//			data.height = 20;
//			nameLabel.setLayoutData(data);
//			
//			data = new FormData();
//			data.left =  new FormAttachment(nameLabel, ITabbedPropertyConstants.HSPACE);
//			//data.right = new FormAttachment(10, 0);
//			data.top = new FormAttachment(0, 0);
//			data.width = 400;
//			data.height = 15;
//			name.setLayoutData(data);
//			
//			data = new FormData();
//			data.left = new FormAttachment(0, 0);
//			data.top = new FormAttachment(nameLabel, ITabbedPropertyConstants.VSPACE);
//			data.width = 100;
//			data.height = 20;
//			expressionLabel.setLayoutData(data);
//			
//			data = new FormData();
//			data.left =  new FormAttachment(expressionLabel, ITabbedPropertyConstants.HSPACE);
//			//data.right = new FormAttachment(10, 0);
//			data.top = new FormAttachment(name, ITabbedPropertyConstants.VSPACE);
//			//data.width= 100;
//			data.width = 400;
//			data.height = 15;
//			expression.setLayoutData(data);
//			return parent;
//		}
//
//		public void setName(Text name) {
//			this.name = name;
//		}
//
//		public Text getName() {
//			return name;
//		}
//
//		public void setExpression(Text expression) {
//			this.expression = expression;
//		}
//
//		public Text getExpression() {
//			return expression;
//		}
//
//		public void setSname(String sname) {
//			this.sname = sname;
//		}
//
//		public String getSname() {
//			return sname;
//		}
//
//		public void setSexpression(String sexpression) {
//			this.sexpression = sexpression;
//		}
//
//		public String getSexpression() {
//			return sexpression;
//		}
//	}
//
//	private class AddRuleListener extends SelectionAdapter {
//		public void widgetSelected(SelectionEvent event) {
//
//			String name = new String();
//			String expression = new String();
//
//			Shell shell = viewer.getControl().getShell();
//			RuleDialog dialog = new RuleDialog(shell);
//			if (dialog.open() == TitleAreaDialog.OK) {
//				name = dialog.getSname();
//				expression = dialog.getSexpression();
//			}
//			// InputDialog dialog = new InputDialog(shell, ADD_RULE_MENU,
//			// "Enter a name for the new rule", null, null);
//			// if (dialog.open() == InputDialog.OK) {
//			// name = dialog.getValue().trim();
//			// }
//			// dialog = new InputDialog(shell, ADD_RULE_MENU,
//			// "Enter a name for the new rule", null, null);
//			// if (dialog.open() == InputDialog.OK) {
//			// expression = dialog.getValue().trim();
//			// }
//			SWRLRule rule = new SWRLRule(name, expression);
//			ShapesEditor editor = null;
//			if (null != (editor = getShapesEditor())) {
//				// editor.getDiagram().addRule(new SWRLRule("testname1",
//				// "testexpression1"));
//				editor.getDiagram().addRule(rule);
//				viewer.refresh();
//				editor.setDirty(true);
//			}
//		}
//	}
//
//	private class RemoveOntologyListener extends SelectionAdapter {
//		public void widgetSelected(SelectionEvent event) {
//			StructuredSelection curSelection = (StructuredSelection) viewer
//					.getSelection();
//			final SWRLRule rule = (SWRLRule) curSelection.getFirstElement();
//			if (rule == null)
//				return;
//			ShapesEditor editor = null;
//			if (null != (editor = getShapesEditor())) {
//				editor.getDiagram().removeRule(rule);
//				viewer.refresh();
//				editor.setDirty(true);
//			}
//		}
//	}
//
//	private class RenameListener extends SelectionAdapter implements
//			IDoubleClickListener {
//		public void widgetSelected(SelectionEvent event) {
//			StructuredSelection curSelection = (StructuredSelection) viewer
//					.getSelection();
//			final SWRLRule rule = (SWRLRule) curSelection.getFirstElement();
//			if (rule == null)
//				return;
//			Shell shell = viewer.getControl().getShell();
//			RuleDialog dialog = new RuleDialog(shell,rule.getName(), rule.getExpression());
//			if(dialog.open() == TitleAreaDialog.OK)
//			{
//				rule.setName(dialog.getSname());
//				rule.setExpression(dialog.getSexpression());
//				viewer.refresh();
//				ShapesEditor editor = null;
//				if (null != (editor = getShapesEditor())) {
//					editor.setDirty(true);
//				}
//			}
//		}
//
//		public void doubleClick(DoubleClickEvent event) {
//			// TODO Auto-generated method stub
//			widgetSelected(null);
//		}
//	}
//
//	@Override
//	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
//		// TODO Auto-generated method stub
//		if (part instanceof IEditorPart) {
//			ShapesEditor editor = null;
//			if ((editor = getShapesEditor()) != null) {
//				viewer.setInput(editor);
//			} else
//				viewer.setInput(null);
//		}
//	}
//}
//
//// class SWRLCellModifier implements ICellModifier
//// {
//// private TableViewer vi;
//// public SWRLCellModifier(TableViewer v)
//// {
//// vi = v;
//// }
//// @Override
//// public boolean canModify(Object element, String property) {
//// // TODO Auto-generated method stub
//// return true;
//// }
////
//// @Override
//// public Object getValue(Object element, String property) {
//// SWRLRule rule = (SWRLRule) element;
//// if(property.equals(SWRLView.RULE_NAME))
//// {
//// return rule.getName();
//// }
//// else if(property.equals(SWRLView.RULE_CONTENT))
//// {
//// return rule.getExpression();
//// }
//// return null;
//// }
////
//// @Override
//// public void modify(Object element, String property, Object value) {
//// TableItem item = (TableItem)element;
//// SWRLRule rule = (SWRLRule) item.getData();
//// if(property.equals(SWRLView.RULE_NAME))
//// {
//// rule.setName((String) value);
//// }
//// else if(property.equals(SWRLView.RULE_CONTENT))
//// {
//// rule.setExpression((String) value);
//// }
//// vi.update(rule, null);
//// }
//// }
//
//class SWRLContentProvider implements IStructuredContentProvider {
//
//	@Override
//	public void dispose() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public Object[] getElements(Object inputElement) {
//		// TODO Auto-generated method stub
//		if (inputElement instanceof ShapesEditor)
//			return ((ShapesEditor) inputElement).getDiagram().getRootDiagram()
//					.getRules().toArray();
//		else
//			return new Object[0];
//	}
//}
//
//class SWRLLabelProvider extends LabelProvider implements ITableLabelProvider {
//
//	@Override
//	public void addListener(ILabelProviderListener listener) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void dispose() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public boolean isLabelProperty(Object element, String property) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void removeListener(ILabelProviderListener listener) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public Image getColumnImage(Object element, int columnIndex) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getColumnText(Object element, int columnIndex) {
//		if (element instanceof SWRLRule) {
//			SWRLRule rule = (SWRLRule) element;
//			switch (columnIndex) {
//			case 0:
//				return rule.getName();
//			case 1:
//				return rule.getExpression();
//			}
//		}
//		return null;
//	}
//}
