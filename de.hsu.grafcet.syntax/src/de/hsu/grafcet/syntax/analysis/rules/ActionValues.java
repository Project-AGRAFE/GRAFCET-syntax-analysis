package de.hsu.grafcet.syntax.analysis.rules;

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import de.hsu.grafcet.*;

public class ActionValues {
	/*
	 * Method "checkForbiddenValues" checks value-property of ContinuousActions for blank space, "=" and ":"
	 */
	public static String checkForbiddenValues (List<EObject> elementList) {
		String out = "Forbidden characters in value-property\n";
		Boolean match = false;
		int counter = 0;
		
		// Iterate over all elements
		for (int i = 0; i < elementList.size(); i++) {
			// Find ContinuousActions
			if (elementList.get(i) instanceof ContinuousAction) {
				ContinuousAction element = ((ContinuousAction) elementList.get(i));
				
				
				
				/*
				 * DEPRECATED with new meta model for terms --> see ocl annotations in terms.ecore 
				 * 
				// Check for blank space
				if (element.getValue().contains(" ")) {
					counter++;
					out += "\t(" + counter + ") ContinuousAction " + element.getID() + " contains blank space\n";
					match = true;
				}
				
				// Check for "="
				if (element.getValue().contains("=")) {
					counter++;
					out += "\t(" + counter + ") ContinuousAction " + element.getID() + " contains \"=\"\n";
					match = true;
				}
				
				// Check for ":"
				if (element.getValue().contains(":")) {
					counter++;
					out += "\t(" + counter + ") ContinuousAction " + element.getID() + " contains \":\"\n";
					match = true;
				}
				
				*/
				
			}
		}
		
		if (match) {
			return out;
		}
		else {
			out = "";
			return out;
		}
		
	}
	
	/*
	 * Method "checkAssignment" checks value-property of StoredActions for an assignment (:=)
	 */
	public static String checkAssignment (List<EObject> elementList) {
		String out = "Incorrect value-property\n";
		Boolean match = false;
		int counter = 0;
		
		/*
		 * DEPRECATED with new meta model for terms --> see ocl annotations in terms.ecore 
		 * 
		 
		
		// Iterate over all elements
		for (int i = 0; i < elementList.size(); i++) {
			// Find StoredActions
			if (elementList.get(i) instanceof StoredAction) {
				StoredAction element = ((StoredAction) elementList.get(i));
				// Check if value property contains an assignment (:=)
				if (!element.getValue().contains(":=")) {
					match = true;
					counter++;
					out += "\t(" + counter + ") StoredAction " + element.getID() + " must contain an assignment (:=)\n";
				}
			}
		}
		*/
		
		if (match) {
			return out;
		}
		else {
			out = "";
			return out;
		}
	}
	

}
