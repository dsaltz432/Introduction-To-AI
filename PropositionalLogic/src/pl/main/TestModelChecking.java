package pl.main;

import pl.modelchecking.ModelChecking;
import pl.core.Negation;
import pl.core.Sentence;
import pl.core.Symbol;
import pl.examples.HornClausesKB;
import pl.examples.ModusPonensKB;
import pl.examples.WumpusWorldKB;
import pl.examples.TruthTellersKB;
import pl.examples.AdvancedTruthTellersKB;

import java.util.ArrayList;
import java.util.List;

public class TestModelChecking {

    public static void main(String...args){

        ModelChecking test = new ModelChecking();
        List<String> failures = new ArrayList<String>();

        failures = testWumpusWorld(test,failures);
        failures = testModusPonens(test,failures);
        failures = testHornClauses(test,failures);
        failures = testTruthTellers(test,failures);
        failures = testAdvancedTruthTellers(test,failures);

        printFailures(failures);
    }

    private static List<String> testModusPonens(ModelChecking test, List<String> failures) {
//        inferQTrue
        ModusPonensKB modusPonensKB = new ModusPonensKB();
        Sentence q = new Symbol("Q");
        if (!test.entails(modusPonensKB,q)){
            failures.add("Test failed: inferQTrue");
        }

//          inferNotQFalse
        modusPonensKB = new ModusPonensKB();
        Sentence notQ = new Negation(new Symbol("Q"));
        if (test.entails(modusPonensKB,notQ)){
            failures.add("Test failed: inferNotQFalse");
        }
        return failures;
    }

    private static List<String> testWumpusWorld(ModelChecking test, List<String> failures) {
//        inferThatNotP11True
        WumpusWorldKB wumpusWorldKB = new WumpusWorldKB();
        Sentence p11 = new Negation(new Symbol("P1,1"));
        if (!test.entails(wumpusWorldKB,p11)){
            failures.add("Test failed: inferThatNotP11True");
        }

//       inferThatP12False
        wumpusWorldKB = new WumpusWorldKB();
        Sentence p12 = new Symbol("P1,2");
        if (test.entails(wumpusWorldKB,p12)){
            failures.add("Test failed: inferThatP12False");
        }

//        inferThatNotP12True
        wumpusWorldKB = new WumpusWorldKB();
        Sentence notP12 = new Negation(new Symbol("P1,2"));
        if (!test.entails(wumpusWorldKB,notP12)){
            failures.add("Test failed: inferThatNotP12True");
        }

//        cantInferP31
        // Given just our current KB, we don't know if the pit is in 2,2 or 3,1
        wumpusWorldKB = new WumpusWorldKB();

        // we should not be able to infer the P31=True
        Sentence p31 = new Symbol("P3,1");
        if (test.entails(wumpusWorldKB,p31)){
            failures.add("Test failed: cantInferP31");
        }

//        givenNotP22InferP31
        // generate new sentence in KB saying that P22=False
        Sentence notP22 = new Negation(new Symbol("P2,2"));
        wumpusWorldKB = new WumpusWorldKB();
        wumpusWorldKB.add(notP22);

        // Now, we are able to use this added information to infer that P31 is true
        p31 = new Symbol("P3,1");
        if (!test.entails(wumpusWorldKB,p31)){
            failures.add("Test failed: givenNotP22InferP31");
        }
        return failures;
    }

