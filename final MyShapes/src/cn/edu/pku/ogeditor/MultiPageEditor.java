package cn.edu.pku.ogeditor;

import iot.client.ManagerDelegate;
import iot.client.ManagerService;
import iot.client.Value;
import iot.equipment.Equipment;
import iot.reasoner.Reasoner;

import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
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
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException;
import org.semanticweb.owlapi.reasoner.ReasonerInterruptedException;

import cn.edu.pku.ogeditor.display.ViewStatusShell;
import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;
import cn.edu.pku.ogeditor.wizards.ObjectsListModel;
import cn.edu.pku.ogeditor.wizards.TableContentProvider;
import cn.edu.pku.ogeditor.wizards.TableLabelProvider;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

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
		IResourceChangeListener, ISelectionListener{

	private static final int LABEL_LENGTH = 300;
	private static final int EDITOR_INDEX = 1;
	public static final String TEMP_OWLFILe_PATH = "D:\\Program Files (x86)\\eclipse\\myWorkspace\\OGEditor\\tmp\\";
	private LongRunningOperation deployThread;
	private ControllerDialog controllerDialog;
	private ShapesEditor editor;
	private Text wsdlT;
	private Text runningStatusArea;
	private TableViewer viewer;

	private OWLOntologyManager ontManager;

	// private Text rfidText;
	// private Text typeText;
	private TableViewer objectsViewer;
	private Text owlArea;
	private Text checkArea;
	private ProgressBar pb;
	private Font titleFont;
	private Font textFont;

	private Button check;
	private Button deploy;
	private Button stop;
	private Button viewStatus;
	public DeployThead deployTh;

	private boolean checkFinish;
	public boolean deployFinish;

	/**
	 * Creates a multi-page editor example.
	 */
	public MultiPageEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);

	}

	@Override
	protected void initializePageSwitching() {
		// TODO Auto-generated method stub
		super.initializePageSwitching();
	}
	

	@Override
	protected void handlePropertyChange(int propertyId) {
		// TODO Auto-generated method stub
		super.handlePropertyChange(propertyId);
	}


	private void initDB() {
		if (null != editor.getDiagram().getRootDiagram().getObjects()) {
			objectsViewer.setInput(editor.getDiagram().getRootDiagram()
					.getObjects());
			wsdlT.setText(editor.getDiagram().getRootDiagram().getWsdlUri());
			objectsViewer.refresh();
			return;
		}
		// ManagerService server = new ManagerService();
		// ManagerDelegate md = server.getManagerPort();
		//
		// List<Value> list = md.getAllProperties();
		// ObjectsListModel objModel= new ObjectsListModel();
		// for(Value tmp : list)
		// {
		// System.out.println(tmp.getEType()+" "+tmp.getEquipment()+" "+tmp.getProperty()+" "+tmp.getValue());
		// objModel.add(tmp);
		// }
		//
		// editor.getDiagram().getRootDiagram().setObjects(objModel);
		// objectsViewer.setInput(editor.getDiagram().getRootDiagram().getObjects());
		// wsdlT.setText(editor.getDiagram().getRootDiagram().getWsdlUri());
		editor.getDiagram().getRootDiagram().setWsdlUri(wsdlT.getText());
	}

	void createPage0() {
		// initDiagram();

		Composite container = new Composite(getContainer(), SWT.NONE);
		container.setLayout(new FormLayout());
		Display display = Display.getDefault();

		titleFont = new Font(display, "Arial", 12, SWT.BOLD);
		textFont = new Font(display, "Cambria", 12, SWT.NORMAL);

		Button next = new Button(container, SWT.PUSH);
		next.setText("Next >>");
		next.setFont(titleFont);
		next.addSelectionListener(new NextListener());

		final Label wsL = new Label(container, SWT.NONE);
		wsL.setText("Web Service Location:");
		wsL.setFont(titleFont);

		final Label wsdlL = new Label(container, SWT.BORDER);
		wsdlL.setText("WSDL URI:");
		wsdlL.setFont(textFont);

		wsdlT = new Text(container, SWT.BORDER);
		wsdlT.setText("http://");

		Button importButton = new Button(container, SWT.NONE);
		importButton.setText("Import");
		importButton.setFont(titleFont);
		importButton.addSelectionListener(new ImportWSDLListener());

		FormData data;

		data = new FormData();
		data.left = new FormAttachment(90);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(1);
		next.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(1);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(next, 10, SWT.BOTTOM);
		wsL.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(wsL, 0, SWT.LEFT);
		data.right = new FormAttachment(next, -10, SWT.LEFT);
		data.top = new FormAttachment(wsL, 0, SWT.BOTTOM);
		wsdlL.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(wsdlL, 0, SWT.LEFT);
		data.right = new FormAttachment(wsdlL, 0, SWT.RIGHT);
		data.top = new FormAttachment(wsdlL, 0, SWT.BOTTOM);
		wsdlT.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(next, 0, SWT.LEFT);
		data.right = new FormAttachment(next, 0, SWT.RIGHT);
		data.top = new FormAttachment(wsL, 0, SWT.BOTTOM);
		importButton.setLayoutData(data);

		final Label objectsListLabel = new Label(container, SWT.NONE);
		objectsListLabel.setText("Properties of Devices and Sensors:");
		objectsListLabel.setFont(titleFont);

		Button refButton = new Button(container, SWT.NONE);
		refButton.setText("Refresh");
		refButton.setFont(titleFont);
		refButton.addSelectionListener(new ImportWSDLListener());

		Table table = new Table(container, SWT.BORDER | SWT.MULTI
				| SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table.setFont(textFont);
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("Equipment");
		column1.setWidth(LABEL_LENGTH);
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setWidth(LABEL_LENGTH);
		column2.setText("Property");
		TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setWidth(LABEL_LENGTH);
		column3.setText("Value");
		
		TableColumn column4 = new TableColumn(table, SWT.NONE);
		column4.setWidth(LABEL_LENGTH);
		column4.setText("Value Type");
		
		TableColumn column5 = new TableColumn(table, SWT.NONE);
		column5.setWidth(LABEL_LENGTH);
		column5.setText("Equipment Type");
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		objectsViewer = new TableViewer(table);
		objectsViewer.setContentProvider(new TableContentProvider());
		objectsViewer.setLabelProvider(new TableLabelProvider());

		FormData listData = new FormData();
		listData.left = new FormAttachment(wsL, 0, SWT.LEFT);
		listData.right = new FormAttachment(wsL, 0, SWT.RIGHT);
		listData.top = new FormAttachment(wsdlT, 30, SWT.BOTTOM);
		objectsListLabel.setLayoutData(listData);

		FormData tableData = new FormData();
		tableData.left = new FormAttachment(wsL, 0, SWT.LEFT);
		tableData.right = new FormAttachment(wsdlT, 0, SWT.RIGHT);
		tableData.top = new FormAttachment(objectsListLabel, 0, SWT.BOTTOM);
		tableData.bottom = new FormAttachment(100);
		table.setLayoutData(tableData);

		FormData refButtonData = new FormData();
		// delButtonData.left = new FormAttachment(table, 0, SWT.RIGHT);
		refButtonData.left = new FormAttachment(importButton, 0, SWT.LEFT);
		refButtonData.right = new FormAttachment(importButton, 0, SWT.RIGHT);
		refButtonData.top = new FormAttachment(objectsListLabel, 0, SWT.BOTTOM);
		refButton.setLayoutData(refButtonData);

		int index = addPage(container);
		setPageText(index, "Collecting Devices and Sensors");

	}

	void createPage1() {
		try {
			editor = new ShapesEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, "Defining Control Rules");
			setPartName(getEditorInput().getName());
			// setPageImage(0, getShapesEditor().getEditorInput()
			// .getImageDescriptor().createImage());
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(),
					"Error creating nested text editor", null, e.getStatus());
		}
	}

	private void createPage2() {
		Composite container = new Composite(getContainer(), SWT.NONE);
		container.setLayout(new FormLayout());
		FormData data;

		Button pre = new Button(container, SWT.PUSH);
		pre.setText("Back");
		pre.setFont(titleFont);
		pre.addSelectionListener(new PreListener());

		final Label genL = new Label(container, SWT.NONE);
		genL.setText(".OWL Generation:");
		genL.setFont(titleFont);
		Button genb = new Button(container, SWT.PUSH);
		genb.setText("Generate");
		genb.setFont(titleFont);
		genb.addSelectionListener(new GenerateListener());

		owlArea = new Text(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		owlArea.setFont(textFont);

		final Label consistL = new Label(container, SWT.NONE);
		consistL.setText("Checking Correctness");
		consistL.setFont(titleFont);
		check = new Button(container, SWT.PUSH);
		check.setText("Check");
		check.setFont(titleFont);
		check.addSelectionListener(new CheckListener());
		check.setEnabled(false);

		// Button = new Button(container, SWT.PUSH);
		// check.setText("Check");
		// check.setFont(titleFont);
		// check.addSelectionListener(new CheckListener());
		// check.setEnabled(false);

		checkArea = new Text(container, SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.BORDER);
		checkArea.setFont(textFont);

		deploy = new Button(container, SWT.PUSH);
		deploy.setText("Deploy");
		deploy.setFont(titleFont);
		deploy.addSelectionListener(new DeployListener());
		deploy.setEnabled(false);

		stop = new Button(container, SWT.PUSH);
		stop.setText("Stop");
		stop.setFont(titleFont);
		stop.addSelectionListener(new StopListener());
		stop.setEnabled(false);

		viewStatus = new Button(container, SWT.PUSH);
		viewStatus.setText("View Status");
		viewStatus.setFont(titleFont);
		viewStatus.addSelectionListener(new ViewStatusListener());
		viewStatus.setEnabled(false);

		pb = new ProgressBar(container, SWT.HORIZONTAL | SWT.SMOOTH);
		pb.setMinimum(0);
		pb.setMaximum(0);



		data = new FormData();
		data.left = new FormAttachment(1);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(2);
		genL.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(genL, 0, SWT.LEFT);
		data.right = new FormAttachment(90);
		data.top = new FormAttachment(genL, 0, SWT.BOTTOM);
		// data.bottom = new FormAttachment(genL, 300, SWT.BOTTOM);
		data.bottom = new FormAttachment(60);
		owlArea.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(owlArea, 10, SWT.RIGHT);
		data.right = new FormAttachment(genL, 0, SWT.RIGHT);
		data.top = new FormAttachment(genL, 0, SWT.BOTTOM);
		// data.bottom = new FormAttachment(genL, 0, SWT.BOTTOM);
		genb.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(genL, 0, SWT.LEFT);
		data.right = new FormAttachment(genL, 0, SWT.RIGHT);
		data.top = new FormAttachment(owlArea, 20, SWT.BOTTOM);
		consistL.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(owlArea, 0, SWT.LEFT);
		data.right = new FormAttachment(owlArea, 0, SWT.RIGHT);
		data.top = new FormAttachment(consistL, 0, SWT.BOTTOM);
		data.bottom = new FormAttachment(87);
		checkArea.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(genb, 0, SWT.LEFT);
		data.right = new FormAttachment(genb, 0, SWT.RIGHT);
		data.top = new FormAttachment(consistL, 0, SWT.BOTTOM);
		check.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(check, 0, SWT.LEFT);
		data.right = new FormAttachment(check, 0, SWT.RIGHT);
		data.top = new FormAttachment(check, 0, SWT.BOTTOM);
		pre.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(50, -300);
		data.right = new FormAttachment(50, -100);
		data.top = new FormAttachment(checkArea, 15, SWT.BOTTOM);
		deploy.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(50, -100);
		data.right = new FormAttachment(50, 100);
		data.top = new FormAttachment(deploy, 0, SWT.TOP);
		stop.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(50, 100);
		data.right = new FormAttachment(50, 300);
		data.top = new FormAttachment(deploy, 0, SWT.TOP);
		viewStatus.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(genL, 0, SWT.LEFT);
		data.right = new FormAttachment(genL, 0, SWT.RIGHT);
		data.top = new FormAttachment(deploy, 5, SWT.BOTTOM);
		data.bottom = new FormAttachment(99);
		pb.setLayoutData(data);

		int index = addPage(container);
		setPageText(index, "Checking Correctness");
	}

	void createPage3() {
		Composite container = new Composite(getContainer(), SWT.NONE);
		container.setLayout(new FormLayout());
		FormData data;

		final Label genL = new Label(container, SWT.NONE);
		genL.setText("Software System");
		Button genb = new Button(container, SWT.PUSH);
		genb.setText("Generate");

		Text systemArea = new Text(container, SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.BORDER);
		systemArea.setText("code generation");

		final Canvas canvas = new Canvas(container, SWT.BORDER);

		ImageData imageData = new ImageData(
				"D:\\Program Files\\eclipse\\myWorkspace\\OGEditor\\icons\\system.jpg");

		Image systemImage = new Image(Display.getDefault(), imageData.scaledTo(
				imageData.width, imageData.height));

		canvas.addPaintListener(new PaintListener() {
			public void paintControl(final PaintEvent event) {
				Image image = (Image) canvas.getData();
				if (image != null) {
					event.gc.drawImage(image, 40, 10);// 定位图像左上角距canvas左上角的距离
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
		// data.bottom = new FormAttachment(genL, 300, SWT.BOTTOM);
		data.bottom = new FormAttachment(70);
		systemArea.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(systemArea, 0, SWT.LEFT);
		data.right = new FormAttachment(systemArea, 0, SWT.RIGHT);
		data.top = new FormAttachment(systemArea, 0, SWT.BOTTOM);
		// data.bottom = new FormAttachment(genL, 300, SWT.BOTTOM);
		data.bottom = new FormAttachment(100);
		canvas.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(systemArea, 10, SWT.RIGHT);
		data.right = new FormAttachment(genL, 0, SWT.RIGHT);
		data.top = new FormAttachment(genL, 0, SWT.BOTTOM);
		// data.bottom = new FormAttachment(genL, 0, SWT.BOTTOM);
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

		createPage2();
		initDB();
		wsdlT.setText(editor.getDiagram().getRootDiagram().getWsdlUri());

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
//		if (this.equals(getSite().getPage().getActiveEditor())) {
//			if (getShapesEditor().equals(getActiveEditor())) {
//				getShapesEditor()
//						.selectionChanged(getActiveEditor(), selection);
//
//			}
//
//		}
		if(this.getActivePage() == 1)
		{
			editor.selectionChanged(part, selection);
//			editor.getPropertySheet().selectionChanged(part, selection);
		}
		
	}

	public ShapesEditor getShapesEditor() {
		// TODO Auto-generated method stub
		return editor;
	}

	public Object getAdapter(Class type) {
		if (type == IPropertySheetPage.class
				|| type == IContentOutlinePage.class
				|| type == ZoomManager.class)
			return getShapesEditor().getAdapter(type);
		return super.getAdapter(type);
	}

	public class ImportWSDLListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (!wsdlT.getText().equals(ShapesDiagram.STAND_WSDL)) {
				editor.getDiagram().getRootDiagram().setObjects(null);
				editor.getDiagram().getRootDiagram()
						.setWsdlUri(wsdlT.getText());
				objectsViewer.setInput(editor.getDiagram().getRootDiagram()
						.getObjects());
				objectsViewer.refresh();
				return;
			}
			ManagerService server = new ManagerService();
			ManagerDelegate md = server.getManagerPort();
			// Manager m = gson.fromJson(md.getManager(), Manager.class);

			// Value v = md.getProperty("a1", "temperature");
			// if(null != v)
			// System.out.println(v.getValue());
			List<Value> list = md.getAllProperties();
			ObjectsListModel objModel = new ObjectsListModel();
			for (Value tmp : list) {
				System.out.println(tmp.getEquipment() + " " + tmp.getProperty()
						+ " " + tmp.getValue());
				objModel.add(tmp);
			}

			editor.getDiagram().getRootDiagram().setObjects(objModel);
			editor.getDiagram().getRootDiagram().setWsdlUri(wsdlT.getText());
			objectsViewer.setInput(editor.getDiagram().getRootDiagram()
					.getObjects());
			objectsViewer.refresh();
			firePropertyChange(PROP_DIRTY);
		}

	}

	public class RefreshTableListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (!wsdlT.getText().equals(ShapesDiagram.STAND_WSDL)) {
				editor.getDiagram().getRootDiagram().setObjects(null);
				editor.getDiagram().getRootDiagram()
						.setWsdlUri(wsdlT.getText());
				objectsViewer.setInput(editor.getDiagram().getRootDiagram()
						.getObjects());
				objectsViewer.refresh();
				return;
			}
			ManagerService server = new ManagerService();
			ManagerDelegate md = server.getManagerPort();
			// Manager m = gson.fromJson(md.getManager(), Manager.class);

			// Value v = md.getProperty("a1", "temperature");
			// if(null != v)
			// System.out.println(v.getValue());
			List<Value> list = md.getAllProperties();
			ObjectsListModel objModel = new ObjectsListModel();
			for (Value tmp : list) {
				System.out.println(tmp.getEquipment() + " " + tmp.getProperty()
						+ " " + tmp.getValue());
				objModel.add(tmp);
			}

			editor.getDiagram().getRootDiagram().setObjects(objModel);
			editor.getDiagram().getRootDiagram().setWsdlUri(wsdlT.getText());
			objectsViewer.setInput(editor.getDiagram().getRootDiagram()
					.getObjects());
			objectsViewer.refresh();
			firePropertyChange(PROP_DIRTY);
		}
	}

	private class GenerateListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			owlArea.setText("");
			String path = "D:\\Program Files (x86)\\eclipse\\myWorkspace\\OGEditor\\tmp\\"
					+ editor.getDiagram().getFileName() + ".owl";
			path = "./tmp/";
			File file = new File(path);
			if (!file.exists())
				file.mkdirs();

			String fileName = editor.getDiagram().getFileName() + ".owl";
			path += fileName;
			file = new File(path);
			if (!file.exists())
				try {
					file.createNewFile();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			editor.SaveAsOWL(path, "owl");

			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line = new String();
				while (null != (line = br.readLine())) {
					owlArea.append(line + "\n");
				}
				fr.close();
				br.close();
				check.setEnabled(true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	private class CheckListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
//			String path = "D:\\Program Files (x86)\\eclipse\\myWorkspace\\OGEditor\\tmp\\"
//					+ editor.getDiagram().getFileName() + ".owl";
//			path = "tmp\\";

			// String fileName = editor.getDiagram().getFileName() + ".owl";
			// path += fileName;

			String path = "D:\\Program Files (x86)\\eclipse\\tmp/room.owl";
			checkFinish = false;

			CheckThread c = new CheckThread(path);

			CheckDisplayThread d = new CheckDisplayThread();

			checkArea.append(">Checking ");
			c.start();
			d.start();

			// else if check not ok, sysout something
		}

		private class CheckDisplayThread extends Thread {

			@Override
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						while (!checkFinish) {
							checkArea.append(". ");
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						checkArea.append("\n");
					}
				});
				// TODO Auto-generated method stub

			}

		}

		private class CheckThread extends Thread {
			private String path;

			public CheckThread(String path) {
				// TODO Auto-generated constructor stub
				this.path = path;
			}

			@Override
			public void run() {
				try {
					ontManager = Reasoner.testConsistent(path);

				} catch (InconsistentOntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ReasonerInterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OWLOntologyCreationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				checkFinish = true;

				Display.getDefault().asyncExec(new Runnable() {
					public void run() {

						if (null == ontManager) {
							System.out.println("\nERROR: inconsistent");
							checkArea
									.append("\n>Correctness check has been completed.\n");
							checkArea
									.append(">The Ontology is inconsistent.\n");

							deploy.setEnabled(false);
							stop.setEnabled(false);
							viewStatus.setEnabled(false);
						} else {
							checkArea
									.append(">Correctness check has been completed.\n");
							checkArea.append(">The Ontology is consistent.\n");

							deploy.setEnabled(true);
							stop.setEnabled(true);
							viewStatus.setEnabled(true);
						}

					}
				});
			}
		}

	}

	private class DeployListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
