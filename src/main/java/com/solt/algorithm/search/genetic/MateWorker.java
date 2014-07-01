package com.solt.algorithm.search.genetic;

import java.util.concurrent.Callable;

public class MateWorker<CHROMOSOME_TYPE extends Chromosome<?, ?>> implements Callable<Integer> {
	private CHROMOSOME_TYPE mother;
	private CHROMOSOME_TYPE father;
	private CHROMOSOME_TYPE child1;
	private CHROMOSOME_TYPE child2;
	
	public MateWorker(CHROMOSOME_TYPE mother, CHROMOSOME_TYPE father, CHROMOSOME_TYPE child1, CHROMOSOME_TYPE child2) {
		this.mother = mother;
		this.father = father;
		this.child1 = child1;
		this.child2 = child2;
	}

	@Override
	public Integer call() throws Exception {
		mother.mate((Chromosome)father, (Chromosome) child1, (Chromosome) child2);
		return null;
	}

}
