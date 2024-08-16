package de.hsu.grafcet.syntax.analysis.check;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import de.hsu.grafcet.syntax.analysis.parialOrder.*;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.cycle.SzwarcfiterLauerSimpleCycles;
import org.jgrapht.alg.util.NeighborCache;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.DirectedPseudograph;

import de.hsu.grafcet.*;
/**
 * 
 * @author Schnakenbeck
 *
 *         Syntactical analysis of the partial order, according to Lesage et al.
 *         Dependencies between hierarchical Grafcets form o partial order, i.e.
 *         no cycle occures. Detects cycles. //TODO Assign a value for the
 *         hierarchical depth for further analysis. //TODO
 *
 */

public class CheckPartialOrder {
	//FIXME not tested yet (especially the partial order extraction)
	//FIXME test with new model: Ausgabe verbugt, makroschritte berücksichtigt?

	/**
	 * Grafcet model
	 */
	private Grafcet globalGrafcet;

	public CheckPartialOrder(URI modelURI, IContainer targetFolder, List<? extends Object> arguments, IFile model) {
		ResourceSet resSet = new ResourceSetImpl();
		Resource res = resSet.getResource(modelURI, true);
		globalGrafcet = (Grafcet) res.getContents().get(0);
	}

	public String runChecking() {
		String out = new String();
		out = "\n\nResult of partial order check" + ":\n";

		Graph<PartialOrderNode, PartialOrderEdge> partialOrder = extractPartialOrder(globalGrafcet);
		//Partielle Ordnung kann formal keine Zyklen enthalten
		List<List<PartialOrderNode>> cycles = calculateCycles(partialOrder);
		if (cycles.size() != 0) {
			out += "Partial order of Grafcet contains " + cycles.size() + " cycle(s): ";
			out += cycles + " ";
			out += "Remove cycles before continue";
		} else {
			out += "Partial order of Grafcet contains no cycles. \n";
			List<Graph<PartialOrderNode, PartialOrderEdge>> dagList = createDagList(partialOrder);
			for (Graph<PartialOrderNode, PartialOrderEdge> dag : dagList) {
				calculateDepth(dag);
			}
			out += "Partial order of Grafcet: " + dagList;
		}
		return out;
	}

	private static List<Graph<PartialOrderNode, PartialOrderEdge>> createDagList(
			Graph<PartialOrderNode, PartialOrderEdge> partialOrder) {
		List<Graph<PartialOrderNode, PartialOrderEdge>> dagList = new LinkedList<>();
		List<Set<PartialOrderNode>> subgraphs = calculateSubgraphs(partialOrder);
		for (Set<PartialOrderNode> subgraph : subgraphs) {
			Graph<PartialOrderNode, PartialOrderEdge> dag = transformToSubDag(subgraph, partialOrder);
			calculateDepth(dag);
			dagList.add(dag);
		}
		return dagList;
	}

	
	/**
	 * @deprecated see {@link HypergrafcetGenerator}
	 * @param globalGrafcet
	 * @return
	 */
	
	public static Graph<PartialOrderNode, PartialOrderEdge> extractPartialOrder(Grafcet globalGrafcet) {
		Graph<PartialOrderNode, PartialOrderEdge> partialOrder = new DirectedPseudograph<>(PartialOrderEdge.class);

		// TODO getFlattenPartialGrafcets() SHOULD do the same as
		// globalGrafcet.getPartialGrafcets;
		// editor SHOULD provide that
		// set method to deprecated afterwards
		List<Grafcet> partialGrafcets = Util.getFlattenPartialGrafcets(globalGrafcet);

		// extract nodes from grafcet
		for (Grafcet partialGrafcet : partialGrafcets) {
			PartialOrderNode node = new PartialOrderNode(partialGrafcet);
			partialOrder.addVertex(node);
		}
		// extract edges
		for (PartialOrderNode sourceNode : partialOrder.vertexSet()) {
			for (EObject object : sourceNode.getPartialGrafcet().eContents()) {
				// search for forced partial Grafcet via enclosing steps
				//TODO: Umgekehrte Suche ggf. schneller: if (partialGrafcet.isEnclosed) {addEdge(partialGrafcet.getEnclosingStep().eContainer(), partialGrafcet}
				if (object instanceof EnclosingStep) {
					for (Grafcet forcedPartialGrafcet : ((EnclosingStep) object).getPartialGrafcets()) {
						// get taget node
						for (PartialOrderNode targetNode : partialOrder.vertexSet()) {
							if (targetNode.getPartialGrafcet().equals(forcedPartialGrafcet)) {
								// add edge
								partialOrder.addEdge(sourceNode, targetNode,
										new PartialOrderEdge(PartialOrderEdgeType.ENCLOSING_STEP));
							}
						}
					}
					// search for forced partial Grafcet via forcing order
				} else if (object instanceof ForcingOrder) {
					// get taget node
					for (PartialOrderNode targetNode : partialOrder.vertexSet()) {
						if (targetNode.getPartialGrafcet().equals(((ForcingOrder) object).getPartialGrafcet())) {
							// add edge
							partialOrder.addEdge(sourceNode, targetNode,
									new PartialOrderEdge(PartialOrderEdgeType.FORCING_ORDER));
						}
					}
				}
			}
		}
		return partialOrder;
	}

