package bn.inference;

import bn.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class ApproxInferencer extends Inferencer {
    private int limit;
    private int validCount;

    public ApproxInferencer(int limit) {
        this.limit = limit;
        this.validCount = 0;
    }

    /**
     * Parses the command line arguments to run a test
     */
    public static void main(String[] args){

        ensureEnoughArgs(args);

        // read command line arguments
        int limit = Integer.parseInt(args[0]);
        String testFile = args[1];
        String queryVarName =  args[2];

        // get BayesianNetwork from file
        BayesianNetwork bn = getBayesianNetworkFromFile(testFile);

        // create boolean domain
        Domain booleanDomain = new Domain();
        booleanDomain.add("true");
        booleanDomain.add("false");

        // get evidence and query variable
        Assignment e = getEvidenceFromArgs(args,booleanDomain,3);
        RandomVariable X = new RandomVariable(queryVarName,booleanDomain);

        // run algorithm
        ApproxInferencer approxInference = new ApproxInferencer(limit);
        Distribution result = approxInference.ask(bn,X,e);

        printResults(result);
    }

    /**
     * Checks to make sure there are a valid number of arguments.
     * Exits the program if an illogical number of arguments is found.
     */
    protected static void ensureEnoughArgs(String[] args){
        if (args.length < 3 || args.length % 2 == 0){
            System.err.println("You did not enter the correct number of command line arguments.");
            System.err.println("Please execute this program in the following format: " +
                    "java bn.inference.TestExactInference <samples> <example.xml> <Query variable> <Evidence variable> <evidence value>...");
            System.exit(0);
        }
    }

    @Override
    public Distribution ask(BayesianNetwork bn, RandomVariable X, Assignment e) {
        Random random = new Random();
        List<RandomVariable> vars = bn.getVariableListTopologicallySorted();
        Map<String,Integer> counts = getSampleCounts(random,vars,e,bn);
        Distribution dist = getDistributionOfQueryVar(X,counts);
        dist.normalize();
        return dist;
    }

    /**
     * Returns a Map containing each RandomVariable's String value mapping
     * to the number of times that the variable was true given the evidence.
     */
    private Map<String,Integer> getSampleCounts(Random random, List<RandomVariable> vars, Assignment e, BayesianNetwork bn) {
        Map<String,Integer> counts = new HashMap<>(vars.size());
        for (int count = 0; count < limit; count++){
            Assignment assignment = shallowCopy(e);
            boolean valid = true;
            for (RandomVariable rv : vars){

                // set the rv to be true so that the variable's prior is used
                set(assignment,rv,true);

                double probability = bn.getProb(rv,assignment);
                Boolean result = getRandResult(probability,random);

                // set rv result to what actually was predicted
                set(assignment,rv,result);

                // reject contradicting samples
                if (contradictsEvidence(e,rv.getName(),result)){
                    valid = false;
                    break;
                }
            }
            // increment count if the sample is valid (not rejected)
            if (valid == true){
                validCount++;
                updateCounts(assignment,counts);
            }
        }
        return counts;
    }

    /**
     * Updates the counts Map so that variables that are true in the assignment
     * are incremented.
     */
    private void updateCounts(Assignment assignment, Map<String, Integer> counts) {
        for (Map.Entry<RandomVariable, Object> entry : assignment.entrySet()){
            boolean value = Boolean.parseBoolean(entry.getValue().toString());

            if (value == true){
                String name = entry.getKey().getName();
                int c = (counts.containsKey(name)) ? counts.get(name) : 0;
                counts.put(name,c+1);
            }
        }
    }

    /**
     * Assign the given variable to the given Boolean value. If the
     * variable already exists in the Assignment then overwrite it
     * with the new Boolean value.
     */
    private void set(Assignment assignment, RandomVariable var, Boolean value) {
        for (RandomVariable rv : assignment.variableSet()){
            if (rv.getName().equals(var.getName())){
                assignment.set(rv,value);
                return;
            }
        }

        // it's not in the assignment, so add a new entry
        assignment.set(var,value);
    }

    /**
     * Returns the Distribution associated with the query variable X, given the counts
     */
    private Distribution getDistributionOfQueryVar(RandomVariable X, Map<String, Integer> counts) {
        Distribution dist = new Distribution();
        double queryCount = (double)counts.get(X.getName());
        double T = queryCount/validCount;
        double F = 1-T;
        dist.put("true", T);
        dist.put("false", F);
        return dist;
    }

    /**
     * Checks to see if the given variable name and its corresponding
     * Boolean result contradict that variable's assignment in the evidence
     */
    private boolean contradictsEvidence(Assignment e, String name, Boolean result) {
        for (Map.Entry<RandomVariable, Object> entry : e.entrySet()){
            if (entry.getKey().getName().equals(name)){ // if the name is in the evidence
                if (result != Boolean.valueOf(entry.getValue().toString())){ // if it isn't equal to the evidence value
                    return true;
                }
            }
        }
        return false;
    }


}
