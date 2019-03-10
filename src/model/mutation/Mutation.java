package model.mutation;

import java.util.List;
import java.util.Random;

import model.cromosome.Chromosome;
import model.cromosome.FSMTest;

public abstract class Mutation {
	protected Random rnd = new Random();
	protected double prob;
	protected int mutacion;
	protected List<FSMTest> tests;
		
	public abstract Chromosome[] mutate(Chromosome[] pob);
	public int getMutation() {
		return mutacion;
	}
}
