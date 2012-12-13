/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.views;

import javax.swing.JFrame;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.model.ShapesDiagram;
import cn.edu.pku.ogeditor.parts.ShapesEditPartFactory;
/**
 * this is just a test
 * @author Xueyuan Xing
 *
 */
public class TestView extends ViewPart {
ScrollingGraphicalViewer graphicalViewer;
FigureCanvas canvas;
ShapesDiagram diagram;
public void createPartControl(Composite parent) {
	Composite a = new Composite(parent, 0);
	Button b = new Button(parent, SWT.PUSH);
//    graphicalViewer = new ScrollingGraphicalViewer();
//    canvas = (FigureCanvas) graphicalViewer.createControl(parent);
//    ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
//    graphicalViewer.setRootEditPart(root);
//    graphicalViewer.setEditDomain(new EditDomain());
//    graphicalViewer.setEditPartFactory(new ShapesEditPartFactory());
//    graphicalViewer.setContents(ShapesEditor.myselfShapesEditor.getDiagram());
}

@Override
public void setFocus() {
	// TODO Auto-generated method stub
	
}
}