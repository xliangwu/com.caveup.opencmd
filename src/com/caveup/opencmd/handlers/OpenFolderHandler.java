package com.caveup.opencmd.handlers;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;

/**
 * Open the folder of the selection resource
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class OpenFolderHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public OpenFolderHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		String osName = System.getProperty("os.name");

		if (osName != null && osName.contains("Windows")) {
			String selectedProjectPath = getSelectedWorkPath(event);
			Runtime rt = Runtime.getRuntime();
			try {
				rt.exec("explorer " + selectedProjectPath + "");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private String getSelectedWorkPath(ExecutionEvent event) {
		String projectDir = getCurrentProject(event);
		// return default path
		return projectDir == null ? "C:\\" : projectDir;
	}

	public String getCurrentProject(ExecutionEvent event) {
		ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
		ISelection selection = selectionService.getSelection();
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			IPath path = null;
			if (element instanceof IResource) {
				path = ((IResource) element).getLocation();
			} else if (element instanceof IJavaElement) {
				path = ((IJavaElement) element).getResource().getLocation();
			}
			if (path != null) {
				File file = path.toFile();
				if (file.isDirectory()) {
					return file.getAbsolutePath();
				} else {
					return file.getParent();
				}
			}
		}

		return null;
	}
}
