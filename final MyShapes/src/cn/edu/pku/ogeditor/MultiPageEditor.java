package cn.edu.pku.ogeditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;

import cn.edu.pku.ogeditor.model.ShapesDiagram;
import cn.edu.pku.ogeditor.properties.ListContentProvider;
import cn.edu.pku.ogeditor.properties.ListLabelProvider;
import cn.edu.pku.ogeditor.views.SWRLRule;
import cn.edu.pku.ogeditor.wizards.ObjectInfo;
import cn.edu.pku.ogeditor.wizards.TableContentProvider;
import cn.edu.pku.ogeditor.wizards.TableLabelProvider;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class MultiPageEditor extends MultiPageEditorPart implements
		IResourceChangeListener, ISelectionListener {

	private static final int LABEL_LENGTH = 260;
	private static final int EDITOR_INDEX = 1;

	@Override
	protected void setInput(IEditorInput input) {
		// TODO Auto-generated method stub
		super.setInput(input);
	}

	/** The text editor used in page 0. */
	private ShapesEditor editor;

	/** The font chosen in page 1. */
	private Font font;

	/** The text widget used in page 2. */
	private StyledText text;

	private Text urisText;
	private Text rfidText;
	private Text typeText;

	private TableViewer objectsViewer;

//	private ShapesDiagram diagram;

	private Text ruleText;

	private ListViewer SWRLViewer;

	private Text owlArea;

	private Text checkArea;

	/**
	 * Creates a multi-page editor example.
	 */
	public MultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);

	}
	
	void createPage0() {
//		initDiagram();

		Composite container = new Composite(getContainer(), SWT.NONE);
		container.setLayout(new FormLayout());
		
		final Label addL = new Label(container, SWT.NONE);
		addL.setText("Add A Detectable Object:");

		final Label uris = new Label(container, SWT.BORDER);
		uris.setText("URIs");
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
		
		FormData data;

		data = new FormData();
		data.left = new FormAttachment(1);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(1);
		addL.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(addL, 0, SWT.LEFT);
//		urisData.right = new FormAttachment(addL, LABEL_LENGTH, SWT.LEFT);
		data.right = new FormAttachment(30);
		data.top = new FormAttachment(addL, 0, SWT.BOTTOM);
		uris.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(uris, 0, SWT.RIGHT);
//		data.right = new FormAttachment(uris, LABEL_LENGTH, SWT.RIGHT);
		data.right = new FormAttachment(60);
		data.top = new FormAttachment(addL, 0, SWT.BOTTOM);
		rfid.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(rfid, 0, SWT.RIGHT);
//		data.right = new FormAttachment(rfid, LABEL_LENGTH, SWT.RIGHT);
		data.right = new FormAttachment(90);
		data.top = new FormAttachment(addL, 0, SWT.BOTTOM);
		type.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(uris, 0, SWT.LEFT);
		data.right = new FormAttachment(uris, 0, SWT.RIGHT);
		data.top = new FormAttachment(uris, 0, SWT.BOTTOM);
		urisText.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(rfid, 0, SWT.LEFT);
		data.right = new FormAttachment(rfid, 0, SWT.RIGHT);
		data.top = new FormAttachment(rfid, 0, SWT.BOTTOM);
		rfidText.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(type, 0, SWT.LEFT);
		data.right = new FormAttachment(type, 0, SWT.RIGHT);
		data.top = new FormAttachment(type, 0, SWT.BOTTOM);
		typeText.setLayoutData(data);

		data = new FormData();
//		addButtonData.left = new FormAttachment(typeText, 0, SWT.RIGHT);
		data.left = new FormAttachment(typeText, 10, SWT.RIGHT);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(addL, 0, SWT.BOTTOM);
		addButton.setLayoutData(data);

		final Label objectsListLabel = new Label(container, SWT.NONE);
		objectsListLabel.setText("List of Detectable Objects:");

		Button delButton = new Button(container, SWT.NONE);
		delButton.setText("delete");
		delButton.addSelectionListener(new DelObjectListener());

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

		objectsViewer = new TableViewer(table);
		objectsViewer.setContentProvider(new TableContentProvider());
		objectsViewer.setLabelProvider(new TableLabelProvider());

//		objects = new ObjectsListModel();
//		objectsViewer.setInput(diagram.getRootDiagram().getObjects());
//
//		objects.add(new ObjectInfo("http://object1", "001", "light"));
//		objects.add(new ObjectInfo("http://object2", "002", "air-conditioning"));
//		objects.add(new ObjectInfo("http://object3", "003", "screen"));

		FormData listData = new FormData();
		listData.left = new FormAttachment(addL, 0, SWT.LEFT);
		listData.right = new FormAttachment(addL, 0, SWT.RIGHT);
		listData.top = new FormAttachment(typeText, 30, SWT.BOTTOM);
		objectsListLabel.setLayoutData(listData);

		FormData tableData = new FormData();
		tableData.left = new FormAttachment(addL, 0, SWT.LEFT);
		tableData.right = new FormAttachment(typeText, 0, SWT.RIGHT);
		tableData.top = new FormAttachment(objectsListLabel, 0, SWT.BOTTOM);
		tableData.bottom = new FormAttachment(100);
		table.setLayoutData(tableData);

		FormData delButtonData = new FormData();
//		delButtonData.left = new FormAttachment(table, 0, SWT.RIGHT);
		delButtonData.left = new FormAttachment(addButton, 0, SWT.LEFT);
		delButtonData.right = new FormAttachment(addButton, 0, SWT.RIGHT);
		delButtonData.top = new FormAttachment(objectsListLabel, 0, SWT.BOTTOM);
		delButton.setLayoutData(delButtonData);

		int index = addPage(container);
		setPageText(index, "Objects");
	}
	

