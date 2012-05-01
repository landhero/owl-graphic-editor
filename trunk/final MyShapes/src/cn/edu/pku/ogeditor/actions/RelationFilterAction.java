/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.widgets.Display;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.dialogs.RelationFilterDialog;
import cn.edu.pku.ogeditor.parts.DiagramEditPart;
/**
 * action used to filter certain relations
 * @author Xueyuan Xing
 */
public class RelationFilterAction extends SelectionAction {
	public RelationFilterAction(ShapesEditor shapesEditor) {
		super(shapesEditor);
		//this.shapesEdtior = shapesEditor;
		this.setText(ActionConstant.RELATIONFILTER_TEXT);
		// TODO Auto-generated constructor stub
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
		RelationFilterDialog dialog = new RelationFilterDialog(Display.getCurrent().getActiveShell());
		//dialog.set
		dialog.open();
	}
}
