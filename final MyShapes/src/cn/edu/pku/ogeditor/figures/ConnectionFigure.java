package cn.edu.pku.ogeditor.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;

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
