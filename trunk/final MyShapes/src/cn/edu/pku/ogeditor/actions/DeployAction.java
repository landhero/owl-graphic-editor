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

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.iot.IOTAuto;
import cn.edu.pku.ogeditor.parts.DiagramEditPart;
/**
 * the action used to relocate all the shapes
 * @author Xueyuan Xing
 */
public class DeployAction extends SelectionAction {
	private ShapesEditor shapesEdtior;
	public static final String FILE_PATH = "file:IOT.owl";
	public static final String FILE_EXTENSION = "owl";
	public DeployAction(ShapesEditor shapesEditor) {
		super(shapesEditor);
		this.shapesEdtior = shapesEditor;
		this.setText(ActionConstant.DEPLOY_TEXT);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected boolean calculateEnabled() {
		List<?> objects = getSelectedObjects();
	    if (objects.isEmpty())
	        return false;
	    if(IOTAuto.isSHOULD_RUN())
	    {
	    	return false;
	    }
	    for (Iterator<?> iter = objects.iterator(); iter.hasNext();) {
	    	Object obj = iter.next();
	    	if (! (obj instanceof DiagramEditPart) && IOTAuto.isSHOULD_RUN())
	    		return false;
	    }
	    return true;
	}
	@Override
	public void run(){
		shapesEdtior.SaveAsOWL(FILE_PATH, FILE_EXTENSION);
		IOTAuto.deploy();
	}
}
