package de.hsu.grafcet.syntax.analysis.parialOrder;

import org.jgrapht.graph.DefaultEdge;

public class PartialOrderEdge extends DefaultEdge{
	PartialOrderEdgeType type;

	public PartialOrderEdge(PartialOrderEdgeType type) {
		super();
		this.type = type;
	}
	
	public PartialOrderEdgeType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "(" + getSource() + " : " + getTarget() + " : " + type + ")";
	}
}