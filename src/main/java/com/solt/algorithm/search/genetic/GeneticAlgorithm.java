package com.solt.algorithm.search.genetic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public abstract class GeneticAlgorithm<CHROMOSOME_TYPE extends Chromosome<?, ?>> {
	protected int populationSize;
	protected int cutLength;
	protected boolean preventRepeat;
	protected double mutationPercent;
	protected double percentToMate;
	private double matingPopulation;
	private ExecutorService pool;
	private CHROMOSOME_TYPE[] chromosomes;
	
	public void iteration() {
		int countToMate = (int) (populationSize * percentToMate);
		int offspringCount = countToMate * 2;
		int offspringIndex = populationSize - offspringCount;
		int matingPopulationSize = (int) (populationSize * matingPopulation);
		Collection<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
		for (int i = 0; i < countToMate; ++i) {
			CHROMOSOME_TYPE mother = chromosomes[i];
			int fatherIdx = (int) (Math.random() * matingPopulationSize);
			CHROMOSOME_TYPE father = chromosomes[fatherIdx];
			CHROMOSOME_TYPE child1 = chromosomes[offspringIndex];
			CHROMOSOME_TYPE child2 = chromosomes[offspringIndex + 1];
			MateWorker<CHROMOSOME_TYPE> worker = new MateWorker<CHROMOSOME_TYPE>(mother, father, child1, child2);
		}
	}
	
	public CHROMOSOME_TYPE getChromosome(int i) {
		return chromosomes[i];
	}

	public int getCutLength() {
		return cutLength;
	}
	
	public double getMatingPopulation() {
		return matingPopulation;
	}
	
	public boolean isPreventRepeat() {
		return preventRepeat;
	}
	
	public double getMutationPercent() {
		return mutationPercent;
	}
	
	public double getPercentToMate() {
		return percentToMate;
	}
	
	public ExecutorService getPool() {
		return pool;
	}
	
	public int getPopulationSize() {
		return populationSize;
	}

}
