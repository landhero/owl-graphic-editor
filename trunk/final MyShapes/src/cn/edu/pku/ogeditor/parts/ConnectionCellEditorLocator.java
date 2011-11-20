/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.parts;
/**
 * CellEditorLocator of the relation. used to rename the relation
 * @author Hansheng Zhang
 */
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.widgets.Text;
import org.eclipse.draw2d.geometry.Rectangle;
public class ConnectionCellEditorLocator implements CellEditorLocator{
	private IFigure figure;
	
	public ConnectionCellEditorLocator(IFigure _f){
		figure = _f;
	}
	
	//@Override
	public void relocate(CellEditor celleditor) {
		// TODO Auto-generated method stub
		Text text = (Text) celleditor.getControl();
		Rectangle rect = figure.getBounds().getCopy();
		figure.translateToAbsolute(rect);
		text.setBounds(rect.x-1, rect.y-1, rect.width+1, rect.height+1);
	}
}
