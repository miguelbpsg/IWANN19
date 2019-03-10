package model.selection;

import java.util.Random;

import controller.ChromosomeFactory;
import model.cromosome.Chromosome;

public class StochasticUniversal implements Selection{
	private Random Rnd = new Random();

	public Chromosome[] select(Chromosome[] pob, int tam_pob) {
		Chromosome[] new_pob = new Chromosome[tam_pob];
		double prob = Rnd.nextDouble()/tam_pob;
		int pos_super = 0;
		double paso = (double)1 / tam_pob; //para que sea en enteros y no valga 0
		for(int i = 0; i < tam_pob; i++) {
			while(prob >= pob[pos_super].getAccScore())
				pos_super++;
			new_pob[i] = ChromosomeFactory.copyChromosome(pob[pos_super]);
			prob += paso;
		}
		return new_pob;
	}

	@Override
	public String toString() {
		return "Universal stochastic";
	}
	
	@Override
	public int getSelection() {
		return 6;
	}
}
