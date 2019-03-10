package model.crossover;

import java.util.Random;

import model.cromosome.Chromosome;

public abstract class Crossover {
	protected double prob;
	protected Random Rnd = new Random();
	protected int crossover;
	
	public abstract Chromosome[] cruza(Chromosome[] pob);
	public int getCrossover() {
		return crossover;
	}
}
