package main.generator;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import main.walk.RegexSplitWalker;
import main.walk.RegexCharacterListGenerator;
import main.walk.MatchElement;
import main.walk.MatchType;
import main.FSA.FSA;
import main.FSA.LookupTableState;

public class SingleStateGenerator implements Generator {
	ParseTree to;
	ParseTree from;
	String diagnostic;

	public SingleStateGenerator(ParseTree from, ParseTree to) {
		this.to = to;
		this.from = from;
	}

	public FSA generate() {
		//  Generate a walker that splits the trees
		//  into arrays of sections, either finite
		//  or infinite.

		RegexSplitWalker walkerFrom = new RegexSplitWalker();
		RegexSplitWalker walkerTo = new RegexSplitWalker();

		// Do the walk to get the arrays.
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(walkerFrom, from);
		walker.walk(walkerTo, to);

		ArrayList<MatchElement> fromElementGroups = walkerFrom.getElementList();
		ArrayList<MatchElement> toElementGroups = walkerTo.getElementList();

		if (fromElementGroups.size() != toElementGroups.size()) {
			diagnostic = "Different number of element groups (" +
				Integer.toString(fromElementGroups.size()) + " vs " + Integer.toString(toElementGroups.size());
			// Different number of elements, so this algorithm
			// cant compute an FSA under this situation.
			return null;
		}

		// Then, create a lookup table.
		char[] lookuptable = new char[256];
		// Keep track if there are any overlaps in the assignments.
		int[] assigned = new int[256];
		for (char i = 0; i < 256; i ++) {
			lookuptable[i] = i;
			assigned[i] = 0;
		}

		for (int i = 0; i < fromElementGroups.size(); i ++) {
			// Get the character set for each item in the element
			// list.
			MatchElement fromEltGroup = fromElementGroups.get(i);
			MatchElement toEltGroup = toElementGroups.get(i);

			MatchType fromEltMatchType = fromEltGroup.matchType();
			MatchType toEltGroupMatchType = toEltGroup.matchType();

			if (fromEltMatchType != toEltGroupMatchType) {
				// We can't mesh these right now with this
				// style.  This is, e.g. one is [A-z]+ and
				// the other is [p-q]*.
				diagnostic = "Element match types " + fromEltMatchType.toString() + " and " + toEltGroupMatchType.toString() + " do not match";
				return null;
			}

			RegexCharacterListGenerator fromSet = new RegexCharacterListGenerator();
			// For the from set:
			walker.walk(fromSet, fromEltGroup.tree());

			// For the to set:
			RegexCharacterListGenerator toSet = new RegexCharacterListGenerator();
			walker.walk(toSet, toEltGroup.tree());

			// Now, make sure that (a) each element in fromSet points
			// to an element in toSet.  Also make sure that each
			// element in toSet points to something not in toSet.
			// The later makes sure that there are no false-positives.
			// E.g. if we are converting the regex 'bear' to 'read',
			// we need to make sure that the string "bead" is not
			// converted to a match.
			switch (fromEltMatchType) {
				case PLUS_MATCH:
				case STAR_MATCH:
				case EXACT_MATCH:
					// Go through each element in the from
					// set and map it to the to set.
					if (fromSet.getLength() != toSet.getLength()) {
						diagnostic = "Exact match regions of different lengths (" + Integer.toString(fromSet.getLength()) + " vs " + Integer.toString(toSet.getLength()) + ")";
						// TODO -- This case could be improved.
						// This is the case where we could have
						// a translation table with more than
						// one output for each input.
						return null;
					}

					for (int j = 0; j < fromSet.getLength(); j ++) {
						char[] valid_chars_from = fromSet.getValidCharactersAtIndex(j);
						char[] valid_chars_to = toSet.getValidCharactersAtIndex(j);

						for (char from: valid_chars_from) {
							if (lookuptable[from] != valid_chars_to[0]) {
								// Only increment the assigment counter if we actually
								// change the list entries.
								assigned[from] += 1;
								lookuptable[from] = valid_chars_to[0];
							}
						}
					}
					break;
				case ALTERNATIVES_MATCH:
					diagnostic = "Alternative matches (A|B) not supported";
					// TODO -- We don't handle this right now.
					return null;
			}
		}

		for (int i = 0; i < 256; i ++) {
			if (assigned[i] > 1) {
				diagnostic = "Lookup table index " + Integer.toString(i) + " assigned multiple times";
				return null;
			}
		}

		// Do this with a RegexCharacterListGenerator,
		// which for each section generates a list of characters
		// that are matched.

		diagnostic = "Successful run";
		LookupTableState lookuptableState = new LookupTableState(lookuptable);
		FSA fsa = new FSA();
		fsa.addState(lookuptableState);
		fsa.setStartIndex(0);
		return fsa;
	}

	public String getDiagnostic() {
		return diagnostic;
	}
}
