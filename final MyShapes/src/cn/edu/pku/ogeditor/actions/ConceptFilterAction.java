package cn.edu.pku.ogeditor.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.widgets.Display;

import cn.edu.pku.ogeditor.ShapesEditor;

public class ConceptFilterAction extends SelectionAction {
	//private ShapesEditor shapesEdtior;
	public ConceptFilterAction(ShapesEditor shapesEditor) {
		super(shapesEditor);
		//this.shapesEdtior = shapesEditor;
		this.setText(ActionConstant.CONCEPTFILTER_TEXT);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected boolean calculateEnabled() {
		return true;
//		List<?> objects = getSelectedObjects();
//	    if (objects.isEmpty())
//	        return false;
//	    for (Iterator<?> iter = objects.iterator(); iter.hasNext();) {
//	    	Object obj = iter.next();
//	    	if (obj instanceof DiagramEditPart)
//	    		return true;
//	    }
//	    return false;
	}
	@Override
	public void run(){
		ConceptFilterDialog dialog = new ConceptFilterDialog(Display.getCurrent().getActiveShell());
		//dialog.set
		dialog.open();
	}
}
