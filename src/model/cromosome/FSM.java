package model.cromosome;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class FSM {
	private List<Node> nodes;
	private List<Transition> transitions;
	private Set<String> inputs;
	private Set<String> outputs;
	private Node initialNode;
	
	public FSM(List<Node> nodes, List<Transition> transitions, Set<String> inputs, Set<String> outputs, Node initialNode) {
		this.nodes = nodes;
		this.transitions = transitions;
		this.inputs = inputs;
		this.outputs = outputs;
		this.initialNode = initialNode;
	}
	
	public FSM(FSM m) {
		this.nodes = new ArrayList<Node>(m.getNodes());
		this.transitions = new ArrayList<Transition>(m.getTransitions());
		this.inputs = new HashSet<String>(m.getInputs());
		this.outputs = new HashSet<String>(m.getOutputs());
		this.initialNode = m.getInitialNode();
	}
	
	public List<Node> getNodes() {
		return nodes;
	}
	
	public List<Transition> getTransitions() {
		return transitions;
	}
	
	public Set<String> getInputs() {
		return inputs;
	}

	public Set<String> getOutputs() {
		return outputs;
	}

	public Node getInitialNode() {
		return initialNode;
	}
	
	public List<FSM> generateAllMutants(){
		List<FSM> mutants = new ArrayList<FSM>();
		for(Transition t : transitions) {
			Iterator<String> iterator = outputs.iterator();
			String o = iterator.next();
			while(iterator.hasNext() && t.getOutput() == o)
				o = iterator.next();
			List<Transition> transAux = new ArrayList<Transition>(transitions);
			transAux.remove(t);
			
			List<Node> nodesAux = new ArrayList<Node>(nodes);
			nodesAux.remove(t.getSource());
			Node newSource = new Node(t.getSource());
			newSource.removeTransition(t);
			nodesAux.add(newSource);
			Transition newTransition = new Transition(newSource, t.getTarget(), t.getInput(), o);
			newSource.addTransition(newTransition);
			
			transAux.add(newTransition);
			FSM mutant = new FSM(nodesAux, transAux, inputs, outputs, initialNode);
			mutants.add(mutant);
			
			for(Node n : nodes) {//changing all targets
				if(!t.getTarget().equals(n)) {
					transAux = new ArrayList<Transition>(transitions);
					transAux.remove(t);
					
					nodesAux = new ArrayList<Node>(nodes);
					nodesAux.remove(t.getSource());
					newSource = new Node(t.getSource());
					newSource.removeTransition(t);
					nodesAux.add(newSource);
					newTransition = new Transition(newSource, n, t.getInput(), t.getOutput());
					newSource.addTransition(newTransition);
					
					transAux.add(newTransition);
					mutant = new FSM(nodes, transAux, inputs, outputs, initialNode);
						mutants.add(mutant);
				}
			}
		}
		return mutants;
	}
	
	public List<FSM> generateMutants(double ratio){
		Random rnd = new Random();
		List<FSM> mutants = new ArrayList<FSM>();
		for(Transition t : transitions) {
			Iterator<String> iterator = outputs.iterator();
			String o = iterator.next();
			while(iterator.hasNext() && t.getOutput() == o)
				o = iterator.next();
			if (rnd.nextDouble() <= ratio) {
				List<Transition> transAux = new ArrayList<Transition>(transitions);
				transAux.remove(t);
				
				List<Node> nodesAux = new ArrayList<Node>(nodes);
				nodesAux.remove(t.getSource());
				Node newSource = new Node(t.getSource());
				newSource.removeTransition(t);
				nodesAux.add(newSource);
				Transition newTransition = new Transition(newSource, t.getTarget(), t.getInput(), o);
				newSource.addTransition(newTransition);
				
				transAux.add(newTransition);
				FSM mutant = new FSM(nodesAux, transAux, inputs, outputs, initialNode);
				mutants.add(mutant);
			}
			
			for(Node n : nodes) {//changing all targets
				if(!t.getTarget().equals(n) && rnd.nextDouble() <= ratio) {
					List<Transition> transAux = new ArrayList<Transition>(transitions);
					transAux.remove(t);
					
					List<Node> nodesAux = new ArrayList<Node>(nodes);
					nodesAux.remove(t.getSource());
					Node newSource = new Node(t.getSource());
					newSource.removeTransition(t);
					nodesAux.add(newSource);
					Transition newTransition = new Transition(newSource, n, t.getInput(), t.getOutput());
					newSource.addTransition(newTransition);
					
					transAux.add(newTransition);
					FSM mutant = new FSM(nodes, transAux, inputs, outputs, initialNode);
						mutants.add(mutant);
				}
			}
		}
		return mutants;
	}

	public List<FSMTest> generateTests(int profMax, int numTests) {
		List<FSMTest> tests = new ArrayList<FSMTest>();
		Random rnd = new Random();
		for(int numTest = 0; numTest < numTests; numTest++) {
			int profTest = 0;
			List<String> ins = new ArrayList<String>();
			List<String> outs = new ArrayList<String>();
			Node actualNode = initialNode;
			while (rnd.nextDouble() > 0.75/profMax && profTest <= profMax) {
				Transition tran = actualNode.getTransition(rnd.nextInt(inputs.size()));
				ins.add(tran.getInput());
				outs.add(tran.getOutput());
				actualNode = tran.getTarget();
				profTest++;
			}
			tests.add(new FSMTest(numTest, ins, outs));
		}
		return tests;
	}

	public String toFile() {
		String file = nodes.size() + "\n" + transitions.size() + "\n";
		for(int i = 0; i < transitions.size(); i++) {
			file += transitions.get(i).toString();
			file += i == transitions.size() - 1 ? "" : "\n";
		}
		return file;
	}
	
	public String toString() {
		return initialNode.toString() + "\t" + transitions.toString();
	}
}
