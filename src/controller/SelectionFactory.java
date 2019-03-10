package controller;

import model.selection.StochasticUniversal;
import model.selection.Ranking;
import model.selection.Remains;
import model.selection.Roulette;
import model.selection.Selection;
import model.selection.Tournament;
import model.selection.Truncation;

public class SelectionFactory {
	
	public static Selection createSelection(int tipo, int participantes, double vict, double elit) {
		switch(tipo) {
		case 1:
			return new Ranking();
		case 2:
			return new Remains();
		case 3:
			return new Roulette();
		case 4:
			return new Tournament(participantes, vict);
		case 5:
			return new Truncation(elit);
		case 6:
			return new StochasticUniversal();
		default:
			System.err.println("PROBLEMA CREANDO SELECCION");
			System.err.printf("Tipo %d, Participantes %d, Elitismo %f, Victoria %f", tipo, participantes, elit, vict);
			System.err.println();
			return null;
		}
	}
	
	public static Selection crearSeleccion(Selection s, int participantes, double vict, double elit) {
		return createSelection(s.getSelection(), participantes, vict, elit);
	}
	
}
