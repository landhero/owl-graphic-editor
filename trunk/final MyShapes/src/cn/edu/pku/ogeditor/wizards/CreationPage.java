package cn.edu.pku.ogeditor.wizards;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

import cn.edu.pku.ogeditor.model.ShapesDiagram;


/**
 * This WizardPage can create an empty .shapes file for the ShapesEditor.
 */
class CreationPage extends WizardNewFileCreationPage {
	private static int fileCount = 1;
	private static final String DEFAULT_EXTENSION = ".multipage";
	private final IWorkbench workbench;
	private ShapesDiagram shapeDiagram;

	/**
	 * Create a new wizard page instance.
	 * @param workbench the current workbench
	 * @param selection the current object selection
	 * @see ShapesCreationWizard#init(IWorkbench, IStructuredSelection)
	 */
	CreationPage(IWorkbench workbench, IStructuredSelection selection) {
		super("OWL Graphic Editor", selection);
		this.workbench = workbench;
		setTitle("Create a new " + DEFAULT_EXTENSION + " file");
		setDescription("Create a new " + DEFAULT_EXTENSION + " file");
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		setFileName("ogeditor" + fileCount + DEFAULT_EXTENSION);
		setPageComplete(validatePage());
	}
	
	/** Return a new ShapesDiagram instance. */
	public void createDefaultContent() {
		shapeDiagram = new ShapesDiagram();
//		return new ShapesDiagram();
	}
	
	/**
	 * This method will be invoked, when the "Finish" button is pressed.
	 * @see ShapesCreationWizard#performFinish()
	 */
	boolean finish() {
		// create a new file, result != null if successful
		IFile newFile = createNewFile();
		fileCount++;
		
		// open newly created file in the editor
		IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
		if (newFile != null && page != null) {
			try {
				IDE.openEditor(page, newFile, true);
			} catch (PartInitException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#getInitialContents()
	 */
	protected InputStream getInitialContents() {
		if(null == shapeDiagram)
		{
			createDefaultContent();
		}
		ByteArrayInputStream bais = null;
		try {
			shapeDiagram.setFileName(getFileName());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(shapeDiagram); // argument must be Serializable
			oos.flush();
			oos.close();
			bais = new ByteArrayInputStream(baos.toByteArray());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return bais;
	}
	
	/**
	 * Return true, if the file name entered in this page is valid.
	 */
	private boolean validateFilename() {
		if (getFileName() != null && getFileName().endsWith(DEFAULT_EXTENSION)) {
			return true;
		}
		setErrorMessage("The 'file' name must end with " + DEFAULT_EXTENSION);
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validatePage()
	 */
	protected boolean validatePage() {
		return super.validatePage() && validateFilename();
	}
	
	public ShapesDiagram getShapeDiagram() {
		return shapeDiagram;
	}

	public void setShapeDiagram(ShapesDiagram shapeDiagram) {
		this.shapeDiagram = shapeDiagram;
	}
}
