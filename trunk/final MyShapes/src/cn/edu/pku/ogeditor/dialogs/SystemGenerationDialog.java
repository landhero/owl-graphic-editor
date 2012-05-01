package cn.edu.pku.ogeditor.dialogs;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SystemGenerationDialog extends Dialog {

	public SystemGenerationDialog(Shell shell) {
		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		super.configureShell(newShell);
	    newShell.setText("System Generation");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());
		container.setSize(1000, 1000);
		FormData data;
		
		final Label genL = new Label(container, SWT.NONE);
		genL.setText("Software System");
		Button genb = new Button(container, SWT.PUSH);
		genb.setText("Generate");
		
		Text systemArea = new Text(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		systemArea.setText("code generation");
		
		final Canvas canvas=new Canvas(container ,SWT.BORDER);
		
		ImageData imageData = new ImageData("D:\\Program Files\\eclipse\\myWorkspace\\OGEditor\\icons\\system.jpg");
		
		Image systemImage = new Image(Display.getDefault(), imageData.scaledTo(imageData.width, imageData.height));
		
		canvas.addPaintListener(new PaintListener(){
			 public void paintControl(final PaintEvent event){
			  Image image=(Image)canvas.getData();
			  if(image!=null){
			   event.gc.drawImage(image,40,10);//¶¨Î»Í¼Ïñ×óÉÏ½Ç¾àcanvas×óÉÏ½ÇµÄ¾àÀë
			  }
			 }
			});
		
		canvas.setData(systemImage);
		canvas.redraw();
			
//		final Label consistL = new Label(container, SWT.NONE);
//		consistL.setText("Consistency Check:");
//		Button check = new Button(container, SWT.PUSH);
//		check.setText("Check");
//		
//		Text consistArea = new Text(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);

		
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
		systemArea.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(systemArea, 0, SWT.LEFT);
		data.right = new FormAttachment(systemArea, 0, SWT.RIGHT);
		data.top = new FormAttachment(systemArea, 0, SWT.BOTTOM);
//		data.bottom = new FormAttachment(genL, 300, SWT.BOTTOM);
		data.bottom = new FormAttachment(100);
		canvas.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(systemArea, 10, SWT.RIGHT);
		data.right = new FormAttachment(genL, 0, SWT.RIGHT);
		data.top = new FormAttachment(genL, 0, SWT.BOTTOM);
//		data.bottom = new FormAttachment(genL, 0, SWT.BOTTOM);
		genb.setLayoutData(data);
		
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
