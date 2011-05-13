/*******************************************************************************
 * Copyright (c) 2004, 2008 Elias Volanakis and others.
?* All rights reserved. This program and the accompanying materials
?* are made available under the terms of the Eclipse Public License v1.0
?* which accompanies this distribution, and is available at
?* http://www.eclipse.org/legal/epl-v10.html
?*
?* Contributors:
?*????Elias Volanakis - initial API and implementation
?*******************************************************************************/
package cn.edu.pku.ogeditor;

import java.util.ArrayList;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.EllipseShape;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;

/**
 * Utility class that can create a GEF Palette.
 * 
 * @see #createPalette()
 */
public final class ShapesEditorPaletteFactory {

	private static PaletteRoot palette = null;
	private static ArrayList<Connection> allUplevelRequiredConnections = new ArrayList<Connection>();
	private static PaletteDrawer requiredConnectionDrawer;
	private static ArrayList<Connection> allUplevelElectiveConnections = new ArrayList<Connection>();
	private static PaletteDrawer electiveConnectionDrawer;
	private static ArrayList<Shape> allUplevelShapes=new ArrayList<Shape>();
	private static PaletteDrawer conceptDrawer;
	/**
	 * @return the allConnectionTool
	 */
	public static ArrayList<Connection> getUplevelAllRequiredConnections() {
		return allUplevelRequiredConnections;
	}
	public static ArrayList<Connection> getUplevelAllElectiveConnections() {
		return allUplevelElectiveConnections;
	}
	/**
	 * @return the allShapeTool
	 */
	public static ArrayList<Shape> getUplevelAllShapes() {
		return allUplevelShapes;
	}

	//	private static boolean dirty = false;
	public static PaletteDrawer getShapeDrawer(){
		return conceptDrawer;
	}

	/**
	 * Creates the PaletteRoot and adds all palette elements. Use this factory
	 * method to create a new palette for your graphical editor.
	 * 
	 * @return a new PaletteRoot
	 */
	static PaletteRoot createPalette() {
		if (palette == null) {
			palette = new PaletteRoot();
			palette.add(createToolsGroup(palette));
			init();
		}
		// SimpleFactory a = new SimpleFactory(null);
		return palette;
	}

	/** Create the "Tools" group. */
	private static PaletteContainer createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar("Tools");

		// Add a selection tool to the group
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		palette.setDefaultEntry(tool);

		// Add a marquee tool to the group
		toolbar.add(new MarqueeToolEntry());

		return toolbar;
	}
	
	private static void init() {
		conceptDrawer = new PaletteDrawer("Concepts");
		requiredConnectionDrawer=new PaletteDrawer("Required Ralations");
		electiveConnectionDrawer=new PaletteDrawer("Elective Ralations");
		palette.add(conceptDrawer);
		palette.add(requiredConnectionDrawer);
		palette.add(electiveConnectionDrawer);
	}

	public static void RemoveRequiredConnectionTool() {
		int size = requiredConnectionDrawer.getChildren().size();
		for (int i = 0; i < size; i++) {
			requiredConnectionDrawer.remove((PaletteEntry) requiredConnectionDrawer
					.getChildren().get(0));
			allUplevelRequiredConnections.remove(0);
		}
	}

	public static void RemoveElectiveConnectionTool() {
		int size = electiveConnectionDrawer.getChildren().size();
		for (int i = 0; i < size; i++) {
			electiveConnectionDrawer.remove((PaletteEntry) electiveConnectionDrawer
					.getChildren().get(0));
			allUplevelElectiveConnections.remove(0);
		}
	}
	public static void AddRequiredConnectionTool(final Connection connection) {
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
	public static void AddElectiveConnectionTool(final Connection connectionTool) {
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

	public static void RemoveShapeTool() {
		int size = conceptDrawer.getChildren().size();
		for (int i = 0; i < size; i++) {
			conceptDrawer.remove((PaletteEntry) conceptDrawer.getChildren().get(0));
			allUplevelShapes.remove(0);
		}
	}
	public static void AddShapeTool(final Shape shape) {
		allUplevelShapes.add(shape);
		if (conceptDrawer != null) {
			CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(
					shape.getName(), 
					"Create a " + shape.getName(), 
					EllipseShape.class,
					new ShapesCreationFactory(shape.getName()),
					ImageDescriptor.createFromFile(ShapesPlugin.class,
					"icons/ellipse16.gif"), 
					ImageDescriptor.createFromFile(ShapesPlugin.class,
					"icons/ellipse24.gif"));
			conceptDrawer.add(component);
		}
	}
	public static void refresh(ShapesDiagram _diagram) {
		allUplevelRequiredConnections = new ArrayList<Connection>();
		allUplevelElectiveConnections = new ArrayList<Connection>();
		palette.remove(conceptDrawer);
		palette.remove(requiredConnectionDrawer);
		palette.remove(electiveConnectionDrawer);
		init();

	}
	public static PaletteDrawer getRequiredConnectionDrawer() {
		return requiredConnectionDrawer;
	}
	public static PaletteDrawer getElectiveConnectionDrawer() {
		return electiveConnectionDrawer;
	}
}