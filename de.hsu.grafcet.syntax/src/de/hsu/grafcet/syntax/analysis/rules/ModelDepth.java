package de.hsu.grafcet.syntax.analysis.rules;

import de.hsu.grafcet.Grafcet;

public class ModelDepth {

	/*
	 * Method "check" checks if all partial Grafcets are on the same plane i.e. are not nested and the global Grafcet does not contain any elements except partial Grafcets and variable declarations
	 */
	public static String check(Grafcet globalGrafcet) {
		String out = "";
		
		if (!globalGrafcet.getActionLinks().isEmpty() ||
				!globalGrafcet.getActionTypes().isEmpty() ||
				!globalGrafcet.getArcs().isEmpty() ||
				!globalGrafcet.getMacrosteps().isEmpty() ||
				!globalGrafcet.getSteps().isEmpty() ||
				!globalGrafcet.getSynchronizations().isEmpty() ||
				!globalGrafcet.getTransitions().isEmpty()) {
			out += "\nGlobal Grafcet " + globalGrafcet.getName() + " must not contain any objects except partial Grafcets (including macrostep expansion) and a variable declaration container!";
		}
		
		for (Grafcet partialGrafcet : globalGrafcet.getPartialGrafcets()) {
			if (!partialGrafcet.getPartialGrafcets().isEmpty()) {
				out += "\nPartial Grafcet " + partialGrafcet.getName() + " contains nested partial Grafcets. According to the standard the Grafcet must not contain nested partial Grfacets";
			}
		}
			
		return out;
	}

}