//			String path = "D:\\Program Files\\eclipse\\myWorkspace\\OGEditor\\tmp\\"
//					+ editor.getDiagram().getFileName() + ".owl";
//			// path = "tmp\\" +
//			// editor.getDiagram().getRootDiagram().getFileName() + ".owl";
//			path = "tmp\\room.owl";
			if (null != deployTh)
				deployTh.stop();
			deployTh = new DeployThead();
			deployFinish = false;
			deployTh.start();
			// if(!canDeploy(file))
			// return;

			pb.setSelection(0);

			while (!deployFinish && pb.getSelection() < 90)
				pb.setSelection(pb.getSelection() + 1);
			pb.setSelection(100);
			// deployThread = new LongRunningOperation(Display.getCurrent(),
			// pb);
			// deployThread.start();
		}

		// private boolean canDeploy(File file) {
		// if (!file.exists()) {
		// MessageDialog.openError(Display.getDefault().getActiveShell(),
		// "Deploy Error", "OWL file hasn't been generated!");
		// return false;
		// }
		// if (checkArea.getText().equals("")) {
		// MessageDialog.openError(Display.getDefault().getActiveShell(),
		// "Deploy Error",
		// "Consistency check hasn't been completed!");
		// return false;
		// }
		// return true;
		// }
	}

	private class DeployThead extends Thread {

		// private OWLOntologyManager man;
		// private Object[] values;
		private ManagerDelegate md;
		private OWLOntology ont;
		private PelletReasoner reasoner;
		private ObjectsListModel objModel;

		public DeployThead() {

			if (null == ontManager)
				return;
			ont = ontManager.getOntology(IRI
					.create("http://www.owl-ontologies.com/Ontology_IOT.owl"));
			if (null == ont)
				return;

			ManagerService server = new ManagerService();
			md = server.getManagerPort();
			objModel = editor.getDiagram().getRootDiagram().getObjects();

			reasoner = PelletReasonerFactory.getInstance().createReasoner(ont);
		}

		@Override
		public void run() {
			if (null == ontManager || null == ont)
				return;
			try {
				OWLDataFactory dfac = ontManager.getOWLDataFactory();
				IRI docIRI = ontManager.getOntologyDocumentIRI(ont);
				String prefix = ont.getOntologyID().getOntologyIRI() + "#";
				System.out.println("\ndocIRI: " + docIRI + "\nprefix: "
						+ prefix);
				deployFinish = true;
				while (deployFinish) {
					// >detecting objects' states ...
					// >initializing instances ...
					// >commiting to reasoner ...
					// >carrying out inference ...
					// >returning inferred results ...
					// >resetting devices' states ...
					Display display= Display.getDefault();
					display.asyncExec(new Runnable() {
						public void run() {
							if (runningStatusArea != null
									&& !runningStatusArea.isDisposed())
								runningStatusArea
										.append(">detecting objects' states ...\n");
						}
					});
					sleep(1000);
							
					display.asyncExec(new Runnable() {
						public void run() {
							if (runningStatusArea != null
									&& !runningStatusArea.isDisposed())
								runningStatusArea
										.append(">initializing instances ...\n");
						}
					});
					sleep(1000);
					
					display.asyncExec(new Runnable() {
						public void run() {
							if (runningStatusArea != null
									&& !runningStatusArea.isDisposed())
								runningStatusArea
										.append(">commiting to reasoner ...\n");
						}
					});
					sleep(1000);
					
					display.asyncExec(new Runnable() {
						public void run() {
							if (runningStatusArea != null
									&& !runningStatusArea.isDisposed())
								runningStatusArea
										.append(">carrying out inference ...\n");
						}
					});
					sleep(1000);

					reasoner.refresh();
					reasoner.getKB().realize();
					
					
					Object[] values = objModel.elements();

					display.asyncExec(new Runnable() {
						public void run() {
							if (runningStatusArea != null
									&& !runningStatusArea.isDisposed())
								runningStatusArea
										.append(">returning inferred results ...\n");
						}
					});
					sleep(1000);
					
					display.asyncExec(new Runnable() {
						public void run() {
							if (runningStatusArea != null
									&& !runningStatusArea.isDisposed())
								runningStatusArea
										.append(">resetting devices' states ...\n");
						}
					});
					sleep(1000);


					for (Object o : values) {
						Value cur = (Value) o;
						String e = cur.getEquipment();
						String p = cur.getProperty();

						if (null == p || p.equals(""))
							continue;

						OWLNamedIndividual ind = dfac.getOWLNamedIndividual(IRI
								.create(prefix + e));
						OWLDataProperty prop = dfac.getOWLDataProperty(IRI
								.create(prefix + p));


						System.out.println("\n" + e + " " + p + ": ##########################################");
						
						
						Set<OWLLiteral> infer = getNewValue(ont, ind, prop,
								reasoner);
						
						
						System.out.println("\nend" + e + " " + p + ": ##########################################\n\n");

						if (!infer.isEmpty()) {
							Set<OWLLiteral> t = ind.getDataPropertyValues(prop,
									ont);
							System.out.println("begin old value: ---------------------------------------------------------");
							for (Iterator<OWLLiteral> it = t.iterator(); it
									.hasNext();) {
								OWLLiteral l = it.next();
								System.out.println(e + " " + p + " " + l.getLiteral());
								OWLAxiom remove = dfac
										.getOWLDataPropertyAssertionAxiom(prop,
												ind, l);
								ontManager.removeAxiom(ont, remove);
								
							}
							System.out.println("end old value: ---------------------------------------------------------");

							System.out.println("begin new value: ---------------------------------------------------------");

							Iterator<OWLLiteral> iter = infer.iterator();
							while(iter.hasNext()) {
								
								OWLLiteral li = iter.next();
								System.out.println(e + " " + p + " " + li.getLiteral());
								ontManager.applyChange(new AddAxiom(ont, dfac
										.getOWLDataPropertyAssertionAxiom(prop,
												ind, li)));
								
								
								md.setProperty(e, p, li.getLiteral());
								
								cur.setValue(li.getLiteral());
								objModel.add(cur);
							}
						}

					}
					if(viewer != null )	
					{
						display.asyncExec(new Runnable() {
							public void run() {
							viewer.refresh();
							}
						});
					}
					objModel.toStream();
					sleep(3000);
				}

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("Error in DeployListener!");
				return;
			}

		}

		public Set<OWLLiteral> getNewValue(OWLOntology ont,
				OWLNamedIndividual ind, OWLDataProperty dpro,
				PelletReasoner reasoner) {
			// System.out.print("\n" + ind.getIRI().getFragment() + "--"
			// + dpro.getIRI().getFragment() + ":");

			Set<OWLLiteral> origin = ind.getDataPropertyValues(dpro, ont);
			Set<OWLLiteral> infer = reasoner.getDataPropertyValues(ind, dpro);
			System.out.println("before remove:--------------------------------------------------------");
			System.out.println(infer);

			for (Iterator<OWLLiteral> it = origin.iterator(); it.hasNext();) {
				OWLLiteral temp = it.next();
//				temp.getLiteral();
				if (infer.contains(temp)) {
					infer.remove(temp);
				}
			}
			System.out.println("end before remove:--------------------------------------------------------");

			System.out.println("after remove:--------------------------------------------------------");
			System.out.println(infer);
			System.out.println("end after remove:--------------------------------------------------------");

			return infer;

		}

		// private Value getWebServiceValue(String e, String p) {
		// // TODO Auto-generated method stub
		// ManagerService server = new ManagerService();
		// ManagerDelegate md = server.getManagerPort();
		// // Manager m = gson.fromJson(md.getManager(), Manager.class);
		//
		// Value v = md.getProperty(e, p);
		// return v;
		// }
	}

	private class StopListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (null != deployThread && deployThread.isAlive())
				deployThread.stop();
			deployFinish = false;

			pb.setSelection(0);
		}
	}

	private class ViewStatusListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {

			if (pb.getSelection() < 100) {
				MessageDialog.openError(Display.getDefault().getActiveShell(),
						"View Status Error",
						"System hasn't complete deployment");
				return;
			}
			String path = "tmp/output.txt";
			new ViewStatusShell(path, ontManager);

			// application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// pb.setSelection(0);
			// new LongRunningOperation(Display.getCurrent(), pb).start();
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			controllerDialog = new ControllerDialog(shell, editor);
			controllerDialog.open();
			viewer = null;
			controllerDialog.close();
		}
	}

	public class ControllerDialog extends Dialog {

		private boolean isRunning;
		
		private ShapesEditor editor;
		private StatusDisplay statusThread;
		private Font titleFont;
		public static final String START_MESS = ">control system start ...";
		public final String[] STATUS = { ">detecting objects' states ...",
				">initializing instances ...", ">commiting to reasoner ...",
				">carrying out inference ...",
				">returning inferred results ...",
				">resetting devices' states ..." };
		private static final int LABEL_LENGTH = 150;

		public ControllerDialog(Shell shell, ShapesEditor editor) {
			super(shell);
			setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
			this.editor = editor;
			// this.runningStatusArea = text;
		}

		@Override
		protected void configureShell(Shell newShell) {
			// TODO Auto-generated method stub
			setRunning(true);
			super.configureShell(newShell);
			newShell.setText("Knowledge Reasoning Part Deployment");
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Composite container = (Composite) super.createDialogArea(parent);
			container.setLayout(new FormLayout());
			FormData data;

			Display display = Display.getDefault();
			Font bigTitleFont = new Font(display, "Arial", 12, SWT.BOLD);
			titleFont = new Font(display, "Arial", 10, SWT.BOLD);

			Font textFont = new Font(display, "Cambria", 10, SWT.NORMAL);

			final Label conL = new Label(container, SWT.SHADOW_OUT | SWT.CENTER);
			conL.setText("Device Control System");
			conL.setFont(bigTitleFont);

			final Label runningStatusL = new Label(container, SWT.NONE);
			runningStatusL.setText("Running status");
			runningStatusL.setFont(titleFont);

			runningStatusArea = new Text(container, SWT.V_SCROLL | SWT.H_SCROLL
					| SWT.BORDER);
			runningStatusArea.setText(START_MESS + "\n");
			runningStatusArea.setFont(textFont);

			Button start = new Button(container, SWT.PUSH);
			start.setText("Start");
			start.setFont(titleFont);
			start.addSelectionListener(new StartListener());

			Button pause = new Button(container, SWT.PUSH);
			pause.setText("Pause");
			pause.setFont(titleFont);
			pause.addSelectionListener(new PauseListener());

			Button clear = new Button(container, SWT.PUSH);
			clear.setText("Clear");
			clear.setFont(titleFont);
			clear.addSelectionListener(new ClearListener());

			final Label DeviceListL = new Label(container, SWT.NONE);
			DeviceListL.setText("Properties of Devices and Sensors:");
			DeviceListL.setFont(titleFont);

			Table table = new Table(container, SWT.BORDER | SWT.MULTI
					| SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
			table.setFont(textFont);

			TableColumn column1 = new TableColumn(table, SWT.NONE);
			column1.setText("Equipment");
			column1.setWidth(LABEL_LENGTH);
			TableColumn column2 = new TableColumn(table, SWT.NONE);
			column2.setWidth(LABEL_LENGTH);
			column2.setText("Property");
			TableColumn column3 = new TableColumn(table, SWT.NONE);
			column3.setWidth(LABEL_LENGTH);
			column3.setText("Value");
			
			TableColumn column4 = new TableColumn(table, SWT.NONE);
			column4.setWidth(LABEL_LENGTH);
			column4.setText("Value Type");
			
			TableColumn column5 = new TableColumn(table, SWT.NONE);
			column5.setWidth(LABEL_LENGTH);
			column5.setText("Equipment Type");

			table.setHeaderVisible(true);
			table.setLinesVisible(true);

			viewer = new TableViewer(table);
			viewer.setContentProvider(new TableContentProvider());
			viewer.setLabelProvider(new TableLabelProvider());

			// objects = new ObjectsListModel();
			viewer.setInput(editor.getDiagram().getRootDiagram().getObjects());

			// ObjectsListModel objects =
			// editor.getDiagram().getRootDiagram().getObjects();
			// Object[] all = objects.elements();
			// for(Object ob : all)
			// {
			// ObjectInfo object = (ObjectInfo)ob;
			// if(Integer.parseInt(object.getRfid()) <= 14533 ||
			// Integer.parseInt(object.getRfid()) >= 14543)
			// object.setOn(true);
			// }
			viewer.refresh();

			data = new FormData();
			data.left = new FormAttachment(1);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(1, 10);
			conL.setLayoutData(data);

			data = new FormData();
			data.left = new FormAttachment(conL, 0, SWT.LEFT);
			data.right = new FormAttachment(conL, -100, SWT.RIGHT);
			data.top = new FormAttachment(conL, 10, SWT.BOTTOM);
			runningStatusL.setLayoutData(data);

			data = new FormData();
			data.left = new FormAttachment(runningStatusL, 0, SWT.LEFT);
			data.right = new FormAttachment(runningStatusL, 0, SWT.RIGHT);
			data.top = new FormAttachment(runningStatusL, 0, SWT.BOTTOM);
			data.bottom = new FormAttachment(60);
			runningStatusArea.setLayoutData(data);

			data = new FormData();
			data.left = new FormAttachment(runningStatusArea, 10, SWT.RIGHT);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(runningStatusL, 0, SWT.BOTTOM);
			start.setLayoutData(data);

			data = new FormData();
			data.left = new FormAttachment(runningStatusArea, 10, SWT.RIGHT);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(start, 0, SWT.BOTTOM);
			pause.setLayoutData(data);

			data = new FormData();
			data.left = new FormAttachment(runningStatusArea, 10, SWT.RIGHT);
			data.right = new FormAttachment(100);
			data.top = new FormAttachment(pause, 0, SWT.BOTTOM);
			clear.setLayoutData(data);

			data = new FormData();
			data.left = new FormAttachment(runningStatusL, 0, SWT.LEFT);
			data.right = new FormAttachment(runningStatusL, 0, SWT.RIGHT);
			data.top = new FormAttachment(runningStatusArea, 20, SWT.BOTTOM);
			DeviceListL.setLayoutData(data);

			FormData tableData = new FormData();
			tableData.left = new FormAttachment(DeviceListL, 0, SWT.LEFT);
			tableData.right = new FormAttachment(DeviceListL, 0, SWT.RIGHT);
			tableData.top = new FormAttachment(DeviceListL, 0, SWT.BOTTOM);
			tableData.bottom = new FormAttachment(100);
			table.setLayoutData(tableData);

			// statusThread = new StatusDisplay(Display.getCurrent());
			// statusThread.start();

			return container;
		}
		

		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			createButton(parent, IDialogConstants.OK_ID, "close", true)
					.setFont(titleFont);
		}

		@Override
		protected void initializeBounds() {
			Shell shell = getShell();
			java.awt.Dimension scrSize = Toolkit.getDefaultToolkit()
					.getScreenSize();
			Insets scrInsets = Toolkit.getDefaultToolkit()
					.getScreenInsets(
							GraphicsEnvironment.getLocalGraphicsEnvironment()
									.getDefaultScreenDevice()
									.getDefaultConfiguration());
			shell.setBounds(scrInsets.left + 1500, scrInsets.top,
					scrSize.width - scrInsets.left - scrInsets.right - 1200,
					scrSize.height - scrInsets.top - scrInsets.bottom - 200);
			super.initializeBounds();
		}

		public void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}

		public boolean isRunning() {
			return isRunning;
		}

		private class StartListener extends SelectionAdapter {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setRunning(false);
				while (statusThread.isAlive()) {
					statusThread.stop();
				}
				setRunning(true);
				statusThread = new StatusDisplay(Display.getCurrent());
				statusThread.start();
			}
		}

		private class PauseListener extends SelectionAdapter {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setRunning(false);
			}
		}

		private class ClearListener extends SelectionAdapter {

			@Override
			public void widgetSelected(SelectionEvent e) {
				runningStatusArea.setText("");
			}
		}

		class StatusDisplay extends Thread {
			private Display display;
			private int count;

			public StatusDisplay(Display display) {
				this.display = display;
				count = -1;
			}

			public void run() {
				// 模仿长时间的任务
				while (isRunning()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count++;
					if (count >= STATUS.length) {
						count = 0;
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					display.asyncExec(new Runnable() {
						public void run() {
							if (runningStatusArea.isDisposed()) {
								setRunning(false);
								return;
							}
							runningStatusArea.append(STATUS[count] + "\n");
						}
					});
				}
			}
		}
	}

	class LongRunningOperation extends Thread {
		private Display display;
		private ProgressBar progressBar;

		public LongRunningOperation(Display display, ProgressBar progressBar) {
			this.display = display;
			this.progressBar = progressBar;
		}

		public void run() {
			// 模仿长时间的任务
			for (int i = 0; i < 100; i++) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
				display.asyncExec(new Runnable() {
					public void run() {
						if (progressBar.isDisposed())
							return;
						// 进度条递增
						progressBar.setSelection(progressBar.getSelection() + 1);
					}
				});
			}
		}
	}

	private class PreListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			setActivePage(getActivePage() - 1);
		}
	}

	private class NextListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			setActivePage(getActivePage() + 1);
			initDiagram();
			editor.getDiagram().getRootDiagram().fireRelocate();
		}
	}

	private void initDiagram() {
		Shape rootShape = new Shape();
		rootShape.setRoot(true); // Thing为根，但从未在Diagram里创建
		rootShape.setName("Thing");
		rootShape.setColor(ColorConstants.orange.getRGB());

		Connection rootConnection = new Connection("ConnectionRoot");
		rootConnection.setName("Relation");

		ShapesDiagram diagram = editor.getDiagram().getRootDiagram();
		ObjectsListModel objectsModel = diagram.getObjects();
		if (null == objectsModel)
			return;
		HashMap<String, ArrayList<Equipment>> allEquipments = objectsModel
				.getEquipments();
		Iterator<Entry<String, ArrayList<Equipment>>> iter = allEquipments
				.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, ArrayList<Equipment>> curEType = iter.next();
			String type = curEType.getKey();
			Shape parentShape = diagram.getShapeByName(type);
			if (null == parentShape) {
				try {
					parentShape = Shape.class.newInstance();
					parentShape.setName(type);
					createShape(parentShape, rootShape, diagram);

					ArrayList<Equipment> curEList = curEType.getValue();
					for (Equipment e : curEList) {
						Shape shape = Shape.class.newInstance();
						shape.setName(e.getName());
						shape.setClass(false);
						shape.setInstanceType(parentShape);
						createShape(shape, rootShape, diagram);
						Connection connection = new Connection(shape,
								parentShape, "instanceOf");

						connection.setParent(rootConnection);
						rootConnection.addChild(connection);
						shape.getDiagram().addConnection(connection);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {

					ArrayList<Equipment> curEList = curEType.getValue();
					for (Equipment e : curEList) {
						Shape shape = diagram.getShapeByName(e.getName());
						if (null != shape)
							continue;
						shape = Shape.class.newInstance();
						shape.setName(e.getName());
						shape.setClass(false);
						shape.setInstanceType(parentShape);
						createShape(shape, rootShape, diagram);
						Connection connection = new Connection(shape,
								parentShape, "instanceOf");

						connection.setParent(rootConnection);
						rootConnection.addChild(connection);
						shape.getDiagram().addConnection(connection);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void createShape(Shape shape, Shape parent, ShapesDiagram diagram) {
		// TODO Auto-generated method stub
		Rectangle bounds = new Rectangle(0, 0, -1, -1);
		shape.setLocation(bounds.getLocation());
		Dimension size = bounds.getSize();
		if (size.width > 0 && size.height > 0)
			shape.setSize(size);
		shape.setColor(ColorConstants.orange.getRGB());
		parent.addChild(shape);
		shape.setParent(parent);
		if (diagram.addChild(shape))
			shape.setDiagram(diagram);
	}

}
