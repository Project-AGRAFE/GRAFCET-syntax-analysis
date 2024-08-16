package de.hsu.grafcet.syntax.analysis.rules;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import de.hsu.grafcet.*;


public class ForcingOrderInit {

	//TODO need to call the check
	//checken, ob Forcing Order teilgrafcet in initiale Situation zwingt, auch wenn die nicht existiert
	
	public static String check(List<EObject> elementList) {
		String out = "";
		
		// Iterate over all elements
		for (EObject forcingOrder : elementList){
			// Find all Forcing Orders 
			if (forcingOrder instanceof ForcingOrder) {
				if (((ForcingOrder) forcingOrder).getForcingOrderType().equals(ForcingOrderType.INITIAL_SITUATION)) {
					boolean initsStep = false;
					for (InitializableType step : ((ForcingOrder) forcingOrder).getPartialGrafcet().getSteps()) {
						if (step.isInitial()) {
							initsStep = true;
						}
					}
					if (!initsStep) {
						out += "ForcingOrder " + ((ForcingOrder) forcingOrder).getId() + "can not initialize partial Grafcet " 
								+ ((ForcingOrder) forcingOrder).getPartialGrafcet().getName() + " because it has no initial step(s)\n";
					}
				}
			}
		}
		return out;
	}
}
