package testFiles;

import java.io.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class HelloWorld {
	public static void main(String[] args) {
		final Display display = Display.getDefault();
		final Shell shell = new Shell();
		shell.setLayout(new RowLayout());
		final Canvas canvas = new Canvas(shell, SWT.BORDER);
		// 监听canvas的重绘事件
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(final PaintEvent event) {
				Image image = (Image) canvas.getData();
				if (image != null) {
					event.gc.drawImage(image, 10, 10);// 定位图像左上角距canvas左上角的距离
				}
			}
		});
		final Image refreshImage = new Image(display,
				"icons\\system.jpg");
		final Image nextIamge = new Image(display,
				"icons\\system.jpg");

		Button button1 = new Button(shell, SWT.NONE);
		button1.setText("大力水手");
		button1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				canvas.setData(nextIamge);
				canvas.redraw();
			}
		});
		Button button2 = new Button(shell, SWT.NONE);
		button2.setText("海绵宝宝");
		button2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				canvas.setData(refreshImage);
				canvas.redraw();
			}
		});
		Button clearButton = new Button(shell, SWT.NONE);
		clearButton.setText("清除图像");
		clearButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				canvas.setData(null);
				canvas.redraw();
			}
		});
		shell.layout();
		shell.open();
		shell.setSize(300, 200);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}