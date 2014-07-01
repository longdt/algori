package com.solt.algorithm.neuralnet.feedback.train.genetic;

import java.util.Arrays;

import com.solt.algorithm.neuralnet.feedback.FeedforwardNetwork;
import com.solt.algorithm.neuralnet.util.MatrixCODEC;
import com.solt.algorithm.search.genetic.Chromosome;
import com.solt.algorithm.search.genetic.GeneticAlgorithm;

public abstract class NeuralChromosome<GA_TYPE extends GeneticAlgorithm<?>> extends Chromosome<Double, GA_TYPE>{
	private static final Double ZERO = Double.valueOf(0);
	private static final double RANGE = 20.0;
	
	private FeedforwardNetwork network;
	

	public FeedforwardNetwork getNetwork() {
		return network;
	}
	
	public void setNetwork(FeedforwardNetwork network) {
		this.network = network;
	}

	public void initGenes(int length) {
		Double[] result = new Double[length];
		Arrays.fill(result, ZERO);
		setGenesDirect(result);
	}

	@Override
	public void mutate() {
		int length = size();
		for (int i = 0; i < length; ++i) {
			double d = getGene(i);
			double ratio = (int) ((RANGE * Math.random()) - RANGE);
			d *= ratio;
			setGene(i, d);
		}
	}

	public void updateNetwork() {
		MatrixCODEC.arrayToNetwork(getGenes(), network);
	}
	
	public void updateGenes() {
		setGenes(MatrixCODEC.networkToArray(this.network));
	}

	public void setGenes(Double[] list) {
		super.setGenes(list);
		calculateCost();
	}


}
