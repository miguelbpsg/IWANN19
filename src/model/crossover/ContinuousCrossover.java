package model.crossover;

import java.util.ArrayList;
import java.util.List;

import controller.ChromosomeFactory;
import model.cromosome.Chromosome;
import model.cromosome.FSMTest;

public class ContinuousCrossover extends Crossover {

	public ContinuousCrossover(double prob) {
		this.prob = prob;
		this.crossover = 1;
	}

	public ContinuousCrossover() {
		this(0.6);
	}

	
	@Override
	public Chromosome[] cruza(Chromosome[] pob) {
		Chromosome ind1, ind2;
		List<FSMTest> t1, t2;
		FSMTest tAux1 = null;
		FSMTest tAux2 = null;
		
		for(int i = 0; i + 1 < pob.length; i = i+2) {		//i < pob.length && i+1 < pob.length --> i < i+1 --> con lo segundo sirve 
				ind1 = ChromosomeFactory.copyChromosome(pob[i]);
				ind2 = ChromosomeFactory.copyChromosome(pob[i+1]);
				t1 = new ArrayList<FSMTest>();
				t2 = new ArrayList<FSMTest>();
				
				for(int j = 0; j < ind1.getGenotype().size(); j++) {
					if (prob >= Rnd.nextDouble()) {
						t1.add(ind1.removeGene(j));
						j--;
						tAux1 = t1.get(0);
					}
				}
				
				for(int j = 0; j < ind2.getGenotype().size(); j++) {
					if (prob >= Rnd.nextDouble()) {
						t2.add(ind2.removeGene(j));
						j--;
						tAux2 = t2.get(0);
					}
				}

				int size = 0;
				for(FSMTest t : t2)
					size += t.getSize();
				while(ind1.getSize() + size > ind1.getTotalSize()) {
					size -= t2.get(t2.size() - 1).getSize();
					t2.remove(t2.size() - 1);
				}
				ind1.setGenes(ind1.getGenotype().size(), t2);

				size = 0;
				for(FSMTest t : t1)
					size += t.getSize();
				while(ind2.getSize() + size > ind2.getTotalSize()) {
					size -= t1.get(t1.size() - 1).getSize();
					t1.remove(t1.size() - 1);
				}
				ind2.setGenes(ind2.getGenotype().size(), t1);
				
				if(ind1.getSpace() == ind1.getTotalSize())
					ind1.setGene(0, tAux1);
				if(ind2.getSpace() == ind2.getTotalSize())
					ind2.setGene(0, tAux2);
				pob[i] = ind1;
				pob[i+1] = ind2;
		}
		return pob;
	}
	
	@Override
	public String toString() {
		return "Continuous crossover";
	}
}
