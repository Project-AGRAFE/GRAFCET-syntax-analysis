package de.hsu.grafcet.syntax.analysis.rules;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import de.hsu.grafcet.*;

public class EnclosingStepInitial {
	
	/*
	 * Method "check" finds EnclosingSteps with property "initial"=true and checks if the corresponding EnclosingStepExpansion
	 * contains an initial step
	 */
	public static String check(List<EObject> elementList) {
		String out = "";
		
		// Iterate over all elements
		for (int i = 0; i < elementList.size(); i++) {
			// Find all EnclosingStepExpansions and check if related EnclosingStep has property "initial=true"
			if(elementList.get(i) instanceof PartialGrafcet) {
				EnclosingStep encl = ((PartialGrafcet) elementList.get(i)).getEnclosingStep();
				if (encl != null) {
					if(encl.isInitial()) {
						// Iterate over all elements in EnclosingStepExpansion and check if an initial step has been set
						boolean initial = false;
						EList <EObject> expansionElements = elementList.get(i).eContents();
						
						for (int j = 0; j < expansionElements.size(); j++) {
							// Check for possible types with property "initial" in EnclosingStepExpansion
							if(expansionElements.get(j) instanceof Step) {
								if(((Step) expansionElements.get(j)).isInitial()) {
									initial = true;
								}
							}
							if(expansionElements.get(j) instanceof EnclosingStep) {
								if(((EnclosingStep) expansionElements.get(j)).isInitial()) {
									initial = true;
								}
							}
						}
						if(!initial) {
							out += "Missing Initial Step in EnclosingStepExpansion \"" + ((PartialGrafcet) elementList.get(i)).getName() + "\"\n";
						}
					}
				}
			}
		}
		return out;
	}
}