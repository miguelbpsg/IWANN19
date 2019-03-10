package model.selection;


import controller.ChromosomeFactory;
import model.cromosome.Chromosome;

public class Truncation implements Selection{
	private double trunc;

	public Truncation(double trunc) {
		this.trunc = trunc;
	}

	public Chromosome[] select(Chromosome[] pob, int tam_pob) {
		Chromosome[] new_pob = new Chromosome[tam_pob];
		int i = 0;
		quicksort(pob, 0, pob.length - 1);
		for(int j = 0; i < tam_pob && j <= Math.ceil(1/trunc); j++) {	//como tal, la j sobra, pero ayuda a ver la cantidad de veces que se selecciona un elemento
			for(int k = 0; i < tam_pob && k < Math.floor(pob.length*trunc); i++, k++)
				new_pob[i] = ChromosomeFactory.copyChromosome(pob[k]);
		}
		return new_pob;
	}
	
	private void quicksort(Chromosome[] pob, int low, int high) {
		if (low < high) {
			int p = partition(pob, low, high);
			quicksort(pob, low, p - 1);
        	quicksort(pob, p + 1, high);
		}
	}
	
	private int partition(Chromosome[] pob, int low, int high) {
	    Chromosome pivot = pob[high];
	    Chromosome aux;
	    int i = low - 1;
	    for (int j = low; j < high; j++)
	        if (pob[j] == pob[j].better(pivot)) {
	            i++;
	            aux = ChromosomeFactory.copyChromosome(pob[i]);
	            pob[i] = ChromosomeFactory.copyChromosome(pob[j]);
	            pob[j] = aux;
	        }
            aux = ChromosomeFactory.copyChromosome(pob[i+1]);
            pob[i+1] = ChromosomeFactory.copyChromosome(pob[high]);
            pob[high] = aux;
	    return i + 1;
	}

	@Override
	public String toString() {
		return "Truncation";
	}
	
	@Override
	public int getSelection() {
		return 5;
	}
}
