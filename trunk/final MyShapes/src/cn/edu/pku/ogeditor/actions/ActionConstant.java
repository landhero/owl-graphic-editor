/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.actions.ActionFactory;

/**
 * Some constants in this package
 * 
 * @author Xueyuan Xing
 */
public class ActionConstant {
	public final static String RELOCATE_ID = "ogeditor.Relocate";
	public final static String CONCEPTFILTER_ID = "ogeditor.ConceptFilter";
	public final static String RELATIONFILTER_ID = "ogeditor.RelationFilter";
	public final static String DEPLOY_ID = "ogeditor.Deploy";
	public final static String STOP_DEPLOY_ID = "ogeditor.StopDeploy";
	public static final String OWLGENERATION_ID = "ogeditor.OWLGeneration";
	public static final String SYSTEMGENERATION_ID = "ogeditor.SystemGeneration";

	public final static String RELOCATE_TEXT = "Relocate";
	public final static String CONCEPTFILTER_TEXT = "Concept Filter";
	public final static String RELATIONFILTER_TEXT = "Relation Filter";
	public final static String DEPLOY_TEXT = "Deploy to IOT";
	public final static String STOP_DEPLOY_TEXT = "Stop Deploy Process";
	public static final String OWLGENERATION_TEXT = "Generate OWL File";
	public static final String SYSTEMGENERATION_TEXT = "Generate System";

	public static List<String> getSelectableActions() {
		List<String> actions = new ArrayList<String>();
		actions.add(ActionFactory.UNDO.getId());
		actions.add(ActionFactory.REDO.getId());
		actions.add(ActionFactory.DELETE.getId());

		actions.add(RELOCATE_ID);
		actions.add(CONCEPTFILTER_ID);
		actions.add(RELATIONFILTER_ID);
		actions.add(STOP_DEPLOY_ID);
		actions.add(OWLGENERATION_ID);
		actions.add(SYSTEMGENERATION_ID);
		return actions;
	}

}
