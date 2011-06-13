package cn.edu.pku.ogeditor.anchor;

import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

public abstract class BorderAnchor extends EllipseAnchor {
	protected double angle;

	public BorderAnchor(IFigure figure) {
		super(figure);
		angle = Double.MAX_VALUE;
	}

	public abstract Point getBorderPoint(Point reference);

	public Point getLocation(Point reference) {
		// ���angleû�б���ʼ����ʹ��ȱʡ��ChopboxAnchor���������һ���߽�ê��
		if (angle == Double.MAX_VALUE)
			return super.getLocation(reference);
		else
			return getBorderPoint(reference);
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
}