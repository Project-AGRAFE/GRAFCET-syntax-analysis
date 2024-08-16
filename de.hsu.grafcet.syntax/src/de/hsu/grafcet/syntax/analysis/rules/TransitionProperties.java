package de.hsu.grafcet.syntax.analysis.rules;

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import de.hsu.grafcet.*;

public class TransitionProperties {
	/*
	 * Method "check" assures that either ConditionEventType or TType of Transitions has been set
	 */
	public static String check(List<EObject> elementList) {
		
		//Event Condition removed from grafcet.ecore. Events (RE/FE) modelted as operator in terms.ecore. 
		//Events and time behavior can be used in same condition. Syntax check below is out dated
		
		/*
		String out = "";
		
		// Iterate over all transitions
		for (int i = 0; i < elementList.size(); i++) {
			if(elementList.get(i) instanceof Transition) {

				String conditionEventType = ((Transition) elementList.get(i)).getConditionEventType().toString();
				String tType = ((Transition) elementList.get(i)).getTType().toString();
				Transition transition = ((Transition) elementList.get(i));
				
				// Check if more than one value (of Condition Event Type and TType) has been set
				if((conditionEventType != "none") && (tType != "none")) {
					out += "Transition " + transition.getID() + ": either Condition Event Type or TType may be set\n";
				}	
			}
		}
		return out;
		 */
		return "";
	}
}