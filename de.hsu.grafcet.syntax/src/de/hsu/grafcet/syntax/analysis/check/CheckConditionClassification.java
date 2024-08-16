package de.hsu.grafcet.syntax.analysis.check;
import de.hsu.grafcet.*;
import de.hsu.grafcet.syntax.analysis.conditionClassification.*;

import java.util.*;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import terms.*;
import terms.impl.AndImpl;
import terms.impl.FallingEdgeImpl;
import terms.impl.NotImpl;
import terms.impl.OrImpl;
import terms.impl.RisingEdgeImpl;


/**
 * 
 * @author Schnakenbeck
 *	
 *	According to the standard the condition on a stored action on event has to be an event, i.e. the return value is true only for an infinitely short amount of time.
 *	Similar for continuous actions with a condition an event should not occur.
 *	This class checks if a condition can lead to an event, or not, or both.
 */

public class CheckConditionClassification {

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
	
	public CheckConditionClassification(URI modelURI, IContainer targetFolder, List<? extends Object> arguments, IFile model) {
		this.targetFolder = targetFolder;
		this.arguments = arguments;
		this.model = model;
		ResourceSet resSet = new ResourceSetImpl();
		Resource res = resSet.getResource(modelURI,	true);
		grafDoc = (Grafcet)res.getContents().get(0);
	}
	
	public String runChecking(){
		String out = new String();	
		out = "Result of condition classification check for GRAFCET-File " + this.model.getName().substring(0, this.model.getName().lastIndexOf(".grafcet")) + ":\n\n";
		
		//FIXME: Wenn alles darauf aufbaut, dass Connected GRAFCET nur in Teilgrafcets erstellt werden, sollte der Editor das auch verhindern!
		List<Grafcet> partialGrafcets = Util.getFlattenPartialGrafcets(grafDoc);
		
		
		int counter = 0;
		for (Grafcet partialGrafcet : partialGrafcets) {
			List<EObject> partialGrafcetElements = partialGrafcet.eContents();
			for (int i = 0; i < partialGrafcetElements.size(); i++) {
				//FIXME wird auf event Type geprüft?
				if (partialGrafcetElements.get(i) instanceof StoredAction) {
					if(((StoredAction)partialGrafcetElements.get(i)).getStoredActionType().equals(StoredActionType.EVENT)) {
						//System.out.println(partialGrafcetElements.get(i));
						out += "(" + counter + ") Condition " + ((StoredAction)partialGrafcetElements.get(i)).getTerm() + " is of type " + getEventType(((StoredAction)partialGrafcetElements.get(i)).getTerm()) + "\n";
						counter++;
					}
				}
				if (partialGrafcetElements.get(i) instanceof ContinuousAction) {					
					if(((ContinuousAction)partialGrafcetElements.get(i)).getContinuousActionType().equals(ContinuousActionType.ASSIGNATION_CONDITION)) {
						//System.out.println(partialGrafcetElements.get(i));
						out += "(" + counter + ") Condition " + ((ContinuousAction)partialGrafcetElements.get(i)).getTerm() + " is of type " + getEventType(((ContinuousAction)partialGrafcetElements.get(i)).getTerm()) + "\n";
						counter++;
					}
				}
				//FIXME final result is missing in .txt
			}
		}
		


		return out;
	}	
	
	private EventType getEventType(Term term) {
		TermWrapper rootTerm = new TermWrapper(term);
		return getEventType(recursiveTermVisitor(rootTerm));
	}
	
	/**
	 * Calculates the corresponding event type for a abstract value set of a condition
	 * @param abstractValue set of {@link AbstractSignalValue}
	 * @return the type of event for the abstract value set
	 */
	private EventType getEventType(Set<AbstractSignalValue> abstractValue) {
		if (abstractValue.equals(DefaultSets.getD()) || 
				abstractValue.equals(DefaultSets.getD_DD_0()) ||
				abstractValue.equals(DefaultSets.getDD_0())) {
			return EventType.NOEVENT;
		}else if (abstractValue.equals(DefaultSets.getD_DD_1()) ||
				abstractValue.equals(DefaultSets.getD_DD_1_DD_0())) {
			return EventType.BOTH;
		}else if (abstractValue.equals(DefaultSets.getDD_1())) {
			return EventType.EVENT;
		}else if (abstractValue.equals(DefaultSets.getEmptySet())) {
			throw new IllegalArgumentException("Term has no Value, Indicates the condition might not change its value");
		}else if (abstractValue.equals(DefaultSets.getD_DD_1_DD_0())) {
			throw new IllegalArgumentException("Term has contradicting values; Algortihm wrong");
		}
		else {
			throw new IllegalArgumentException("Default not implemented");
		}
	}
	
