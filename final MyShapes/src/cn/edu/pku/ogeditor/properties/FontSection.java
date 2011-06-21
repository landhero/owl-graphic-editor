package cn.edu.pku.ogeditor.properties;

import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import cn.edu.pku.ogeditor.figures.ShapeFigure;
import cn.edu.pku.ogeditor.parts.ShapeEditPart;

public class FontSection extends AbstractPropertySection {

	private Text fontText;
	private ShapeEditPart sep;
	public FontSection() {
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

		CLabel fontLabel = getWidgetFactory().createCLabel(composite, "Font:"); //$NON-NLS-1$
		fontText = getWidgetFactory().createText(composite, ""); //$NON-NLS-1$
		fontText.setEditable(false);
		Button fontButton = getWidgetFactory().createButton(composite,
				"Change...", SWT.PUSH); //$NON-NLS-1$
		fontButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				FontDialog ftDialog = new FontDialog(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell());

				FontData fontdata = ((ShapeFigure)sep.getFigure())
				.getFont().getFontData()[0];
				String value = fontdata.toString();

				if ((value != null) && (value.length() > 0)) {
					ftDialog.setFontList(new FontData[] {new FontData(value)});
				}
				FontData fData = ftDialog.open();

				if (fData != null) {
					value = fData.toString();
					Font newFont = new Font(null,fData);
					((ShapeFigure)sep.getFigure()).setFont(newFont);
					fontText.setText(StringConverter.asString(fData));
				}
			}
		});

		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(fontText,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		fontLabel.setLayoutData(data);

		data = new FormData();
		data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
		data.right = new FormAttachment(fontButton,
				-ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(0, 0);
		fontText.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(0, 0);
		data.height = buttonHeight;
		fontButton.setLayoutData(data);
	}
	public void refresh() {
		if(((ShapeFigure)sep.getFigure()).getFont() != null)
		{
			FontData fontData = ((ShapeFigure)sep.getFigure()).getFont().getFontData()[0];
			String value = fontData.toString();

			if ((value != null) && (value.length() > 0)) {
				fontText.setText(StringConverter.asString(fontData));
			}
		}
	}
}
