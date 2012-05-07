package cn.edu.pku.ogeditor.dialogs;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.edu.pku.ogeditor.ShapesEditor;

public class OWLGenerationDialog extends Dialog {

	private ShapesEditor se;
	private Text owlArea;
	private Text checkArea;
	public OWLGenerationDialog(Shell shell, ShapesEditor se) {
		super(shell);
		this.se = se;
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
		FormData data;
		
		final Label genL = new Label(container, SWT.NONE);
		genL.setText(".owl generation:");
		Button genb = new Button(container, SWT.PUSH);
		genb.setText("Generate");
		genb.addSelectionListener(new GenerateListener());
		
		owlArea = new Text(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		
		final Label consistL = new Label(container, SWT.NONE);
		consistL.setText("Consistency Check:");
		Button check = new Button(container, SWT.PUSH);
		check.setText("Check");
		check.addSelectionListener(new CheckListener());
		
		checkArea = new Text(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		
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
		checkArea.setLayoutData(data);
		
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
	
	private class GenerateListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			owlArea.setText("");
			String path = "D:\\Program Files\\eclipse\\myWorkspace\\OGEditor\\tmp\\" + se.getDiagram().getFileName() + ".owl";
			se.SaveAsOWL(path, "owl");
			File file = new File(path);
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line = new String();
				while(null != (line = br.readLine()))
				{
					owlArea.append(line + "\n");
				}
				fr.close();
				br.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private class CheckListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			checkArea.setText("");
			checkArea.setText("¡­¡­\nConsistency check is over¡­¡­\n");
		}
	}
}
