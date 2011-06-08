package cn.edu.pku.ogeditor.views;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.model.ShapesDiagram;
import cn.edu.pku.ogeditor.parts.ShapesEditPartFactory;
//xingxueyuan
public class TestView extends ViewPart {
ScrollingGraphicalViewer graphicalViewer;
FigureCanvas canvas;
ShapesDiagram diagram;
public void createPartControl(Composite parent) {
    graphicalViewer = new ScrollingGraphicalViewer();
    canvas = (FigureCanvas) graphicalViewer.createControl(parent);
    ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
    graphicalViewer.setRootEditPart(root);
    graphicalViewer.setEditDomain(new EditDomain());
    graphicalViewer.setEditPartFactory(new ShapesEditPartFactory());
    graphicalViewer.setContents(ShapesEditor.myselfShapesEditor.getDiagram());
}

@Override
public void setFocus() {
	// TODO Auto-generated method stub
	
}
}