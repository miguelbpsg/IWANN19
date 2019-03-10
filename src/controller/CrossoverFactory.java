package controller;

import model.crossover.ContinuousCrossover;
import model.crossover.Crossover;
import model.crossover.StandardCrossover;

public class CrossoverFactory {
	public static Crossover createCrossover(Crossover c, double prob) { 
		switch(c.getCrossover()) {
		case 1:
			return new ContinuousCrossover(prob);
		case 2:
			return new StandardCrossover(prob);
		default:
			System.err.println("Error en factoria cruce");
			return null;
		}
	}
}
