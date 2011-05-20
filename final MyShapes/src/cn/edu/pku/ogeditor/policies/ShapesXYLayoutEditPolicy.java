package cn.edu.pku.ogeditor.policies;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import cn.edu.pku.ogeditor.commands.ShapeCreateCommand;
import cn.edu.pku.ogeditor.commands.ShapeSetConstraintCommand;
import cn.edu.pku.ogeditor.model.Shape;
import cn.edu.pku.ogeditor.model.ShapesDiagram;
import cn.edu.pku.ogeditor.parts.ShapeEditPart;

/**
 * EditPolicy for the Figure used by this edit part.
 * Children of XYLayoutEditPolicy can be used in Figures with XYLayout.
 * @author Elias Volanakis
 */
public class ShapesXYLayoutEditPolicy extends XYLayoutEditPolicy {
	
	/* (non-Javadoc)
	 * @see ConstrainedLayoutEditPolicy#createChangeConstraintCommand(ChangeBoundsRequest, EditPart, Object)
	 */
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request,
			EditPart child, Object constraint) {
		if (child instanceof ShapeEditPart && constraint instanceof Rectangle) {
			// return a command that can move and/or resize a Shape
			return new ShapeSetConstraintCommand(
					(Shape) child.getModel(), request, (Rectangle) constraint);
		}
		return super.createChangeConstraintCommand(request, child, constraint);
	}
	
	/* (non-Javadoc)
	 * @see ConstrainedLayoutEditPolicy#createChangeConstraintCommand(EditPart, Object)
	 */
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		// not used in this example
		return null;
	}
	
	/* (non-Javadoc)
	 * @see LayoutEditPolicy#getCreateCommand(CreateRequest)
	 */
	protected Command getCreateCommand(CreateRequest request) {
		Object childClass = request.getNewObjectType();
		if(childClass == Shape.class)
			// return a command that can add a Shape to a ShapesDiagram 
			return new ShapeCreateCommand((Shape)request.getNewObject(), 
					(ShapesDiagram)getHost().getModel(), (Rectangle)getConstraintFor(request));
		return null;
	}
}