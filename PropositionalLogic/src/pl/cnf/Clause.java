/*
 * File: Clause.java
 * Creator: George Ferguson
 * Created: Tue Mar 15 15:58:15 2011
 * Time-stamp: <Fri Mar 23 14:50:20 EDT 2012 ferguson>
 */

package pl.cnf;

import pl.modelchecking.MyModel;
import pl.core.BinaryCompoundSentence;
import pl.core.BinaryConnective;
import pl.core.Sentence;
import pl.util.ArraySet;

/**
 * A clause is a disjunction of literals.
 * We represent it as a set for ease of use later.
 */
public class Clause extends ArraySet<Literal> {

	/**
	 * Return a new Clause constructed from the given Sentence, which
	 * must be a disjunction of literals (i.e., an AtomicSentence, the
	 * negation of an AtomicSentence, or a BinaryCompoundSentence whose
	 * connective is OR in which case its arguments must be disjunctions
	 * of literals.
	 */
	public Clause(Sentence s) throws IllegalArgumentException {
		super();
		toClauses(s, this);
	}

	protected static void toClauses(Sentence s, Clause c) throws IllegalArgumentException {
		if (s instanceof BinaryCompoundSentence) {
			BinaryCompoundSentence bcs = (BinaryCompoundSentence)s;
			BinaryConnective conn = bcs.getConnective();
			if (conn == BinaryConnective.OR) {
				Sentence lhs = bcs.getLhs();
				Sentence rhs = bcs.getRhs();
				toClauses(lhs, c);
				toClauses(rhs, c);
				return;
			}
		}
		// Otherwise
		c.add(new Literal(s));
	}

	/**
	 * Return the string representation of this Clause.
	 */
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("{");
		boolean first = true;
		for (Literal literal : this) {
			if (!first) {
				buf.append(",");
			} else {
				first = false;
			}
			buf.append(literal.toString());
		}
		buf.append("}");
		return buf.toString();
	}




	/* ****************************************
				My added methods
	 **************************************** */

    /**
     * Return true if this Clause is satisfied by the given Model.
     * That is, if any of its Literals is satisfied by the Model.
     */

	// my version of isSatisfiedBy for a Clause
	public boolean isSatisfiedBy(MyModel model){
		for (Literal l : this){
			if (l.isSatisfiedBy(model)){
				return true;
			}
		}
		return false;
	}

	// at least one literal in the clause is true in the model
	public boolean knownToBeTrue(MyModel model) {
		for (Literal l : this){
			if (model.containsSymbol(l.getContent()) && l.isSatisfiedBy(model)){
				return true;
			}
		}
		return false;
	}

	// every literal in the clause is known to be faulse
	public boolean knownToBeFalse(MyModel model) {
		for (Literal l : this){
			if (!model.containsSymbol(l.getContent())){
				return false;
			}
			if (l.isSatisfiedBy(model)){
				return false;
			}
		}
		return true;
	}


}
