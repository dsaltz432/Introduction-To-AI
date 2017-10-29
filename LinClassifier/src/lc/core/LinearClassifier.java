package lc.core;

import math.util.VectorOps;

import java.util.List;
import java.util.Random;

abstract public class LinearClassifier {
	
	protected double[] weights;
	
	public LinearClassifier(double[] weights) {
		this.weights = weights;
	}
	
	public LinearClassifier(int ninputs) {
		this.weights = new double[ninputs];
	}
	
	/**
	 * Update the weights of this LinearClassifer using the given
	 * inputs/output example and learning rate alpha.
	 */
	abstract public void update(double[] x, double y, double alpha);

	/**
	 * Threshold the given value using this LinearClassifier's
	 * threshold function.
	 */
	abstract public double threshold(double z);

	/**
	 * Evaluate the given input vector using this LinearClassifier
	 * and return the output value.
	 * This value is: Threshold(w \cdot x)
	 */
	public double eval(double[] x) {
		return threshold(VectorOps.dot(this.weights, x));
	}
	
	/**
	 * Train this LinearClassifier on the given Examples for the
	 * given number of steps, using given learning rate schedule.
	 * ``Typically the learning rule is applied one example at a time,
	 * choosing examples at random (as in stochastic gradient descent).''
	 * See AIMA p. 724.
	 */
	public void train(List<Example> examples, int nsteps, LearningRateSchedule schedule) {
		Random random = new Random();
		int n = examples.size();
		for (int i=1; i <= nsteps; i++) {
			int j = random.nextInt(n);
			Example ex = examples.get(j);
			double alpha = schedule.alpha(i);
			this.update(ex.inputs, ex.output, schedule.alpha(i));
			this.trainingReport(examples, i,  nsteps);
		}
	}

	public void trainLogistic(List<Example> examples, int nsteps, LearningRateSchedule schedule) {
		Random random = new Random();
		int n = examples.size();
		for (int i=1; i <= nsteps; i++) {
			int j = random.nextInt(n);
			Example ex = examples.get(j);
			double alpha = schedule.alpha(i);
			this.update(ex.inputs, ex.output, schedule.alpha(i));
			this.trainingReportLogistic(examples, i,  nsteps);
		}
	}

	/**
	 * Train this LinearClassifier on the given Examples for the
	 * given number of steps, using given constant learning rate.
	 */
	public void train(List<Example> examples, int nsteps, double constant_alpha) {
		train(examples, nsteps, new LearningRateSchedule() {
			public double alpha(int t) { return constant_alpha; }
		});
	}

	public void trainLogistic(List<Example> examples, int nsteps, double constant_alpha) {
		trainLogistic(examples, nsteps, new LearningRateSchedule() {
			public double alpha(int t) { return constant_alpha; }
		});
	}


	/**
	 * This method is called after each weight update during training.
	 * Subclasses can override it to gather statistics or update displays.
	 */
	protected void trainingReport(List<Example> examples, int stepnum, int nsteps) {
		System.out.println(stepnum + "\t" + accuracy(examples));
	}

	protected void trainingReportLogistic(List<Example> examples, int stepnum, int nsteps) {
		System.out.println(stepnum + "\t" + logisticAccuracy(examples));
	}

	/**
	 * Return the proportion of the given Examples that are classified
	 * correctly by this LinearClassifier.
	 * This is probably only meaningful for classifiers that use
	 * a hard threshold. Use with care.
	 */
	public double accuracy(List<Example> examples) {
		int ncorrect = 0;
		for (Example ex : examples) {
			double result = eval(ex.inputs);
			if (result == ex.output) {
				ncorrect += 1;
			}
		}
		return (double)ncorrect / examples.size();
	}

	public double logisticAccuracy(List<Example> examples) {
		int ncorrect = 0;
		for (Example ex : examples) {
			double result = eval(ex.inputs);
			if ((ex.output == 0 && result <= .5)
			|| (ex.output == 1 && result > .5)){
				ncorrect += 1;
			}
		}
		return (double)ncorrect / examples.size();
	}

}
