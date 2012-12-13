/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import cn.edu.pku.ogeditor.model.Connection;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;

/**
 * Create new new .ogeditor-file. Those files can be used with the ShapesEditor
 * (see plugin.xml).
 * 
 * @author Xueyuan Xing
 * @author Hansheng Zhang
 * @author Tao Wu
 */
public class ShapesCreationWizard extends Wizard implements INewWizard {

	private CreationPage page1;
//	private AddDectectedObjectsPage page2;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		// add pages to this wizard
		addPage(page1);
//		addPage(page2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// create pages for this wizard
		setWindowTitle("OWL Graphic Editor");
		page1 = new CreationPage(workbench, selection);
//		page2 = new AddDectectedObjectsPage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		// TODO 自动生成方法存根
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					// TODO 自动生成方法存根
					doFinish(monitor);
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	protected void doFinish(IProgressMonitor monitor) {

		/* AddressList[] lists = editListsConfigPage.getSelection(); */

		// 下面做相应的增加地址项和地址信息列表的操作

		Display display = null;
		Shell shell = getShell();
		if (shell == null)
			display = PlatformUI.getWorkbench().getDisplay();
		else
			display = shell.getDisplay();
		display.asyncExec(new Runnable() {
			public void run() {

				page1.finish();

//				if (getContainer().getCurrentPage() == page1) {
//					page1.finish();
//				} else if (getContainer().getCurrentPage() == page2) {
//					page1.createDefaultContent();
//					page1.getShapeDiagram().setObjects(page2.getObjects());
//					initDiagram();
//					page1.finish();
//				}
			}

			private void initDiagram() {
				Shape rootShape = new Shape();
				rootShape.setRoot(true);	//Thing为根，但从未在Diagram里创建
				rootShape.setName("Thing");
				rootShape.setColor(ColorConstants.orange.getRGB());
				
				Connection rootConnection = new Connection("ConnectionRoot");
				rootConnection.setName("Relation");
				
				ShapesDiagram diagram = page1.getShapeDiagram();
				ObjectsListModel objectsModel = diagram.getObjects();
				if (null == objectsModel)
					return;
				Object[] objects = objectsModel.elements();
//				for (Object cur : objects) {
//					ObjectInfo object = (ObjectInfo)cur;
//					Shape parentShape = diagram.getShapeByName(object.getType());
//					if(null == parentShape)
//					{
//						try {
//							parentShape = Shape.class.newInstance();
//							parentShape.setName(object.getType());
//							createShape(parentShape, rootShape, diagram);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					try {
//						Shape shape = Shape.class.newInstance();
//						shape.setName(object.getRfid());
//						createShape(shape, rootShape, diagram);
//						Connection connection = new Connection(shape, parentShape, "instance of");
//
//						connection.setParent(rootConnection);
//						rootConnection.addChild(connection);
//						shape.getDiagram().addConnection(connection);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
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
				if(diagram.addChild(shape))
					shape.setDiagram(diagram);
			}
		});
	}
}
