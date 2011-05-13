/*******************************************************************************
 * Copyright (c) 2004, 2005 Elias Volanakis and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
?* http://www.eclipse.org/legal/epl-v10.html
?*
?* Contributors:
?*????Elias Volanakis - initial API and implementation
?*******************************************************************************/
package cn.edu.pku.ogeditor;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;

/**
 * Contributes actions to a toolbar.
 * This class is tied to the editor in the definition of editor-extension (see plugin.xml).
 * @author Elias Volanakis
 */
public class ShapesEditorActionBarContributor extends ActionBarContributor {

/**
 * Create actions managed by this contributor.
 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
 */
protected void buildActions() {
	addRetargetAction(new DeleteRetargetAction());
	addRetargetAction(new UndoRetargetAction());
	addRetargetAction(new RedoRetargetAction());
    addRetargetAction(new ZoomInRetargetAction()); 
    addRetargetAction(new ZoomOutRetargetAction()); 
}

/**
 * Add actions to the given toolbar.
 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(org.eclipse.jface.action.IToolBarManager)
 */
public void contributeToToolBar(IToolBarManager toolBarManager) {
	toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
	toolBarManager.add(getAction(ActionFactory.REDO.getId()));
	toolBarManager.add(new Separator()); 
    toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN)); 
    toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT)); 
    toolBarManager.add(new ZoomComboContributionItem(getPage())); 
}

public void contributeToMenu(IMenuManager menuManager) {
	MenuManager viewMenu = new MenuManager("V&iew");
	viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
	viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
	menuManager.add(viewMenu);
	}

/*
 * (non-Javadoc)
 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
 */
protected void declareGlobalActionKeys() {
	// currently none
}

}