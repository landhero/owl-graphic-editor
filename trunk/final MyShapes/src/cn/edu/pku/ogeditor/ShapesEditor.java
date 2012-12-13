/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor;

import iot.reasoner.MergeOwlAndSWRL;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
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
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import cn.edu.pku.ogeditor.actions.ActionConstant;
import cn.edu.pku.ogeditor.actions.ConceptFilterAction;
import cn.edu.pku.ogeditor.actions.DeployAction;
import cn.edu.pku.ogeditor.actions.OWLGenerationAction;
import cn.edu.pku.ogeditor.actions.RelationFilterAction;
import cn.edu.pku.ogeditor.actions.RelocateAction;
import cn.edu.pku.ogeditor.actions.StopDeployAction;
import cn.edu.pku.ogeditor.actions.SystemGenerationAction;
import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapeProperty;
import cn.edu.pku.ogeditor.model.ShapesDiagram;
import cn.edu.pku.ogeditor.parts.DiagramEditPart;
import cn.edu.pku.ogeditor.parts.ShapeEditPart;
import cn.edu.pku.ogeditor.parts.ShapesEditPartFactory;
import cn.edu.pku.ogeditor.parts.ShapesTreeEditPartFactory;
import cn.edu.pku.ogeditor.views.DecriptionView;
import cn.edu.pku.ogeditor.views.SWRLRule;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * A graphical editor with flyout palette that can edit .ogeditor files. The
 * binding between the .ogeditor file extension and this editor is done in
 * plugin.xml
 * 
 * @author Xueyuan Xing
 * @author Tao Wu
 * @author Hansheng Zhang
 */