	/**
	 * @deprecated Checks partial order: - How many cycles - How many disjoint
	 *             subgraphs - Hierarchy depth of each node
	 * @param rawPartialOrderGraph partial order represented as {@link Graph}
	 * @return List of disjoint DAGs with hierarchical depth stored in the nodes;
	 *         null and error message otherwise
	 */
	private static List<Graph<PartialOrderNode, PartialOrderEdge>> checkpartialOrder(
			Graph<PartialOrderNode, PartialOrderEdge> rawPartialOrderGraph) {
		List<List<PartialOrderNode>> cycles = calculateCycles(rawPartialOrderGraph);
		if (cycles.size() != 0) {
			System.out.println(rawPartialOrderGraph + " contains" + cycles.size() + " cycle(s):");
			System.out.println(cycles);
			return null;
		} else {
			List<Graph<PartialOrderNode, PartialOrderEdge>> dagList = new LinkedList<>();
			for (Set<PartialOrderNode> subgraph : calculateSubgraphs(rawPartialOrderGraph)) {
				Graph<PartialOrderNode, PartialOrderEdge> dag = transformToSubDag(subgraph, rawPartialOrderGraph);
				calculateDepth(dag);
				dagList.add(dag);
			}
			return dagList;
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

	/**
	 * Creates a Directed Acyclic Graph from a set of (sub)nodes and ALL edges from the partial order
	 * If a node from @param nodes exist in  @param g the corresponding edges are added to the new DAG
	 * @param nodes Set of nodes for the new DAG.  @param g must contain the Set @param nodes
	 * @param g original partial order containing the edges
	 * @return directed acyclic graph
	 */
	public static DirectedAcyclicGraph<PartialOrderNode, PartialOrderEdge> transformToSubDag(
			Set<PartialOrderNode> nodes, Graph<PartialOrderNode, PartialOrderEdge> g) {
		DirectedAcyclicGraph<PartialOrderNode, PartialOrderEdge> dag = new DirectedAcyclicGraph<>(
				PartialOrderEdge.class);
		for (PartialOrderNode n : nodes) {
			dag.addVertex(n);

		}
		for (PartialOrderEdge e : g.edgeSet()) {
			if (dag.containsVertex(g.getEdgeSource(e))) {
				dag.addEdge(g.getEdgeSource(e), g.getEdgeTarget(e), e);
			}
		}
		return dag;
	}

	/**
	 * Calculates a list with all cycles, i.e. a List of nodes, of a {@link Graph}
	 * 
	 * @param g
	 * @return List of List containing the nodes of a cycle
	 */
	public static List<List<PartialOrderNode>> calculateCycles(Graph<PartialOrderNode, PartialOrderEdge> g) {
		SzwarcfiterLauerSimpleCycles<PartialOrderNode, PartialOrderEdge> cd = new SzwarcfiterLauerSimpleCycles<>(g);
		return cd.findSimpleCycles();
	}

	/**
	 * Calculates the depth of each node for an DAG. Algorithm by Lesage et al.:
	 * Hierarchical approach to GRAFCET using forcing order
	 * 
	 * changes the depth value in {@link PartialOrderNode}
	 * 
	 * @param g {@link DirectedAcyclicGraph} instance with {@link PartialOrderNode}
	 *          vertices
	 */
	public static void calculateDepth(Graph<PartialOrderNode, PartialOrderEdge> g) {
		if (!(g instanceof DirectedAcyclicGraph<?, ?>)) {
			throw new IllegalArgumentException();
		}
		// setting big Number for infinite
		int bigDepht = g.vertexSet().size() * 10;
		for (PartialOrderNode n : g.vertexSet()) {
			n.setDepth(bigDepht);
		}
		boolean isDepthBig = true;
		while (isDepthBig) {
			isDepthBig = false;
			for (PartialOrderNode n : g.vertexSet()) {
				if (n.getDepth() >= bigDepht) {
					isDepthBig = true;
					n.setDepth(1);
					for (PartialOrderNode pn : new NeighborCache<PartialOrderNode, PartialOrderEdge>(g)
							.predecessorsOf(n)) {
						int newDepth = Math.max(n.getDepth(), pn.getDepth() + 1);
						n.setDepth(newDepth);
					}
				}
			}
		}
	}

	private void assignHierarchicalLevel() {

	}

	private void detectCycles() {

	}
}
