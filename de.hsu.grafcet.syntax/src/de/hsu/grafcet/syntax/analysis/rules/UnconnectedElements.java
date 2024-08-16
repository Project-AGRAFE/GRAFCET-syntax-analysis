package de.hsu.grafcet.syntax.analysis.rules;

import java.util.List;
import org.eclipse.emf.ecore.EObject;

import de.hsu.grafcet.*;

public class UnconnectedElements {

	/*
	 * Method "check" finds StoredActions, ContinuousActions and ForcingOrders that are not connected to a Step
	 */
	public static String check(List<EObject> elementList) {
		String out = "Unconnected elements\n";
		Boolean output = false;
		int counter = 0;
		
		// Iterate over all elements
		for (int i = 0; i < elementList.size(); i++) {
			// Find StoredActions
			if (elementList.get(i) instanceof StoredAction) {
				// 'match' will be >true< (error) until an AL connected to the element has been found
				Boolean match = true;
				StoredAction element = ((StoredAction) elementList.get(i));

				// Scan all ALs
				for (int j = 0; j < elementList.size(); j++) {
					if (elementList.get(j) instanceof ActionLink) {
						ActionLink link = ((ActionLink) elementList.get(j));
						// Check every AL if it is connected to the element
						if (element.getId() == link.getActionType().getId()) {
							match = false;
						}
					}
				}
				
				if(match) {
					output = true;
					counter++;
					out += "\t(" + counter + ") StoredAction " + element.getId() + " is not connected\n";
				}
			}
			
			if (elementList.get(i) instanceof ContinuousAction) {
				// 'match' will be >true< (error) until an AL connected to the element has been found
				Boolean match = true;
				ContinuousAction element = ((ContinuousAction) elementList.get(i));

				// Scan all ALs
				for (int j = 0; j < elementList.size(); j++) {
					if (elementList.get(j) instanceof ActionLink) {
						ActionLink link = ((ActionLink) elementList.get(j));
						// Check every AL if it is connected to the element
						if (element.getId() == link.getActionType().getId()) {
							match = false;
						}
					}
				}
				
				if(match) {
					output = true;
					counter++;
					out += "\t(" + counter + ") ContinuousAction " + element.getId() + " is not connected\n";
				}
			}
			
			if (elementList.get(i) instanceof ForcingOrder) {
				// 'match' will be >true< (error) until an AL connected to the element has been found
				Boolean match = true;
				ForcingOrder element = ((ForcingOrder) elementList.get(i));

				// Scan all ALs
				for (int j = 0; j < elementList.size(); j++) {
					if (elementList.get(j) instanceof ActionLink) {
						ActionLink link = ((ActionLink) elementList.get(j));
						// Check every AL if it is connected to the element
						if (element.getId() == link.getActionType().getId()) {
							match = false;
						}
					}
				}
				if(match) {
					output = true;
					counter++;
					out += "\t(" + counter + ") ForcingOrder " + element.getId() + " is not connected\n";
				}
			}
		}
		
		if (output) {
			return out;
		}
		else {
			out = "";
			return out;
		}
	}
}
