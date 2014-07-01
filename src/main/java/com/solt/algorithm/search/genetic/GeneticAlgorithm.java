package com.solt.algorithm.search.genetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public abstract class GeneticAlgorithm<CHROMOSOME_TYPE extends Chromosome<?, ?>> {
	private static final ChromosomeComparator comparator = new ChromosomeComparator();
	protected int populationSize;
	protected int cutLength;
	protected boolean preventRepeat;
	protected double mutationPercent;
	protected double percentToMate;
	protected double matingPopulation;
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
			try {
				if (pool != null) {
					tasks.add(worker);
				} else {
					worker.call();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			offspringIndex += 2;
		}
		if (pool != null) {
			try {
				pool.invokeAll(tasks);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		sortChromosomes();
	}
	
	public void sortChromosomes() {
		Arrays.sort(chromosomes, comparator);
	}

	public CHROMOSOME_TYPE getChromosome(int i) {
		return chromosomes[i];
	}
	
	public void setChromosome(int i, CHROMOSOME_TYPE chromosome) {
		chromosomes[i] = chromosome;
	}
	
	public void setChromosomes(CHROMOSOME_TYPE[] chromosomes) {
		this.chromosomes = chromosomes;
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
	
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public void setCutLength(int cutLength) {
		this.cutLength = cutLength;
	}

	public void setPreventRepeat(boolean preventRepeat) {
		this.preventRepeat = preventRepeat;
	}

	public void setMutationPercent(double mutationPercent) {
		this.mutationPercent = mutationPercent;
	}

	public void setPercentToMate(double percentToMate) {
		this.percentToMate = percentToMate;
	}

	public void setMatingPopulation(double matingPopulation) {
		this.matingPopulation = matingPopulation;
	}

	public void setPool(ExecutorService pool) {
		this.pool = pool;
	}

}

class ChromosomeComparator implements Comparator<Chromosome<?, ?>> {

	@Override
	public int compare(Chromosome<?, ?> o1, Chromosome<?, ?> o2) {
		if (o1.getCost() > o2.getCost()) {
			return 1;
		} else if (o1.getCost() < o2.getCost()) {
			return -1;
		}
		return 0;
	}
	
}
