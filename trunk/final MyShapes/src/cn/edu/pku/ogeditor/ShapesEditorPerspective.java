package cn.edu.pku.ogeditor;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

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
