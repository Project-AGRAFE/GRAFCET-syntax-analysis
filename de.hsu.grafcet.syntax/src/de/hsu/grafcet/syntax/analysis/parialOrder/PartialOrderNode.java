package de.hsu.grafcet.syntax.analysis.parialOrder;

import de.hsu.grafcet.Grafcet;

public class PartialOrderNode{
	
	Grafcet partialGrafcet;
	int depth;

	public PartialOrderNode(Grafcet partialGrafcet) {
		super();
		this.partialGrafcet = partialGrafcet;
	}

	public Grafcet getPartialGrafcet() {
		return partialGrafcet;
	}
	public void setPartialGrafcet(Grafcet partialGrafcet) {
		this.partialGrafcet = partialGrafcet;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getDepth() {
		return depth;
	}
	
	@Override
	public String toString() {
		return partialGrafcet.getName() + "<" + depth + ">";
	}
}
