package com.solt.algorithm.neuralnet.feedback.train.genetic;

import com.solt.algorithm.neuralnet.feedback.FeedforwardNetwork;
import com.solt.algorithm.search.genetic.GeneticAlgorithm;

public class NeuralGeneticAlgorithm<GA_TYPE extends GeneticAlgorithm<?>> extends GeneticAlgorithm<NeuralChromosome<GA_TYPE>> {
	
	public FeedforwardNetwork getNetwork() {
		NeuralChromosome<GA_TYPE> c = getChromosome(0);
		c.updateNetwork();
		return c.getNetwork();
	}
	
}
