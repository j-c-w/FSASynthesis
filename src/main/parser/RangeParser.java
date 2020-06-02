package main.parser;

import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.tree.TerminalNode;

public class RangeParser {
	public static char[] parse_nodes(List<TerminalNode> items) {
		for (TerminalNode item : items) {
			System.out.println(item);
			System.out.println(item.getText());
		}

		assert(false); // Not implemented yet.
		return new char[] {};
	}

	public static char[] parse(List<Character> items) {
		Stack<Character> stack = new Stack<Character>();
		boolean negate = false;
		for (int i = 0; i < items.size(); i ++) {
			Character item = items.get(i);
			// ^ means negate if at the start of a regular
			// expression.
			if (item == '^' && i == 0) {
				negate = true;
			// handle character ranges.
			} else if (item == '-' && stack.size() > 0 && i < items.size() - 1) {
				Character rangeStart = stack.pop();
				// Get the end character for the range.
				i += 1;
				Character rangeEnd = items.get(i);

				// Compute the range:
				for (int j = (int) rangeStart; j <= (int)rangeEnd; j ++) {
					stack.push((char) j);
				}
			} else {
				stack.push(item);
			}
		}

		// Now, convert this to a character array.
		if (negate) {
			// Elements NOT in the stack are the ones to return.
			char[] result = new char[256 - stack.size()];

			int insert_index = 0;
			for (int i = 0; i < 256; i ++) {
				if (! stack.contains((char) i)) {
					result[insert_index] = (char) i;
					insert_index += 1;
				}
			}

			return result;
		} else {
			// Elements in the stack are the ones to return.
			char[] result = new char[stack.size()];

			for (int i = 0; i < result.length; i ++) {
				result[i] = stack.pop();
			}

			return result;
		}
	}
}
