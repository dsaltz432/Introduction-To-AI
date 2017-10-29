package pl.main;

import pl.dpll.DPLL;
import pl.core.Negation;
import pl.core.Sentence;
import pl.core.Symbol;
import pl.examples.*;

import java.util.ArrayList;
import java.util.List;

public class TestDPLL {

    public static void main(String... args){

        DPLL test = new DPLL();
        List<String> failures = new ArrayList<String>();

        failures = testWumpusWorld(test,failures);
        failures = testModusPonens(test,failures);
        failures = testHornClauses(test,failures);
        failures = testTruthTellers(test,failures);
        failures = testAdvancedTruthTellers(test,failures);

        printFailures(failures);
    }


    private static List<String> testModusPonens(DPLL test, List<String> failures) {

        // inferQ
        ModusPonensKB modusPonensKB = new ModusPonensKB();
        Sentence q = new Symbol("Q");
        if (test.satisfiable(modusPonensKB,q)){ // we expect it to be NOT unsatisfiable
            failures.add("Test failed: inferQ");
        }

        // inferNotQ
        modusPonensKB = new ModusPonensKB();
        Sentence notQ = new Negation(new Symbol("Q"));
        if (!test.satisfiable(modusPonensKB,notQ)){ // we expect it to be YES satisfiable
            failures.add("Test failed: inferNotQ");
        }
        return failures;
    }

    private static List<String> testWumpusWorld(DPLL test, List<String> failures) {

        // canInferNotP11
        WumpusWorldKB wumpusWorldKB = new WumpusWorldKB();
        Sentence notP11 = new Negation(new Symbol("P1,1"));
        if (test.satisfiable(wumpusWorldKB,notP11)){ // we expect it to be NOT satisfiable
            failures.add("Test failed: canInferNotP11");
        }

        // canInferNotP12
        wumpusWorldKB = new WumpusWorldKB();
        Sentence p12 = new Negation(new Symbol("P1,2"));
        if (test.satisfiable(wumpusWorldKB,p12)){ // we expect it to be NOT satisfiable --> (we know p12=False)
            failures.add("Test failed: canInferNotP12");
        }

        // cantInferP12
        wumpusWorldKB = new WumpusWorldKB();
        Sentence notP12 = new Symbol("P1,2");
        if (!test.satisfiable(wumpusWorldKB,notP12)){ // NOT satisfiable, we can't infer it
            failures.add("Test failed: cantInferP12");
        }

        // cantInferP31
        // Given just our current KB, we don't know if the pit is in 2,2 or 3,1
        wumpusWorldKB = new WumpusWorldKB();

        // we should not be able to infer the P31=True
        Sentence p31 = new Symbol("P3,1");
        if (!test.satisfiable(wumpusWorldKB,p31)){ // we expect it to be YES satisfiable --> (can't entail Beta)
            failures.add("Test failed: cantInferP31");
        }

        // givenNotP22InferP31
        // generate new sentence in KB saying that P22=False
        Sentence notP22 = new Negation(new Symbol("P2,2"));
        wumpusWorldKB = new WumpusWorldKB();
        wumpusWorldKB.add(notP22);

        // Now, we are able to use this added information to infer that P31 is true
        p31 = new Symbol("P3,1");
        if (test.satisfiable(wumpusWorldKB,p31)){ // we expect it to be NOT satisfiable --> (Yes infer that P3,1=TRUE)
            failures.add("Test failed: givenNotP22InferP31");
        }
        return failures;
    }

    private static List<String> testTruthTellers(DPLL test, List<String> failures){

        //Part A
        //infer Amy is false
        TruthTellersKB truthTellersKB = new TruthTellersKB();
        Sentence amy = new Negation(new Symbol("amy"));
        if (test.satisfiable(truthTellersKB,amy)){
            failures.add("Test failed: interAmyFalse");
        }
        //Bob is false
        truthTellersKB = new TruthTellersKB();
        Sentence bob = new Negation(new Symbol("bob"));
        if (test.satisfiable(truthTellersKB,bob)){
            failures.add("Test failed: interBobFalse");
        }
        //Cal is true
        truthTellersKB = new TruthTellersKB();
        Sentence cal = new Symbol("cal");
        if (test.satisfiable(truthTellersKB,cal)){
            failures.add("Test failed: inferCalTrue");
        }
        //----------------------------------------------------------
        //Part B

        //infer Amy is False
        truthTellersKB = new TruthTellersKB();
        Sentence amy2 = new Negation(new Symbol("amy2"));
        if (!test.satisfiable(truthTellersKB,amy2)){
            failures.add("Test failed: interAmy2False");
        }
        //infer bob2 is False
        truthTellersKB = new TruthTellersKB();
        Sentence bob2 = new Negation(new Symbol("bob2"));
        if (test.satisfiable(truthTellersKB,bob2)){
            failures.add("Test failed: inferBob2False");
        }
        //infer Cal2 is True
        truthTellersKB = new TruthTellersKB();
        bob2 = new Symbol("cal2");
        if (!test.satisfiable(truthTellersKB,bob2)){
            failures.add("Test failed: inferCal2True");
        }
        return failures;
    }

