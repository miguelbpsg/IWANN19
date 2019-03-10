package controller;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.cromosome.Chromosome;
import model.cromosome.FSM;
import model.cromosome.FSMTest;
import model.cromosome.Node;
import model.cromosome.Transition;

public class ChromosomeFactory {
	
	public static Chromosome copyChromosome(Chromosome c) {
			return new Chromosome(c);
	}
	
	public static Chromosome createChromosome(List<FSMTest> tests, int size, int[][] testsVSmutants, int maxPenalty) {
			return new Chromosome(tests, size, testsVSmutants, maxPenalty);
	}
	
	public static FSM readSpecification(String file) {
		String inFile = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				line = br.readLine();
				if (line != null)
					sb.append("\n");
			}
			inFile = sb.toString();
		    br.close();
		}
		catch(IOException e) {
			System.err.println(e.getMessage());
		}
		String[] lines = inFile.split("\n");

		if(lines.length > 1) {
			int numNodes = Integer.parseInt(lines[0]);
			List<Node> nodes = new ArrayList<Node>();
			List<Transition> transitions = new ArrayList<Transition>();
			Set<String> inputs = new HashSet<String>();
			Set<String> outputs = new HashSet<String>();
			for(int i = 0; i < numNodes; i++)
				nodes.add(new Node(i));
			for(int index = 1; index < lines.length; index++) {
				String[] line = lines[index].split(" ");
				if (line.length == 4) {
					Transition t = new Transition(nodes.get(Integer.parseInt(line[0])),
						nodes.get(Integer.parseInt(line[1])), line[2], line[3]);
					nodes.get(Integer.parseInt(line[0])).addTransition(t);
					transitions.add(t);
					inputs.add(line[2]);
					outputs.add(line[3]);
				}
				else System.err.println("error parsing, line " + index + " does not have two nodes and two strings");
			}

			return new FSM(nodes,transitions,inputs,outputs,nodes.get(0));
		}
		else System.err.println("There is not an actual FSM in the file " +file);
		return null;
	}

	public static List<FSM> readMutants(String file) {
		String inFile = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				line = br.readLine();
				if (line != null)
					sb.append("\n");
			}
			inFile = sb.toString();
		    br.close();
		}
		catch(IOException e) {
			System.err.println(e.getMessage());
		}
		String[] lines = inFile.split("\n");

		List<FSM> mutants = new ArrayList<FSM>();
		if(lines.length > 2) {
			int numMutants = Integer.parseInt(lines[0]);
			Set<String> inputs = new HashSet<String>();
			Set<String> outputs = new HashSet<String>();
			String[] line = lines[1].split(" ");
			for(int i = 0; i < line.length; i++)
				inputs.add(line[i]);
			line = lines[2].split(" ");
			for(int i = 0; i < line.length; i++)
				outputs.add(line[i]);
			
			int index = 3;
			for(int mutant = 0; mutant < numMutants; mutant++) {
				int numNodes = Integer.parseInt(lines[index]);
				index++;
				List<Node> nodes = new ArrayList<Node>();
				for(int node = 0; node < numNodes; node++)
					nodes.add(new Node(node));

				int numTransitions = Integer.parseInt(lines[index]);
				index++;
				List<Transition> transitions = new ArrayList<Transition>();
				for(int transition = 0; transition < numTransitions; transition++, index++) {
					line = lines[index].split(" ");
					if (line.length == 4) {
						Transition t = new Transition(nodes.get(Integer.parseInt(line[0])),
							nodes.get(Integer.parseInt(line[1])), line[2], line[3]);
						nodes.get(Integer.parseInt(line[0])).addTransition(t);
						transitions.add(t);
						inputs.add(line[2]);
						outputs.add(line[3]);
					}
					else System.err.println("error parsing, line " + index + " does not have two nodes and two strings");
				}
				mutants.add(new FSM(nodes,transitions,inputs,outputs,nodes.get(0)));
			}
			return mutants;
		}
		else System.err.println("There is not an actual set of FSM in the file " +file);
		return null;
	}

	public static List<FSMTest> readTests(String file) {
		String inFile = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				line = br.readLine();
				if (line != null)
					sb.append("\n");
			}
			inFile = sb.toString();
		    br.close();
		}
		catch(IOException e) {
			System.err.println(e.getMessage());
		}
		String[] lines = inFile.split("\n");

		List<FSMTest> tests = new ArrayList<FSMTest>();
		if(lines.length > 1) {
			for(int index = 0; index < lines.length; index = index + 2) {
				List<String> inputs = new ArrayList<String>();
				List<String> outputs = new ArrayList<String>();
				String[] lineIN = lines[index].split(" ");
				String[] lineOUT = lines[index+1].split(" ");
				if(lineIN.length == lineOUT.length) {
					for(int entry=0; entry < lineIN.length; entry++) {
						inputs.add(lineIN[entry]);
						outputs.add(lineOUT[entry]);
					}
					tests.add(new FSMTest(index/2,inputs,outputs));
				}
				else System.err.println("Not the same number of inputs and outputs at line" + index);
			}
			return tests;
		}
		else System.err.println("There is not an actual testSuite in the file " +file);
		return null;
	}

}
