package cn.edu.pku.ogeditor.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.parts.DiagramEditPart;

public class RelocateAction extends SelectionAction {
	private ShapesEditor shapesEdtior;
	public RelocateAction(ShapesEditor shapesEditor) {
		super(shapesEditor);
		this.shapesEdtior = shapesEditor;
		this.setText("Relocate");
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
		shapesEdtior.getDiagram().fireRelocate();
	}
}