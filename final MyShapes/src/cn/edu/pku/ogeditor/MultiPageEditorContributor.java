package cn.edu.pku.ogeditor;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Manages the installation/deinstallation of global actions for multi-page
 * editors. Responsible for the redirection of global actions to the active
 * editor. Multi-page contributor replaces the contributors for the individual
 * editors in the multi-page editor.
 */
public class MultiPageEditorContributor extends
		MultiPageEditorActionBarContributor {
	private IEditorPart activeEditorPart;
	private Action sampleAction;

	/**
	 * Creates a multi-page contributor.
	 */
	public MultiPageEditorContributor() {
		super();
		createActions();
		// buildActions();
	}

	/**
	 * Returns the action registed with the given text editor.
	 * 
	 * @return IAction or null if editor is null.
	 */
	protected IAction getAction(ITextEditor editor, String actionID) {
		return (editor == null ? null : editor.getAction(actionID));
	}

	/*
	 * (non-JavaDoc) Method declared in
	 * AbstractMultiPageEditorActionBarContributor.
	 */

	public void setActivePage(IEditorPart part) {
		if (activeEditorPart == part)
			return;

		activeEditorPart = part;

		IActionBars actionBars = getActionBars();
		if (actionBars != null) {

			ShapesEditor editor = (part instanceof ShapesEditor) ? (ShapesEditor) part
					: null;
			if (null == editor)
				return;
			ActionRegistry registry = (ActionRegistry) editor
					.getAdapter(ActionRegistry.class);
//			for (int i = 0; i < globalActionKeys.size(); i++) {
//				String id = (String) globalActionKeys.get(i);
				actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), registry.getAction(ActionFactory.DELETE.getId()));
//			}

			// actionBars.setGlobalActionHandler(
			// ActionFactory.DELETE.getId(),
			// new DeleteRetargetAction());
			// actionBars.setGlobalActionHandler(
			// ActionFactory.UNDO.getId(),
			// new UndoRetargetAction());
			// actionBars.setGlobalActionHandler(
			// ActionFactory.REDO.getId(),
			// new RedoRetargetAction());
			// actionBars.setGlobalActionHandler(
			// ActionFactory.CUT.getId(),
			// getAction(editor, ITextEditorActionConstants.CUT));
			// actionBars.setGlobalActionHandler(
			// ActionFactory.COPY.getId(),
			// getAction(editor, ITextEditorActionConstants.COPY));
			// actionBars.setGlobalActionHandler(
			// ActionFactory.PASTE.getId(),
			// getAction(editor, ITextEditorActionConstants.PASTE));
			// actionBars.setGlobalActionHandler(
			// ActionFactory.SELECT_ALL.getId(),
			// getAction(editor, ITextEditorActionConstants.SELECT_ALL));
			// actionBars.setGlobalActionHandler(
			// ActionFactory.FIND.getId(),
			// getAction(editor, ITextEditorActionConstants.FIND));
			// actionBars.setGlobalActionHandler(
			// IDEActionFactory.BOOKMARK.getId(),
			// getAction(editor, IDEActionFactory.BOOKMARK.getId()));
			actionBars.updateActionBars();
		}

	}

	private void createActions() {
		sampleAction = new Action() {
			public void run() {
				MessageDialog.openInformation(null, "MultiPageTest",
						"Sample Action Executed");
			}
		};
		sampleAction.setText("Sample Action");
		sampleAction.setToolTipText("Sample Action tool tip");
		sampleAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages()
				.getImageDescriptor(IDE.SharedImages.IMG_OBJS_TASK_TSK));
		// addAction(new DeleteAction((IWorkbenchPart)activeEditorPart));
	}

	// private void addAction(DeleteAction deleteAction) {
	// // TODO Auto-generated method stub
	// getActionRegistry().registerAction(action);
	// addGlobalActionKey(action.getId());
	// }
	public void contributeToMenu(IMenuManager manager) {
		IMenuManager menu = new MenuManager("Editor &Menu");
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
		menu.add(sampleAction);
	}

	public void contributeToToolBar(IToolBarManager manager) {
		manager.add(new Separator());
		manager.add(sampleAction);
	}
}
