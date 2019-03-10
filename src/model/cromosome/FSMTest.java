package model.cromosome;

import java.util.ArrayList;
import java.util.List;

public class FSMTest {
	private int id;
	private List<String> inputs;
	private List<String> outputs;
	
	public FSMTest(int id, List<String> inputs, List<String> outputs) {
		this.id = id;
		this.inputs = inputs;
		this.outputs = outputs;
		if (inputs.size() != outputs.size())
			System.err.println("There are not as many inputs as outputs at test " + id);
	}
	
	public FSMTest(FSMTest t) {
		this.id = t.getId();
		this.inputs = t.copyInputs();
		this.outputs = t.copyOutputs();
	}
	
	public int killMutant(FSM mutant) {
		Node node = mutant.getInitialNode();
		Transition t;
		for(int i = 0; i < inputs.size(); i++) {
			t = node.getTransition(inputs.get(i));
			if(outputs.get(i).equals(t.getOutput()))
				node = t.getTarget();
			else return i;
		}
		return Integer.MAX_VALUE;
	}
	
	public int getId() {
		return id;
	}
	
	public List<String> getInputs() {
		return inputs;
	}

	public List<String> getOutputs() {
		return outputs;
	}
	
	public List<String> copyInputs() {
		List<String> copy = new ArrayList<String>();
		for(int i = 0; i < inputs.size(); i++) {
			copy.add(new String(inputs.get(i)));
		}
		return copy;
	}
	
	public List<String> copyOutputs() {
		List<String> copy = new ArrayList<String>();
		for(int i = 0; i < outputs.size(); i++) {
			copy.add(new String(outputs.get(i)));
		}
		return copy;
	}

	public int getSize() {
		return inputs.size();
	}
	
	public String toFile() {
		String file = "";
		for(int i = 0; i < inputs.size(); i++) {
			file += inputs.get(i);
			file += i == inputs.size() - 1 ? "\n" : " ";
		}
		for(int i = 0; i < outputs.size(); i++) {
			file += outputs.get(i);
			file += (i == outputs.size() - 1) ? "" : " ";
		}
		return file;
	}
	
	public String toString() {
		return "" + id;
	}
}