    private static List<String> testTruthTellers(ModelChecking test, List<String> failures){
        TruthTellersKB truthTellersKB = new TruthTellersKB();

        //part A

        //infer Amy is false
        Sentence amy = new Symbol("amy");
        if (test.entails(truthTellersKB,amy)){
            failures.add("Test failed: inferAmyFalse");
        }

        //Bob is false
        truthTellersKB = new TruthTellersKB();
        Sentence bob = new Symbol("bob");
        if (test.entails(truthTellersKB,bob)){
            failures.add("Test failed: inferBobFalse");
        }
        //Cal is true.
        truthTellersKB = new TruthTellersKB();
        Sentence cal = new Symbol("cal");
        if(test.entails(truthTellersKB,cal)){
            failures.add("Test failed: inferCalTrue");
        }

        //----------------------------------------------------------
        //Part B

        //infer Amy2 is False
        truthTellersKB = new TruthTellersKB();
        Symbol amy2 = new Symbol("amy2");
        if (test.entails(truthTellersKB,amy2)){
            failures.add("Test failed: inferAmy2False");
        }
        //infer bob2 is False
        truthTellersKB = new TruthTellersKB();
        Symbol bob2 = new Symbol("bob2");
        if (test.entails(truthTellersKB,bob2)){
            failures.add("Test failed: inferBob2False");
        }
        //infer Cal2 is True
        truthTellersKB = new TruthTellersKB();
        bob2 = new Symbol("cal2");
        if (test.entails(truthTellersKB,bob2)){
            failures.add("Test failed: inferCal2True");
        }
        return failures;
    }

    private static List<String> testAdvancedTruthTellers(ModelChecking test, List<String> failures){
        AdvancedTruthTellersKB Advtruthtellers = new AdvancedTruthTellersKB();

        //infer Amy False
        Sentence amy = new Symbol("amy");
        if (test.entails(Advtruthtellers,amy)){
            failures.add("Test failed: inferAmyFalse");
        }
        //infer Bob is False
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence bob = new Symbol("bob");
        if (test.entails(Advtruthtellers,bob)){
            failures.add("Test failed: inferBobFalse");
        }
        //infer Cal is False
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence cal = new Symbol("cal");
        if (test.entails(Advtruthtellers,cal)){
            failures.add("Test failed: inferCalFalse");
        }

        //infer dee is False
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence dee = new Symbol("dee");
        if (test.entails(Advtruthtellers,dee)){
            failures.add("Test failed: inferDeeFalse");
        }

        //infer eli is false
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence eli = new Symbol("eli");
        if (test.entails(Advtruthtellers,eli)){
            failures.add("Test failed: inferEliFalse");
        }
        //infer fay is false
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence fay = new Symbol("fay");
        if (test.entails(Advtruthtellers,fay)){
            failures.add("Test failed: inferFayFalse");
        }

        //infer not Gil
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence gil = new Symbol("gil");
        if (test.entails(Advtruthtellers,gil)){
            failures.add("Test failed: inferGilFalse");
        }

        //infer not Hal
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence hal = new Symbol("hal");
        if (test.entails(Advtruthtellers,hal)){
            failures.add("Test failed: inferHalFalse");
        }

        //infer not Ida
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence ida = new Symbol("ida");
        if (test.entails(Advtruthtellers,ida)){
            failures.add("Test failed: inferIdaFalse");
        }
        //infer Jay False
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence jay = new Negation(new Symbol("jay"));
        if (test.entails(Advtruthtellers,jay)){
            failures.add("Test failed: inferJayTrue");
        }
        //infer Kay False
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence kay = new Negation(new Symbol("kay"));
        if (test.entails(Advtruthtellers,kay)){
            failures.add("Test failed: inferKayTrue");
        }
        //infer Lee False
        Advtruthtellers = new AdvancedTruthTellersKB();
        Sentence lee = new Symbol("lee");
        if (test.entails(Advtruthtellers,lee)){
            failures.add("Test failed: inferleeFalse");
        }
        return failures;
    }

    private static List<String> testHornClauses(ModelChecking test, List<String> failures) {
        // tests question a
        //inferMythicalFalse
        HornClausesKB hornClausesKB = new HornClausesKB();
        Sentence myth = new Symbol("myth");
        if (test.entails(hornClausesKB,myth)){
            failures.add("Test failed: inferMythicalFalse");
        }

        // tests question b
        //inferMagicalTrue
        hornClausesKB = new HornClausesKB();
        Sentence mag = new Symbol("mag");
        if (!test.entails(hornClausesKB,mag)){
            failures.add("Test failed: inferMagicalTrue");
        }

        // tests question c
        //inferHornedTrue
        hornClausesKB = new HornClausesKB();
        Sentence h = new Symbol("h");
        if (!test.entails(hornClausesKB,h)){
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
