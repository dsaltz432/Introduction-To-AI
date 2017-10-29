
package pl.dpll;

import pl.cnf.CNFConverter;
import pl.cnf.Clause;
import pl.cnf.Literal;
import pl.core.KB;
import pl.core.Negation;
import pl.core.Sentence;
import pl.core.Symbol;
import pl.main.SymbolAssignment;
import pl.modelchecking.MyModel;

import java.util.*;

public class DPLL {

    /**
     * Takes a KB and a Sentence as parameters.
     * The goal is to determine if KB entails beta.
     * We achieve this by proof by contradiction, showing that (KB ^ NOT beta) is unsatisfiable
     * We combine the sentences in the KB, and the beta Sentence to generate a Set of clauses
     * We then feed in the clauses, the symbols in the KB, and an empty Model into the dpll algorithm.
     *
     * dpll returns false if there is no model in which KB and NOT beta are satisfied, meaning KB entails beta
     */
    public boolean satisfiable(KB kb, Sentence beta){
        Collection<Sentence> sentences = kb.sentences();
        sentences.add(new Negation(beta)); // add  NOT Beta
        Set<Clause> clauses = convertToClauses(sentences);
        Stack<Symbol> symbols = new Stack<Symbol>();
        symbols.addAll(kb.symbols());
        return dpll(clauses,symbols,new MyModel());
    }

    // algorithm based figure AIAMA 7.17
    private boolean dpll(Set<Clause> clauses, Stack<Symbol> symbols, MyModel model) {

        if (model.allClausesTrue(clauses)) return true;
        if (model.atLeastOneClauseFalse(clauses)) return false;

        SymbolAssignment pure = findPureSymbol(clauses,model,symbols);
        if (pure != null) {
            symbols = removeLitFromSymbols(pure,symbols);
            return dpll(clauses,symbols,model.assign(pure.getSymbol(),pure.getValue()));
        }

        SymbolAssignment unit = findUnitClause(clauses,model,symbols);
        if (unit != null){
            symbols = removeLitFromSymbols(unit,symbols);
            return dpll(clauses,symbols,model.assign(unit.getSymbol(),unit.getValue()));
        }

        Symbol p  = symbols.pop();
        return dpll(clauses,symbols,model.assign(p,true)) || dpll(clauses,symbols,model.assign(p,false));
    }

    // takes a Literal and Stack<Symbol>, and returns a new Stack<Symbol> without the specified Literal
    public Stack<Symbol> removeLitFromSymbols(SymbolAssignment symAssign, Stack<Symbol> symbols){
        Stack<Symbol> temp = new Stack<Symbol>();
        for (Symbol sym : symbols){
            if (!sym.toString().equals(symAssign.getSymbol().toString())){
                temp.add(sym);
            }
        }
        return temp;
    }

    // in dpll, a unit clause is defined as a clause where every literal is assigned false by the model,
    // except for one literal which is unassigned by the model. The unassigned literal
    // must be true for the clause (and therefore the sentence) to be true, therefore,
    // we assign the Symbol of that literal in the model such that the literal is true
    private SymbolAssignment findUnitClause(Set<Clause> clauses, MyModel model, Stack<Symbol> symbols) {
        for (Clause clause : clauses){
            int unassigned = 0;
            int trueInModel = 0;
            SymbolAssignment symbolAssignment = null;
            for (Literal l : clause){
                if (model.containsSymbol(l.getContent())){ // if assigned in model
                    if (l.isSatisfiedBy(model)) {// if the literal is true in the model
                        trueInModel++;
                    }
                } else { // unassigned in model
                    symbolAssignment = new SymbolAssignment(l.getContent(),getBool(l));
                    unassigned++;
                }
                if (unassigned > 1 || trueInModel > 0){ // not unit clause, so try the next clause
                    break;
                }
            }
            if (unassigned == 1 && trueInModel == 0){
                return symbolAssignment;
            }
        }
        return null;
    }

    // takes a Literal and returns true for a Polarity of POSITIVE and false for a Polarity of NEGATIVE
    private boolean getBool(Literal l) {
        return (l.getPolarity() == Literal.Polarity.POSITIVE) ? true : false;
    }

    // Add all literals, except those in a clause known to be true,
    // or those that are already assigned in the model
    // used a HashMap<String,Literal> so that we could return the Symbol assignment at the end
    private SymbolAssignment findPureSymbol(Set<Clause> clauses, MyModel model, Stack<Symbol> symbols) {
        HashMap<String,Literal> map = new HashMap<String,Literal>();
        for (Clause clause : clauses){
            if (!clause.knownToBeTrue(model)) { // skip the clause if it's known to be true
                for (Literal l : clause) {
                    if (!model.containsSymbol(l.getContent())) {    // only include literals not assigned in model
                        if (l.getPolarity() == Literal.Polarity.NEGATIVE) {
                            map.put("n" + l.getContent().toString(), l);
                        } else {
                            map.put(l.getContent().toString(), l);
                        }
                    }
                }
            }
        }

        // if there is any symbol in the clauses that only has a positive or negative polarity, and not both, return that symbol
        for (Symbol sym : symbols){ // symbols contains the symbols not yet assigned in the model
            if (map.containsKey(sym.toString()) && !map.containsKey("n" + sym.toString())) {
                Literal l = map.get(sym.toString());
                return new SymbolAssignment(l.getContent(),getBool(l));
            }
            else if (!map.containsKey(sym.toString()) && map.containsKey("n" + sym.toString())){
                Literal l = map.get("n" + sym.toString());
                return new SymbolAssignment(l.getContent(),getBool(l));
            }
        }
        return null;
    }

    // takes a collection of sentences and returns a set of clauses from those sentences
    private Set<Clause> convertToClauses(Collection<Sentence> sentences) {
        Set<Clause> clauses = new HashSet<Clause>();
        for (Sentence s : sentences) {
            clauses.addAll(CNFConverter.convert(s));
        }
        return clauses;
    }

}
