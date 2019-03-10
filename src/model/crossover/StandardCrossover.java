package model.crossover;

import java.util.List;

import controller.ChromosomeFactory;
import model.cromosome.Chromosome;
import model.cromosome.FSMTest;

public class StandardCrossover extends Crossover {

	public StandardCrossover(double prob) {
		this.prob = prob;
		this.crossover = 2;
	}

	public StandardCrossover() {
		this(0.6);
	}
	
	@Override
	public Chromosome[] cruza(Chromosome[] pob) {
		Chromosome ind1, ind2;
		List<FSMTest> t1, t2;
		int pos1, pos2;
		FSMTest tAux1, tAux2;
		
		for(int i = 0; i + 1 < pob.length; i = i+2) {		//i < pob.length && i+1 < pob.length --> i < i+1 --> con lo segundo sirve 
			if (prob >= Rnd.nextDouble()) {
				ind1 = ChromosomeFactory.copyChromosome(pob[i]);
				ind2 = ChromosomeFactory.copyChromosome(pob[i+1]);
				
				pos1 = Rnd.nextInt(ind1.getGenotype().size());	//posición de corte1
				pos2 = Rnd.nextInt(ind2.getGenotype().size());	//posición de corte2
				
				tAux1 = ind1.getGene(pos1);
				tAux2 = ind2.getGene(pos2);

				t1 = ind1.getGenes(pos1);
				t2 = ind2.getGenes(pos2);

				int size = 0;
				for(FSMTest t : t2)
					size += t.getSize();
				while(ind1.getSizeTests(0, pos1) + size > ind1.getTotalSize()) {
					size -= t2.get(t2.size() - 1).getSize();
					t2.remove(t2.size() - 1);
				}
				ind1.setGenes(pos1, t2);

				size = 0;
				for(FSMTest t : t1)
					size += t.getSize();
				while(ind2.getSizeTests(0, pos2) + size > ind2.getTotalSize()) {
					size -= t1.get(t1.size() - 1).getSize();
					t1.remove(t1.size() - 1);
				}
				ind2.setGenes(pos2, t1);
				
				
				if(ind1.getSpace() == ind1.getTotalSize())
					ind1.setGene(0, tAux1);
				if(ind2.getSpace() == ind2.getTotalSize())
					ind2.setGene(0, tAux2);
				pob[i] = ind1;
				pob[i+1] = ind2;
			}
		}
		return pob;
	}
	
	@Override
	public String toString() {
		return "Standard crossover";
	}
}