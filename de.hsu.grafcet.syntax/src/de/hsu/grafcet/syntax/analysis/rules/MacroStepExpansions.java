package de.hsu.grafcet.syntax.analysis.rules;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import de.hsu.grafcet.*;


public class MacroStepExpansions {
	/*
	 * Method "checkEntryExitStep" checks every MacrostepExpansion for an EntryStep and an ExitStep
	 */
	
	/**
	 * @deprecated according to reworking of meta model
	 * @param elementList
	 * @return
	 */
	public static String checkEntryExitStep (List<EObject> elementList) {
		String out = "";
		int counter = 0;
		
		// Iterate over all elements
		for (int i = 0; i < elementList.size(); i++) {
			// Find all MacrostepExpansions
			if (elementList.get(i) instanceof MacrostepExpansion) {
				int match = 0;
				// Get child-elements
				EList <EObject> expansionElements = elementList.get(i).eContents();
				// Check every MacrostepExpansion for EntryStep and ExitStep
				for (int j = 0; j < expansionElements.size(); j++) {
					if (expansionElements.get(j) instanceof EntryStep) {
						match++;
					}
					if (expansionElements.get(j) instanceof ExitStep) {
						match++;
					}
				}
				/*
				// getMacrostep() is DEPRECATED:
				
				if (match < 2) {
					counter++;
					// If MacrostepExpansion has been connected to a valid MacroStep display the corresponding MacroStep
					if (((MacrostepExpansion) elementList.get(i)).getMacrostep() != null) {
						out += "\t(" + counter + ") MacrostepExpansion linked to MacroStep " + ((MacrostepExpansion) elementList.get(i)).getMacrostep().getID() + "\n";
					}
					// Display "MacroStep Expansion null" if the MacrostepExpansion has not been connected to a MacroStep
					else {
						out += "\t(" + counter + ") MacrostepExpansion " + ((MacrostepExpansion) elementList.get(i)).getMacrostep() + "\n";
					}
				}
				*/
			}
		}
		if (out != "") {
			out = "Missing entry and/or exit step\n" + out;
			return out;
		}
		else {
			return out;
		}
	}
}
