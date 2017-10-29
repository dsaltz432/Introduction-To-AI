package lc.example;

import lc.core.LinearClassifier;
import math.util.VectorOps;


public class PerceptronClassifier extends LinearClassifier {
    public PerceptronClassifier(double[] weights) {
        super(weights);
    }

    @Override
    public void update(double[] x, double y, double alpha) {
        for (int i = 0; i < weights.length; i++) { // for each attribute in the linear model

            double currentPrediction = threshold(VectorOps.dot(weights,x));
            double error = y-currentPrediction;
            weights[i] += alpha * error * x[i];

        }
    }

    @Override
    public double threshold(double z) {
        if (z >= 0){
            return 1;
        }
        return 0;
    }
}
