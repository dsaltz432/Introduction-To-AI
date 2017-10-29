
/*

-An implementation of the Model interface
-Uses a HashMap to record the assignment of true or false values to Symbols

*/


package pl.modelchecking;

import pl.cnf.Clause;
import pl.core.KB;
import pl.core.Model;
import pl.core.Sentence;
import pl.core.Symbol;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class MyModel implements Model{

    protected HashMap<String,Boolean> map;

    public MyModel(){
       map = new HashMap<String,Boolean>();
    }

    /**
     * Used in TestDPLL
     * -assigns a boolean to a symbol, and returns the updated MyModel
     */
    public MyModel assign(Symbol sym, boolean value){
        map.put(sym.toString(),value);
        return this;
    }

    /**
     * Used in TestModelChecking
     */
    @Override
    public void set(Symbol sym, boolean value) {
        map.put(sym.toString(),value);
    }

    /**
     * The method that ultimately compares every Symbol in a sentence to the truth value in the model
     * get(sym) should return true if the model has sym being true, and false if the model has sym being false
     */
    @Override
    public boolean get(Symbol sym) {
        return map.get(sym.toString());
    }

    public boolean containsSymbol(Symbol sym){
        return map.containsKey(sym.toString());
    }

    // if any sentence does not satisfy the KB, then the model fails
    public boolean satisfies(KB kb) {
        for (Sentence s : kb.sentences()){
            if (!s.isSatisfiedBy(this)){
               return false;
            }
//            if (!satisfies(s)){
//                return false;
//            }
        }
        return true;
    }

    @Override
    public boolean satisfies(Sentence sentence) {
        return sentence.isSatisfiedBy(this);
    }

    @Override
    public void dump() {
        if (!map.isEmpty()){
            for (Map.Entry<String,Boolean> entry : map.entrySet()){
                System.out.print(entry.getKey() + ": " + entry.getValue() + "  ");
            }
        }
    }


    public boolean atLeastOneClauseFalse(Set<Clause> clauses) {
        for (Clause clause : clauses){
            if (clause.knownToBeFalse(this)){
                return true;
            }
        }
        return false;
    }

    public boolean allClausesTrue(Set<Clause> clauses) {
        for (Clause clause : clauses){
            if (!clause.knownToBeTrue(this)) {
                return false;
            }
        }
        return true;
    }

}
