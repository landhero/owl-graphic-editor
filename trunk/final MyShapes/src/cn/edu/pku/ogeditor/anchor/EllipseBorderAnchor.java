package cn.edu.pku.ogeditor.anchor;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

public class EllipseBorderAnchor extends BorderAnchor {
	public EllipseBorderAnchor(IFigure figure) {
		super(figure);
	}

	@Override
	public Point getBorderPoint(Point reference) {
		//�õ�owner���Σ�ת��Ϊ��������
		Rectangle r = Rectangle.SINGLETON;
		r.setBounds(getOwner().getBounds());
		getOwner().translateToAbsolute(r);
		
		// ��Բ���̺�ֱ�߷��̣���2Ԫ2�η���
		double a = r.width >> 1;
		double b = r.height >> 1;
		double k = Math.tan(angle);		
		double dx = 0.0, dy = 0.0;
		
		dx = Math.sqrt(1.0 / (1.0 / (a * a) + k * k / (b * b)));
		if(angle > Math.PI / 2 || angle < -Math.PI / 2)
			dx = -dx;
		dy = k * dx;
		
		// �õ���Բ���ĵ㣬����ê��ƫ�ƣ��õ�����ê������
		PrecisionPoint pp = new PrecisionPoint(r.getCenter());	
		pp.translate((int)dx, (int)dy);
		return new Point(pp);	
	}
}