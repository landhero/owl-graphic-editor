package cn.edu.pku.ogeditor.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.widgets.Display;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.dialogs.SystemGenerationDialog;
import cn.edu.pku.ogeditor.parts.DiagramEditPart;

public class SystemGenerationAction extends SelectionAction {

	public SystemGenerationAction(ShapesEditor shapesEditor) {
		super(shapesEditor);
		//this.shapesEdtior = shapesEditor;
		this.setText(ActionConstant.SYSTEMGENERATION_TEXT);
	}

	@Override
	protected boolean calculateEnabled() {
		List<?> objects = getSelectedObjects();
	    if (objects.isEmpty())
	        return false;
	    for (Iterator<?> iter = objects.iterator(); iter.hasNext();) {
	    	Object obj = iter.next();
	    	if (obj instanceof DiagramEditPart)
	    		return true;
	    }
	    return false;
	}

	@Override
	public void run(){
		SystemGenerationDialog dialog = new SystemGenerationDialog(Display.getDefault().getActiveShell());
		dialog.open();
	}
}
