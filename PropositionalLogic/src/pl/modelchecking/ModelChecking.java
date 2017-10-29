package pl.modelchecking;

import pl.core.KB;
import pl.core.Sentence;
import pl.core.Symbol;

import java.util.ArrayList;
import java.util.List;

public class ModelChecking {

    // beta is entailed by alpha unless there's a model that is satisfied by the KB but not satisfied by beta
    public boolean entails(KB kb, Sentence beta) {
        MyModel[] models = generateModels(kb);
        for (MyModel m : models){
            if (m.satisfies(kb) && !m.satisfies(beta)){
                return false;
            }
        }
        return true;
    }

    /*
-Generates all 2^n models
-Accomplishes this by taking advantage of how binary numbers work
-takes the binary number from 0 to 2^n, converts it to a string, and then has
    each character represent a symbol.
-The symbols must be ordered in the same way that each new model runs, I used an ArrayList to maintain the order
*/
    private MyModel[] generateModels(KB kb){
        List<Symbol> symbols = new ArrayList<Symbol>();
        symbols.addAll(kb.symbols());
        int n = symbols.size();
        int numModels = (int)Math.pow(2,n);
        MyModel[] models = new MyModel[numModels];

        for (int i = 0; i < numModels; i++) { // 2^n total models
            String binaryString = Integer.toBinaryString(i);
            while (binaryString.length() < n){
                binaryString = "0" + binaryString;
            }
            MyModel m = new MyModel();
            for (int j = 0; j < binaryString.length(); j++){
                boolean b = binaryString.charAt(j) == '0' ? false : true;
                m.set(symbols.get(j),b);
            }
            models[i] = m;
        }
        return models;
    }

}