public class ShapesEditor extends GraphicalEditorWithFlyoutPalette implements
		Serializable, ITabbedPropertySheetPageContributor {
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		IEditorPart activeEditor = getSite().getPage().getActiveEditor();
		if (activeEditor instanceof MultiPageEditor) {
			MultiPageEditor mpe = (MultiPageEditor) activeEditor;
			if (mpe.getShapesEditor() == this)
				updateActions(ActionConstant.getSelectableActions());
		}
		super.selectionChanged(part, selection);
		
		 if (null != propertySheet && !selection.isEmpty())// && part == this)
		 {
//			 selection.
			 propertySheet.selectionChanged(part, selection);
		 }
	}

	private static final long serialVersionUID = 1L;
	private static final String NS = "http://odm/concept-ont#";
	/** This is the root of the editor's model. */
	private ShapesDiagram diagram;
	public PaletteViewer paletteViewer;
	/** Palette component, holding the tools and shapes. */
	private PaletteRoot PALETTE_MODEL;
	private String selectedDragSourceToolLabel;
	private boolean dirty = false;
	private OntModel ontModel;
	private TabbedPropertySheetPage propertySheet;
	public static ShapesEditor myselfShapesEditor;

	/** Create a new ShapesEditor instance. This is called by the Workspace. */
	public ShapesEditor() {
		setEditDomain(new DefaultEditDomain(this));
		myselfShapesEditor = this;
	}

	public ShapesDiagram getDiagram() {
		return diagram;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void createActions() {
		super.createActions();
		IAction action = new RelocateAction(this);
		action.setId(ActionConstant.RELOCATE_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());
		action = new ConceptFilterAction(this);
		action.setId(ActionConstant.CONCEPTFILTER_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());
		action = new RelationFilterAction(this);
		action.setId(ActionConstant.RELATIONFILTER_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());

		action = new OWLGenerationAction(this);
		action.setId(ActionConstant.OWLGENERATION_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());
		action = new SystemGenerationAction(this);
		action.setId(ActionConstant.SYSTEMGENERATION_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());

		action = new DeployAction(this);
		action.setId(ActionConstant.DEPLOY_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());
		action = new StopDeployAction(this);
		action.setId(ActionConstant.STOP_DEPLOY_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());
	}

	/**
	 * Configure the graphical viewer before it receives contents.
	 * <p>
	 * This is the place to choose an appropriate RootEditPart and
	 * EditPartFactory for your editor. The RootEditPart determines the behavior
	 * of the editor's "work-area". For example, GEF includes zoomable and
	 * scrollable root edit parts. The EditPartFactory maps model elements to
	 * edit parts (controllers).
	 * </p>
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();

		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new ShapesEditPartFactory());
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));

		// configure the context menu provider
		ContextMenuProvider cmProvider = new ShapesEditorContextMenuProvider(
				viewer, getActionRegistry());
		viewer.setContextMenu(cmProvider);
		getSite().registerContextMenu(cmProvider, viewer);
		getSite().setSelectionProvider(viewer);
		initZoomManager();
	}

	/**
	 * Set up the editor's inital content (after creation).
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(getModel()); // set the contents of this editor
		viewer.addDropTargetListener(createTransferDropTargetListener());

	}

	private void initZoomManager() {
		ZoomManager manager = ((ScalableFreeformRootEditPart) getGraphicalViewer()
				.getRootEditPart()).getZoomManager();
		getActionRegistry().registerAction(new ZoomInAction(manager));
		getActionRegistry().registerAction(new ZoomOutAction(manager));
		double[] zoomLevels;
		zoomLevels = new double[] { 0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0,
				4.0, 5.0, 10.0, 20.0 };
		manager.setZoomLevels(zoomLevels);
		ArrayList<String> zoomContributions;
		zoomContributions = new ArrayList<String>();
		zoomContributions.add(ZoomManager.FIT_ALL);
		zoomContributions.add(ZoomManager.FIT_HEIGHT);
		zoomContributions.add(ZoomManager.FIT_WIDTH);
		manager.setZoomLevelContributions(zoomContributions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.parts.GraphicalEditor#commandStackChanged(java.util
	 * .EventObject)
	 */
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}

	private void createOutputStream(OutputStream os) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(os);
		ShapesDiagram tempDiagram = getModel();
		while (tempDiagram.getFather() != null)
			tempDiagram = tempDiagram.getFather();
		oos.writeObject(tempDiagram);
		oos.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#
	 * createPaletteViewerProvider()
	 */
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {
			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				paletteViewer = viewer;
				viewer.addDragSourceListener(createTransferDragSourceListener());
			}
		};
	}

	private TransferDragSourceListener createTransferDragSourceListener() {
		return new TemplateTransferDragSourceListener(paletteViewer) {
			public void dragStart(DragSourceEvent event) {
				super.dragStart(event);
				List<?> selection = getViewer().getSelectedEditParts();
				if (selection.size() == 1) {
					EditPart editpart = (EditPart) getViewer()
							.getSelectedEditParts().get(0);
					Object model = editpart.getModel();
					if (model instanceof PaletteEntry)
						ShapesEditor.myselfShapesEditor.selectedDragSourceToolLabel = ((PaletteEntry) model)
								.getLabel();
					else
						ShapesEditor.myselfShapesEditor.selectedDragSourceToolLabel = "Error";
				}
			}
		};
	}

	/**
	 * Create a transfer drop target listener. When using a
	 * CombinedTemplateCreationEntry tool in the palette, this will enable model
	 * element creation by dragging from the palette.
	 * 
	 * @see #createPaletteViewerProvider()
	 */
	private TransferDropTargetListener createTransferDropTargetListener() {
		return new TemplateTransferDropTargetListener(getGraphicalViewer()) {
			protected CreationFactory getFactory(Object template) {
				return new ShapesCreationFactory(
						ShapesEditor.myselfShapesEditor.selectedDragSourceToolLabel);
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor
	 * )
	 */
	public void doSave(IProgressMonitor monitor) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			createOutputStream(out);
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.setContents(new ByteArrayInputStream(out.toByteArray()), true,
					false, //
					monitor); // progress monitor
			getCommandStack().markSaveLocation();
			setDirty(false);
		} catch (CoreException ce) {
			ce.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * save the current .ogeditor-file as .owl-file.
	 * 
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
			// save as owl file
			String fileextension = path.getFileExtension();
			String filepath = Platform.getLocation().toString()
					+ path.toString();

			if (fileextension.equals("owl") || fileextension.equals("rdf")
					|| fileextension.equals("ttl")) {
				System.out.println("dosaveas: is .owl");
				System.out.println("path:" + filepath);
				System.out.println("ws: " + ResourcesPlugin.getWorkspace());

				SaveAsOWL(filepath, fileextension);

			} else {
				System.out.println("dosaveas: not .owl");
				// try to save the editor's contents under a different file name
				final IFile file = ResourcesPlugin.getWorkspace().getRoot()
						.getFile(path);
				try {
					new ProgressMonitorDialog(shell).run(false, // don't fork
							false, // not cancelable
							new WorkspaceModifyOperation() { // run this
								// operation
								public void execute(
										final IProgressMonitor monitor) {
									try {
										ByteArrayOutputStream out = new ByteArrayOutputStream();
										createOutputStream(out);
										file.create(new ByteArrayInputStream(
												out.toByteArray()), // contents
												true, // keep saving, even if
														// IFile is out of sync
														// with the Workspace
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
					// should not happen, since the monitor dialog is not
					// cancelable
					ie.printStackTrace();
				} catch (InvocationTargetException ite) {
					ite.printStackTrace();
				}
			}
		}
	}

	public void SaveAsOWL(String filepath, String fileextension) {

		System.out.println("----saveasowl:start");
		System.out.println("saveasowl:no modelfactory");
		ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		System.out.println("saveasowl:create modelfactory");
		// creat ontology
		Ontology shOnt = ontModel.createOntology(NS);
		shOnt.addProperty(RDFS.comment,
				"This is an ontology for a process-oriented requirement modeling tool.");
		shOnt.addProperty(RDFS.label, "Concept Ontology");
		shOnt.addProperty(OWL.versionInfo, "$serialVersionUID:1.0 Dec. 2012$");

		ShapesDiagram rootDiagram = getDiagram().getRootDiagram();
		createOntologyForDiagram(rootDiagram);

		String tmpOwlPath = filepath + ".tmpowl";
		String tmpSwrlPath = filepath + ".tmprule";

		/*
		 * Save OntModel ontM to the OWL file
		 */
		try {
			// tmpowl
			File file = new File(tmpOwlPath);
			System.out.println(file.getAbsolutePath());
			if (file.exists())
				file.delete();

			file.createNewFile();
			FileOutputStream out = new FileOutputStream(tmpOwlPath);
			if (fileextension.equals("owl") || fileextension.equals("rdf"))
				ontModel.write(out, "RDF/XML-ABBREV");
			else if (fileextension.equals("ttl"))
				ontModel.write(out, "Turtle");
			out.close();
			System.out.println("has written to " + tmpOwlPath);

			// tmpswrl
			file = new File(tmpSwrlPath);
			if (!file.exists())
				file.createNewFile();
			// next, save rules;
			Object[] rules = getDiagram().getRootDiagram().getRules()
					.elements();
			saveRulesFile(tmpSwrlPath, rules);
			System.out.println("has written to " + tmpSwrlPath);

			MergeOwlAndSWRL.merge(tmpOwlPath, tmpSwrlPath, filepath);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void saveRulesFile(String path, Object[] rules) {
		File f = new File(path);
		try {
			if (f.exists())
				f.delete();

			f.createNewFile();

			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			int len = rules.length;
			if (len > 0) {
				Object o = rules[0];
				SWRLRule r = (SWRLRule) o;
				bw.write(r.getExpression());
				for (int i = 1;i < len; i++) {
					bw.newLine();
					r = (SWRLRule) rules[i];
					bw.write(r.getExpression());
				}
			}
			bw.close();
			fw.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void createOntologyForDiagram(ShapesDiagram curDiagram) {
		List<Shape> curShapes = curDiagram.getChildren();
		// String std = "http://www.w3.org/2001/XMLSchema";
		for (int i = 0; i < curShapes.size(); i++) {
			Shape curShape = curShapes.get(i);
			if (!curShape.isClass())
				continue;
			// 不进行是否有重复的判断的前提是本体中所有类的名字都是唯一的
			OntClass curClass = ontModel.createClass(NS + curShape.getName());

			// property part, need modified
			List<ShapeProperty> curPros = curShape.getProperties();
			for (ShapeProperty pro : curPros) {
				DatatypeProperty property = ontModel.createDatatypeProperty(NS
						+ pro.getName());
				property.setRange(ShapeProperty.type2XSDType(pro.getType()));
				curClass.addProperty(property, pro.getValue());
				// if (pro.getType().equals(ShapeProperty.BOOLEAN_TYPE)) {
				//
				// curClass.addLiteral(property,
				// Boolean.parseBoolean(pro.getValue()));
				// } else if (pro.getType().equals(ShapeProperty.FLOAT_TYPE)) {
				// curClass.addLiteral(property,
				// Float.parseFloat(pro.getValue()));
				// } else if (pro.getType().equals(ShapeProperty.INT_TYPE)) {
				// curClass.addLiteral(property,
				// Integer.parseInt(pro.getValue()));
				// } else {
				// curClass.addLiteral(property, pro.getValue());
				// }
			}
			Shape parentShape = curShape.getParent();
			OntClass parentClass = ontModel.getOntClass(NS
					+ parentShape.getName());
			if (parentClass == null)
				continue;
			parentClass.addSubClass(curClass);
		}

		// create instances
		for (int i = 0; i < curShapes.size(); i++) {
			Shape curShape = curShapes.get(i);
			if (curShape.isClass())
				continue;
			Shape parType = curShape.getInstanceType();
			OntClass parTypeClass = ontModel
					.getOntClass(NS + parType.getName());
			if (null == parTypeClass)
				continue;
			// 不进行是否有重复的判断的前提是本体中所有类的名字都是唯一的
			Individual curInstance = parTypeClass.createIndividual(NS
					+ curShape.getName());

			// property part, need modified
			List<ShapeProperty> curPros = curShape.getProperties();
			for (ShapeProperty pro : curPros) {
				DatatypeProperty property = ontModel.getDatatypeProperty(NS
						+ pro.getName());
				// property.setRange(ShapeProperty.type2XSDType(pro.getType()));
				if (pro.getType().equals(ShapeProperty.BOOLEAN_TYPE)) {

					curInstance.addLiteral(property,
							Boolean.parseBoolean(pro.getValue()));
				} else if (pro.getType().equals(ShapeProperty.FLOAT_TYPE)) {
					curInstance.addLiteral(property,
							Float.parseFloat(pro.getValue()));
				} else if (pro.getType().equals(ShapeProperty.INT_TYPE)) {
					curInstance.addLiteral(property,
							Integer.parseInt(pro.getValue()));
				} else {
					curInstance.addLiteral(property, pro.getValue());
				}
			}

		}

		for (int i = 0; i < curShapes.size(); i++) {
			Shape curShape = curShapes.get(i);
			if (!curShape.isClass())
				continue;
			OntClass srcClass = ontModel.getOntClass(NS + curShape.getName());
			OntClass tarClass;
			List<Connection> sCons = curShape.getSourceConnections();
			for (int j = 0; j < sCons.size(); j++) {
				Connection curCon = sCons.get(j);
				Shape tarShape = curCon.getTarget();
				tarClass = ontModel.getOntClass(NS + tarShape.getName());

				ObjectProperty curOP = ontModel.getObjectProperty(NS
						+ curCon.getName());
				if (curOP == null)
					curOP = ontModel
							.createObjectProperty(NS + curCon.getName());
				curOP.addDomain(srcClass);
				curOP.addRange(tarClass);
				// SomeValuesFromRestriction svf = ontModel
				// .createSomeValuesFromRestriction(null, curOP, tarClass);
				// srcClass.addSuperClass(svf);
				// System.out.println("svfname:"
				// + svf.getSomeValuesFrom().getLocalName());
				// System.out.println(curOP.getRange() + " endHolyshit");
			}
		}
		for (int i = 0; i < curDiagram.getLowerLevelDiagrams().size(); i++) {
			createOntologyForDiagram(curDiagram.getLowerLevelDiagrams().get(i));
		}
	}

	/**
	 * Provide PropertySheet, Outline and Zoom interface.
	 */
	public Object getAdapter(Class type) {
		myselfShapesEditor = this;

		if (type == IPropertySheetPage.class) {
			if (null == getPropertySheet())
				setPropertySheet(new TabbedPropertySheetPage(this));
			return getPropertySheet();
		} else if (type == IContentOutlinePage.class)
			return new OutlinePage(new TreeViewer());
		else if (type == ZoomManager.class)
			return ((ScalableFreeformRootEditPart) getGraphicalViewer()
					.getRootEditPart()).getZoomManager();
		return super.getAdapter(type);
	}

	ShapesDiagram getModel() {
		return getDiagram();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot
	 * ()
	 */
	public PaletteRoot getPaletteRoot() {
		if (PALETTE_MODEL == null)
			PALETTE_MODEL = new ShapesEditorPaletteRoot();
		return PALETTE_MODEL;
	}

	private void handleLoadException(Exception e) {
		System.err.println("** Load failed. Using default model. **");
		e.printStackTrace();
		setDiagram(new ShapesDiagram());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}

	public boolean isDirty() {
		return getCommandStack().isDirty() || dirty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		try {
			IFile file = ((IFileEditorInput) input).getFile();
			ObjectInputStream in = new ObjectInputStream(file.getContents());
			setDiagram((ShapesDiagram) in.readObject());
			in.close();
			setPartName(file.getName());
			((ShapesEditorPaletteRoot) getPaletteRoot()).refresh();
		} catch (IOException e) {
			handleLoadException(e);
		} catch (CoreException e) {
			handleLoadException(e);
		} catch (ClassNotFoundException e) {
			handleLoadException(e);
		}
	}

	public DecriptionView getDescription() {
		DecriptionView dv = null;
		dv = (DecriptionView) getSite().getWorkbenchWindow().getActivePage()
				.findView("cn.edu.pku.ogeditor.descriptionView");
		return dv;
	}

	public void setDirty(boolean dirty) {
		// TODO Auto-generated method stub
		this.dirty = dirty;
		firePropertyChange(PROP_DIRTY);
	}

	/**
	 * refresh the view with given diagram
	 * 
	 * @param diagram
	 *            the given diagram you want to refresh the view with.
	 */
	public void refreshModel(ShapesDiagram diagram) {
		// TODO Auto-generated method stub
		this.setDiagram(diagram);
		getGraphicalViewer().setContents(getModel());
		((ShapesEditorPaletteRoot) getPaletteRoot()).refresh();
	}

	protected FigureCanvas getEditor() {
		return (FigureCanvas) getGraphicalViewer().getControl();
	}

	/**
	 * Creates an outline pagebook for this editor.
	 */
	class OutlinePage extends ContentOutlinePage implements IAdaptable {

		private PageBook pageBook;
		private Control outline;
		private Canvas overview;
		private IAction showOutlineAction, showOverviewAction;
		static final int ID_OUTLINE = 0;
		static final int ID_OVERVIEW = 1;
		private Thumbnail thumbnail;
		private DisposeListener disposeListener;

		public OutlinePage(EditPartViewer viewer) {
			super(viewer);
		}

		public void init(IPageSite pageSite) {
			super.init(pageSite);
			ActionRegistry registry = getActionRegistry();
			IActionBars bars = pageSite.getActionBars();
			String id = ActionFactory.UNDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.REDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.DELETE.getId();
			bars.updateActionBars();
		}

		protected void configureOutlineViewer() {
			getViewer().setEditDomain(getEditDomain());
			getViewer().setEditPartFactory(new ShapesTreeEditPartFactory());
			ContextMenuProvider provider = new ShapesEditorContextMenuProvider(
					getViewer(), getActionRegistry());
			getViewer().setContextMenu(provider);
			getSite().registerContextMenu("cn.edu.pku.ogeditor.contextmenu", //$NON-NLS-1$
					provider, getSite().getSelectionProvider());
			// getViewer().setKeyHandler(getCommonKeyHandler());
			getViewer()
					.addDropTargetListener(
							(TransferDropTargetListener) new TemplateTransferDropTargetListener(
									getViewer()));
			IToolBarManager tbm = getSite().getActionBars().getToolBarManager();
			showOutlineAction = new Action() {
				public void run() {
					showPage(ID_OUTLINE);
				}
			};
			showOutlineAction.setImageDescriptor(ImageDescriptor
					.createFromFile(ShapesPlugin.class, "icons/outline.gif")); //$NON-NLS-1$
			showOutlineAction.setToolTipText("Show Outline");
			tbm.add(showOutlineAction);
			showOverviewAction = new Action() {
				public void run() {
					showPage(ID_OVERVIEW);
				}
			};
			showOverviewAction.setImageDescriptor(ImageDescriptor
					.createFromFile(ShapesPlugin.class, "icons/overview.gif")); //$NON-NLS-1$
			showOverviewAction.setToolTipText("Show Overview");
			tbm.add(showOverviewAction);
			showPage(ID_OUTLINE);
		}

		public void createControl(Composite parent) {
			pageBook = new PageBook(parent, SWT.NONE);
			outline = getViewer().createControl(pageBook);
			overview = new Canvas(pageBook, SWT.NONE);
			pageBook.showPage(outline);
			configureOutlineViewer();
			hookOutlineViewer();
			initializeOutlineViewer();
		}

		public void dispose() {
			unhookOutlineViewer();
			if (thumbnail != null) {
				thumbnail.deactivate();
				thumbnail = null;
			}
			super.dispose();
		}

		public Object getAdapter(Class type) {
			if (type == ZoomManager.class)
				return getGraphicalViewer().getProperty(
						ZoomManager.class.toString());
			return null;
		}

		public Control getControl() {
			return pageBook;
		}

		protected void hookOutlineViewer() {
			getSelectionSynchronizer().addViewer(getViewer());
		}

		protected void initializeOutlineViewer() {
			setContents(getDiagram());
		}

		protected void initializeOverview() {
			LightweightSystem lws = new LightweightSystem(overview);
			RootEditPart rep = getGraphicalViewer().getRootEditPart();
			if (rep instanceof ScalableFreeformRootEditPart) {
				ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) rep;
				thumbnail = new ScrollableThumbnail((Viewport) root.getFigure());
				thumbnail.setBorder(new MarginBorder(3));
				thumbnail.setSource(root
						.getLayer(LayerConstants.PRINTABLE_LAYERS));
				lws.setContents(thumbnail);
				disposeListener = new DisposeListener() {
					public void widgetDisposed(DisposeEvent e) {
						if (thumbnail != null) {
							thumbnail.deactivate();
							thumbnail = null;
						}
					}
				};
				getEditor().addDisposeListener(disposeListener);
			}
		}

		public void setContents(Object contents) {
			getViewer().setContents(contents);
		}

		protected void showPage(int id) {
			if (id == ID_OUTLINE) {
				showOutlineAction.setChecked(true);
				showOverviewAction.setChecked(false);
				pageBook.showPage(outline);
				if (thumbnail != null)
					thumbnail.setVisible(false);
			} else if (id == ID_OVERVIEW) {
				if (thumbnail == null)
					initializeOverview();
				showOutlineAction.setChecked(false);
				showOverviewAction.setChecked(true);
				pageBook.showPage(overview);
				thumbnail.setVisible(true);
			}
		}

		protected void unhookOutlineViewer() {
			getSelectionSynchronizer().removeViewer(getViewer());
			if (disposeListener != null && getEditor() != null
					&& !getEditor().isDisposed())
				getEditor().removeDisposeListener(disposeListener);
		}
	}

	public String getContributorId() {
		// return getSite().getId();
		return "OWL Graphic Editor";

	}

	public void setDiagram(ShapesDiagram diagram) {
		this.diagram = diagram;
	}

	public void setPropertySheet(TabbedPropertySheetPage propertySheet) {
		this.propertySheet = propertySheet;
	}

	public TabbedPropertySheetPage getPropertySheet() {
		return propertySheet;
	}
}