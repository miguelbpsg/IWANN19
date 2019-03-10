package model.cromosome;

import java.util.ArrayList;
import java.util.List;

public class Chromosome {
	private List<FSMTest> tests;
	private int totalSize;
	
	private int[][] testsVSmutants;
	private int maxPenalty;
	
	private List<Integer> unkilledMutants = new ArrayList<Integer>();
	
	private double fitness;		//the smaller the better.
	private double score; //puntuación relativa(fitness/suma fitness)
	private double accScore; //puntuación acumulada para selección
	private int rank = 0;	//higher rank = more likely to get chosen
	
	public Chromosome(List<FSMTest> tests, int totalSize, int[][] testsVSmutants, int maxPenalty) {	
		this.tests = tests;
		this.totalSize = totalSize;
		this.testsVSmutants = testsVSmutants;
		this.maxPenalty = maxPenalty;
		
		int size = 0;
		for(FSMTest t : tests)
			size += t.getSize();
		if (size > totalSize)
			System.err.println("Too many tests initializing a cromosome");
	}

	public Chromosome(Chromosome c) {		//copia
		this.tests = c.copyGenotype();
		this.totalSize = c.getTotalSize();
		this.testsVSmutants = c.getTable();
		this.maxPenalty = c.getMaxPenalty();
		
		this.fitness = c.getFitness();
		this.score = c.getScore();
		this.accScore = c.getAccScore();
		this.rank = c.getRank();
		
		int size = 0;
		for(FSMTest t : tests)
			size += t.getSize();
		if (size > totalSize)
			System.err.println("Too many tests initializing a cromosome");
	}
	
	public Chromosome better(Chromosome other) {		//the minimum
		 return other.getFitness() < fitness ? other : this;
	}
	 
	public Chromosome worse(Chromosome other) {			//the maximum
		 return other.getFitness() > fitness ? other : this;
	}
	
	public List<FSMTest> getGenotype() {
		return tests;
	}
	
	public List<FSMTest> copyGenotype() {
		List<FSMTest> copy = new ArrayList<FSMTest>();
		for(int i = 0; i < tests.size(); i++) {
			copy.add(new FSMTest(tests.get(i)));
		}
		return copy;
	}
	
	public int[][] getTable() {
		return testsVSmutants;
	}
	
	public int getMaxPenalty() {
		return maxPenalty;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public double getScore() {
		return score;
	}
	
	public double getAccScore() {
		return accScore;
	}
	
	public int getRank() {
		return rank;
	}

	public FSMTest removeGene(int pos) {
		return tests.remove(pos);
	}
	
	public FSMTest getGene(int pos) {
		return tests.get(pos);
	}

	public List<FSMTest> getGenes(int init, int end){
		List<FSMTest> l = new ArrayList<FSMTest>();
		for (int i = init; i < end; i++) {
			FSMTest t = tests.get(i);
			l.add(t);
		}
		return l;
	}
	
	public List<FSMTest> getGenes(int init) {
		return getGenes(init, tests.size());
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	public void setAccScore(double accScore) {
		this.accScore = accScore;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public void setGene(int pos, FSMTest t) {
		FSMTest aTest = null;
		boolean eliminated = false;
		if(pos < tests.size()) {
			aTest = tests.remove(pos);
			eliminated = true;
		}
		int size = 0;
		for(FSMTest test : tests)
			size += test.getSize();
		if(size + t.getSize() <= totalSize)
			tests.add(pos, t);
		else if(eliminated)
			tests.add(aTest);
	}	

	public void setGenes(int pos, List<FSMTest> ts) {
		for(int i = tests.size() - 1; i >= pos; i--)
			tests.remove(i);
		for(int i = 0; i < ts.size() && tests.size() + ts.get(i).getSize() < totalSize; i++)
			tests.add(ts.get(i));
	}
	
	public int getTotalSize() {
		return totalSize;
	}
	
	public int getSize() {
		int size = 0;
		for(FSMTest t : tests)
			size += t.getSize();
		return size;
	}
	
	public int getSizeTest(int i) {
		return tests.get(i).getSize();
	}
	
	public int getSizeTests(int init, int end) {
		int size = 0;
		for(int i = init; i < end; i++)
			size += tests.get(i).getSize();
		return size;
	}

	public int getSpace() {
		int size = 0;
		for(FSMTest t : tests)
			size += t.getSize();
		return totalSize - size;
	}
	
//FITNESS
	public void evaluate() {
		unkilledMutants = new ArrayList<Integer>();
		fitness = 0;
		for(int posMutant = 0; posMutant < testsVSmutants[0].length; posMutant++) {
			int minDepth = 5*maxPenalty;
			for(int posTest = 0; posTest < tests.size(); posTest++) {
				if(minDepth > testsVSmutants[tests.get(posTest).getId()][posMutant])
					minDepth = testsVSmutants[tests.get(posTest).getId()][posMutant];
			}
			if(minDepth == 5*maxPenalty)
				unkilledMutants.add(posMutant);
			fitness+= minDepth;
		}
	}
	
	public String toString() {
		return tests.toString();
	}

}
