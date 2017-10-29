package pl.examples;

import pl.core.*;

public class TruthTellersKB extends KB {

    public TruthTellersKB(){
        super();
    /*
    Part A
% Three people, Amy, Bob, and Cal, are each either a liar 
% or a truth-teller. Assume that liars always lie, and 
% truth-tellers always tell the truth.
%  - Amy says, "Cal and I are truthful."
%  - Bob says, "Cal is a liar."
%  - Cal says, "Bob speaks the truth or Amy lies."
*/
    Symbol amy = intern("amy");
    Symbol cal = intern("cal");
    Symbol bob = intern("bob");
    add(new Implication(amy, new Conjunction(cal,amy)));
    add(new Implication(bob, new Negation(cal)));
    add(new Implication(cal, new Disjunction(bob,new Negation(amy))));
//-----------------------------------------------------------------    
/*
    Part B
% Three people, Amy, Bob, and Cal, are each either a liar 
% or a truth-teller. 0Assume that liars always lie, and 
% truth-tellers always tell the truth.
%  - Amy says, "Cal is not honest."
%  - Bob says, "Amy and Cal never lie."
%  - Cal says, "Bob is correct."
*/   
    Symbol amy2 = intern("amy2");
    Symbol cal2 = intern("cal2");
    Symbol bob2 = intern("bob2");
    add(new Implication(amy2, new Negation(cal2)));
    add(new Implication(bob2, new Conjunction(amy2, cal2)));
    add(new Implication(cal2, bob2));
                        
    }
        
        public static void main(String[] argv) {
		      new TruthTellersKB().dump();
	  }
}
