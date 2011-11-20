/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.model;

import org.eclipse.draw2d.geometry.Dimension;
/**
 * model of the bend point
 * @author Tao Wu
 */
public class ConnectionBendpoint extends ModelElement {

	private static final long serialVersionUID = 1L;
	private float weight = 0.5f;
	private Dimension d1, d2;

	public ConnectionBendpoint() {}

	public Dimension getFirstRelativeDimension() {
		return d1;
	}

	public Dimension getSecondRelativeDimension() {
		return d2;
	}

	public float getWeight() {
		return weight;
	}

	public void setRelativeDimensions(Dimension dim1, Dimension dim2) {
		d1 = dim1;
		d2 = dim2;
	}

	public void setWeight(float w) {
		weight = w;
	}

}
