package controller;

import model.initialization.Initialization;
import model.initialization.IncrementalInitialization;

public class InitializationFactory {

	public static Initialization createInitialization(Initialization i, int size) {
		switch(i.getInitialization()) {
		case 1:
			return new IncrementalInitialization(size);
		default:
			System.err.println("Error en factoria inicialización");
			return null;
		}
	}
}
