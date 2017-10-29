package lc.example;

import lc.core.LinearClassifier;
import math.util.VectorOps;

/**
 * Created by danielsaltz on 4/27/17.
 */
public class LogisticClassifier extends LinearClassifier{
    public LogisticClassifier(double[] weights) {
        super(weights);
    }

    @Override
    public void update(double[] x, double y, double alpha) {
        for (int i = 0; i < weights.length; i++) { // for each attribute in the linear model

            double currentPrediction = threshold(VectorOps.dot(weights,x));
            double error = y-currentPrediction;
            weights[i] += alpha * error * currentPrediction * (1-currentPrediction) * x[i];

        }
    }

    @Override
    public double threshold(double z) {
        return 1/(1+Math.exp(-z));
    }
}
