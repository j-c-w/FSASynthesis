package main.FSA;

import java.util.ArrayList;

import main.FSA.State;

public class FSA {
	ArrayList<State> states;
	int startState;

	public FSA() {
		states = new ArrayList<State>();
		startState = -1;
	}

	public void addState(State state) {
		states.add(state);
	}

	public void setStartIndex(int ind) {
		this.startState = ind;
	}

	public void print() {
		for (State state : states) {
			state.print();
		}
	}
}
