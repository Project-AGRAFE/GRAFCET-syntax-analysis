package de.hsu.grafcet.syntax.analysis.rules;

import java.util.List;
import org.eclipse.emf.ecore.EObject;

import de.hsu.grafcet.*;

public class Arcs {
	/*
	 * Method "findDuplicate" finds Arcs with the same source and target 
	 */
	public static String findDuplicate(List<EObject> elementList) {
		String out = "Duplicate Arcs\n";
		Boolean match = false;
		int counter = 0;
		
		// Iterate over all elements
		for (int i = 0; i < elementList.size() - 1; i++) {
			// Consider Arcs only
			if (!(elementList.get(i) instanceof Arc)) continue;
			for (int j = i + 1; j < elementList.size(); j++) {
				if (!(elementList.get(j) instanceof Arc)) continue;
				
				Arc arc1 = ((Arc) elementList.get(i));
				Arc arc2 = ((Arc) elementList.get(j));
				
				// Check if source of arc1 and arc2 as well as target of arc1 and arc2 are the same
				if ((arc1.getSource() == arc2.getSource()) && (arc1.getTarget() == arc2.getTarget())) {
					match = true;
					counter++;
					out += "\t(" + counter + ") ";
					
					if (arc1.getSource() instanceof Transition)    out += "Source: Transition "    + ((Transition)arc1.getSource()).getId();
					if (arc1.getSource() instanceof EnclosingStep) out += "Source: EnclosingStep " + ((EnclosingStep)arc1.getSource()).getId();
					if (arc1.getSource() instanceof Step)          out += "Source: Step "         + ((Step)arc1.getSource()).getId();
					if (arc1.getSource() instanceof Macrostep)     out += "Source: Macrostep "    + ((Macrostep)arc1.getSource()).getId();
					if (arc1.getSource() instanceof EntryStep)     out += "Source: EntryStep "    + ((EntryStep)arc1.getSource()).getId();
					if (arc1.getSource() instanceof ExitStep)      out += "Source: ExitStep "     + ((EntryStep)arc1.getSource()).getId();
						
					if (arc1.getTarget() instanceof Transition)    out += ", Target: Transition " + ((Transition)arc1.getTarget()).getId();
					if (arc1.getTarget() instanceof EnclosingStep) out += ", Target: EnclosingStep " + ((EnclosingStep)arc1.getTarget()).getId();
					if (arc1.getTarget() instanceof Step)          out += ", Target: Step " + ((Step)arc1.getTarget()).getId();
					if (arc1.getTarget() instanceof Macrostep)     out += ", Target: Macrostep " + ((Macrostep)arc1.getTarget()).getId();
					if (arc1.getTarget() instanceof EntryStep)     out += ", Target: EntryStep " + ((EntryStep)arc1.getTarget()).getId();
					if (arc1.getTarget() instanceof ExitStep)      out += ", Target: ExitStep " + ((EntryStep)arc1.getTarget()).getId();

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
	
	/*
	 * Method "findOpposing" finds Arcs with opposing source and target 
	 */
	public static String findOpposing(List<EObject> elementList){
		String out = "Opposing Arcs\n";
		Boolean match = false;
		int counter = 0;
		
		// Iterate over all elements
		for (int i = 0; i < elementList.size() - 1; i++) {
			// Consider Arcs only
			if(!(elementList.get(i) instanceof Arc)) continue;
			for (int j = i + 1; j < elementList.size(); j++) {
				if(!(elementList.get(j) instanceof Arc)) continue;
				
				Arc arc1 = ((Arc)elementList.get(i));
				Arc arc2 = ((Arc)elementList.get(j));
				
				// Check if source of arc1 is equivalent to target of arc2 and vice versa
				if((arc1.getSource() == arc2.getTarget()) && (arc2.getSource() == arc1.getTarget())) {
					match = true;
					counter++;
					out += "\t(" + counter + ") ";
					
					if(arc2.getSource() instanceof Transition) 		out += "Source: Transition " + ((Transition)arc2.getSource()).getId();
					if(arc2.getSource() instanceof EnclosingStep) 	out += "Source: EnclosingStep " + ((EnclosingStep)arc2.getSource()).getId();
					if(arc2.getSource() instanceof Step) 			out += "Source: Step " + ((Step)arc2.getSource()).getId();
					if(arc2.getSource() instanceof Macrostep) 		out += "Source: Macrostep " + ((Macrostep)arc2.getSource()).getId();
					if(arc2.getSource() instanceof EntryStep) 		out += "Source: EntryStep " + ((EntryStep)arc2.getSource()).getId();
					if(arc2.getSource() instanceof ExitStep)		out += "Source: ExitStep " + ((EntryStep)arc2.getSource()).getId();
					
					if(arc2.getTarget() instanceof Transition) 		out += ", Target: Transition " + ((Transition)arc2.getTarget()).getId();
					if(arc2.getTarget() instanceof EnclosingStep) 	out += ", Target: EnclosingStep " + ((EnclosingStep)arc2.getTarget()).getId();
					if(arc2.getTarget() instanceof Step) 			out += ", Target: Step " + ((Step)arc2.getTarget()).getId();
					if(arc2.getTarget() instanceof Macrostep) 		out += ", Target: Macrostep " + ((Macrostep)arc2.getTarget()).getId();
					if(arc2.getTarget() instanceof EntryStep) 		out += ", Target: EntryStep " + ((EntryStep)arc2.getTarget()).getId();
					if(arc2.getTarget() instanceof ExitStep) 		out += ", Target: ExitStep " + ((EntryStep)arc2.getTarget()).getId();
					
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
