/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
?*******************************************************************************/
package cn.edu.pku.ogeditor;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The plugin class (singleton).
 * <p>
 * This instance can be shared between all extensions in the plugin. Information
 * shared between extensions can be persisted by using the PreferenceStore.
 * </p>
 * @see org.eclipse.ui.plugin.AbstractUIPlugin#getPreferenceStore()
 * @author Xueyuan Xing
 * @author Tao Wu
 * @author Hansheng Zhang
 */
public class ShapesPlugin extends AbstractUIPlugin {

/** Single plugin instance. */
private static ShapesPlugin singleton;

/**
 * Returns the shared plugin instance.
 */
public static ShapesPlugin getDefault() {
	return singleton;
}

/** 
 * The constructor. 
 */
public ShapesPlugin() {
	if (singleton == null) {
		singleton = this;
	}
}

}