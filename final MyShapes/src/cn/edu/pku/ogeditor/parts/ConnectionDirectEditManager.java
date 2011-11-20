/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.parts;
/**
 * edit manager of the relationship. used to rename the relation
 * @author Xueyuan Xing
 * @author Hansheng Zhang
 */
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

import cn.edu.pku.ogeditor.model.Connection;
public class ConnectionDirectEditManager extends DirectEditManager{
	protected VerifyListener verifyListener;
	private Connection connection;
	public ConnectionDirectEditManager (GraphicalEditPart source, 
			Class editorType, CellEditorLocator locator){
		super (source, editorType, locator);
		connection = (Connection)source.getModel();
	}

	protected void unhookListeners() {
		super.unhookListeners();
		Text text = (Text) getCellEditor().getControl();
		text.removeVerifyListener(verifyListener);
		verifyListener = null;
	}

	@Override
	protected void initCellEditor() {
		// TODO Auto-generated method stub
		verifyListener = new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				Text text = (Text) getCellEditor().getControl();
				String oldText = text.getText();
				String leftText = oldText.substring(0, event.start);
				String rightText = oldText.substring(event.end, oldText.length());
				GC gc = new GC(text);
				Point size = gc.textExtent(leftText + event.text + rightText);
				gc.dispose();
				if (size.x != 0) {
					size = text.computeSize(size.x, SWT.DEFAULT);
				}
				getCellEditor().getControl().setSize(size.x, size.y);
			}
		};

		getCellEditor().setValue(connection.getName());
		Text text = (Text) getCellEditor().getControl();
		text.addVerifyListener(verifyListener);
		text.selectAll();
	}
}
