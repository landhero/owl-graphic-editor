package cn.edu.pku.ogeditor.parts;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

import cn.edu.pku.ogeditor.figures.ShapeFigure;

public class ShapeCellEditorLocator implements CellEditorLocator{
	private ShapeFigure shapeFigure;
	public ShapeCellEditorLocator(ShapeFigure f){
		shapeFigure = f;
	}
	
	
	
	public void relocate(CellEditor celleditor) {
		// TODO Auto-generated method stub
	      Text text = (Text) celleditor.getControl();
	      //Point pref = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	      Rectangle rect = shapeFigure.getLabel().getTextBounds();
	      shapeFigure.getLabel().translateToAbsolute(rect);
	      text.setBounds(rect.x - 1, rect.y - 1, rect.width + 1, rect.height + 1);
	}

}