	//TODO: recursive algorithm in combination with the TermWrapper probably not really efficient. Every time a TermWrapper is instanciated from one of the childs all its sub childs are stored as well which causes an unnecessary overhead 
	/**
	 * Recursive algorithm which visits all the children of a term and calculates the abstract value sets for each child via a depth first search.
	 * Fist it checks if the current child is a leaf (i.e. the abstract value set can calculated independently from its subterms), and if any it calculates the abstract value set.
	 * If there are up to two childs they will be visited in a recursive way.
	 * If all childs are visited the abstract value set of the current child is calculated using {@link calculateAbstractValue}
	 * @param term current child or root term using the TermWrapper class
	 * @return abstract output value set of the visited term in the tree
	 */
	public static Set<AbstractSignalValue> recursiveTermVisitor(TermWrapper term) {
		Set<AbstractSignalValue> abstractOutputValue = new LinkedHashSet<AbstractSignalValue>();
		
		if(term.getTerm() instanceof Addition ||
				term.getTerm() instanceof Substraction ||
				term.getTerm() instanceof IntegerConstant) {
			throw new IllegalArgumentException("If root term is of sort Integer the syntax is incorrect. Otherwise there is a bug in the algorithm");
		}else if(term.getTerm() instanceof Variable) {
			abstractOutputValue = DefaultSets.getD();
		}else if(term.getTerm() instanceof GreaterThan || 
				term.getTerm() instanceof LessThan || 
				term.getTerm() instanceof Equality) {
			abstractOutputValue = DefaultSets.getD();
		}else if(term.getTerm() instanceof BooleanConstant) {
			//TODO refinement: C_o or C_1 
			abstractOutputValue = DefaultSets.getEmptySet();
		}else if(term.getTerm() instanceof BooleanOperator) {
			if (!term.isVisitedFirst()) {
				TermWrapper firstChild = new TermWrapper(((BooleanOperator)term.getTerm()).getSubterm().get(0));
				term.setAbstractInputValueFirst(recursiveTermVisitor(firstChild));
				term.setVisitedFirst(true);
			}
			if (!term.isVisitedSecond()) {
				try {
					TermWrapper secondChild = new TermWrapper(((BooleanOperator)term.getTerm()).getSubterm().get(1));
					term.setAbstractInputValueSecond(recursiveTermVisitor(secondChild));
					term.setVisitedSecond(true);
				} catch (Exception e) {
					//isVisitedSecond should return true if getSubterm().get(1) is null
					//otherwise there is a bug:
					e.printStackTrace();
				}
			}
			if (term.isVisitedFirst() && term.isVisitedSecond()){
				Set<AbstractSignalValue> output = calculateAbstractValue(term.getTerm().getClass(),
						term.getAbstractInputValueFirst(), term.getAbstractInputValueSecond());
				abstractOutputValue.addAll(output);
			}

		}else {
			throw new IllegalArgumentException("Bug in the algorithm: Unhandled term instance; Could be null");
		}

		return abstractOutputValue;
	}
	
	/**
	 *  Calculates union of the elementary results of the abstract operator values over the value sets of its operands
	 *  For elementary results of the abstract operator values the corresponding {@link abstractAND}, {@link abstractOR}, {@link abstractNOT}, {@link abstractRE}, {@link abstractFE} is called
	 *  See external documentation
	 * @param termClass class of the abstract operand i.e. AND, OR, NOT, RE, FE
	 * @param valueSetFirstOperand AbstractSignalValue set of the first operand - should not be null
	 * @param valueSetSecondOperand AbstractSignalValue set of the second operand - should be null for NOT, RE, FE operator
	 * @return abstract output value Set of operator
	 */
	
	private static Set<AbstractSignalValue> calculateAbstractValue(Class termClass, 
			Set<AbstractSignalValue> valueSetFirstOperand, Set<AbstractSignalValue> valueSetSecondOperand){
		Set<AbstractSignalValue> valueSetOperator = new LinkedHashSet<AbstractSignalValue>();
		
		for (AbstractSignalValue valueFirstOperand : valueSetFirstOperand) {
			if(valueSetSecondOperand == null && valueSetFirstOperand != null) {
				if(termClass == NotImpl.class) {
					valueSetOperator.addAll(abstractNOT(valueFirstOperand));
				}
				if(termClass == RisingEdgeImpl.class) {
					valueSetOperator.addAll(abstractRE(valueFirstOperand));
				}
				if(termClass == FallingEdgeImpl.class) {
					valueSetOperator.addAll(abstractFE(valueFirstOperand));
				}
			}else if (valueSetSecondOperand != null && valueSetFirstOperand != null){
				for (AbstractSignalValue valueSecondOperand : valueSetSecondOperand) {
					if(termClass == AndImpl.class) {
						valueSetOperator.addAll(abstractAND(valueFirstOperand, valueSecondOperand));
					}
					if(termClass == OrImpl.class) {
						valueSetOperator.addAll(abstractOR(valueFirstOperand, valueSecondOperand));
					}
				}
			}else {
				throw new IllegalArgumentException("error in calculateAbstractValue: value of second operand should not be null!");
			}
		}
		return valueSetOperator;
	}
	
