package de.hsu.grafcet.syntax.analysis.rules;

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import de.hsu.grafcet.*;

//import grafcet.ActionLink;
//import grafcet.ContinuousAction;
//import grafcet.StoredAction;
//import grafcet.ForcingOrder;
//import grafcet.Step;

public class ActionLinks {
		/*
		 * Method "findDuplicate" finds ActionLinks with the same source and target
		 */
		public static String findDuplicate(List<EObject> elementList) {
		String out = "Duplicate ActionLinks\n";
		Boolean match = false;
		int counter = 0;
		
		// Iterate over all elements
		for (int i = 0; i < elementList.size() - 1; i++) {
			// Consider ActionLinks only
			if (!(elementList.get(i) instanceof ActionLink)) continue;
			for (int j = i + 1; j < elementList.size(); j++) {
				if (!(elementList.get(j) instanceof ActionLink)) continue;
				
				ActionLink al1 = ((ActionLink) elementList.get(i));
				ActionLink al2 = ((ActionLink) elementList.get(j));
				
				// Check if source of al1 and al2 as well as target of al1 and al2 are the same
				if ((al1.getStep() == al2.getStep()) && (al1.getActionType() == al2.getActionType())) {
					match = true;
					out += "\t";
					
					if (al1.getStep() instanceof Step) {
						counter++;
						out += "(" + counter + ") Source: Step "    + ((Step)al1.getStep()).getId();
					}
					if (al1.getActionType() instanceof ContinuousAction) out += ", Target: ContinuousAction "    + ((ContinuousAction)al1.getActionType()).getId();
					if (al1.getActionType() instanceof StoredAction) out += ", Target: StoredAction "    + ((StoredAction)al1.getActionType()).getId();
					if (al1.getActionType() instanceof ForcingOrder) out += ", Target: ForcingOrder "    + ((ForcingOrder)al1.getActionType()).getId();
					out += "\n";
				}
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
}
