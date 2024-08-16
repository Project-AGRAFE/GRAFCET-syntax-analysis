package de.hsu.grafcet.syntax.analysis.rules;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import de.hsu.grafcet.*;

public class EncStepExpActivationLink {
	/*
	 * Method "check" validates that every EnclosingStepExpansion contains a Step with property "ActivationLink"=true
	 */
	
	//TODO: prüfe bidirektionale referenz (for each berücksichtigen): enclosingStep.getpartialGrafcet().getEnclosingStep == enclosingStep
	
	/**
	 * @deprecated AGRAFE has a different interpretation of IEC 60848 partial Grafcets
	 * 
	 * @param elementList
	 * @return
	 */
	public static String check(List<EObject> elementList) {
		String out = "";
		
		// Iterate over all elements
		for (int i = 0; i < elementList.size(); i++) {
			// Find all EnclosingStepExpansions and check if everyone contains a Step with property "ActivationLink"=true
			if (elementList.get(i) instanceof PartialGrafcet) {
				EList <EObject> expansionElements = elementList.get(i).eContents();
				
				if (expansionElements != null) {
					boolean activationLink = false;
					for (int j = 0; j < expansionElements.size(); j++) {
						if (expansionElements.get(j) instanceof Step) {
							if(((Step) expansionElements.get(j)).isActivationLink()) {
								activationLink = true;
							}
						}
						if (expansionElements.get(j) instanceof EnclosingStep) {
							if(((EnclosingStep) expansionElements.get(j)).isActivationLink()) {
								activationLink = true;
							}
						}
					}
					if (!activationLink) {
						out += "Missing Activation Link in EnclosingStepExpansion \"" + ((PartialGrafcet) elementList.get(i)).getName() + "\"\n";
					}
				}
			}
		}
		return out;
	}
}
