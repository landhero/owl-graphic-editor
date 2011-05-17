package cn.edu.pku.ogeditor;

import java.util.ArrayList;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;

public class ShapesEditorPaletteRoot extends PaletteRoot {
	public ShapesEditorPaletteRoot(){
		super();
		createToolsGroup();
		init();
	}
	private ArrayList<Connection> allUplevelRequiredConnections = new ArrayList<Connection>();
	private PaletteDrawer requiredConnectionDrawer;
	private ArrayList<Connection> allUplevelElectiveConnections = new ArrayList<Connection>();
	private PaletteDrawer electiveConnectionDrawer;
	private ArrayList<Shape> allUplevelShapes=new ArrayList<Shape>();
	private PaletteDrawer conceptDrawer;
	/**
	 * @return the allConnectionTool
	 */
	public ArrayList<Connection> getUplevelAllRequiredConnections() {
		return allUplevelRequiredConnections;
	}
	public ArrayList<Connection> getUplevelAllElectiveConnections() {
		return allUplevelElectiveConnections;
	}
	/**
	 * @return the allShapeTool
	 */
	public ArrayList<Shape> getUplevelAllShapes() {
		return allUplevelShapes;
	}

	//	private static boolean dirty = false;
	public PaletteDrawer getShapeDrawer(){
		return conceptDrawer;
	}

	/**
	 * Creates the PaletteRoot and adds all palette elements. Use this factory
	 * method to create a new palette for your graphical editor.
	 * 
	 * @return a new PaletteRoot
	 */
	/** Create the "Tools" group. */
	private void createToolsGroup() {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");

		// Add a selection tool to the group
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		setDefaultEntry(tool);

		// Add a marquee tool to the group
		toolbar.add(new MarqueeToolEntry());
		add(toolbar);
	}
	
	private void init() {
		conceptDrawer = new PaletteDrawer("Concepts");
		requiredConnectionDrawer=new PaletteDrawer("Required Ralations");
		electiveConnectionDrawer=new PaletteDrawer("Elective Ralations");
		add(conceptDrawer);
		add(requiredConnectionDrawer);
		add(electiveConnectionDrawer);
	}

	public void RemoveRequiredConnectionTool() {
		int size = requiredConnectionDrawer.getChildren().size();
		for (int i = 0; i < size; i++) {
			requiredConnectionDrawer.remove((PaletteEntry) requiredConnectionDrawer
					.getChildren().get(0));
			allUplevelRequiredConnections.remove(0);
		}
	}

	public void RemoveElectiveConnectionTool() {
		int size = electiveConnectionDrawer.getChildren().size();
		for (int i = 0; i < size; i++) {
			electiveConnectionDrawer.remove((PaletteEntry) electiveConnectionDrawer
					.getChildren().get(0));
			allUplevelElectiveConnections.remove(0);
		}
	}
	public void AddRequiredConnectionTool(final Connection connection) {
		allUplevelRequiredConnections.add(connection);
		if (requiredConnectionDrawer != null) {
			ImageDescriptor newConnectionDescriptor = ImageDescriptor
			.createFromFile(ShapesPlugin.class,
					"icons/connection_common.gif");
			ConnectionCreationToolEntry tool = new ConnectionCreationToolEntry(
					connection.getName(), "Create a "
					+ connection.getName() + " relation",
					new CreationFactory() {
						public Object getNewObject() {
							return null;
						}
						public Object getObjectType() {
							return connection.getName();
						}
					}, newConnectionDescriptor, newConnectionDescriptor);
			requiredConnectionDrawer.add(tool);
		}
		else System.err.println("Drawer is null!");
	}
	public void AddElectiveConnectionTool(final Connection connectionTool) {
		allUplevelElectiveConnections.add(connectionTool);
		if (electiveConnectionDrawer != null) {
			ImageDescriptor newConnectionDescriptor = ImageDescriptor
			.createFromFile(ShapesPlugin.class,
					"icons/connection_common.gif");
			ConnectionCreationToolEntry tool = new ConnectionCreationToolEntry(
					connectionTool.getName(), "Create a "
					+ connectionTool.getName() + " relation",
					new CreationFactory() {
						public Object getNewObject() {
							return null;
						}
						public Object getObjectType() {
							return connectionTool.getName();
						}
					}, newConnectionDescriptor, newConnectionDescriptor);
			electiveConnectionDrawer.add(tool);
		}
		else System.err.println("Drawer is null!");
	}

	public void RemoveShapeTool() {
		int size = conceptDrawer.getChildren().size();
		for (int i = 0; i < size; i++) {
			conceptDrawer.remove((PaletteEntry) conceptDrawer.getChildren().get(0));
			allUplevelShapes.remove(0);
		}
	}
	public void AddShapeTool(final Shape shape) {
		allUplevelShapes.add(shape);
		if (conceptDrawer != null) {
			CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(
					shape.getName(), 
					"Create a " + shape.getName(), 
					Shape.class,
					new ShapesCreationFactory(shape.getName()),
					ImageDescriptor.createFromFile(ShapesPlugin.class,
					"icons/ellipse16.gif"), 
					ImageDescriptor.createFromFile(ShapesPlugin.class,
					"icons/ellipse24.gif"));
			conceptDrawer.add(component);
		}
	}
	public void refresh(ShapesDiagram _diagram) {
		allUplevelRequiredConnections = new ArrayList<Connection>();
		allUplevelElectiveConnections = new ArrayList<Connection>();
		remove(conceptDrawer);
		remove(requiredConnectionDrawer);
		remove(electiveConnectionDrawer);
		init();

	}
	public PaletteDrawer getRequiredConnectionDrawer() {
		return requiredConnectionDrawer;
	}
	public PaletteDrawer getElectiveConnectionDrawer() {
		return electiveConnectionDrawer;
	}
}