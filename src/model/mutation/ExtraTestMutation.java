package model.mutation;

import java.util.List;

import model.cromosome.Chromosome;
import model.cromosome.FSMTest;

public class ExtraTestMutation extends Mutation{

	public ExtraTestMutation(double prob, List<FSMTest> tests) {
		this.prob = prob;
		this.mutacion = 1;
		this.tests = tests;
	}
	
	public ExtraTestMutation() {
		this.mutacion = 1;
	}
	public Chromosome[] mutate(Chromosome[] pob) {
		for(int i = 0; i < pob.length; i++)
			if(rnd.nextDouble() <= prob) {
				FSMTest t = new FSMTest(tests.get(rnd.nextInt(tests.size())));
				if(pob[i].getSpace() > t.getSize())
					pob[i].setGene(pob[i].getGenotype().size(), t);
			}
		return pob;
	}
	
	@Override
	public String toString() {
		return "Extra test mutation";
	}
}
