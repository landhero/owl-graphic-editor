/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.anchor;

import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
/**
 * the base class of the anchor
 * @author Xueyuan Xing
 */
public abstract class BorderAnchor extends EllipseAnchor {
	protected double angle;

	public BorderAnchor(IFigure figure) {
		super(figure);
		angle = Double.MAX_VALUE;
	}

	public abstract Point getBorderPoint(Point reference);

	/**
	 * get the location of the anchor
	 * @param reference the reference point
	 * @return the anchor point
	 */
	public Point getLocation(Point reference) {
		// 如果angle没有被初始化，使用缺省的ChopboxAnchor，否则计算一个边界锚点
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