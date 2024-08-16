package de.hsu.grafcet.syntax.analysis.rules;

import java.util.List;
import org.eclipse.emf.ecore.EObject;

import de.hsu.grafcet.*;

public class InitialStep {
	/*
	 * Method "check" checks if at least one initial step has been set
	 */
	public static String check(List<EObject> elementList) {
		String out = "";
		Boolean initialStep = false;
		
		// Iterate over all elements
		for (int i = 0; i < elementList.size(); i++) {
			// Look for Steps
			if (elementList.get(i) instanceof Step) {
				Step step = ((Step) elementList.get(i));
				
				// Check every step if it is an initial step
				if (step.isInitial()) {
					// Once an initial step has been found 'initialStep' is being set to >true<
					initialStep = true;
				}
			}
			
			if (elementList.get(i) instanceof EnclosingStep) {
				EnclosingStep enclosingstep = ((EnclosingStep) elementList.get(i));
				
				// Check every step if it is an initial step
				if (enclosingstep.isInitial()) {
					// Once an initial step has been found 'initialStep' is being set to >true<
					initialStep = true;
				}
			}
		}
		
		if (!initialStep) {
			out += "No initial step has been set\n";
		}
		return out;
	}

}