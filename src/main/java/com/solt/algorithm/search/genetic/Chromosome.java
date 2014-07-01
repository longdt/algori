package com.solt.algorithm.search.genetic;

import java.util.HashSet;
import java.util.Set;

import com.solt.algorithm.neuralnet.NeuralNetworkError;

public abstract class Chromosome<GENE_TYPE, GA_TYPE extends GeneticAlgorithm> {
	protected GENE_TYPE[] genes;
	protected GA_TYPE geneticAlgorithm;
	protected double cost;

	public void mate(Chromosome<GENE_TYPE, GA_TYPE> father,
			Chromosome<GENE_TYPE, GA_TYPE> offspring1,
			Chromosome<GENE_TYPE, GA_TYPE> offspring2)
			throws NeuralNetworkError {
		int geneLength = getGenes().length;
		int cutPoint1 = (int) (Math.random() * (geneLength - getGeneticAlgorithm().getCutLength()));
		int cutPoint2 = cutPoint1 + getGeneticAlgorithm().getCutLength();
		Set<GENE_TYPE> taken1 = new HashSet<GENE_TYPE>();
		Set<GENE_TYPE> taken2 = new HashSet<GENE_TYPE>();
		for (int i = 0; i < geneLength; ++i) {
			if ((i < cutPoint1) || (i > cutPoint2)) {
			} else {
				offspring1.setGene(i, father.getGene(i));
				offspring2.setGene(i, this.getGene(i));
				taken1.add(offspring1.getGene(i));
				taken2.add(offspring2.getGene(i));
			}
		}
		for (int i = 0; i < geneLength; ++i) {
			if ((i < cutPoint1) || (i > cutPoint2)) {
				if (getGeneticAlgorithm().isPreventRepeat()) {
					offspring1.setGene(i, getNotTaken(this, taken1));
					offspring2.setGene(i, getNotTaken(father, taken2));
				} else {
					offspring1.setGene(i, getGene(i));
					offspring2.setGene(i, father.getGene(i));
				}
			}
		}
		if (Math.random() < geneticAlgorithm.getMutationPercent()) {
			offspring1.mutate();
		}
		if (Math.random() < geneticAlgorithm.getMutationPercent()) {
			offspring2.mutate();
		}
		offspring1.calculateCost();
		offspring2.calculateCost();
	}

	public abstract void calculateCost();

	public abstract void mutate();
	
	public double getCost() {
		return cost;
	}

	private GENE_TYPE getNotTaken(Chromosome<GENE_TYPE, GA_TYPE> chromosome,
			Set<GENE_TYPE> taken) {
		int geneLength = chromosome.size();
		GENE_TYPE trial = null;
		for (int i = 0; i < geneLength; ++i) {
			trial = chromosome.getGene(i);
			if (taken.add(trial)) {
				return trial;
			}
		}
		return null;
	}

	public int size() {
		return genes.length;
	}

	public GENE_TYPE getGene(int i) {
		return genes[i];
	}
	
	public void setGene(int i, GENE_TYPE value) {
		genes[i] = value;
	}

	public GA_TYPE getGeneticAlgorithm() {
		return geneticAlgorithm;
	}

	private GENE_TYPE[] getGenes() {
		return genes;
	}

}
