package de.hsu.grafcet.syntax.analysis.check;


import java.io.*;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.jgrapht.Graph;
//import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;

import de.hsu.grafcet.*;
import de.hsu.grafcet.syntax.analysis.parialOrder.PartialOrderEdge;
import de.hsu.grafcet.syntax.analysis.parialOrder.PartialOrderNode;

public class Util {
	

	
	/**
	 * Flattens and returns all elements of classes Node and ActionType from the partial Grafcets of a global Grafcet 
	 * @param global Grafcet
	 * @return Set of elements of classes Node and ActionType
	 */
	public static Set<EObject> getFlattenElements(Grafcet grafcet){
		Set<EObject> flatList = new LinkedHashSet<EObject>();
		for (Grafcet partialGrafcet : grafcet.getPartialGrafcets()) {
			for (EObject o : partialGrafcet.eContents()) {
				if (o instanceof PartialGrafcet || o instanceof MacrostepExpansion) {
					throw new IllegalArgumentException("The Grafcet must not contain nested partial Grafcets");
				} else {
					flatList.add(o);
				}
			}
		}
		return flatList;
	}
	
		
	/**
	 * Flattens and returns all elements from the partial Grafcets of a global Grafcet
	 * @deprecated a list of all flatten elements is usually not necessary. Use getFlattenElements() or getPartialGrafcets()
	 * @param rootList Elements of a global Grafcet
	 * @return list of elements of the partial Grafcets in the global Grafcet
	 */
	protected static List<EObject> getAllFlattenElements(EList<EObject> rootList){
		List<EObject> flatList = new LinkedList<EObject>();
		recursiveFlattenElements(rootList, flatList);
		return flatList;
	}
	
	//Sowohl Modellgröße, als auch Algorithmus können wahrscheinlich optimiert werden
	private static void recursiveFlattenElements(EList<EObject> rootList, List<EObject> flatList){
		for (int i = 0; i < rootList.size(); i++) {
			flatList.add(rootList.get(i));
			if (rootList.get(i) instanceof PartialGrafcet || rootList.get(i) instanceof MacrostepExpansion) {
				EList <EObject> expansionElements = rootList.get(i).eContents();
				Util.recursiveFlattenElements(expansionElements, flatList);
			}
		}
	}
	
	/**
	 * Flattens and returns all partial Grafcets from global Grafcet
	 * @deprecated partial Grafcets must not be nested
	 * @param globalGrafcet global Grafcet
	 * @return list of partial grafcets in global grafcet
	 */
	protected static List<Grafcet> getFlattenPartialGrafcets(Grafcet globalGrafcet){
		List<Grafcet> partialGrafcets = new LinkedList<Grafcet>();
		recursiveFlattenPartialGrafcets(globalGrafcet, partialGrafcets);
		return partialGrafcets;
	}
	
	private static void recursiveFlattenPartialGrafcets(Grafcet globalGrafcet, List<Grafcet> partialGrafcets) {
		for (Grafcet partialGrafcet : globalGrafcet.getPartialGrafcets()) {
			partialGrafcets.add(partialGrafcet);
			if (partialGrafcet.getPartialGrafcets().size() != 0) {
				recursiveFlattenPartialGrafcets(partialGrafcet, partialGrafcets);
			}
		}
	}
	
	/**
	 * Calculates sets of nodes for each disjoint subgraph from a {@link Graph}
	 * 
	 * @param g the Graph containing teh subgraphs
	 * @return List of disjoint Sets containing the nodes of the subgraphs
	 */
	public static List<Set<PartialOrderNode>> calculateSubgraphs(Graph<PartialOrderNode, PartialOrderEdge> g) {
		ConnectivityInspector<PartialOrderNode, PartialOrderEdge> ci = new ConnectivityInspector<>(g);
		return ci.connectedSets();
	}
	
	public static void createOutputFile(String out, String pathName) {
		//System.out.println(out);
		
		//TODO getParentsFile, createNewFile in anderem Paket nachpflegen
		
		try {
            //Whatever the file path is.
            File statText = new File(pathName);
            
            statText.getParentFile().mkdirs();
            statText.createNewFile();
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            w.write(out);
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
            e.printStackTrace();
        }
	}
}
