package de.hsu.grafcet.syntax.popup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.*;

import de.hsu.grafcet.*;
import de.hsu.grafcet.syntax.analysis.check.*;


public class RunSyntacticAnalysis implements IObjectActionDelegate {

	private Shell shell;
	protected List<IFile> files;	
	private Grafcet selectedDoc;
	
	
	/**
	 * Constructor for Action1.
	 */
	public RunSyntacticAnalysis() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		if (files != null) {
			IRunnableWithProgress operation = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					try {
						Iterator<IFile> filesIt = files.iterator();
						while (filesIt.hasNext()) {
							IFile model = (IFile)filesIt.next();
							URI modelURI = URI.createPlatformResourceURI(model.getFullPath().toString(), true);
							try {
								IContainer target = model.getProject().getFolder("Syntax Analysis");
								
								String out;
								CheckRules checkRules = new CheckRules(modelURI, target, getArguments(), model);
								out = checkRules.runChecking();
								Util.createOutputFile(out, target.getLocation().toString() + "/Result_Rules_" + model.getName().substring(0, model.getName().lastIndexOf(".grafcet")) + ".txt");
								
								CheckConditionClassification ccc = new CheckConditionClassification(modelURI, target, getArguments(), model);
								out = ccc.runChecking();
								Util.createOutputFile(out, target.getLocation().toString() + "/Result_ConditionClassification_" + model.getName().substring(0, model.getName().lastIndexOf(".grafcet")) + ".txt");
								
								CheckPartialOrder cpo = new  CheckPartialOrder(modelURI, target, getArguments(), model);
								out = cpo.runChecking();
								Util.createOutputFile(out, target.getLocation().toString() + "/Result_PartialOrder_" + model.getName().substring(0, model.getName().lastIndexOf(".grafcet")) + ".txt");
								
								CheckConnectedGrafcets ccg = new  CheckConnectedGrafcets(modelURI, target, getArguments(), model);
								out = ccg.runChecking();
								Util.createOutputFile(out, target.getLocation().toString() + "/Result_ConnectedGrafcets_" + model.getName().substring(0, model.getName().lastIndexOf(".grafcet")) + ".txt");
								
								
								
							} finally {
								model.getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
							}
						}
					} catch (CoreException e) {
						IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
						Activator.getDefault().getLog().log(status);
					}
				}
			};
			try {
				PlatformUI.getWorkbench().getProgressService().run(true, true, operation);
			} catch (InvocationTargetException e) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
//				Activator.getDefault().getLog().log(status);
				e.printStackTrace();
			} catch (InterruptedException e) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
				Activator.getDefault().getLog().log(status);
			}
		}
		MessageDialog.openInformation(
				shell,
				"Actions",
				"Checking was executed.");
		
	}
	
	public void createOutputFile(String out, String pathName) {
		System.out.println(out);
		
		try {
            //Whatever the file path is.
            File statText = new File(pathName);
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            w.write(out);
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
        }
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@SuppressWarnings("unchecked")
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			files = ((IStructuredSelection) selection).toList();
		}
	}
	
	public boolean isEnabled() {
		return selectedDoc != null;
	}
	
	protected List<? extends Object> getArguments() {
		return new ArrayList<String>();
	}

}


