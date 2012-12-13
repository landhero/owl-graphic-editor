package cn.edu.pku.ogeditor.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.pku.ogeditor.model.Connection;
/**
 * rename the relation between two concepts
 * @author Hansheng Zhang
 */
public class ConnectionRenameCommand extends Command {

    private Connection connection;

    private String newName;

    private String oldName;

    public void setName(String name) {
        newName = name;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

	public boolean canExecute()
	{
//		if(connection.getSource().getDiagram().ContainConnectionName(newName))
//		{
//			connection.setName(connection.getName());
//			return false;
//		}	
		return true;
	}
    public void execute() {
        oldName = connection.getName();
        connection.setName(newName);
        connection.getSource().getDiagram().addConnectionName(connection);
    }

    public void redo() {
        connection.setName(newName);
    }

    public void undo() {
        connection.setName(oldName);
    }

    public String getLabel() {
        return "Rename Connection";
    }
}