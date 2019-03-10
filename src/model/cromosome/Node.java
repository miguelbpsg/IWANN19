package model.cromosome;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private int id;
	private List<Transition> transitions;
	
	public Node(int id) {
		this.id = id;
		this.transitions = new ArrayList<Transition>();
	}
	
	public Node(Node n) {
		this.id = n.getId();
		this.transitions = new ArrayList<Transition>();
		for(Transition t : n.getTransitions()) {
			transitions.add(new Transition(this,t.getTarget(),t.getInput(),t.getOutput()));	
		}
	}
	
	public int getId() {
		return id;
	}
	
	public List<Transition> getTransitions() {
		return transitions;
	}
	
	public Transition getTransition(int pos) {
		return transitions.get(pos);
	}
	
	public Transition getTransition(String input) {
		for(Transition t : transitions) {
			if(t.getInput().equals(input))
				return t;
		}
		System.err.println("Transition for input "+input+" at node "+toString()+ " not found");
		return null;
	}
	
	public void removeTransition(Transition t) {
		transitions.remove(t);
	}
	
	public void addTransition(Transition t) {
		transitions.add(t);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Node) {
			if (id == ((Node) o).getId() && transitions.containsAll(((Node) o).getTransitions()) && ((Node) o).getTransitions().containsAll(transitions))
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Node " + id;
	}
}
