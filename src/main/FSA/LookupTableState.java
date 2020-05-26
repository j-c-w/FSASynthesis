package main.FSA;

public class LookupTableState extends State {
	int state_id;
	char[] outputs;
	State[] next_states;

	// Create a state that has a mapper to certain outputs.
	// The `State` array maps this state to the next state.
	// If there are null entries in the next_state array,
	// the state remains the same.  If the access to the next
	// state array would be out of bounds, the state remains
	// the same.
	public LookupTableState(char[] outputs, State[] next_state) {
		this.outputs = outputs;
		this.next_states = next_state;

		state_id = (outputs.hashCode() + next_state.hashCode()) % 1000;
	}

	// We can also create a state where the default action is
	// to always stay in the same state.
	public LookupTableState(char[] outputs) {
		this(outputs, new State[0]);
	}

	public char transition_output(char input) {
		return this.outputs[input];
	}

	public State transition_state(char input) {
		State result;
		if ((int) input < next_states.length) {
			result = next_states[input];
		} else {
			result = null;
		}

		if (result == null) {
			return this;
		} else {
			return result;
		}
	}

	public void print() {
		System.out.println(this.toString());
	}

	public int getID() {
		return this.state_id;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 256; i ++) {
			if (outputs[i] == (char) i) {
				continue;
			}
			builder.append((char) i);
			builder.append("| ");
			builder.append(outputs[i]);
			builder.append(", ");
			State next = transition_state((char) i);
			if (next == null) {
				builder.append("null");
			} else {
				builder.append(next.getID());
			}
			builder.append("\n");
		}

		return "State " + Integer.toString(state_id) + ":\n" +
			builder.toString();
	}
}
