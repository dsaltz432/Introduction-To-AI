package pl.examples;

import pl.core.*;

/**
 * Created by danielsaltz on 3/1/17.
 */
public class HornClausesKB extends KB {

    public HornClausesKB() {
        super();

//      Rules for horn Clauses
//      Myth-> Mythical
//      imm-> Immortal
//      mam-> Mammal
//      h-> Horned
//      mag-> Magical
        Symbol myth = intern("myth");
        Symbol imm = intern("imm");
        Symbol mam = intern("mam");
        Symbol h = intern("h");
        Symbol mag = intern("mag");
        add(new Implication(myth, imm));
        add(new Implication(new Negation(myth), new Conjunction(new Negation(imm), mam)));
        add(new Implication(new Disjunction(imm,mam),h));
        add(new Implication(h,mag));
    }

    public static void main(String[] argv) {
        new HornClausesKB().dump();
    }
}