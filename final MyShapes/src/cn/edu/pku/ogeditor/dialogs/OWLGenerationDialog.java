package cn.edu.pku.ogeditor.dialogs;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class OWLGenerationDialog extends Dialog {

	public OWLGenerationDialog(Shell shell) {
		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		super.configureShell(newShell);
	    newShell.setText("OWL Generation");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());
		container.setSize(1000, 1000);
		FormData data;
		
		final Label genL = new Label(container, SWT.NONE);
		genL.setText(".owl generation:");
		Button genb = new Button(container, SWT.PUSH);
		genb.setText("Generate");
		
		Text owlArea = new Text(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		
		final Label consistL = new Label(container, SWT.NONE);
		consistL.setText("Consistency Check:");
		Button check = new Button(container, SWT.PUSH);
		check.setText("Check");
		
		Text consistArea = new Text(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);

		
		data = new FormData();
		data.left = new FormAttachment(1);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(1);
		genL.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(genL, 0, SWT.LEFT);
		data.right = new FormAttachment(genL, -100, SWT.RIGHT);
		data.top = new FormAttachment(genL, 0, SWT.BOTTOM);
//		data.bottom = new FormAttachment(genL, 300, SWT.BOTTOM);
		data.bottom = new FormAttachment(70);
		owlArea.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(owlArea, 10, SWT.RIGHT);
		data.right = new FormAttachment(genL, 0, SWT.RIGHT);
		data.top = new FormAttachment(genL, 0, SWT.BOTTOM);
//		data.bottom = new FormAttachment(genL, 0, SWT.BOTTOM);
		genb.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(genL, 0, SWT.LEFT);
		data.right = new FormAttachment(genL, 0, SWT.RIGHT);
		data.top = new FormAttachment(owlArea, 30, SWT.BOTTOM);
		consistL.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(owlArea, 0, SWT.LEFT);
		data.right = new FormAttachment(owlArea, 0, SWT.RIGHT);
		data.top = new FormAttachment(consistL, 0, SWT.BOTTOM);
//		data.bottom = new FormAttachment(consistL, 300, SWT.BOTTOM);
		data.bottom = new FormAttachment(100);
		consistArea.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(genb, 0, SWT.LEFT);
		data.right = new FormAttachment(genb, 0, SWT.RIGHT);
		data.top = new FormAttachment(consistL, 0, SWT.BOTTOM);
//		data.bottom = new FormAttachment(genL, 0, SWT.BOTTOM);
		check.setLayoutData(data);
		
		return container;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "close",
				true);
	}
	@Override
	protected void initializeBounds() {
		Shell shell = getShell();
		Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();  
		Insets scrInsets=Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
		shell.setBounds(scrInsets.left,scrInsets.top,scrSize.width-scrInsets.left-scrInsets.right,scrSize.height-scrInsets.top-scrInsets.bottom);
		super.initializeBounds();
	}
}