//	private void initDiagram() {
//		// TODO Auto-generated method stub
//		IEditorInput input = getEditorInput();
//		try {
//			IFile file = ((IFileEditorInput) input).getFile();
//			ObjectInputStream in = new ObjectInputStream(file.getContents());
//			diagram = (ShapesDiagram) in.readObject();
//			in.close();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	/**
	 * Creates page 0 of the multi-page editor, which contains a text editor.
	 */
	void createPage1() {
		try {
			editor = new ShapesEditor();
			int index = addPage(editor, getEditorInput());
//			editor.setDiagram(diagram.getRootDiagram());
			setPageText(index, "Editor");
			setPartName(getEditorInput().getName());
//			setPageImage(0, getShapesEditor().getEditorInput()
//					.getImageDescriptor().createImage());
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
					"Error creating nested text editor", null, e.getStatus());
		}
	}
	

	private void createPage2() {
		Composite container = new Composite(getContainer(), SWT.NONE);
		container.setLayout(new FormLayout());
		FormData data;
		
		final Label genL = new Label(container, SWT.NONE);
		genL.setText(".owl generation:");
		Button genb = new Button(container, SWT.PUSH);
		genb.setText("Generate");
		genb.addSelectionListener(new GenerateListener());
		
		owlArea = new Text(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		
		final Label consistL = new Label(container, SWT.NONE);
		consistL.setText("Consistency Check:");
		Button check = new Button(container, SWT.PUSH);
		check.setText("Check");
		check.addSelectionListener(new CheckListener());
		
		checkArea = new Text(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		
		data = new FormData();
		data.left = new FormAttachment(1);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(1);
		genL.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(genL, 0, SWT.LEFT);
		data.right = new FormAttachment(90);
		data.top = new FormAttachment(genL, 0, SWT.BOTTOM);
//		data.bottom = new FormAttachment(genL, 300, SWT.BOTTOM);
		data.bottom = new FormAttachment(70);
		owlArea.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(owlArea, 10, SWT.RIGHT);
		data.right = new FormAttachment(genL, 0, SWT.RIGHT);
		data.top = new FormAttachment(genL, 0, SWT.BOTTOM);
//		data.bottom = new FormAttachment(genL, 0, SWT.BOTTOM);
		genb.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(genL, 0, SWT.LEFT);
		data.right = new FormAttachment(genL, 0, SWT.RIGHT);
		data.top = new FormAttachment(owlArea, 30, SWT.BOTTOM);
		consistL.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(owlArea, 0, SWT.LEFT);
		data.right = new FormAttachment(owlArea, 0, SWT.RIGHT);
		data.top = new FormAttachment(consistL, 0, SWT.BOTTOM);
//		data.bottom = new FormAttachment(consistL, 300, SWT.BOTTOM);
		data.bottom = new FormAttachment(100);
		checkArea.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(genb, 0, SWT.LEFT);
		data.right = new FormAttachment(genb, 0, SWT.RIGHT);
		data.top = new FormAttachment(consistL, 0, SWT.BOTTOM);
//		data.bottom = new FormAttachment(genL, 0, SWT.BOTTOM);
		check.setLayoutData(data);
		int index = addPage(container);
		setPageText(index, "OWL Generation");
	}

	void createPage4() {

		Composite container = new Composite(getContainer(), SWT.NONE);
		container.setLayout(new FormLayout());

		FormData data;
		final Label addL = new Label(container, SWT.NONE);
		addL.setText("Add a SWRL Rule");
		ruleText = new Text(container, SWT.BORDER );
		Button addButton = new Button(container, SWT.NONE);
		addButton.setText("add");
		addButton.addSelectionListener(new AddRuleListener());	
		final Label listL = new Label(container, SWT.NONE);
		listL.setText("List of  SWRL Rules");

		Button delButton = new Button(container, SWT.NONE);
		delButton.setText("delete");
		delButton.addSelectionListener(new DelRuleListener());
		
		SWRLViewer = new ListViewer(container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

		SWRLViewer.setContentProvider(new ListContentProvider());
		SWRLViewer.setLabelProvider(new ListLabelProvider());
		
//		SWRLListModel input = new SWRLListModel();
		SWRLViewer.setInput(editor.getDiagram().getRootDiagram().getRules());
//		
//		input.add(new SWRLRule("Room(?r) ¡Ä isOccupied(?r, true) ¡Ä Room_Temperature(?r, ?t) ¡Ä swrlb:greaterThan(?t, 30.0) ¡Ä Air_Condition(?x) ¡Ä  isIn(?x, ?r) ¡ú  isOn(?x, true)"));
//		input.add(new SWRLRule("Room(?r) ¡Ä Room_PersonNum(?r, ?n) ¡Ä swrlb:lessThan(?n, 4) ¡Ä Air_Condition(?x) ¡Ä isIn(?x, ?r) ¡Ä isOn(?x, true) ¡ú  Air_Condition_Temperature(?x, 27)"));
//		input.add(new SWRLRule("Room(?r) ¡Ä Room_PersonNum(?r, ?n) ¡Ä swrlb:greaterThan(?n, 3) ¡Ä Air_Condition(?x) ¡Ä isIn(?x, ?r) ¡Ä isOn(?x, true) ¡ú  Air_Condition_Temperature(?x, 26)"));
		
		data = new FormData();
		data.left = new FormAttachment(1);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(1);
		addL.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(addL, 0, SWT.LEFT);
		data.right = new FormAttachment(90);
		data.top = new FormAttachment(addL, 0, SWT.BOTTOM);
		ruleText.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(ruleText, 10, SWT.RIGHT);
		data.right = new FormAttachment(addL, 0, SWT.RIGHT);
		data.top = new FormAttachment(addL, 0, SWT.BOTTOM);
		addButton.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(addL, 0, SWT.LEFT);
		data.right = new FormAttachment(addL, 0, SWT.RIGHT);
		data.top = new FormAttachment(ruleText, 5, SWT.BOTTOM);
		listL.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(addL, 0, SWT.LEFT);
		data.right = new FormAttachment(ruleText, 0, SWT.RIGHT);
		data.top = new FormAttachment(listL, 0, SWT.BOTTOM);
		data.bottom = new FormAttachment(100);
		SWRLViewer.getList().setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(addButton, 0, SWT.LEFT);
		data.right = new FormAttachment(addButton, 0, SWT.RIGHT);
		data.top = new FormAttachment(listL, 0, SWT.BOTTOM);
		delButton.setLayoutData(data);


		int index = addPage(container);
		setPageText(index, "SWRL Rules");
	}

	void createPage3() {
		Composite container = new Composite(getContainer(), SWT.NONE);
		container.setLayout(new FormLayout());
		FormData data;
		
		final Label genL = new Label(container, SWT.NONE);
		genL.setText("Software System");
		Button genb = new Button(container, SWT.PUSH);
		genb.setText("Generate");
		
		Text systemArea = new Text(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		systemArea.setText("code generation");
		
		final Canvas canvas=new Canvas(container ,SWT.BORDER);
		
		ImageData imageData = new ImageData("D:\\Program Files\\eclipse\\myWorkspace\\OGEditor\\icons\\system.jpg");
		
		Image systemImage = new Image(Display.getDefault(), imageData.scaledTo(imageData.width, imageData.height));
		
		canvas.addPaintListener(new PaintListener(){
			 public void paintControl(final PaintEvent event){
			  Image image=(Image)canvas.getData();
			  if(image!=null){
			   event.gc.drawImage(image,40,10);//¶¨Î»Í¼Ïñ×óÉÏ½Ç¾àcanvas×óÉÏ½ÇµÄ¾àÀë
			  }
			 }
			});
		
		canvas.setData(systemImage);
		canvas.redraw();

		data = new FormData();
		data.left = new FormAttachment(1);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(1);
		genL.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(genL, 0, SWT.LEFT);
		data.right = new FormAttachment(90);
		data.top = new FormAttachment(genL, 0, SWT.BOTTOM);
//		data.bottom = new FormAttachment(genL, 300, SWT.BOTTOM);
		data.bottom = new FormAttachment(70);
		systemArea.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(systemArea, 0, SWT.LEFT);
		data.right = new FormAttachment(systemArea, 0, SWT.RIGHT);
		data.top = new FormAttachment(systemArea, 0, SWT.BOTTOM);
//		data.bottom = new FormAttachment(genL, 300, SWT.BOTTOM);
		data.bottom = new FormAttachment(100);
		canvas.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(systemArea, 10, SWT.RIGHT);
		data.right = new FormAttachment(genL, 0, SWT.RIGHT);
		data.top = new FormAttachment(genL, 0, SWT.BOTTOM);
//		data.bottom = new FormAttachment(genL, 0, SWT.BOTTOM);
		genb.setLayoutData(data);

		int index = addPage(container);
		setPageText(index, "System Generation");
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createPage0();
		createPage1();
		
		objectsViewer.setInput(editor.getDiagram().getRootDiagram().getObjects());

		createPage2();
		createPage3();
	}




	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		getShapesEditor().doSave(monitor);
		try {
			IFileEditorInput input = (IFileEditorInput) editor.getEditorInput();
			IFile file = ((IFileEditorInput) input ).getFile();
			ObjectInputStream in = new ObjectInputStream(file.getContents());
			ShapesDiagram tmpdiagram = (ShapesDiagram) in.readObject();
			in.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the
	 * text for page 0's tab, and updates this multi-page editor's input to
	 * correspond to the nested editor's.
	 */
	public void doSaveAs() {
		IEditorPart editor = getEditor(EDITOR_INDEX);
		editor.doSaveAs();
		setPageText(EDITOR_INDEX, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(EDITOR_INDEX);
		IDE.gotoMarker(getEditor(EDITOR_INDEX), marker);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException(
					"Invalid Input: Must be IFileEditorInput");

		super.init(site, editorInput);
		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener(this);
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);		
	}
	
	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow()
							.getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) editor.getEditorInput())
								.getFile().getProject()
								.equals(event.getResource())) {
							IEditorPart editorPart = pages[i].findEditor(editor
									.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		if (this.equals(getSite().getPage().getActiveEditor())) {
			if (getShapesEditor().equals(getActiveEditor()))
			{
				getShapesEditor()
				.selectionChanged(getActiveEditor(), selection);
				
			}
				
		}
	}

	public ShapesEditor getShapesEditor() {
		// TODO Auto-generated method stub
		return editor;
	}
	
	public Object getAdapter(Class type) {
		if(type == IPropertySheetPage.class || type == IContentOutlinePage.class || type == ZoomManager.class)
			return getShapesEditor().getAdapter(type);
		return super.getAdapter(type);
	}

	public class AddObjectListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			ObjectInfo object = new ObjectInfo(urisText.getText(), rfidText.getText(), typeText.getText());
			editor.getDiagram().getRootDiagram().getObjects().add(object);
			refreshTexts();
			firePropertyChange(PROP_DIRTY);
		}
	}
	
	public class DelObjectListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			int[] indices = objectsViewer.getTable().getSelectionIndices();
			ArrayList<ObjectInfo> dels = new ArrayList<ObjectInfo>();
			for(int index : indices)
			{
				ObjectInfo object = (ObjectInfo) objectsViewer.getElementAt(index);
				dels.add(object);
//				objects.remove(object);
			}
			editor.getDiagram().getRootDiagram().getObjects().removeAll(dels);
			objectsViewer.refresh();
			firePropertyChange(PROP_DIRTY);
		}
	}

	private void refreshTexts() {
		urisText.setText("http://");
		rfidText.setText("");
		typeText.setText("");
	}
	
	private class AddRuleListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			SWRLRule rule = new SWRLRule(ruleText.getText());
			editor.getDiagram().getRootDiagram().getRules().add(rule);
			ruleText.setText("");
			SWRLViewer.refresh();
		}

	}
	
	private class DelRuleListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			int[] indices = SWRLViewer.getList().getSelectionIndices();
			ArrayList<SWRLRule> dels = new ArrayList<SWRLRule>();
			for(int index : indices)
			{
				SWRLRule rule = (SWRLRule) SWRLViewer.getElementAt(index);
				dels.add(rule);
//				objects.remove(object);
			}
			editor.getDiagram().getRootDiagram().getRules().removeAll(dels);
			SWRLViewer.refresh();
		}
	}

	private class GenerateListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			owlArea.setText("");
			String path = "D:\\Program Files\\eclipse\\myWorkspace\\OGEditor\\tmp\\" + editor.getDiagram().getFileName() + ".owl";
			editor.SaveAsOWL(path, "owl");
			File file = new File(path);
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line = new String();
				while(null != (line = br.readLine()))
				{
					owlArea.append(line + "\n");
				}
				fr.close();
				br.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private class CheckListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			checkArea.setText("");
			checkArea.setText("¡­¡­\nConsistency check is over¡­¡­\n");
		}
	}
}
