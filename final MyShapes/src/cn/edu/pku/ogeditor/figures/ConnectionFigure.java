/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
/**
 * the figure of relation
 * @author Xueyuan Xing
 */
public class ConnectionFigure extends PolylineConnection {
	private Label label;
	public ConnectionFigure(String name)
	{
		this.setTargetDecoration(new PolygonDecoration()); // arrow at target endpoint
		setLabel(new Label());
		getLabel().setOpaque(true);
		getLabel().setText(name);
		getLabel().setBackgroundColor(ColorConstants.lightGray);
		this.add(getLabel(), new MidpointLocator(this, 0));
	}
	public void setLabel(Label label) {
		this.label = label;
	}
	public Label getLabel() {
		return label;
	}
	public void setLabelName(String name)
	{
		getLabel().setText(name);
	}
}
