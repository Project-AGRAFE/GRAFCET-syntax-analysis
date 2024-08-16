package de.hsu.grafcet.syntax.analysis.rules;

import java.util.List;
import org.eclipse.emf.ecore.EObject;

import de.hsu.grafcet.*;

public class InvalidIds {
	/*
	 * Method "checkForDuplicates" checks every element type for duplicate IDs 
	 */
	public static String checkForDuplicates(List<EObject> elementList) {
		String out = "Duplicate IDs\n";
		boolean match = false;
		int counter = 0;
		// Iterate over all elements
		for (int i = 0; i < elementList.size() - 1; i++) {
			for (int j = i + 1; j < elementList.size(); j++) {
				
				// Special case: Steps and EnclosingSteps use the same ID-range
				if ((elementList.get(j) instanceof Step && elementList.get(i) instanceof EnclosingStep && ((Step) elementList.get(j)).getId() == ((EnclosingStep) elementList.get(i)).getId())) {
					counter++;
					out += "\t(" + counter + ") Step " + ((Step) elementList.get(j)).getId() + "\n";
					match = true;
				}
				
				if ((elementList.get(i) instanceof Step && elementList.get(j) instanceof EnclosingStep && ((Step) elementList.get(i)).getId() == ((EnclosingStep) elementList.get(j)).getId())) {
					counter++;
					out += "\t(" + counter + ") EnclosingStep " + ((EnclosingStep) elementList.get(j)).getId() + "\n";
					match = true;
				}
				
				// Consider all elements except of the one selected in higher for-loop
				if(elementList.get(i).getClass() != elementList.get(j).getClass()) { continue; }
				
				// Check all elements with property "ID" for duplicates
				if (elementList.get(i) instanceof StoredAction && ((StoredAction) elementList.get(i)).getId() == ((StoredAction) elementList.get(j)).getId()) {
					counter++;
					out += "\t(" + counter + ") StoredAction " + ((StoredAction) elementList.get(i)).getId() + "\n";
					match = true;
				}
				
				if (elementList.get(i) instanceof ContinuousAction && ((ContinuousAction) elementList.get(i)).getId() == ((ContinuousAction) elementList.get(j)).getId()) {
					counter++;
					out += "\t(" + counter + ") ContinuousAction " + ((ContinuousAction) elementList.get(i)).getId() + "\n";
					match = true;
				}
				
				if (elementList.get(i) instanceof ForcingOrder && ((ForcingOrder) elementList.get(i)).getId() == ((ForcingOrder) elementList.get(j)).getId()) {
					counter++;
					out += "\t(" + counter + ") ForcingOrder " + ((ForcingOrder) elementList.get(i)).getId() + "\n";
					match = true;
				}
				
				if (elementList.get(i) instanceof Transition && ((Transition) elementList.get(i)).getId() == ((Transition) elementList.get(j)).getId()) {
					counter++;
					out += "\t(" + counter + ") Transition " + ((Transition) elementList.get(i)).getId() + "\n";
					match = true;
				}
				
				if (elementList.get(i) instanceof EnclosingStep && ((EnclosingStep) elementList.get(i)).getId() == ((EnclosingStep) elementList.get(j)).getId()) {
					counter++;
					out += "\t(" + counter + ") EnclosingStep " + ((EnclosingStep) elementList.get(i)).getId() + "\n";
					match = true;
				}
				
				if (elementList.get(i) instanceof EntryStep && ((EntryStep) elementList.get(i)).getId() == ((EntryStep) elementList.get(j)).getId()) {
					counter++;
					out += "\t(" + counter + ") EntryStep " + ((EntryStep) elementList.get(i)).getId() + "\n";
					match = true;
				}
				
				if (elementList.get(i) instanceof ExitStep && ((ExitStep) elementList.get(i)).getId() == ((ExitStep) elementList.get(j)).getId()) {
					counter++;
					out += "\t(" + counter + ") ExitStep " + ((ExitStep) elementList.get(i)).getId() + "\n";
					match = true;
				}
			
				if (elementList.get(i) instanceof Step && ((Step) elementList.get(i)).getId() == ((Step) elementList.get(j)).getId()) {
					counter++;
					out += "\t(" + counter + ") Step " + ((Step) elementList.get(i)).getId() + "\n";
					match = true;
				}
				
				if (elementList.get(i) instanceof Macrostep && ((Macrostep) elementList.get(i)).getId() == ((Macrostep) elementList.get(j)).getId()) {
					counter++;
					out += "\t(" + counter + ") Macrostep " + ((Macrostep) elementList.get(i)).getId() + "\n";
					match = true;
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
	
	/*
	 * Method "checkForZero" checks property "ID" of all elements for unallowed value "0"
	 */
	public static String checkForZero(List<EObject> elementList) {
		String out = "Unallowed IDs\n";
		boolean match = false;
		int counter = 0;
		
		for (int i = 0; i < elementList.size(); i++) {
			if (elementList.get(i) instanceof StoredAction && ((StoredAction)elementList.get(i)).getId() <= 0) {
				counter++;
				out += "\t(" + counter + ") StoredAction " + ((StoredAction)elementList.get(i)).getId() + "\n";
				match = true;
			}
			
			if (elementList.get(i) instanceof ContinuousAction && ((ContinuousAction)elementList.get(i)).getId() <= 0) {
				counter++;
				out += "\t(" + counter + ") ContinuousAction " + ((ContinuousAction)elementList.get(i)).getId() + "\n";
				match = true;
			}
			
			if (elementList.get(i) instanceof ForcingOrder && ((ForcingOrder)elementList.get(i)).getId() <= 0) {
				counter++;
				out += "\t(" + counter + ") ForcingOrder " + ((ForcingOrder)elementList.get(i)).getId() + "\n";
				match = true;
			}
			
			if (elementList.get(i) instanceof Transition && ((Transition)elementList.get(i)).getId() <= 0) {
				counter++;
				out += "\t(" + counter + ") Transition " + ((Transition)elementList.get(i)).getId() + "\n";
				match = true;
			}
			
			if (elementList.get(i) instanceof EnclosingStep && ((EnclosingStep)elementList.get(i)).getId() <= 0) {
				counter++;
				out += "\t(" + counter + ") EnclosingStep " + ((EnclosingStep)elementList.get(i)).getId() + "\n";
				match = true;
			}
			
			if (elementList.get(i) instanceof EntryStep && ((EntryStep)elementList.get(i)).getId() <= 0) {
				counter++;
				out += "\t(" + counter + ") EntryStep " + ((EntryStep)elementList.get(i)).getId() + "\n";
				match = true;
			}
			
			if (elementList.get(i) instanceof ExitStep && ((ExitStep)elementList.get(i)).getId() <= 0) {
				counter++;
				out += "\t(" + counter + ") ExitStep " + ((ExitStep)elementList.get(i)).getId() + "\n";
				match = true;
			}
			
			if (elementList.get(i) instanceof Step && ((Step)elementList.get(i)).getId() <= 0) {
				counter++;
				out += "\t(" + counter + ") Step " + ((Step)elementList.get(i)).getId() + "\n";
				match = true;
			}
			
			if (elementList.get(i) instanceof Macrostep && ((Macrostep)elementList.get(i)).getId() <= 0) {
				counter++;
				out += "\t(" + counter + ") Macrostep " + ((Macrostep)elementList.get(i)).getId() + "\n";
				match = true;
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
