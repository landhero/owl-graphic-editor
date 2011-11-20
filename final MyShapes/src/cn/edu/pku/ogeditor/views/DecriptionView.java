/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.views;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import cn.edu.pku.ogeditor.model.Shape;
/**
 * the decription for a concept
 * @author Hansheng Zhang
 */
public class DecriptionView extends ViewPart {
	Label label;
	Text text;
	
	private Shape father;
	public DecriptionView(){}
	@Override
	public void createPartControl(Composite composite) {
		// TODO Auto-generated method stub
		composite.setLayout(new GridLayout());
		label = new Label(composite,SWT.WRAP);
		label.setText("");
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		text = new Text(composite,SWT.WRAP);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.addModifyListener(new ModifyListener(){
			//@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				if (father!=null){
					father.setDescription(text.getText());
				}
			}}
		);
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}
	public String getText(){
		return text.getText();
	}
	public void setText(String _s){
		text.setText(_s);
	}
	public void setFather(Shape _shape){
		if (_shape != null){
			father = _shape;
			text.setText(_shape.getDescription());
			label.setText("description for "+_shape.getName());
		}
	}
}
