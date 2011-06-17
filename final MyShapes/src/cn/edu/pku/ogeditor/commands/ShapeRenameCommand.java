package cn.edu.pku.ogeditor.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.pku.ogeditor.model.Shape;

public class ShapeRenameCommand extends Command {
	private String oldText, newText;
	private Shape shape;
	
	public boolean canExecute()
	{
		if(shape.getDiagram().ContainShapeName(newText))
		{
			//弹出对话框之类
			shape.setName(shape.getName());
			return false;
		}	
		return true;
	}
	@Override
	public void execute(){
		oldText = shape.getName();
		shape.setName(newText);
	}
	public void setModel(Object model){
		shape = (Shape) model;
	}
	public void setText(String text){
		newText = text;
	}
	@Override
	public void undo(){
		shape.setName(oldText);
	}
}
