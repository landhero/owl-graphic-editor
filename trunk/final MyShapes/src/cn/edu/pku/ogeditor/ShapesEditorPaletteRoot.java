/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.MarqueeSelectionTool;
import org.eclipse.jface.resource.ImageDescriptor;

import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;
/**
 * specialize the palette root of the ShapesEditor
 * @author Xueyuan Xing
 * @author Hansheng Zhang
 */
public class ShapesEditorPaletteRoot extends PaletteRoot {

	private ArrayList<Connection> allUpperLevelConnections = new ArrayList<Connection>();
	private PaletteDrawer connectionDrawer;
	private ArrayList<Shape> allUpperLevelShapes=new ArrayList<Shape>();
	private PaletteDrawer conceptDrawer;
	private Shape rootShape;
	private Connection rootConnection;
	private Shape separatorShape;
	private Connection separatorConnection;
	private Connection subClassConnection;

	public ShapesEditorPaletteRoot(){
		super();
		createToolsGroup();
		init();
	}
	/**
	 * @return the allConnectionTool
	 */
	public ArrayList<Connection> getAllUpperLevelConnections() {
		return allUpperLevelConnections;
	}
	/**
	 * @return the allShapeTool
	 */
	public ArrayList<Shape> getAllUpperLevelShapes() {
		return allUpperLevelShapes;
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

		PaletteStack marqueeStack = new PaletteStack("cn.edu.p$ku.ogeditor.MarqueeStack", "", null);
		MarqueeToolEntry marquee = new MarqueeToolEntry();
		marquee.setLabel("Marquee Concepts");
		marqueeStack.add(marquee);
		marquee = new MarqueeToolEntry();
		marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
				new Integer(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED));
		marquee.setLabel("Marquee Relations");
		marqueeStack.add(marquee);
		marquee = new MarqueeToolEntry();
		marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
				new Integer(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED 
						| MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED));
		marquee.setLabel("Marquee All");
		marqueeStack.add(marquee);
		marqueeStack.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
		entries.add(marqueeStack);

		controlGroup.addAll(entries);
		add(controlGroup);
	}

	private void init() {
		conceptDrawer = new PaletteDrawer("Concepts");
		connectionDrawer=new PaletteDrawer("Relations");
		//electiveConnectionDrawer=new PaletteDrawer("Elective Relations");
		add(conceptDrawer);
		add(connectionDrawer);
		//add(electiveConnectionDrawer);
		rootShape=new Shape();
		rootShape.setRoot(true);	//Thing为根，但从未在Diagram里创建
		rootShape.setName("Thing");
		rootShape.setColor(ColorConstants.orange.getRGB());
		addShapeTool(rootShape);

		separatorShape=new Shape();
		separatorShape.setSeparator(true);
		addShapeTool(separatorShape);

		separatorConnection=new Connection("Seperator");
		separatorConnection.setSeparator(true);


		rootConnection=new Connection("ConnectionRoot");
		rootConnection.setName("Relation");
		rootConnection.setRequired(true);
		addConnectionTool(rootConnection);
//		addConnectionTool(separatorConnection);
		
		subClassConnection = new Connection("subClassOf");
		subClassConnection.setName("subClassOf");
		subClassConnection.setRequired(true);
		addConnectionTool(subClassConnection);
		addConnectionTool(separatorConnection);

	}
	public void addShapeTool(final Shape shape) {
		allUpperLevelShapes.add(shape);
		if(shape.isSeparator())
		{
			conceptDrawer.add(new PaletteSeparator());
			return;
		}
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
	public void addConnectionTool(final Connection connection) {
		allUpperLevelConnections.add(connection);
		if(connection.isSeparator())
		{
			connectionDrawer.add(new PaletteSeparator());
			return;
		}
		if (connectionDrawer != null) {
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
			connectionDrawer.add(tool);
		}
		else System.err.println("Drawer is null!");
	}

	public void removeConnectionTool() {
		int size = connectionDrawer.getChildren().size();
		for (int i = 0; i < size; i++) {
			connectionDrawer.remove((PaletteEntry) connectionDrawer
					.getChildren().get(0));
			allUpperLevelConnections.remove(0);
		}
	}

	public void removeShapeTool() {
		int size = conceptDrawer.getChildren().size();
		for (int i = 0; i < size; i++) {
			conceptDrawer.remove((PaletteEntry) conceptDrawer.getChildren().get(0));
			allUpperLevelShapes.remove(0);
		}
	}

	public void refresh() {
		ShapesDiagram curDiagram = ShapesEditor.myselfShapesEditor.getModel();
		removeShapeTool();
		removeConnectionTool();

		if(curDiagram.getFather() == null)
		{
			addShapeTool(rootShape);
			addShapeTool(separatorShape);

			addConnectionTool(rootConnection);
//			addConnectionTool(separatorConnection);
			
			addConnectionTool(subClassConnection);
			addConnectionTool(separatorConnection);
			return;
		}
			
		else
		{
			ShapesDiagram fatherDiagram = curDiagram.getFather();
			List<Shape> concepts = fatherDiagram.getAllShapesNames();
			for(int i=0;i<concepts.size();i++)
			{
				addShapeTool(concepts.get(i));
			}
			List<Connection> relations = fatherDiagram.getAllConnectionsNames();
			for(int i=0;i<relations.size();i++)
			{
				addConnectionTool(relations.get(i));
			}
		}
	}
	public PaletteDrawer getConnectionDrawer() {
		return connectionDrawer;
	}
}
