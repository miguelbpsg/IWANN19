package model.initialization;

import java.util.List;
import java.util.Random;

import model.cromosome.Chromosome;
import model.cromosome.FSM;
import model.cromosome.FSMTest;

public abstract class Initialization {
	protected int initialization;
	protected int size;
	protected Random rnd = new Random();

	public abstract Chromosome[] initialize(int size_pob, List<FSMTest> allTests, List<FSM> mutants);
	public int getInitialization() {
		return initialization;
	}
	
}