    private static List<String> testAdvancedTruthTellers(DPLL test, List<String> failures){
        AdvancedTruthTellersKB Advtruthtellers = new AdvancedTruthTellersKB();

        //infer Amy False
        Sentence amy = new Negation(new Symbol("amy"));
        if (!test.satisfiable(Advtruthtellers,amy)){
            failures.add("Test failed: inferAmyFalse");
        }
        //Bob is false
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence bob = new Negation(new Symbol("bob"));
        if(!test.satisfiable(Advtruthtellers,bob)){
            failures.add("Test failed: interBobFalse");
        }
        // Cal is false
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence cal = new Negation(new Symbol("cal"));
        if(!test.satisfiable(Advtruthtellers,cal)){
            failures.add("Test failed: interCalFalse");
        }
        // Dee is false
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence dee = new Negation(new Symbol("dee"));
        if(!test.satisfiable(Advtruthtellers,dee)){
            failures.add("Test failed: interDeeFalse");
        }
        // Eli is false
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence eli = new Negation(new Symbol("eli"));
        if(!test.satisfiable(Advtruthtellers,eli)){
            failures.add("Test failed: interEliFalse");
        }
        // Fay is false
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence fay = new Negation(new Symbol("fay"));
        if(!test.satisfiable(Advtruthtellers,fay)){
            failures.add("Test failed: interFayFalse");
        }
        //infer Gil False
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence gil = new Negation(new Symbol("gil"));
        if (!test.satisfiable(Advtruthtellers,gil)){
            failures.add("Test failed: inferGilFalse");
        }
        //infer Hal False
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence hal = new Negation(new Symbol("hal"));
        if (!test.satisfiable(Advtruthtellers,hal)){
            failures.add("Test failed: inferHalFalse");
        }
        //infer Ida False
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence ida = new Negation(new Symbol("ida"));
        if (!test.satisfiable(Advtruthtellers,ida)){
            failures.add("Test failed: inferIdaFalse");
        }
        //infer Jay False
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence jay = new Negation(new Symbol("jay"));
        if (!test.satisfiable(Advtruthtellers,jay)){
            failures.add("Test failed: inferJayFalse");
        }
        //infer Kay False
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence kay = new Negation(new Symbol("kay"));
        if (!test.satisfiable(Advtruthtellers,kay)){
            failures.add("Test failed: inferKayFalse");
        }
        //infer Lee False
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence lee = new Negation(new Symbol("lee"));
        if (!test.satisfiable(Advtruthtellers,lee)){
            failures.add("Test failed: inferLeeFalse");
        }
        return failures;
    }

    private static List<String> testHornClauses(DPLL test, List<String> failures) {
        // tests question a
        //inferMythicalFalse
        HornClausesKB hornClausesKB = new HornClausesKB();
        Sentence myth = new Symbol("myth");
        if (!test.satisfiable(hornClausesKB,myth)){
            failures.add("Test failed: nferMythicalFalse");
        }

        // tests question b
        //inferMagicalTrue
        hornClausesKB = new HornClausesKB();
        Sentence mag = new Symbol("mag");
        if (test.satisfiable(hornClausesKB,mag)){
            failures.add("Test failed: inferMagicalTrue");
        }

        // tests question c
        //inferHornedTrue
        hornClausesKB = new HornClausesKB();
        Sentence h = new Symbol("h");
        if (test.satisfiable(hornClausesKB,h)){
            failures.add("Test failed: inferHornedTrue");
        }
        return failures;
    }

    private static void printFailures(List<String> failures) {
        System.out.println();
        if (failures.isEmpty()){
            System.out.println("All tests passed!");
        } else {
            for (String s : failures){
                System.out.println(s);
            }
        }
    }

}