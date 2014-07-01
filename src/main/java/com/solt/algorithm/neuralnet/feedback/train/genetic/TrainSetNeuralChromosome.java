package com.solt.algorithm.neuralnet.feedback.train.genetic;

import com.solt.algorithm.neuralnet.feedback.FeedforwardNetwork;

public class TrainSetNeuralChromosome extends
		NeuralChromosome<TrainningSetNeuralGeneticAlgorithm> {
	public TrainSetNeuralChromosome(
			TrainningSetNeuralGeneticAlgorithm ga,
			FeedforwardNetwork network) {
		setNetwork(network);
		setGeneticAlgorithm(ga);
		initGenes(network.getWeightMatrixSize());
		updateGenes();
	}

	@Override
	public void calculateCost() {
		updateNetwork();
		double[][] input = geneticAlgorithm.getInput();
		double[][] ideal = geneticAlgorithm.getIdeal();
		cost = getNetwork().calculateError(input, ideal);
	}
}
