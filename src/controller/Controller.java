package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

import model.cromosome.Chromosome;
import model.cromosome.FSM;
import model.cromosome.FSMTest;
import model.crossover.Crossover;
import model.initialization.Initialization;
import model.mutation.Mutation;
import model.replacement.Replacement;
import model.selection.Selection;
import view.Ventana;

public class Controller {
	private double[] mejores;
	private double[] peores;
	private double[] medias;
	private Chromosome[] mejorHastaAhora;
	private double[] generaciones;
	private Ventana v;
	private Hebra hebra;
	
	public void run(int size_pop, int iters, Initialization ini, int max_tests,
					Selection s, int participantes, double victoria, double trunc,
					Crossover c, double prob_cruce,
					Mutation m, double prob_mut,
					String testsFile, String mutantsFile,
					Replacement r, double elit,
					Ventana v) {
		long startTime = System.currentTimeMillis();
		
		List<FSMTest> totalTests = ChromosomeFactory.readTests(testsFile);
		List<FSM> mutants = ChromosomeFactory.readMutants(mutantsFile);
		
		Initialization metInic= InitializationFactory.createInitialization(ini, max_tests);
		Selection metSelec = SelectionFactory.crearSeleccion(s, participantes, victoria, trunc);
		Crossover metCruce = CrossoverFactory.createCrossover(c, prob_cruce);
		Mutation metMut = MutationFactory.createMutation(m, prob_mut, totalTests);
		Replacement metReempl = ReplacementFactory.createReplacement(r, elit);
		
		Chromosome[] poblacion = metInic.initialize(size_pop, totalTests, mutants);
		this.v = v;
		
		mejores = new double[iters+1];
		peores = new double[iters+1];
		medias = new double[iters+1];
		mejorHastaAhora = new Chromosome[iters+1];
		generaciones = new double[iters+1];
		
		Chromosome mejorGlobal = score(poblacion);
		Chromosome mejor = mejorGlobal;
		mejores[0] = mejor.getFitness();
		mejorHastaAhora[0] = mejorGlobal;
		medias[0] = media(poblacion);
		peores[0] = peor(poblacion);
		generaciones[0] = 0;
        for(int i = 0; i < iters; i++) {
        	poblacion =
			metReempl.replace(
				poblacion,
				metMut.mutate(
					metCruce.cruza(
						metSelec.select(poblacion, size_pop)
					)
				)
			);
			mejor = score(poblacion);
            mejorGlobal = mejorGlobal.better(mejor);
            mejores[i+1] = mejor.getFitness();
            mejorHastaAhora[i+1] = mejorGlobal;
            medias[i+1] = media(poblacion);
            peores[i+1] = peor(poblacion);
            generaciones[i+1] = i+1;
    	
            /*if((hebra != null && !hebra.equals(null)) && hebra.isAlive())
    			hebra.interrupt();*/
            hebra = new Hebra(mejorGlobal, i);
            hebra.start();

		}
		System.out.println(System.currentTimeMillis() - startTime);
	}

