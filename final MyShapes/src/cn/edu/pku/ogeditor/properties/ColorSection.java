package cn.edu.pku.ogeditor.properties;

import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import cn.edu.pku.ogeditor.figures.ShapeFigure;
import cn.edu.pku.ogeditor.parts.ShapeEditPart;

public class ColorSection extends AbstractPropertySection {

	private Text colorText;
	private ShapeEditPart sep;
	public ColorSection() {
		// TODO Auto-generated constructor stub
	}
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		if(!(selection instanceof IStructuredSelection))
		{
			return;
		}
		Object input = ((IStructuredSelection) selection).getFirstElement();
		if(input instanceof ShapeEditPart)
			sep = (ShapeEditPart) input;
	}
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
		.createFlatFormComposite(parent);
		FormData data;

		Shell shell = new Shell();
		GC gc = new GC(shell);
		gc.setFont(shell.getFont());
		Point point = gc.textExtent("");//$NON-NLS-1$
		int buttonHeight = point.y + 5;
		gc.dispose();
		shell.dispose();

		CLabel colorLabel = getWidgetFactory().createCLabel(composite, "Color:"); //$NON-NLS-1$
		colorText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		colorText.setEditable(false);
		Button colorButton = getWidgetFactory().createButton(composite,
				"Change...", SWT.PUSH); //$NON-NLS-1$
		colorButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				ColorDialog clDialog = new ColorDialog(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell());

				RGB clData = clDialog.open();

				if (clData != null) {
					//value = clData.toString();
					Color newColor = new Color(null,clData);
					sep.getCastedModel().setColor(newColor.getRGB());
					colorText.setText(StringConverter.asString(clData));
				}
			}
		});

		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(colorText,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		colorLabel.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(colorButton,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		colorText.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		data.height = buttonHeight;
		colorButton.setLayoutData(data);
	}
	public void refresh() {
		RGB colorData = ((ShapeFigure)sep.getFigure()).getEllipseFigure().getBackgroundColor().getRGB();
		String value = colorData.toString();

		if ((value != null) && (value.length() > 0)) {
			colorText.setText(StringConverter.asString(colorData));
		}
	}
}
