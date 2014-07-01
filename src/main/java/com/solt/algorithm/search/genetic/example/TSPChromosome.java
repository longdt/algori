package com.solt.algorithm.search.genetic.example;

import com.solt.algorithm.search.genetic.Chromosome;

public class TSPChromosome extends Chromosome<Integer, TSPGeneticAlgorithm>{
	private City[] cities;
	
	public TSPChromosome(TSPGeneticAlgorithm ga, City[] cities) {
		geneticAlgorithm = ga;
		this.cities = cities;
		//init random gene
		genes = new Integer[cities.length];
		boolean[] taken = new boolean[cities.length];
		int candidate;
		for (int i = 0; i < genes.length; ++i) {
			do {
				candidate = (int) (Math.random() * cities.length);
			} while (taken[candidate]);
			genes[i] = candidate;
			taken[candidate] = true;
			//tunning find last city
			if (i == cities.length - 2) {
				candidate = 0;
				while (taken[candidate]) {
					++candidate;
				}
				genes[i + 1] = candidate;
			}
		}
		calculateCost();
	}
	@Override
	public void calculateCost() {
		double cost = 0;
		for (int i = 0; i < cities.length - 1; ++i) {
			double dist = cities[getGene(i)].proximity(cities[getGene(i + i)]);
			cost += dist;
		}
		super.cost = cost;
	}

	@Override
	public void mutate() {
		int length = size();
		int iswap1 = (int) (Math.random() * length );
		int iswap2 = (int) (Math.random() * length );
		Integer temp  = getGene(iswap1);
		setGene(iswap1, getGene(iswap2));
		setGene(iswap2, temp);
	}

}
