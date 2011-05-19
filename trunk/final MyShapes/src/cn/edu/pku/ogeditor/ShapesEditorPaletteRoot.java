package cn.edu.pku.ogeditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.MarqueeSelectionTool;
import org.eclipse.jface.resource.ImageDescriptor;

import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;

public class ShapesEditorPaletteRoot extends PaletteRoot {
	
	private ArrayList<Connection> allUplevelRequiredConnections = new ArrayList<Connection>();
	private PaletteDrawer requiredConnectionDrawer;
	private ArrayList<Connection> allUplevelElectiveConnections = new ArrayList<Connection>();
	private PaletteDrawer electiveConnectionDrawer;
	private ArrayList<Shape> allUplevelShapes=new ArrayList<Shape>();
	private PaletteDrawer conceptDrawer;
	private Shape rootShape;
	private Connection requiredRootConnection;
	private Connection electiveRootConnection;
	
	public ShapesEditorPaletteRoot(){
		super();
		createToolsGroup();
		init();
	}
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
		PaletteGroup controlGroup = new PaletteGroup("cn.edu.pku.ogeditor.paletteGroup");

		List<PaletteEntry> entries = new ArrayList<PaletteEntry>();

		ToolEntry tool = new PanningSelectionToolEntry();
		entries.add(tool);
		setDefaultEntry(tool);

		PaletteStack marqueeStack = new PaletteStack("cn.edu.pku.ogeditor.MarqueeStack", "", null); //$NON-NLS-1$
		marqueeStack.add(new MarqueeToolEntry());
		MarqueeToolEntry marquee = new MarqueeToolEntry();
		marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
				new Integer(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED));
		marqueeStack.add(marquee);
		marquee = new MarqueeToolEntry();
		marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
				new Integer(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED 
				| MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED));
		marqueeStack.add(marquee);
		marqueeStack.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
		entries.add(marqueeStack);
		
		controlGroup.addAll(entries);
		add(controlGroup);
	}
	
	private void init() {
		conceptDrawer = new PaletteDrawer("Concepts");
		requiredConnectionDrawer=new PaletteDrawer("Required Relations");
		electiveConnectionDrawer=new PaletteDrawer("Elective Relations");
		add(conceptDrawer);
		add(requiredConnectionDrawer);
		add(electiveConnectionDrawer);
		rootShape=new Shape();
		rootShape.setName("Thing");
		AddShapeTool(rootShape);

		requiredRootConnection=new Connection("RequiredRoot");
		requiredRootConnection.setName("RequiredRelation");
		requiredRootConnection.setRequired(true);
		AddRequiredConnectionTool(requiredRootConnection);

		electiveRootConnection=new Connection("ElectiveRoot");
		electiveRootConnection.setName("ElectiveRelation");
		electiveRootConnection.setRequired(false);
		AddElectiveConnectionTool(electiveRootConnection);
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
	public void refresh() {
		ShapesDiagram curDiagram = ShapesEditor.myselfShapesEditor.getModel();
		RemoveShapeTool();
		RemoveRequiredConnectionTool();
		RemoveElectiveConnectionTool();
		//请注意下面这段代码，里面有错误，其实每次进入下面这个if循环都会创建新的rootShape和两种rootConnection,日不过软件在目前的使用中没有表现出来罢了@吴韬
		if(curDiagram.getFather() == null)
		{
			AddShapeTool(rootShape);

			AddRequiredConnectionTool(requiredRootConnection);

			AddElectiveConnectionTool(electiveRootConnection);
		}
		else
		{
			List<Shape> concepts = curDiagram.getFather().getChildren();
			Connection tempConnection;
			for(int i=0;i<concepts.size();i++){
				Shape shapeTemp=(Shape)concepts.get(i);
				AddShapeTool(shapeTemp);
				for(int j=0;j<shapeTemp.getSourceConnections().size();j++){
					tempConnection=(Connection)shapeTemp.getSourceConnections().get(j);
					if(tempConnection.isRequired())
						AddRequiredConnectionTool(tempConnection);
					else 
						AddElectiveConnectionTool(tempConnection);
				}
			}
		}
	}
	public PaletteDrawer getRequiredConnectionDrawer() {
		return requiredConnectionDrawer;
	}
	public PaletteDrawer getElectiveConnectionDrawer() {
		return electiveConnectionDrawer;
	}
}
