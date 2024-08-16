package de.hsu.grafcet.syntax.analysis.check;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;

import de.hsu.grafcet.*;
/**
 * 
 * @author Schnakenbeck
 *	
 *	Standard distinguishs between global Grafcet, partial Grafcet and connected Grafcet. 
 *	Global Grafcet contains a set of partial Grafcets witch contain steps, transitions etc. and can control each other.
 *	A connected Grafcet is a set of linked elements.
 *	This class checks if every connected Grafcet can be initialized. 
 *
 */

public class CheckConnectedGrafcets {

	private IFile model;
	
	/**
	 * Grafcet model
	 */
	private Grafcet grafDoc;

	/**
	 * The output folder.
	 */
	private IContainer targetFolder;

	/**
	 * The other arguments.
	 */
	List<? extends Object> arguments;
	
	public CheckConnectedGrafcets(URI modelURI, IContainer targetFolder, List<? extends Object> arguments, IFile model) {
		this.targetFolder = targetFolder;
		this.arguments = arguments;
		this.model = model;
		ResourceSet resSet = new ResourceSetImpl();
		Resource res = resSet.getResource(modelURI,	true);
		grafDoc = (Grafcet)res.getContents().get(0);
	}
	

	public String runChecking(){
		String out = "\n\nResult of check for connected Grafcets for GRAFCET-File " + this.model.getName().substring(0, this.model.getName().lastIndexOf(".grafcet")) + ":\n\n";
		
		//Es wird eine Liste benötigt, in der alle Schritte drin sind, die von einer Zwangssteuerung aktiviert werden.
		//FIXME makroschritte und Quelltransitionen berücksichtigen
		
		Graph<EObject, DefaultEdge> graph = extractGraph(grafDoc);
		List<Set<EObject>> subgraphs = calculateSubgraphs(graph); 
		Set<Set<EObject>> initializableSubgraphs = new LinkedHashSet<Set<EObject>>();
		
		Set<InitializableType> forcedSteps = getForcedSteps(grafDoc);
		
		for (Set<EObject> s : subgraphs) {
			for (EObject o : s) {
				if (o instanceof InitializableType) {
					if (((InitializableType) o).isInitial() //Wenn Makroschritt Initialschritt enthält, wird das hier nicht beachtet
							|| ((InitializableType) o).isActivationLink()
							|| forcedSteps.contains(o)){ //sind das die selben Objekte, die da referenziert werden?
						//kann initialisiert werden
						//s ist initialisierbar
						//Zwangsstuerungen noch beachten
						initializableSubgraphs.add(s);
					}
				}
			}
		}
		subgraphs.removeAll(initializableSubgraphs); //TODO funktioniert hier die removeAll() richtig, da es ja ein Set über Sets über Schritte ist
		//subgraphs enthält ALLE Nodes, initializableSubgraphs NUR InitializableType, daher unten for-Verschachtelung
		
		if(subgraphs.size() > 0) {
			out += "The following connected Grafcet(s) is (are) not initializable: ";
			
			for (Set<EObject> s : subgraphs) {
				out += "\n";
				for(EObject o : s) {
					if (o instanceof InitializableType) {
						out += " Step " + ((InitializableType)o).getId();
					}
					
				}
			}
		}
		
		return out;
	}	
	
	/**
	 * Represents all {@link Node} and {@link Arc} instances of a @param globalGrafcet as Graph object as verices and edges. Connectivity can be analyzed.  
	 * @param globalGrafcet Global Grafcet to transorm into an corresponding Graph
	 * @return Flattened graph of all the nodes and arcs of the Grafcet.
	 */
	public static Graph<EObject, DefaultEdge> extractGraph(Grafcet globalGrafcet) {
		Graph<EObject, DefaultEdge> graph = new DirectedPseudograph<>(DefaultEdge.class);

		Set<EObject> elementList = Util.getFlattenElements(globalGrafcet);

		//Damit edges hinzugefügt werden können, müssen erst alle vertices hinzugefügt werden
		for (EObject o: elementList) {
			if (o instanceof Node) {
				graph.addVertex(o);
			}
		}
		for (EObject o: elementList) {
			if (o instanceof Arc) {
				graph.addEdge(((Arc)o).getSource(), ((Arc)o).getTarget(), new DefaultEdge());
			}
		}
		return graph;
	}
	
	private static Set<InitializableType> getForcedSteps(Grafcet globalGrafcet){
		Set<InitializableType> forcedSteps = new LinkedHashSet<InitializableType>();
		
		//mögliche Optimierung: getFlattenElements wird doppelt ausgeführt (siehe extractGraph())
		Set<EObject> elementList = Util.getFlattenElements(globalGrafcet);
		
		for (EObject o : elementList) {
			if (o instanceof ForcingOrder) {
				if (((ForcingOrder) o).getForcedSteps() != null 
						&& ((ForcingOrder) o).getForcingOrderType().equals(ForcingOrderType.EXPLICIT_SITUATION)) {
					forcedSteps.addAll(((ForcingOrder) o).getForcedSteps());
				}else {
					//sonst kleiner Syntaxfehler im Modell
				}
			}
		}
		return forcedSteps;
	}
	
	/**
	 * Calculates sets of nodes for each disjoint subgraph from a {@link Graph}
	 * 
	 * @param g the Graph containing the subgraphs
	 * @return List of disjoint Sets containing the nodes of the subgraphs
	 */
	public static List<Set<EObject>> calculateSubgraphs(Graph<EObject, DefaultEdge> g) {
		ConnectivityInspector<EObject, DefaultEdge> ci = new ConnectivityInspector<>(g);
		return ci.connectedSets();
	}
	
}
