package de.hsu.grafcet.syntax.popup;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import javax.annotation.Resource;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import de.hsu.grafcet.Grafcet;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;


public class XMIToPlcCode implements IObjectActionDelegate {

	private Shell shell;
	protected List<IFile> files;	
	private Grafcet selectedDoc;
	
	
	/**
	 * Constructor for Action1.
	 */
	public XMIToPlcCode() {
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
		/*
		if (isEnabled()) {
			EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(selectedDoc);	
			domain.getCommandStack().execute(new ValidateGrafcet(selectedDoc));
			//new ValidateGrafcet(selectedDoc);
		}
		*/
		
		
		if (files != null) {
			IRunnableWithProgress operation = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					
						Iterator<IFile> filesIt = files.iterator();
						while (filesIt.hasNext()) {
							System.out.println("Test");
															
							System.out.println("Test1");				
							IFile model = (IFile)filesIt.next();
							System.out.println(URI.createPlatformResourceURI(model.getFullPath().toString(), true));
							URI modelURI = URI.createPlatformResourceURI(model.getFullPath().toString(), true);
							
							ResourceSet set = new XtextResourceSet();
							org.eclipse.emf.ecore.resource.Resource xmiResource = set.getResource(modelURI, true);
							//org.eclipse.emf.ecore.resource.ResourceSet xmiResource = set.createResource(targetURI);
							
							//XtextResourceSet set = new XtextResourceSet();
							
							IContainer target = model.getProject().getFolder("PlcToXMI");
							System.out.println(target.getLocation().toString());
							URI targetURI = URI.createFileURI(target.getLocation().toString()+ "/GenPlc_" + model.getName().substring(0, model.getName().lastIndexOf(".xmi")) + ".plccode");
							XtextResource resource = (XtextResource) set.createResource(targetURI);
							System.out.println(targetURI.toString());
						
							EcoreUtil.resolveAll(resource);
							System.out.println(model.toString());
							
							//org.eclipse.emf.ecore.resource.Resource xmiResource = set.createResource(targetURI);
							resource.getContents().add(xmiResource.getContents().get(0));
							System.out.println("Test5");
							
							try {
							  resource.save(null);
							  System.out.println("Test6");
							} catch (IOException e) {
							    e.printStackTrace();
							}
							
							
						}
					
				}
			};
			try {
				PlatformUI.getWorkbench().getProgressService().run(true, true, operation);
			} catch (InvocationTargetException e) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
				Activator.getDefault().getLog().log(status);
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

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@SuppressWarnings("unchecked")
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			files = ((IStructuredSelection) selection).toList();
		}
		/*
		selectedDoc = null;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1
					&& structuredSelection.getFirstElement() instanceof GlobalGRAFCET) {
				selectedDoc = (GlobalGRAFCET) structuredSelection
						.getFirstElement();
			}
		}
		action.setEnabled(isEnabled());
		*/
	}
	
	public boolean isEnabled() {
		return selectedDoc != null;
	}
	
	protected List<? extends Object> getArguments() {
		return new ArrayList<String>();
	}

}


