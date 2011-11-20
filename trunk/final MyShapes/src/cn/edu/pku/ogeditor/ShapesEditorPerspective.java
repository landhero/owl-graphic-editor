/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
/**
 * the perspective of the plug-in
 * @author Xueyuan Xing
 */
public class ShapesEditorPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.2f, editorArea);
		left.addView("cn.edu.pku.ogeditor.views.hierarchyView");
		//left.addView("org.eclipse.jdt.ui.PackageExplorer");
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.75f, editorArea);
		bottom.addView(IPageLayout.ID_PROP_SHEET);
		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, 0.8f, editorArea);
		right.addView("cn.edu.pku.ogeditor.views.ConceptOutlineView");
		//layout.setEditorAreaVisible(false);
		
	}
}
