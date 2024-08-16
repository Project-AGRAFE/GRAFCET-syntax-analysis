package de.hsu.grafcet.syntax.analysis.rules;

import java.util.List;
import org.eclipse.emf.ecore.EObject;

import de.hsu.grafcet.*;

public class SynchronizationConflicts {
	
	/*
	 * Due to lack of ID-property of synchronizations the following methods (case1 to case6) use .hashCode() for identifying synchronizations
	 * This method checks uniqueness of hashCodes of all synchronizations
	 */	
	public static String hashCheck(List<EObject> elementList) {
		String out = "";
		boolean duplicate = false;
		
		// Iterate over all elements
		for (int i = 0; i < elementList.size(); i++) {
			// Look for synchronizations
			if (elementList.get(i) instanceof Synchronization) {
				Synchronization currentSynchronization = ((Synchronization) elementList.get(i));
				for (int j = i + 1; j < elementList.size(); j++) {
					if(elementList.get(j) instanceof Synchronization) {
						Synchronization checkSynchronization = ((Synchronization) elementList.get(j));
						// Check if there are duplicates
						if (currentSynchronization.hashCode() == checkSynchronization.hashCode()) {
							duplicate = true;
						}
					}
				}
			}
		}
		if (duplicate) {
			out = "An error occurred during verfication. Please run the verification again.\n";
			return out;
		}
		else {
			return out;
		}
	}
	
	public static String check(List<EObject> elementList) {
		/*
		 * Method calls sub-rules
		 */
		String out = "Unallowed structures\n";
		
		Integer counter = 0;
		String buffer = "";
		
		buffer = SynchronizationConflicts.case1(elementList, counter);
		if (!buffer.isEmpty()) {
			out += buffer;
			counter += buffer.split("\n").length;
			buffer = "";
		}
		
		buffer = SynchronizationConflicts.case2(elementList, counter);
		if (!buffer.isEmpty()) {
			out += buffer;
			counter += buffer.split("\n").length;
			buffer = "";
		}
		
		buffer = SynchronizationConflicts.case3(elementList, counter);
		if (!buffer.isEmpty()) {
			out += buffer;
			counter += buffer.split("\n").length;
			buffer = "";
		}
		
		buffer = SynchronizationConflicts.case4(elementList, counter);
		if (!buffer.isEmpty()) {
			out += buffer;
			counter += buffer.split("\n").length;
			buffer = "";
		}
		
		buffer = SynchronizationConflicts.case5(elementList, counter);
		if (!buffer.isEmpty()) {
			out += buffer;
			counter += buffer.split("\n").length;
			buffer = "";
		}
		
		buffer = SynchronizationConflicts.case6(elementList, counter);
		if (!buffer.isEmpty()) {
			out += buffer;
			counter += buffer.split("\n").length;
			buffer = "";
		}
		
		return out;
	}
	
	/* 	Algorithm scans for the following pattern
	 * 		_____________________________________________
	 * 		_____________________________________________
	 * 			|			|			|			|
	 * 			|			|			|			|
	 * 		   \|/		   \|/		   \|/		   \|/
	 *       t1-----     t2-----   t..-----		tn-----
	 */
	public static String case1(List<EObject> elementList, Integer counter) {
		String out = "";
		
		for (int i = 0; i < elementList.size(); i++) {
			boolean match = false;
			String transition = "";
			
			if (elementList.get(i) instanceof Arc) {
				Node currentArcSource = ((Arc) elementList.get(i)).getSource();
				Node currentArcTarget = ((Arc) elementList.get(i)).getTarget();
				
				// Arc with source-type: synchronization
				if (currentArcSource instanceof Synchronization) {
					// Target-type: transition
					if (currentArcTarget instanceof Transition) {
						for (int j = i + 1; j < elementList.size(); j++) {
							if (elementList.get(j) instanceof Arc) {
								Node checkArcSource = ((Arc) elementList.get(j)).getSource();
								Node checkArcTarget = ((Arc) elementList.get(j)).getTarget();
								if (checkArcTarget instanceof Transition) {
									// No ID-property available - using hashCode for identification of synchronizations
									if (currentArcSource.hashCode() == checkArcSource.hashCode()) {
										if (!match) {
											transition += ((Transition) checkArcTarget).getId();
											match = true;
										}
									}
								}
							}
						}
					}
				}
			}
			if (match) {
				counter++;
				out += "\t(" + counter + ") Concerning Transition " + transition + "\n";
			}
		}
		return out;
	}

