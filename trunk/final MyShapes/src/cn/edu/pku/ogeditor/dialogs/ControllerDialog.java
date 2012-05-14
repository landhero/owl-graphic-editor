package cn.edu.pku.ogeditor.dialogs;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import cn.edu.pku.ogeditor.ShapesEditor;
import cn.edu.pku.ogeditor.wizards.ObjectInfo;
import cn.edu.pku.ogeditor.wizards.ObjectsListModel;

public class ControllerDialog extends Dialog {

	private Text runningStatusArea;
	private boolean isRunning;
	private TableViewer viewer;
	private ShapesEditor editor;
	private StatusDisplay statusThread;
	private Font titleFont;
	public static final String START_MESS = ">control system start ...";
	public static final String[] STATUS = {">detecting objects' states ...", ">initializing instances ...", ">commiting to reasoner ...", ">carrying out inference ...", ">returning inferred results ...", ">resetting devices' states ..."};
	private static final int LABEL_LENGTH = 300;
	
	public ControllerDialog(Shell shell, ShapesEditor editor) {
		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.editor = editor;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		setRunning(true);
		super.configureShell(newShell);
	    newShell.setText("Controller");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());
		FormData data;
		
		Display display = Display.getDefault();
		Font bigTitleFont = new Font(display, "Arial", 14, SWT.BOLD);
		titleFont = new Font(display, "Arial", 12, SWT.BOLD);

		Font textFont = new Font(display, "Cambria", 12, SWT.NORMAL);
		
		final Label conL = new Label(container, SWT.SHADOW_OUT | SWT.CENTER);
		conL.setText("Controller");
		conL.setFont(bigTitleFont);
		
		final Label runningStatusL = new Label(container, SWT.NONE);
		runningStatusL.setText("Running status");
		runningStatusL.setFont(titleFont);
		
		runningStatusArea = new Text(container, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		runningStatusArea.setText(START_MESS + "\n");
		runningStatusArea.setFont(textFont);
		
		Button start = new Button(container, SWT.PUSH);
		start.setText("Start");
		start.setFont(titleFont);
		start.addSelectionListener(new StartListener());
		
		Button pause = new Button(container, SWT.PUSH);
		pause.setText("Pause");
		pause.setFont(titleFont);
		pause.addSelectionListener(new PauseListener());

		Button clear = new Button(container, SWT.PUSH);
		clear.setText("Clear");
		clear.setFont(titleFont);
		clear.addSelectionListener(new ClearListener());
		
		final Label DeviceListL = new Label(container, SWT.NONE);
		DeviceListL.setText("Device List");
		DeviceListL.setFont(titleFont);
		
		Table table = new Table(container, SWT.BORDER | SWT.MULTI
				| SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table.setFont(textFont);
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("RFID");
		column1.setWidth(LABEL_LENGTH);
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setWidth(LABEL_LENGTH);
		column2.setText("TYPE");
		TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setWidth(LABEL_LENGTH);
		column3.setText("STATUS");
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer = new TableViewer(table);
		viewer.setContentProvider(new TableContentProvider());
		viewer.setLabelProvider(new TableLabelProvider());

//		objects = new ObjectsListModel();
		viewer.setInput(editor.getDiagram().getRootDiagram().getObjects());
		
		ObjectsListModel objects = editor.getDiagram().getRootDiagram().getObjects();
		Object[] all = objects.elements();
		for(Object ob : all)
		{
			ObjectInfo object = (ObjectInfo)ob;
			if(Integer.parseInt(object.getRfid()) <= 14533 || Integer.parseInt(object.getRfid()) >= 14543)
				object.setOn(true);
		}
		viewer.refresh();
		
		data = new FormData();
		data.left = new FormAttachment(1);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(1, 10);
		conL.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(conL, 0, SWT.LEFT);
		data.right = new FormAttachment(conL, -100, SWT.RIGHT);
		data.top = new FormAttachment(conL, 10, SWT.BOTTOM);
		runningStatusL.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(runningStatusL, 0, SWT.LEFT);
		data.right = new FormAttachment(runningStatusL, 0, SWT.RIGHT);
		data.top = new FormAttachment(runningStatusL, 0, SWT.BOTTOM);
		data.bottom = new FormAttachment(60);
		runningStatusArea.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(runningStatusArea, 10, SWT.RIGHT);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(runningStatusL, 0, SWT.BOTTOM);
		start.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(runningStatusArea, 10, SWT.RIGHT);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(start, 0, SWT.BOTTOM);
		pause.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(runningStatusArea, 10, SWT.RIGHT);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(pause, 0, SWT.BOTTOM);
		clear.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(runningStatusL, 0, SWT.LEFT);
		data.right = new FormAttachment(runningStatusL, 0, SWT.RIGHT);
		data.top = new FormAttachment(runningStatusArea, 20, SWT.BOTTOM);
		DeviceListL.setLayoutData(data);
		
		FormData tableData = new FormData();
		tableData.left = new FormAttachment(DeviceListL, 0, SWT.LEFT);
		tableData.right = new FormAttachment(DeviceListL, 0, SWT.RIGHT);
		tableData.top = new FormAttachment(DeviceListL, 0, SWT.BOTTOM);
		tableData.bottom = new FormAttachment(100);
		table.setLayoutData(tableData);
		
		statusThread = new StatusDisplay(Display.getCurrent());
		statusThread.start();
		
		return container;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "close",
				true).setFont(titleFont);
	}
	@Override
	protected void initializeBounds() {
		Shell shell = getShell();
		Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();  
		Insets scrInsets=Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
		shell.setBounds(scrInsets.left + 100, scrInsets.top + 100, scrSize.width-scrInsets.left-scrInsets.right - 200, scrSize.height-scrInsets.top-scrInsets.bottom - 200);
		super.initializeBounds();
	}
	
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public boolean isRunning() {
		return isRunning;
	}
	
	private class StartListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			setRunning(false);
			while(statusThread.isAlive())
			{
				statusThread.stop();
			}
			setRunning(true);
			statusThread = new StatusDisplay(Display.getCurrent());
			statusThread.start();
		}
	}
	private class PauseListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			setRunning(false);
		}
	}
	private class ClearListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			runningStatusArea.setText("");
		}
	}

	class StatusDisplay extends Thread {
		private Display display;
		private int count;

		public StatusDisplay(Display display) {
			this.display = display;
			count = -1;
		}

		public void run() {
			// 模仿长时间的任务
			while (isRunning()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count++;
				if(count >= STATUS.length)
				{
					count = 0;
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				display.asyncExec(new Runnable() {
					public void run() {
						if(runningStatusArea.isDisposed())	
						{
							setRunning(false);
							return;
						}
						runningStatusArea.append(STATUS[count] + "\n");
					}
				});
			}
		}
	}
}
