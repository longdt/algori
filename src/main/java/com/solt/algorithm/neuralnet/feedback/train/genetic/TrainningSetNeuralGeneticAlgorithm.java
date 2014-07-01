package com.solt.algorithm.neuralnet.feedback.train.genetic;

import com.solt.algorithm.neuralnet.feedback.FeedforwardNetwork;
import com.solt.algorithm.neuralnet.feedback.train.Train;

public class TrainningSetNeuralGeneticAlgorithm extends NeuralGeneticAlgorithm<TrainningSetNeuralGeneticAlgorithm> implements Train {
	private double[][] input;
	private double[][] ideal;

	public TrainningSetNeuralGeneticAlgorithm(FeedforwardNetwork network,
			boolean reset, double[][] input, double[][] ideal, int populationSize,
			double mutationPercent, double percentToMate) {
		setPopulationSize(populationSize);
		setMutationPercent(mutationPercent);
		setPercentToMate(percentToMate);
		setMatingPopulation(percentToMate * 2);
		
		this.input = input;
		this.ideal = ideal;
		
		setChromosomes(new TrainSetNeuralChromosome[populationSize]);
		for (int i = 0; i < populationSize; ++i) {
			FeedforwardNetwork chromosomeNet = (FeedforwardNetwork) network.clone();
			if (reset) {
				chromosomeNet.reset();
			}
			
			TrainSetNeuralChromosome c = new TrainSetNeuralChromosome(this, chromosomeNet);
			c.updateGenes();
			setChromosome(i, c);
		}
		sortChromosomes();
		
	}

	@Override
	public double getError() {
		FeedforwardNetwork network = getNetwork();
		return network.calculateError(input, ideal);
	}
	
	public double[][] getIdeal() {
		return ideal;
	}
	
	public double[][] getInput() {
		return input;
	}

}
