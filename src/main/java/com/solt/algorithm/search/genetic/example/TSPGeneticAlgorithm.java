package com.solt.algorithm.search.genetic.example;

import com.solt.algorithm.search.genetic.GeneticAlgorithm;

public class TSPGeneticAlgorithm extends GeneticAlgorithm {

	public TSPGeneticAlgorithm(City[] cities, int populationSize,
			double mutationPercent, double percentToMate, double matingPopulationPercent, int cutLength) {
		this.populationSize = populationSize;
		this.mutationPercent = mutationPercent;
		this.percentToMate = percentToMate;
		this.matingPopulation = matingPopulationPercent;
		this.cutLength = cutLength;
		preventRepeat = true;
		setChromosomes(new TSPChromosome[populationSize]);
		for (int i = 0; i < populationSize; ++i) {
			TSPChromosome c = new TSPChromosome(this, cities);
			setChromosome(i, c);
		}
		sortChromosomes();
	}

}
