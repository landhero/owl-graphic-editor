package cn.edu.pku.ogeditor.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import cn.edu.pku.ogeditor.commands.ConnectionRenameCommand;
import cn.edu.pku.ogeditor.figures.ConnectionFigure;
import cn.edu.pku.ogeditor.model.Connection;

public class ConnectionDirectEditPolicy  extends DirectEditPolicy{

    protected Command getDirectEditCommand(DirectEditRequest request) {
        ConnectionRenameCommand cmd = new ConnectionRenameCommand();
        cmd.setConnection((Connection) getHost().getModel());
        cmd.setName((String) request.getCellEditor().getValue());
        return cmd;
    }
    protected void showCurrentEditValue(DirectEditRequest request) {
        String value = (String) request.getCellEditor().getValue();
        ((ConnectionFigure)getHostFigure()).setLabelName(value);
    }
}
