package cn.edu.pku.ogeditor;

import org.eclipse.gef.requests.CreationFactory;

import cn.edu.pku.ogeditor.model.Shape;

public class ShapesCreationFactory implements CreationFactory {

	private String shapeName;
	public ShapesCreationFactory(String shapeName) {
		// TODO Auto-generated constructor stub
		this.shapeName = shapeName;
	}

	@Override
	public Object getNewObject() {
		// TODO Auto-generated method stub
		try {
			Shape newShape = Shape.class.newInstance();
			newShape.setName(shapeName);
			return newShape;
		} catch (Exception exc) {
			return null;
		}
	}

	@Override
	public Object getObjectType() {
		// TODO Auto-generated method stub
		return Shape.class;
	}

}