	/* 	Algorithm scans for the following pattern
	 * 
	 *       t1-----     t2-----   t..-----		tn-----
	 *       	| 			|			|			|
	 * 			|			|			|			|
	 * 		   \|/		   \|/		   \|/		   \|/
	 * 		_____________________________________________
	 * 		_____________________________________________
	 */
	public static String case2(List<EObject> elementList, Integer counter) {
		String out = "";
		
		for (int i = 0; i < elementList.size(); i++) {
			boolean match = false;
			String transition = "";
						
			if (elementList.get(i) instanceof Arc) {
				Node currentArcSource = ((Arc) elementList.get(i)).getSource();
				Node currentArcTarget = ((Arc) elementList.get(i)).getTarget();
				
				// Arc with target-type: synchronization
				if (currentArcTarget instanceof Synchronization) {
					// Source-type: transition
					if (currentArcSource instanceof Transition) {
						for (int j = i + 1; j < elementList.size(); j++) {
							if (elementList.get(j) instanceof Arc) {
								Node checkArcSource = ((Arc) elementList.get(j)).getSource();
								Node checkArcTarget = ((Arc) elementList.get(j)).getTarget();
								if (checkArcSource instanceof Transition) {	
									// No ID-property available - using hashCode for identification of synchronizations
									if (currentArcTarget.hashCode() == checkArcTarget.hashCode()) {
										if (!match) {
											transition += ((Transition) checkArcSource).getId();
											match = true;
										}
									}
								}
							}
						}
					}
				}
			}
			if (match) {
				counter++;
				out += "\t(" + counter + ") Concerning Transition " + transition + "\n";
			}
		}
		return out;
	}
	
	/* 	Algorithm scans for the following pattern
	 * 		_________
	 * 		_________
	 * 			|
	 * 			|
	 * 		   \|/
	 *       -------
	 *       |  1  |
	 *       |     |
	 *       -------
	 */
	public static String case3(List<EObject> elementList, Integer counter) {
		String out = "";
		
		for (int i = 0; i < elementList.size(); i++) {
			if (elementList.get(i) instanceof Arc) {
				Node currentArcSource = ((Arc) elementList.get(i)).getSource();
				Node currentArcTarget = ((Arc) elementList.get(i)).getTarget();
				
				// Arc with source-type: synchronization
				if (currentArcSource instanceof Synchronization) {
					// Target-type: Step
					if (currentArcTarget instanceof Step) {
						Boolean match = true;
						for (int j = 0; j < elementList.size(); j++) {
							if (j == i) continue;
							if (elementList.get(j) instanceof Arc) {
								Node checkArcSource = ((Arc) elementList.get(j)).getSource();
								// No ID-property available - using hashCode for identification of synchronizations
								if (currentArcSource.hashCode() == checkArcSource.hashCode()) {
									match = false;
								}
							}
						}
						if(match) {
							counter++;
							out += "\t(" + counter + ") Concerning Step " + ((Step) currentArcTarget).getId() + "\n";
						}
					}
				}
			}
		}
		return out;
	}
	
	/* 	Algorithm scans for the following pattern
	  * 
	 *       -------
	 *       |  1  |
	 *       |     |
	 *       -------
	 * 			|
	 * 			|
	 * 		   \|/
	 * 		_________
	 * 		_________
	 */
	public static String case4(List<EObject> elementList, Integer counter) {
		String out = "";
		
		for (int i = 0; i < elementList.size(); i++) {
			if (elementList.get(i) instanceof Arc) {
				Node currentArcSource = ((Arc) elementList.get(i)).getSource();
				Node currentArcTarget = ((Arc) elementList.get(i)).getTarget();
				
				// Arc with source-type: synchronization
				if (currentArcSource instanceof Step) {
					// Target-type: Step
					if (currentArcTarget instanceof Synchronization) {
						Boolean match = true;
						for (int j = 0; j < elementList.size(); j++) {
							if (j == i) continue;
							if (elementList.get(j) instanceof Arc) {
								Node checkArcTarget = ((Arc) elementList.get(j)).getTarget();
								// No ID-property available - using hashCode for identification of synchronizations
								if (currentArcTarget.hashCode() == checkArcTarget.hashCode()) {
									match = false;
								}
							}
						}
						if(match) {
							counter++;
							out += "\t(" + counter + ") Concerning Step " + ((Step) currentArcSource).getId() + "\n";
						}
					}
				}
			}
		}
		return out;
	}
	