	/**
	 * Definition of operators in abstract domain.
	 * @param firstValue elementary abstract value
	 * @return abstract output value set for elementary input value @param firstValue
	 */
	private static Set<AbstractSignalValue> abstractNOT(AbstractSignalValue firstValue){
		Set<AbstractSignalValue> valueSetOperator = new LinkedHashSet<AbstractSignalValue>();
		switch (firstValue) {
		case D:
			valueSetOperator = DefaultSets.getD();
			break;
		case DD_1:
			valueSetOperator = DefaultSets.getDD_0();
			break;
		case DD_0:
			valueSetOperator = DefaultSets.getDD_1();
			break;
		default:
			valueSetOperator = DefaultSets.getEmptySet(); //TODO refinement C_0, C_1
			break;
		}
		return valueSetOperator;
	}
	private static Set<AbstractSignalValue> abstractRE(AbstractSignalValue firstValue){
		Set<AbstractSignalValue> valueSetOperator = new LinkedHashSet<AbstractSignalValue>();
		switch (firstValue) {
		case D:
			valueSetOperator = DefaultSets.getDD_1();
			break;
		case DD_1:
			valueSetOperator = DefaultSets.getDD_1();
			break;
		case DD_0:
			valueSetOperator = DefaultSets.getEmptySet();
			break;
		default:
			valueSetOperator = DefaultSets.getEmptySet(); //TODO refinement C_0, C_1
			break;
		}
		return valueSetOperator;
	}
	private static Set<AbstractSignalValue> abstractFE(AbstractSignalValue firstValue){
		Set<AbstractSignalValue> valueSetOperator = new LinkedHashSet<AbstractSignalValue>();
		switch (firstValue) {
		case D:
			valueSetOperator = DefaultSets.getDD_1();
			break;
		case DD_1:
			valueSetOperator = DefaultSets.getEmptySet();
			break;
		case DD_0:
			valueSetOperator = DefaultSets.getDD_1();
			break;
		default:
			valueSetOperator = DefaultSets.getEmptySet(); //TODO refinement C_0, C_1
			break;
		}
		return valueSetOperator;
	}
	
	private static Set<AbstractSignalValue> abstractAND(AbstractSignalValue firstValue,
			AbstractSignalValue secondValue){
		Set<AbstractSignalValue> valueSetOperator = new LinkedHashSet<AbstractSignalValue>();

		if (firstValue == secondValue) {
			valueSetOperator.add(firstValue);
		}else {
			Set<AbstractSignalValue> valueSetOperands = new LinkedHashSet<AbstractSignalValue>() {{
				add(firstValue);
				add(secondValue);
			}};
			//TODO für andere operatoren Analog
			//if(valueSetOperands.contains(c_1 /bzw. c_0)){
			//valueSetOperator = c_0 / bzw. valueSetOperands\c_0}
			if(valueSetOperands.equals(DefaultSets.getD_DD_1())) {
				valueSetOperator = DefaultSets.getDD_1();
			}
			if(valueSetOperands.equals(DefaultSets.getD_DD_0())) {
				valueSetOperator = DefaultSets.getD_DD_0();
			}
			if(valueSetOperands.equals(DefaultSets.getDD_1_DD_0())) {
				valueSetOperator = DefaultSets.getDD_1();
			}
		}
		return valueSetOperator;
	}
	
	private static Set<AbstractSignalValue> abstractOR(AbstractSignalValue firstValue,
			AbstractSignalValue secondValue){
		Set<AbstractSignalValue> valueSetOperator = new LinkedHashSet<AbstractSignalValue>();

		if (firstValue == secondValue) {
			valueSetOperator.add(firstValue);
		}else {
			Set<AbstractSignalValue> valueSetOperands = new LinkedHashSet<AbstractSignalValue>() {{
				add(firstValue);
				add(secondValue);
			}};
			if(valueSetOperands.equals(DefaultSets.getD_DD_1())) {
				valueSetOperator = DefaultSets.getD_DD_1();
			}
			if(valueSetOperands.equals(DefaultSets.getD_DD_0())) {
				valueSetOperator = DefaultSets.getDD_0();
			}
			if(valueSetOperands.equals(DefaultSets.getDD_1_DD_0())) {
				valueSetOperator = DefaultSets.getDD_0();
			}
		}
		return valueSetOperator;
	}
	
}
