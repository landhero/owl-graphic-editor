package cn.edu.pku.ogeditor.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.pku.ogeditor.model.Shape;

public class ShapeRenameCommand extends Command {
	private String oldText, newText;
	private Shape shape;
	
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
