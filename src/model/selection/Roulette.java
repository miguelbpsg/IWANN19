package model.selection;

import java.util.Random;

import controller.ChromosomeFactory;
import model.cromosome.Chromosome;

public class Roulette implements Selection{
	private Random Rnd = new Random();
	
	@Override
	public Chromosome[] select(Chromosome[] pob, int tam_pob) {
		int sel_super[] = new int[tam_pob];
		double prob;
		int pos_super;
		for(int i = 0; i < tam_pob; i++) {
			prob = Rnd.nextDouble();
			pos_super = 0;
			while(prob > pob[pos_super].getAccScore())
				pos_super++;
			sel_super[i] = pos_super;
		}
		Chromosome[] new_pob = new Chromosome[tam_pob];
		for (int i = 0; i < tam_pob; i++)
			new_pob[i] = ChromosomeFactory.copyChromosome(pob[sel_super[i]]);
		return new_pob;
	}

	@Override
	public String toString() {
		return "Roulette";
	}
	
	@Override
	public int getSelection() {
		return 3;
	}

}