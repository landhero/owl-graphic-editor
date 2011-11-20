/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import cn.edu.pku.ogeditor.commands.ShapeRenameCommand;
import cn.edu.pku.ogeditor.figures.ShapeFigure;
/**
 * edit policy to direct edit the concept. used to rename the concept.
 * @author Xueyuan Xing
 */
public class ShapeDirectEditPolicy extends DirectEditPolicy{

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		// TODO Auto-generated method stub
		ShapeRenameCommand cmd = new ShapeRenameCommand();
		cmd.setModel(getHost().getModel());
		cmd.setText((String)request.getCellEditor().getValue());
		return cmd;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		// TODO Auto-generated method stub
	      String value = (String) request.getCellEditor().getValue();
	      ((ShapeFigure) getHostFigure()).setLabelName(value);
	}

}
