package de.hsu.grafcet.syntax.analysis.check;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import de.hsu.grafcet.*;
import de.hsu.grafcet.syntax.analysis.rules.*;


//import grafcet.*;
/**
 * 
 * @author Julius/Will
 * 
 * Analysis of correct syntax based on rules (see org.eclipse.gmf.grafcet.actions.analysis.syntax.rules) witch can be checked on the model instances directly.
 * Additional rules to the meta-model.
 */

public class CheckRules {

	private IFile model;
	
	/**
	 * Grafcet model
	 */
	private Grafcet grafDoc;

	/**
	 * The output folder.
	 */
	private IContainer targetFolder;

	/**
	 * The other arguments.
	 */
	List<? extends Object> arguments;
	
	public CheckRules(URI modelURI, IContainer targetFolder, List<? extends Object> arguments, IFile model) {
		this.targetFolder = targetFolder;
		this.arguments = arguments;
		this.model = model;
		ResourceSet resSet = new ResourceSetImpl();
		Resource res = resSet.getResource(modelURI,	true);
		grafDoc = (Grafcet)res.getContents().get(0);
	}
	
	/*
	 * The following recursion creates a flattened list in order to check both "regular" elements as well as "child"-elements of
	 * MacroStepExpansions and EnclosingStepExpansions
	 */
	/**
	 * @deprecated use getFlattenElements() from Utility
	 * @param rootList
	 * @param flatList
	 */
	private void flattenElements(EList<EObject> rootList, List<EObject> flatList){
		for (int i = 0; i < rootList.size(); i++) {
			flatList.add(rootList.get(i));
			if (rootList.get(i) instanceof PartialGrafcet || rootList.get(i) instanceof MacrostepExpansion) {
				EList <EObject> expansionElements = rootList.get(i).eContents();
				this.flattenElements(expansionElements, flatList);
			}
		}
	}
	
	public String runChecking(){
		String out = new String();	
		out = "Result of rule based check for GRAFCET-File " + this.model.getName().substring(0, this.model.getName().lastIndexOf(".grafcet")) + ":\n\n";
		
		out += ModelDepth.check(grafDoc);
		
		List<EObject> elementList = new LinkedList<EObject>();
		this.flattenElements(grafDoc.eContents(), elementList);
		Boolean tempResultEmpty = false;
		int counter = 0;
		String buffer = "";
		
		// Call rules
		
		buffer = InitialStep.check(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + buffer + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		
		buffer = InvalidIds.checkForDuplicates(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + buffer + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		
		buffer = InvalidIds.checkForZero(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + buffer + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		
		buffer = Arcs.findDuplicate(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + buffer + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		
		buffer = Arcs.findOpposing(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + buffer + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		
		
		buffer = ActionLinks.findDuplicate(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + buffer + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		
		buffer = EnclosingStepInitial.check(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + buffer + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		
		/* deprecated:
		buffer = EncStepExpActivationLink.check(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + buffer + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		*/
		
		buffer = InvalidForcedsteps.check(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + buffer + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		
		buffer = TransitionProperties.check(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + buffer + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		
		buffer = SynchronizationConflicts.hashCheck(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + buffer + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		
		buffer = SynchronizationConflicts.check(elementList);
		System.out.println(buffer);
		if (!buffer.isEmpty() && buffer != "Unallowed structures\n") {
			counter++;
			out += "(" + counter + ") " + buffer + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		
		buffer = UnconnectedElements.check(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + UnconnectedElements.check(elementList) + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		
		buffer = ActionValues.checkForbiddenValues(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + ActionValues.checkForbiddenValues(elementList) + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		
		buffer = ActionValues.checkAssignment(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + ActionValues.checkAssignment(elementList) + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
		
		buffer = MacroStepExpansions.checkEntryExitStep(elementList);
		if (!buffer.isEmpty()) {
			counter++;
			out += "(" + counter + ") " + MacroStepExpansions.checkEntryExitStep(elementList) + "\n";
			buffer = "";
		}
		else {
			tempResultEmpty = true;
		}
	
		if (!targetFolder.getLocation().toFile().exists()) {
			targetFolder.getLocation().toFile().mkdirs();
		}
		
		/*//moved to RunSyntaxAnalysis
		 
		System.out.println(out);
		
		try {
            //Whatever the file path is.
            File statText = new File(targetFolder.getLocation().toString() + "/Result_" + this.model.getName().substring(0, this.model.getName().lastIndexOf(".grafcet")) + ".txt" );
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            w.write(out);
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
        }
		*/
		return out;
	}	
}