	public void generateData(String file, int profMax, int numTests) {
		FSM spec = ChromosomeFactory.readSpecification(file);

		List<FSMTest> tests = spec.generateTests(profMax, numTests);
		String testsText = "";
		for(int i = 0; i < tests.size(); i++) {
			testsText += tests.get(i).toFile();
			if(i == tests.size() - 1 || tests.get(i).getSize() == 0)
				testsText += "";
			else
				testsText += "\n";
		}
		String[] text = testsText.split("\n");

		try {
			PrintWriter writer = new PrintWriter("files/Tests.txt", "UTF-8");
			for(int i = 0; i < text.length - 1; i++)
				writer.println(text[i]);
			writer.print(text[text.length-1]);
			writer.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		List<FSM> mutants = spec.generateAllMutants();
		String mutantsText = mutants.size() + "\n";
		Set<String> inputs = spec.getInputs();
		Iterator<String> it = inputs.iterator();
		while (it.hasNext()) {
			mutantsText += it.next() + (it.hasNext() ? " " : "\n");
		}
		Set<String> outputs = spec.getOutputs();
		it = outputs.iterator();
		while (it.hasNext()) {
			mutantsText += it.next() + (it.hasNext() ? " " : "\n");
		}		
		for(int i = 0; i < mutants.size(); i++)
			mutantsText += mutants.get(i).toFile() + (i == mutants.size() - 1 ? "" : "\n");
		
		text = mutantsText.split("\n");
		try {
			PrintWriter writer = new PrintWriter("files/Mutants.txt", "UTF-8");
			for(int i = 0; i < text.length - 1; i++)
				writer.println(text[i]);
			writer.print(text[text.length-1]);
			writer.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void combinatory(String testsFile, String mutantsFile, int max_inputs, Ventana v) {
		long startTime = System.currentTimeMillis();
		
		List<FSMTest> allTests = ChromosomeFactory.readTests(testsFile);
		List<FSM> mutants = ChromosomeFactory.readMutants(mutantsFile);

		int[][] testsVSmutants = new int[allTests.size()][mutants.size() + 1];
		int maxPenalty = 0;
		for(int test = 0; test < allTests.size(); test++) {
			for(int mutant = 1; mutant < mutants.size(); mutant++) {
				testsVSmutants[test][mutant] = allTests.get(test).killMutant(mutants.get(mutant));
				if (testsVSmutants[test][mutant] > maxPenalty && testsVSmutants[test][mutant] < Integer.MAX_VALUE)
					maxPenalty = testsVSmutants[test][mutant];
			}
			testsVSmutants[test][0] = test;
		}
		
		List<List<FSMTest>> subsets = new ArrayList<List<FSMTest>>();
		
		Chromosome c = bestSubset(new ArrayList<FSMTest>(), 0, subsets, allTests, max_inputs, testsVSmutants, maxPenalty, 0);
		v.actualizaResultado(c);
		
		System.out.println(System.currentTimeMillis() - startTime);
	}
	
	public void greedy(String testsFile, String mutantsFile, int max_inputs, Ventana v) {
		long startTime = System.currentTimeMillis();

		List<FSMTest> allTests = ChromosomeFactory.readTests(testsFile);
		List<FSM> mutants = ChromosomeFactory.readMutants(mutantsFile);
		
		
		int[][] testsVSmutants = new int[allTests.size()][mutants.size() + 1];
		int maxPenalty = 0;
		for(int test = 0; test < allTests.size(); test++) {
			for(int mutant = 1; mutant < mutants.size(); mutant++) {
				testsVSmutants[test][mutant] = allTests.get(test).killMutant(mutants.get(mutant));
				if (testsVSmutants[test][mutant] > maxPenalty && testsVSmutants[test][mutant] < Integer.MAX_VALUE)
					maxPenalty = testsVSmutants[test][mutant];
			}
			testsVSmutants[test][0] = test;
		}
		List<FSMTest> tests = new ArrayList<FSMTest>();
		int[][] dynamicTable = testsVSmutants;
		for(int size = 0; size < max_inputs && dynamicTable.length > 0 && dynamicTable[0].length > 0;) {
			quicksort(dynamicTable, 0, dynamicTable.length -1);
			List<Integer> removedColumns = new ArrayList<Integer>();
			for(int j = 0; j < dynamicTable[0].length; j++) {
				if(dynamicTable[0][j] < Integer.MAX_VALUE)
					removedColumns.add(j);
			}
			int[][] aux = dynamicTable;
			dynamicTable = new int[aux.length - 1][aux[0].length - removedColumns.size()];
			for(int j = 0; j < aux.length - 1; j++) {
				dynamicTable[j][0] = aux[j+1][0];
				for(int k = 1, l = 1; k < aux[0].length - removedColumns.size(); l++) {
					if(!removedColumns.contains(l)) {
						dynamicTable[j][k] = aux[j+1][l];
						k++;
					}
				}
			}
			size += allTests.get(aux[0][0]).getSize();
			if(size < max_inputs)
				tests.add(allTests.get(aux[0][0]));
		}
		Chromosome c = ChromosomeFactory.createChromosome(tests, max_inputs, testsVSmutants, maxPenalty);
		c.evaluate();
		v.actualizaResultado(c);
		System.out.println(System.currentTimeMillis() - startTime);
	}

	
	private Chromosome bestSubset(List<FSMTest> actualTest, int pos, List<List<FSMTest>> subsets, List<FSMTest> allTests, int max_inputs, int[][] testsVSmutants, int maxPenalty, int size) {
		if(pos < allTests.size()) {
			List<FSMTest> t = new ArrayList<FSMTest>(actualTest);
			Chromosome c1 = bestSubset(actualTest, pos + 1, subsets, allTests, max_inputs, testsVSmutants, maxPenalty, size);
			if(allTests.get(pos).getSize() + size <= max_inputs) {
				t.add(allTests.get(pos));
				size += allTests.get(pos).getSize();
				Chromosome c2 = bestSubset(t, pos + 1, subsets, allTests, max_inputs, testsVSmutants, maxPenalty, size);
				c1 = c1.getFitness() < c2.getFitness() ? c1 : c2;
			}
			return c1;
		}
		Chromosome c = new Chromosome(actualTest, max_inputs, testsVSmutants, maxPenalty);
		c.evaluate();
		return c;
	}

	private double media(Chromosome[] pob) {
		double m = 0;
		for(int i = 0; i < pob.length; i++)
			m += pob[i].getFitness();
		return m/pob.length;
	}
	
	private double peor(Chromosome[] pob) {
		Chromosome peor = pob[0];
		for (int i = 1; i < pob.length; i++)
			peor = pob[i].worse(peor);
		return peor.getFitness();
	}
	
	public double[] getMejores() {
		return mejores;
	}
	
	public double[] getMedias() {
		return medias;
	}
	
	public double[] getPeores() {
		return peores;
	}
	
	public Chromosome[] getMejoresAbsolutos() {
		return mejorHastaAhora;
	}
	
	public double[] getGeneraciones() {
		return generaciones;
	}
	
	private Chromosome score(Chromosome[] pob) {
		double total = 0;
		double acum = 0;
	
		double[] fitness = new double[pob.length];
		int[] posiciones = new int[pob.length];
		for(int i = 0; i < pob.length; i++){
			total += pob[i].getFitness();
			fitness[i] = pob[i].getFitness();
			posiciones[i] = i;
		}
		total *= 1.05;
		
		double punt_total = 0;
		for(int i = 0; i < pob.length; i++) {
			pob[i].setScore(total - pob[i].getFitness());
			punt_total += total - pob[i].getFitness();
		}
		
		quicksort(posiciones, fitness, 0, pob.length-1);

		for(int i = 0; i < pob.length; i++) {
			pob[i].setScore(pob[i].getScore()/punt_total);
			acum += pob[i].getScore();
			pob[i].setAccScore(acum);
			pob[posiciones[i]].setRank(pob.length - i);
		}

		return pob[posiciones[pob.length - 1]];
	}
	
	private void quicksort(int[] pos, double[] val, int low, int high) {
		if (low < high) {
			int p = partition(pos, val, low, high);
			quicksort(pos, val, low, p - 1);
        	quicksort(pos, val, p + 1, high);
		}
	}
	
	private void quicksort(int[][] pos, int low, int high) {
		if (low < high) {
			int p = partition(pos, low, high);
			quicksort(pos, low, p - 1);
        	quicksort(pos, p + 1, high);
		}
	}
	
	private int partition(int[] pos, double[] val, int low, int high) {
	    double pivot = val[high];
	    int aux;
	    double auxd;
	    
	    int i = low - 1;
	    for (int j = low; j < high; j++)
	        if (val[j] >= pivot) {
	            i++;
	            aux = pos[i];
	            pos[i] = pos[j];
	            pos[j] = aux;
	            auxd = val[i];
	            val[i] = val[j];
	            val[j] = auxd;
	        }
        aux = pos[i+1];
        pos[i+1] = pos[high];
        pos[high] = aux;
        auxd = val[i+1];
        val[i+1] = val[high];
        val[high] = auxd;
	    return i + 1;
	}
	
	private int partition(int[][] pos, int low, int high) {
		int[] pivot = pos[high];
		int[] aux;
		
		int i = low - 1;
		for(int j = low; j < high; j++) {
			int pivotVal = 0, lineVal = 0;
			for(int k = 1; k < pivot.length; k++) {
				pivotVal += pivot[k];
				lineVal += pos[j][k];
			}
			if(lineVal >= pivotVal) {
				i++;
				aux = pos[i];
				pos[i] = pos[j];
				pos[j] = aux;
			}
		}
		aux = pos[i+1];
		pos[i+1] = pos[high];
		pos[high] = aux;
		
		return i+1;
	}
	
	private final class Hebra extends Thread {
		private Chromosome mejor;
		private int i;
		private double[] generacion;
		private double[] mejores;
		private double[] peores;
		private double[] medias;	
		private double[] absolutos;
		
		public Hebra(Chromosome c, int i) {
			super();
			mejor = c;
			i = i+2;		//por cómo se inicializa todo, debe ser así
			this.i = i;
			double[] g = Controller.this.getGeneraciones();
			double[] m = Controller.this.getMejores();
			double[] p = Controller.this.getPeores();
			double[] a = Controller.this.getMedias();
			Chromosome[] croms = Controller.this.getMejoresAbsolutos();
			generacion = new double[i];
			mejores = new double[i];
			peores = new double[i];
			medias = new double[i];
			absolutos = new double[i];
			for(int j = 0; j < i; j++){
				generacion[j] = g[j];
				mejores[j] = m[j];
				peores[j] = p[j];
				medias[j] = a[j];
				absolutos[j] = croms[j].getFitness();
			}
		}
		
		@Override
		public void run() {
			try {
				Thread.sleep(10);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						v.actualizaResultado(mejor);
						v.actualizaGrafica(generacion, mejores, peores, medias, absolutos);
						v.actualizaGrafica(generacion, mejores, peores, medias, absolutos);
					}
				});
			} catch (InterruptedException e) {
				System.err.println("Matando Hebra de controlador "+i);
			}
		}
	}
}
