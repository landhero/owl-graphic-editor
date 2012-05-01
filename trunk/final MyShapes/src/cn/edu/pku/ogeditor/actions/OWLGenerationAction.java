package cn.edu.pku.ogeditor.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.widgets.Display;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.dialogs.OWLGenerationDialog;
import cn.edu.pku.ogeditor.parts.DiagramEditPart;

public class OWLGenerationAction extends SelectionAction {

	private ShapesEditor se;
	public OWLGenerationAction(ShapesEditor shapesEditor) {
		super(shapesEditor);
		this.se = shapesEditor;
		this.setText(ActionConstant.OWLGENERATION_TEXT);
	}

	@Override
	protected boolean calculateEnabled() {
		List<?> objects = getSelectedObjects();
	    if (objects.isEmpty())
	        return false;
	    for (Iterator<?> iter = objects.iterator(); iter.hasNext();) {
	    	Object obj = iter.next();
	    	if (obj instanceof DiagramEditPart)
	    	{
	    		return true;
	    	}
	    }
	    return false;
	}

	@Override
	public void run(){
		OWLGenerationDialog dialog = new OWLGenerationDialog(Display.getDefault().getActiveShell(), se);
		dialog.open();
	}
}
