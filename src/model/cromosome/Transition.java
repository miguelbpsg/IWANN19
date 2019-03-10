package model.cromosome;

public class Transition {
	private String input;
	private String output;
	private Node source;
	private Node target;
	
	public Transition(Node source, Node target, String input, String output) {
		this.input = input;
		this.output = output;
		this.source = source;
		this.target = target;
	}
	
	public Transition(Transition t) {
		this.input = t.getInput();
		this.output = t.getOutput();
		this.source = t.getSource();
		this.target = t.getTarget();
	}
	
	public String getInput() {
		return input;
	}
	
	public String getOutput() {
		return output;
	}
	
	public Node getSource() {
		return source;
	}
	
	public Node getTarget() {
		return target;
	}
	
	@Override
	public String toString() {
		return source.getId() +" " + target.getId() + " " + input + " " + output;
	}
}
