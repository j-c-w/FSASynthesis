package main.FSA;

abstract class State {
	abstract public char transition_output(char input);
	abstract public State transition_state(char input);
	// Doesn't need to be unique, but should probably be
	// unique if possible -- only used for pretty printing
	// right now.
	abstract public int getID();

	abstract public void print();
}
