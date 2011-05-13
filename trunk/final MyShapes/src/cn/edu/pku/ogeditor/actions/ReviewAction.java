package cn.edu.pku.ogeditor.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.model.ShapesDiagram;
import cn.edu.pku.ogeditor.parts.*;

public class ReviewAction extends SelectionAction {
	private ShapesDiagram diagram;
	public ReviewAction(ShapesEditor shapesEditor) {
		super(shapesEditor);
		diagram = shapesEditor.getDiagram();
		this.setText("Review");
		// TODO Auto-generated constructor stub
	}
	@Override
	protected boolean calculateEnabled() {
		List<?> objects = getSelectedObjects();
	    if (objects.isEmpty())
	        return false;
	    for (Iterator<?> iter = objects.iterator(); iter.hasNext();) {
	    	Object obj = iter.next();
	    	if (! (obj instanceof DiagramEditPart))
	    		return false;
	    }
	    return true;
	}
	@Override
	public void run(){
		diagram.fireRelocate();
	}
}
