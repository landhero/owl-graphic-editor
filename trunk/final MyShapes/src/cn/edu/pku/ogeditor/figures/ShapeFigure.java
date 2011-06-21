/*
 * Created on 2005-1-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cn.edu.pku.ogeditor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;

/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ShapeFigure extends Figure {
	private Ellipse ellipseFigure;
	private Label label;
	private Label labelParent;

	public ShapeFigure(String name, String parentName, RGB color) {
		ellipseFigure = new Ellipse();
		label = new Label();
		labelParent = new Label();
		ellipseFigure.setBackgroundColor(ColorConstants.orange);
		label.setText(name);
		labelParent.setText("Parent: " + parentName);
		ellipseFigure.setBackgroundColor(new Color(null, color));
		
		//添加顺序一定要注意
		this.add(ellipseFigure);
		this.add(labelParent);
		this.add(label);

	}

	public Label getLabel() {
		return label;
	}

	public String getText() {
		return this.getLabel().getText();
	}

	public void setArea(Point p) {
		Rectangle rect = this.ellipseFigure.getBounds();
		rect.width = p.x;
		rect.height = p.y;
		setBounds(rect);
	}

	public void setBounds(Rectangle rect) {
		super.setBounds(rect);
		Rectangle rectParentLabel = new Rectangle(rect);
		rectParentLabel.height = 20;
		Rectangle rectLabel = new Rectangle(rect);
		rectLabel.height = rect.height - rectParentLabel.height;
		rectLabel.y += rectParentLabel.height;

		this.ellipseFigure.setBounds(rectLabel);
		this.getLabel().setBounds(rectLabel);
		this.labelParent.setBounds(rectParentLabel);
	}

	public void setLabelName(String value) {
		// TODO Auto-generated method stub
		getLabel().setText(value);

	}

	public IFigure getEllipseFigure() {
		// TODO Auto-generated method stub
		return ellipseFigure;
	}
}