package de.hsu.grafcet.syntax.analysis.rules;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import de.hsu.grafcet.*;

public class InvalidForcedsteps {
	/*
	 * Method "check" finds all ForcingOrders and checks if every assigned ForcedStep is a child of the corresponding EnclosingStepExpansion
	 */
	public static String check(List<EObject> elementList) {
		String out = "";
		
		for (int i = 0; i < elementList.size(); i++) {
			if (elementList.get(i) instanceof ForcingOrder) {
				String forcingOrder = ((ForcingOrder) elementList.get(i)).getForcingOrderType().toString();
				
				if(forcingOrder == "explicitSit") {
					// List contains assigned (forced) steps
					EList<InitializableType> assignedSteps = ((ForcingOrder) elementList.get(i)).getForcedSteps();
					
					// List contains valid steps
					PartialGrafcet expansion = ((ForcingOrder) elementList.get(i)).getPartialGrafcet();
					if (expansion != null) {
						if (expansion.eContainer() instanceof EObject) {
							EList<InitializableType> validSteps = expansion.getSteps();
			
							// Iterate over all assigned steps and check if valid
							for (int j = 0; j < assignedSteps.size(); j++) {
								boolean match = false;
								for (int k = 0; k < validSteps.size(); k++) {
									if(assignedSteps.get(j).getId() == validSteps.get(k).getId()) {
										match = true;
									}
								}
								if (!match) {
									out += "Forcedstep " + assignedSteps.get(j).getId() + " (ForcingOrder " + ((ForcingOrder) elementList.get(i)).getId() + ") is not part of EnclosingStepExpansion " + expansion.getName() + "\n";
								}
							}
						}
					}
				}
			}
		}
		return out;
	}
}