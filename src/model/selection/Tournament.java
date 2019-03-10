package model.selection;

import java.util.Random;

import controller.ChromosomeFactory;
import model.cromosome.Chromosome;

public class Tournament implements Selection {
	private int participantes;
	private double prob;
	private Random Rnd = new Random();
	
	public Tournament(){
		participantes = 2;
		prob = 1;
	}
	
	public Tournament(double prob) {
		this();
		this.prob = prob;
	}
	
	public Tournament(int elementos) {
		this();
		this.participantes = elementos;
	}
	
	public Tournament(int elementos, double prob) {
		this.participantes = elementos;
		this.prob = prob;
	}
	
	@Override
	public Chromosome[] select(Chromosome[] pob, int tam_pob) {
		Chromosome[] new_pob = new Chromosome[tam_pob];
		int[] index = new int[participantes];
		int ganador;
		for(int i = 0; i < tam_pob; i++) {
			for(int j = 0; j < participantes; j++)
				index[j] = Rnd.nextInt(pob.length);
			ganador = eliminatoria(pob, participantes, index);
			new_pob[i] = ChromosomeFactory.copyChromosome(pob[ganador]);
		}
		return new_pob;
	}

	private int eliminatoria(Chromosome[] pob, int particip, int[] index) {
		if (particip == 1)
			return index[0];
		int max = index[0];
		int index_max = 0;
		for(int i = 0; i < particip; i++) {
			if(pob[index[i]].getScore() > pob[max].getScore()) {
				max = index[i];
				index_max = i;
			}
		}
		
		if (Rnd.nextDouble() < prob)
			return max;
		particip--;
		int[] index2 = new int[particip];
		int i = 0;
		for (; i < index_max; i++)
			index2[i] = index[i];
		for (; i < particip; i++)
			index2[i] = index[i+1];
		return eliminatoria(pob, particip, index2);
	}

	@Override
	public String toString() {
		return "Tournament";
	}
	
	@Override
	public int getSelection() {
		return 4;
	}

}
