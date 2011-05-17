/*******************************************************************************
[ * Copyright (c) 2004, 2005 Elias Volanakis and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Elias Volanakis - initial API and implementation
?*******************************************************************************/
package cn.edu.pku.ogeditor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import cn.edu.pku.ogeditor.actions.ReviewAction;
import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;
import cn.edu.pku.ogeditor.parts.ShapesEditPartFactory;
import cn.edu.pku.ogeditor.parts.ShapesTreeEditPartFactory;
import cn.edu.pku.ogeditor.views.DecriptionView;
import cn.edu.pku.ogeditor.views.HierarchyView;

import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

/**
 * A graphical editor with flyout palette that can edit .shapes files.
 * The binding between the .shapes file extension and this editor is done in plugin.xml
 * @author Elias Volanakis
 */
public class ShapesEditor 
extends GraphicalEditorWithFlyoutPalette implements Serializable
{
	private static final long serialVersionUID = 1L; //
	/** This is the root of the editor's model. */
	private ShapesDiagram diagram;
	public PaletteViewer paletteViewer;
	/** Palette component, holding the tools and shapes. */
	private PaletteRoot PALETTE_MODEL;
	private String selectedDragSourceToolLabel;
	public static ShapesEditor myselfShapesEditor;
	/** Create a new ShapesEditor instance. This is called by the Workspace. */
	public ShapesEditor() {
		setEditDomain(new DefaultEditDomain(this));
		myselfShapesEditor=this;
	}

	public ShapesDiagram getDiagram(){
		return diagram;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void createActions(){
		super.createActions();
		IAction action=new ReviewAction(this);
		action.setId("ogeditor.Review");
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());
	}
	/**
	 * Configure the graphical viewer before it receives contents.
	 * <p>This is the place to choose an appropriate RootEditPart and EditPartFactory
	 * for your editor. The RootEditPart determines the behavior of the editor's "work-area".
	 * For example, GEF includes zoomable and scrollable root edit parts. The EditPartFactory
	 * maps model elements to edit parts (controllers).</p>
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();

		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new ShapesEditPartFactory());
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));

		// configure the context menu provider
		ContextMenuProvider cmProvider =
			new ShapesEditorContextMenuProvider(viewer, getActionRegistry());
		viewer.setContextMenu(cmProvider);
		getSite().registerContextMenu(cmProvider, viewer);
		initZoomManager();
	}
	
	private void initZoomManager() {
		ZoomManager manager = ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager(); 
		getActionRegistry().registerAction(new ZoomInAction(manager)); 
		getActionRegistry().registerAction(new ZoomOutAction(manager)); 
		double[] zoomLevels; 
		zoomLevels = new double[] {0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 10.0, 20.0}; 
		manager.setZoomLevels(zoomLevels);
		ArrayList<String> zoomContributions;
		zoomContributions = new ArrayList<String>(); 
		zoomContributions.add(ZoomManager.FIT_ALL); 
		zoomContributions.add(ZoomManager.FIT_HEIGHT); 
		zoomContributions.add(ZoomManager.FIT_WIDTH); 
		manager.setZoomLevelContributions(zoomContributions);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#commandStackChanged(java.util.EventObject)
	 */
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}

	private void createOutputStream(OutputStream os) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(getModel());
		oos.close();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#createPaletteViewerProvider()
	 */
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				paletteViewer=viewer;
				viewer.addDragSourceListener(createTransferDragSourceListener());
			}	
		};
	}

	private TransferDragSourceListener createTransferDragSourceListener() {
		return new TemplateTransferDragSourceListener(paletteViewer){
			public void dragStart(DragSourceEvent event)
			{
				super.dragStart(event);
				List<?> selection = getViewer().getSelectedEditParts();
				if (selection.size() == 1) {
					EditPart editpart = (EditPart) getViewer().getSelectedEditParts()
							.get(0);
					Object model = editpart.getModel();
					if (model instanceof PaletteEntry)
						ShapesEditor.myselfShapesEditor.selectedDragSourceToolLabel = ((PaletteEntry) model).getLabel();
					else ShapesEditor.myselfShapesEditor.selectedDragSourceToolLabel = "Error";
				}
			}
		};
	}
	/**
	 * Create a transfer drop target listener. When using a CombinedTemplateCreationEntry
	 * tool in the palette, this will enable model element creation by dragging from the palette.
	 * @see #createPaletteViewerProvider()
	 */
	private TransferDropTargetListener createTransferDropTargetListener() {
		return new TemplateTransferDropTargetListener(getGraphicalViewer()) {
			protected CreationFactory getFactory(Object template) {
				return new ShapesCreationFactory(ShapesEditor.myselfShapesEditor.selectedDragSourceToolLabel);
			}
		};
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			createOutputStream(out);
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();

			file.setContents(
					new ByteArrayInputStream(out.toByteArray()), 
					true,  // keep saving, even if IFile is out of sync with the Workspace
					false, // dont keep history
					monitor); // progress monitor
			getCommandStack().markSaveLocation();
		} catch (CoreException ce) { 
			ce.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {
		// Show a SaveAs dialog
		Shell shell = getSite().getWorkbenchWindow().getShell();
		SaveAsDialog dialog = new SaveAsDialog(shell);
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();
		IPath path = dialog.getResult();	

		if (path != null) {
			System.out.println("dosaveas:path not null");
			//save as owl file
			String fileextension= path.getFileExtension();
			String filepath = Platform.getLocation().toString()+path.toString();

			if(fileextension.equals("owl") || fileextension.equals("rdf") || fileextension.equals("ttl")){
				System.out.println("dosaveas: is .owl");
				System.out.println("path:"+filepath);
				//System.out.println("ws: "+ResourcesPlugin.getWorkspace());

				//SaveAsOWL(filepath,fileextension);

			}
			else{
				System.out.println("dosaveas: not .owl");
				// try to save the editor's contents under a different file name
				final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				try {
					new ProgressMonitorDialog(shell).run(
							false, // don't fork
							false, // not cancelable
							new WorkspaceModifyOperation() { // run this operation
								public void execute(final IProgressMonitor monitor) {
									try {
										ByteArrayOutputStream out = new ByteArrayOutputStream();
										createOutputStream(out);
										file.create(
												new ByteArrayInputStream(out.toByteArray()), // contents
												true, // keep saving, even if IFile is out of sync with the Workspace
												monitor); // progress monitor
									} catch (CoreException ce) {
										ce.printStackTrace();
									} catch (IOException ioe) {
										ioe.printStackTrace();
									} 
								}
							});
					// set input to the new file
					setInput(new FileEditorInput(file));
					getCommandStack().markSaveLocation();
				} catch (InterruptedException ie) {
					// should not happen, since the monitor dialog is not cancelable
					ie.printStackTrace(); 
				} catch (InvocationTargetException ite) { 
					ite.printStackTrace(); 
				}
			}
		}	
	}

	protected void SaveAsOWL(String filepath,String fileextension){

		System.out.println("----saveasowl:start");	

		/* Creat Ontology classes and properties for sh
		 */ 
		//base namespace, not a real URI
		String NS="http://somewhere/concept-ont#"; //base namespace

		//base model
		System.out.println("saveasowl:no modelfactory");	
		OntModel ontM = ModelFactory.createOntologyModel();
		System.out.println("saveasowl:create modelfactory");	
		//creat ontology
		Ontology shOnt = ontM.createOntology(NS);
		shOnt.addProperty(RDFS.comment, "This is an ontology for a process-oriented requirement modeling tool.");
		shOnt.addProperty(RDFS.label, "Concept Ontology");
		shOnt.addProperty(OWL.versionInfo, "$serialVersionUID:1.0 Dec. 2010$");


		//creat classes
		OntClass shape = ontM.createClass(NS+"Concept");
		OntClass connection = ontM.createClass(NS+"Relation");
		System.out.println("----------------URI:"+shape.getURI());


		OntClass ellip = ontM.createClass(NS+"ProcessShape");shape.addSubClass(ellip);
		OntClass rect = ontM.createClass(NS+"DataShape");shape.addSubClass(rect);
		OntClass tri= ontM.createClass(NS+"ActorShape");shape.addSubClass(tri);
		//

		//creat properties
		//properties of the class Shape
		//sname
		DatatypeProperty sname = ontM.createDatatypeProperty(NS+"conceptName"); 
		sname.addDomain(shape);
		sname.addRange(XSD.xstring);
		//sdes
		DatatypeProperty sdes = ontM.createDatatypeProperty(NS+"conceptDescription");
		sdes.addDomain(shape);
		sdes.addRange(XSD.xstring);
		//ssize
		DatatypeProperty ssize = ontM.createDatatypeProperty(NS+"shapeSize");
		//sloc
		DatatypeProperty sloc = ontM.createDatatypeProperty(NS+"shapeLocation");
		//sheight
		DatatypeProperty sheight = ontM.createDatatypeProperty(NS+"shapeHeight");
		sheight.addDomain(shape);
		sheight.addRange(XSD.xdouble);
		//swidth
		DatatypeProperty swidth = ontM.createDatatypeProperty(NS+"shapeWidth");
		swidth.addDomain(shape);
		swidth.addRange(XSD.xdouble);
		//xloc
		DatatypeProperty xloc = ontM.createDatatypeProperty(NS+"xLoc");
		xloc.addDomain(shape);
		xloc.addRange(XSD.xdouble);
		//yloc
		DatatypeProperty yloc = ontM.createDatatypeProperty(NS+"yLoc");
		yloc.addDomain(shape);
		yloc.addRange(XSD.xdouble);
		//subPropertyOf
		ssize.addSubProperty(sheight);ssize.addSubProperty(swidth);
		sloc.addSubProperty(xloc);sloc.addSubProperty(yloc);
		//isSourceOf && isTargetOf
		ObjectProperty isSourceOf = ontM.createObjectProperty(NS+"isSourceOf");
		isSourceOf.addDomain(shape);
		isSourceOf.addRange(connection);
		ObjectProperty isTargetOf = ontM.createObjectProperty(NS+"isTargetOf");
		isTargetOf.addDomain(shape);
		isTargetOf.addRange(connection);

		//properties of the class Connection
		//cname
		DatatypeProperty cname = ontM.createDatatypeProperty(NS+"relationName");
		cname.addDomain(connection);
		cname.addRange(XSD.xstring);
		//cstyle
		DatatypeProperty cstyle = ontM.createDatatypeProperty(NS+"relationStyle");
		cstyle.addDomain(connection);
		cstyle.addRange(XSD.integer);
		RDFList vlist = ontM.createList(new Literal[]{ontM.createTypedLiteral(0),
				ontM.createTypedLiteral(1),
				ontM.createTypedLiteral(2)});
		AllValuesFromRestriction avr = ontM.createAllValuesFromRestriction(null, cstyle, vlist);
		//csource
		ObjectProperty csource = ontM.createObjectProperty(NS+"relationSource");
		csource.addDomain(connection);
		csource.addRange(shape);
		//ctarget
		ObjectProperty ctarget = ontM.createObjectProperty(NS+"relationTarget");
		ctarget.addDomain(connection);
		ctarget.addRange(shape);



		/* Get list of shapes in the diagram to sh
		 */ 
		List<Shape> sh = new ArrayList<Shape>();
		sh.addAll(diagram.getChildren());


		/* Lead sh to OntModel ontM
		 */ 
		//the index for connection individuals
		int cindex = 0;
		//list of cname and cURI
		List<Integer> chashlist = new ArrayList<Integer>();
		List<String>  curilist  = new ArrayList<String>();

		//add shape individuals and connection individuals
		for(int i=0; i<sh.size(); i++){
			Shape stemp = sh.get(i);
			//create a shape individual
			Individual ind = shape.createIndividual(NS+"conceptInd-"+stemp.hashCode());

			//add property value
			ind.addLiteral(sname,stemp.getName());
			ind.addLiteral(sdes, stemp.getDescription());
			ind.addLiteral(sheight, stemp.getSize().height);
			ind.addLiteral(swidth, stemp.getSize().width);
			ind.addLiteral(xloc, stemp.getLocation().x);
			ind.addLiteral(yloc, stemp.getLocation().y);

			List<Connection> sCons = stemp.getSourceConnections();
			List<Connection> tCons = stemp.getTargetConnections();
			for(int j=0; j<sCons.size(); j++){
				Connection ctemp = sCons.get(j);
				//the connection individual has exisited
				if(chashlist.contains(ctemp.hashCode())){
					//get the exisited connection individual
					Individual conInd = ontM.getIndividual(curilist.get(chashlist.indexOf(ctemp.hashCode())));
					//ind.addLiteral(isSourceOf, conInd);
					ind.addProperty(isSourceOf, conInd);
					conInd.addProperty(csource, ind);//
					continue;
				}
				//if the connection is not exisit: creat new connection individual			
				Individual conInd = connection.createIndividual(NS+"relationInd-"+ctemp.hashCode());
				conInd.addLiteral(cname, ctemp.getName());
				/*for anchor conInd.addLiteral(cstyle, ctemp.getLineStyle());*/
				//add the conInd name and curilist
				chashlist.add(cindex, ctemp.hashCode());
				curilist.add(cindex, conInd.getURI());
				//add the connection to isSourceOf
				ind.addProperty(isSourceOf, conInd);
				//add ind to csource
				conInd.addProperty(csource, ind);
				cindex++;
			}
			for(int j=0; j<tCons.size(); j++){
				Connection ctemp = tCons.get(j);
				//the connection individual has exisited
				if(chashlist.contains(ctemp.hashCode())){
					//get the exisited connection individual
					Individual conInd = ontM.getIndividual(curilist.get(chashlist.indexOf(ctemp.hashCode())));
					ind.addProperty(isTargetOf, conInd);
					conInd.addProperty(ctarget, ind);
					continue;
				}
				//if the connection is not exisit: creat new connection individual			
				Individual conInd = connection.createIndividual(NS+"relationInd-"+ctemp.hashCode());
				conInd.addLiteral(cname, ctemp.getName());
				/*for anchor conInd.addLiteral(cstyle, ctemp.getLineStyle());*/
				//add the conInd name and curilist
				chashlist.add(cindex, ctemp.hashCode());
				curilist.add(cindex, conInd.getURI());
				//add the connection to isSourceOf
				ind.addProperty(isTargetOf, conInd);
				//add ind to csource
				conInd.addProperty(ctarget, ind);
				cindex++;
			}
		}


		/* Save OntModel ontM to the OWL file
		 */ 
		try{	

			FileOutputStream out = new FileOutputStream(filepath);
			if(fileextension.equals("owl") || fileextension.equals("rdf"))
				ontM.write(out);
			else if(fileextension.equals("ttl"))
				ontM.write(out,"Turtle");
			System.out.println("has written to "+filepath);

		}catch(IOException e){
			e.printStackTrace();
		}

	}

	public Object getAdapter(Class type) {
		myselfShapesEditor = this;
		if (type == IContentOutlinePage.class)
			return new ShapesOutlinePage(new TreeViewer());
		else if (type == ZoomManager.class) 
			return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
		else if (type == HierarchyView.class) 
			return new HierarchyView();
		return super.getAdapter(type);
	}

	ShapesDiagram getModel() {
		return diagram;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot()
	 */
	public PaletteRoot getPaletteRoot() {
		if (PALETTE_MODEL == null)
			PALETTE_MODEL = new ShapesEditorPaletteRoot();
		return PALETTE_MODEL;
	}
	private void handleLoadException(Exception e) {
		System.err.println("** Load failed. Using default model. **");
		e.printStackTrace();
		diagram = new ShapesDiagram();
	}

	/**
	 * Set up the editor's inital content (after creation).
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(getModel()); // set the contents of this editor
		viewer.addDropTargetListener(createTransferDropTargetListener());

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	public boolean isDirty()
	{
		return getCommandStack().isDirty() ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		try {
			IFile file = ((IFileEditorInput) input).getFile();
			ObjectInputStream in = new ObjectInputStream(file.getContents());
			diagram = (ShapesDiagram) in.readObject();
			in.close();
			setPartName(file.getName());
			((ShapesEditorPaletteRoot)getPaletteRoot()).refresh(diagram);
		} catch (IOException e) { 
			handleLoadException(e); 
		} catch (CoreException e) { 
			handleLoadException(e); 
		} catch (ClassNotFoundException e) { 
			handleLoadException(e); 
		}
	}
	public DecriptionView getDescription(){
		DecriptionView dv = null;
		dv =(DecriptionView) getSite().getWorkbenchWindow().getActivePage().findView("cn.edu.pku.ogeditor.descriptionView");
		return dv;
	}


	
	/**
	 * Creates an outline pagebook for this editor.
	 */
	public class ShapesOutlinePage extends ContentOutlinePage {	
		/**
		 * Create a new outline page for the shapes editor.
		 * @param viewer a viewer (TreeViewer instance) used for this outline page
		 * @throws IllegalArgumentException if editor is null
		 */
		public ShapesOutlinePage(EditPartViewer viewer) {
			super(viewer);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
		 */
		public void createControl(Composite parent) {
			// create outline viewer page
			getViewer().createControl(parent);
			// configure outline viewer
			getViewer().setEditDomain(getEditDomain());
			getViewer().setEditPartFactory(new ShapesTreeEditPartFactory());
			// configure & add context menu to viewer
			ContextMenuProvider cmProvider = new ShapesEditorContextMenuProvider(
					getViewer(), getActionRegistry()); 
			getViewer().setContextMenu(cmProvider);
			getSite().registerContextMenu(
					"cn.edu.pku.ogeditor.outline.contextmenu",
					cmProvider, getSite().getSelectionProvider());		
			// hook outline viewer
			getSelectionSynchronizer().addViewer(getViewer());
			// initialize outline viewer with model
			getViewer().setContents(getModel());
			// show outline viewer
		}

		/* (non-Javadoc)
		 * @see org.eclipse.ui.part.IPage#dispose()
		 */
		public void dispose() {
			// unhook outline viewer
			getSelectionSynchronizer().removeViewer(getViewer());
			// dispose
			super.dispose(); 
		}


		/* (non-Javadoc)
		 * @see org.eclipse.ui.part.IPage#getControl()
		 */
		public Control getControl() {

			return getViewer().getControl();
		}

		/**
		 * @see org.eclipse.ui.part.IPageBookViewPage#init(org.eclipse.ui.part.IPageSite)
		 */
		public void init(IPageSite pageSite) {
			super.init(pageSite);
			ActionRegistry registry = getActionRegistry();
			IActionBars bars = pageSite.getActionBars();
			String id = ActionFactory.UNDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.REDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.DELETE.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			
			
			
		}
	}
}