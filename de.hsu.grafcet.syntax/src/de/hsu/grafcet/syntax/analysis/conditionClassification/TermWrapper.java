package de.hsu.grafcet.syntax.analysis.conditionClassification;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import terms.Operator;
import terms.Term;
import terms.Variable;

/**
 * 
 * @author Schnakenbeck
 *	Wrapper class for terms to calculate their abstract value
 *	Can handle all terms regardless of number of sub-terms:
 *		-visited flag is set to true, if the corresponding sub-term is null
 *		-corresponding abstract value set remains null
 */

public class TermWrapper{

	Set<AbstractSignalValue> abstractInputValueFirst = null;
	Set<AbstractSignalValue> abstractInputValueSecond = null;
	boolean visitedFirst = false;
	boolean visitedSecond = false;
	Term term;
	
	public TermWrapper(Term term) {
		super();
		this.term = term;
		calculateChilds();
	}
	
	/**
	 * Root term can have different number of sub-terms (e.g. Variable has zero sub-terms, Not one and And two)
	 * Method marks sub-term as visited in case there are no sub-terms
	 * In this case abstract input value sets remain null.
	 */
	private void calculateChilds() {
		if (this.term instanceof Variable){
		}
		if (this.term instanceof Operator) {
			if (((Operator) term).getSubterm().size() == 0) {
				visitedFirst = true;
				visitedSecond = true;
			}else if (((Operator) term).getSubterm().size() == 1) {
				visitedSecond = true;
			}else if (((Operator) term).getSubterm().size() == 2) {
			}else {
				throw new IllegalArgumentException("Term" + term + "has more than 2 subterms");
			}
		}
	}
	
	public Term getTerm() {
		return term;
	}
	public void setTerm(Term term) {
		this.term = term;
	}	
	public boolean isVisitedFirst() {
		return visitedFirst;
	}

	public void setVisitedFirst(boolean visitedFirst) {
		this.visitedFirst = visitedFirst;
	}

	public boolean isVisitedSecond() {
		return visitedSecond;
	}

	public void setVisitedSecond(boolean visitedSecond) {
		this.visitedSecond = visitedSecond;
	}

	public Set<AbstractSignalValue> getAbstractInputValueFirst() {
		return abstractInputValueFirst;
	}
	public void setAbstractInputValueFirst(Set<AbstractSignalValue> abstractInputValueFirst) {
		this.abstractInputValueFirst = abstractInputValueFirst;
	}
	public Set<AbstractSignalValue> getAbstractInputValueSecond() {
		return abstractInputValueSecond;
	}
	public void setAbstractInputValueSecond(Set<AbstractSignalValue> abstractInputValueSecond) {
		this.abstractInputValueSecond = abstractInputValueSecond;
	}

	
	
	
}
