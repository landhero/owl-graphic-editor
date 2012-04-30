/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Create new new .ogeditor-file. Those files can be used with the ShapesEditor
 * (see plugin.xml).
 * 
 * @author Xueyuan Xing
 * @author Hansheng Zhang
 * @author Tao Wu
 */
public class ShapesCreationWizard extends Wizard implements INewWizard {

	private CreationPage page1;
	private AddDectectedObjectsPage page2;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		// add pages to this wizard
		addPage(page1);
		addPage(page2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// create pages for this wizard
		setWindowTitle("OWL Graphic Editor");
		page1 = new CreationPage(workbench, selection);
		page2 = new AddDectectedObjectsPage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		// TODO 自动生成方法存根
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					// TODO 自动生成方法存根
					doFinish(monitor);
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	protected void doFinish(IProgressMonitor monitor) {

		/*AddressList[] lists = editListsConfigPage.getSelection();*/

		//下面做相应的增加地址项和地址信息列表的操作
		
		Display display = null;
		Shell shell = getShell();
		if (shell == null) display = PlatformUI.getWorkbench().getDisplay();
		else display = shell.getDisplay();
		display.asyncExec(new Runnable(){
			public void run() {

				if(getContainer().getCurrentPage() == page1)
				{
					page1.finish();
				}
				else if(getContainer().getCurrentPage() == page2)
				{
//					input = editListsConfigPage.getEditorInput();
//					AddressList[] lists = editListsConfigPage.getSelection();
//					item = editListsConfigPage.getAddressItem();
//					
//					AddressListManager listManager = input.getManager();
//					listManager.removeAll();
//					for(int i = 0; i < lists.length; i++){
//						listManager.add(lists[i], false);
//					}
				}
//				manager.addAddresses(new AddressItem[]{item});
//				try {
//					IWorkbenchPage page = PlatformUI.getWorkbench().
//					getActiveWorkbenchWindow().getActivePage();
//					page.openEditor(input, "com.plugindev.addressbook.tableEditor");
//					input.getManager().saveDescriptions();
//				} 
//				catch (PartInitException e) {
//					System.out.println(e);
//				}
			}
			
		});
		
	}

}