	/* 	Algorithm scans for the following pattern
	 * 
	 *      t1-----     -------
	 * 			|       |  1  |
	 * 			|       |     |
	 * 		    |       -------
	 *          |          |
	 *          |          |
	 *         \|/        \|/
	 * 		____________________
	 * 		____________________
	 */
	public static String case5(List<EObject> elementList, Integer counter) {
		String out = "";
		
		for (int i = 0; i < elementList.size(); i++) {
			if (elementList.get(i) instanceof Arc) {
				Node currentArcSource = ((Arc) elementList.get(i)).getSource();
				Node currentArcTarget = ((Arc) elementList.get(i)).getTarget();
				
				// Arc with source-type: transition
				if (currentArcSource instanceof Transition) {
					// Target-type: Synchronization
					if (currentArcTarget instanceof Synchronization) {
						Boolean match = false;
						String transition = "";
						String step = "";
						for (int j = 0; j < elementList.size(); j++) {
							if (elementList.get(j) instanceof Arc) {
								Node checkArcSource = ((Arc) elementList.get(j)).getSource();
								Node checkArcTarget = ((Arc) elementList.get(j)).getTarget();
								// No ID-property available - using hashCode for identification of synchronizations
								if (currentArcTarget.hashCode() == checkArcTarget.hashCode()) {
									if (checkArcSource instanceof Step) {
										if (!match) {
											transition += ((Transition) currentArcSource).getId();
											step += ((Step) checkArcSource).getId();
											match = true;
										}
									}
								}
							}
						}
						if(match) {
							counter++;
							out += "\t(" + counter + ") Concerning Transition " + transition + " and Step " + step + "\n";
						}
					}
				}
			}
		}
		return out;
	}
	
	/* 	Algorithm scans for the following pattern
	 * 
	 *      ____________________
	 *		____________________
	 *          |          |
	 *          |          |
	 *          |         \|/
	 *          |       -------
	 *          |       |  1  |
	 *         \|/      |     |
	 *      t1-----     -------
	 */
	public static String case6(List<EObject> elementList, Integer counter) {
		String out = "";
		
		for (int i = 0; i < elementList.size(); i++) {
			if (elementList.get(i) instanceof Arc) {
				Node currentArcSource = ((Arc) elementList.get(i)).getSource();
				Node currentArcTarget = ((Arc) elementList.get(i)).getTarget();
				
				// Arc with source-type: synchronization
				if (currentArcSource instanceof Synchronization) {
					// Target-type: Transition
					if (currentArcTarget instanceof Transition) {
						Boolean match = false;
						String transition = "";
						String step = "";
						for (int j = 0; j < elementList.size(); j++) {
							if (elementList.get(j) instanceof Arc) {
								Node checkArcSource = ((Arc) elementList.get(j)).getSource();
								Node checkArcTarget = ((Arc) elementList.get(j)).getTarget();
								// No ID-property available - using hashCode for identification of synchronizations
								if (currentArcSource.hashCode() == checkArcSource.hashCode()) {
									if (checkArcTarget instanceof Step) {
										if (!match) {
											transition += ((Transition) currentArcTarget).getId();
											step += ((Step) checkArcTarget).getId();
											match = true;
										}
									}
								}
							}
						}
						if(match) {
							counter++;
							out += "\t(" + counter + ") Concerning Transition " + transition + " and Step " + step + "\n";
						}
					}
				}
			}
		}
		return out;
	}
}