package com.caveup.opencmd.handlers;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;

import com.caveup.opencmd.Activator;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class OpencmdHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public OpencmdHandler() {
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
				rt.exec("cmd.exe /c start cmd.exe /K \"cd /d " + selectedProjectPath + "\"");
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
		IProject project = null;
		ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
		ISelection selection = selectionService.getSelection();
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if (element instanceof IResource) {
				project = ((IResource) element).getProject();
			} else if (element instanceof IJavaElement) {
				IJavaProject jProject = ((IJavaElement) element).getJavaProject();
				project = jProject.getProject();
			}
		}
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		File workspaceDirectory = workspace.getRoot().getLocation().toFile();
		if (project != null) {
			StringBuilder ret = new StringBuilder(workspaceDirectory.getAbsolutePath());
			ret.append(File.separator);
			ret.append(project.getFullPath().toFile().getName());
			ILog log = Activator.getDefault().getLog();
			log.log(new Status(IStatus.OK, Activator.PLUGIN_ID, 0, ret.toString(), null));
			return ret.toString();
		}
		return null;
	}
}
