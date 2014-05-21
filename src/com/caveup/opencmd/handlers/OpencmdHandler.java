package com.caveup.opencmd.handlers;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

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
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec(new String[] { "cmd.exe","/c","" });
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
