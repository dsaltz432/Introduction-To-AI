package lc.example;

import lc.core.Example;
import lc.core.LearningRateSchedule;
import lc.display.ClassifierDisplay;

import java.io.IOException;
import java.util.List;

/**
 * Created by danielsaltz on 4/27/17.
 */
public class LogisticClassifierTest {
    public static void main(String[] argv) throws IOException {
        if (argv.length < 3) {
            System.out.println("usage: java LogisticClassifierTest data-filename nsteps alpha");
            System.out.println("       specify alpha=0 to use decaying learning rate schedule (AIMA p725)");
            System.exit(-1);
        }
        String filename = "src/lc/example/"+argv[0]; // specific to intellij project structure
        int nsteps = Integer.parseInt(argv[1]);
        double alpha = Double.parseDouble(argv[2]);
        System.out.println("filename: " + filename);
        System.out.println("nsteps: " + nsteps);
        System.out.println("alpha: " + alpha);

        ClassifierDisplay display = new ClassifierDisplay("LogisticClassifierTest: " + filename);
        List<Example> examples;
        if (filename.contains("iris")){
            examples = Data.readIrisDataset(filename);
        } else if (filename.contains("earthquake")){
            examples = Data.readBooleanDataset(filename);
        } else {
            examples = Data.readBooleanDataset(filename);
        }

        // initialize weights
        double[] weights = new double[examples.get(0).inputs.length];
        LogisticClassifier classifier = new LogisticClassifier(weights) {
            public void trainingReportLogistic(List<Example> examples, int stepnum, int nsteps) {
                double accuracy = logisticAccuracy(examples);
                System.out.println(stepnum + "\t" + accuracy);
                display.addPoint(stepnum/(double)nsteps, accuracy);
            }
        };
        if (alpha > 0) {
            classifier.trainLogistic(examples, nsteps, alpha);
        } else {
            classifier.trainLogistic(examples, 100000, new LearningRateSchedule() {
                public double alpha(int t) { return 1000.0/(1000.0+t); }
            });
        }
    }

}
