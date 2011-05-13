package cn.edu.pku.ogeditor.parts;

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
