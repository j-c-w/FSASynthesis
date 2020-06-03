package main.walk;

import main.parser.PCREParser;

public class MatchElement {
	MatchType matchType;
	PCREParser.ElementContext elt;

	Integer minRepeats;
	Integer maxRepeats;

	boolean isOptional = false;

	public MatchElement(PCREParser.ElementContext elt) {
		this.elt = elt;
		matchType = computeMatchType();
	}

	public int getMinRepeats() {
		return minRepeats;
	}

	public int getMaxRepeats() {
		return maxRepeats;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void printSizes() {
		System.out.print("Min: ");
		System.out.print(minRepeats);
		System.out.print(", Max: ");
		System.out.println(maxRepeats);
	}

	private MatchType computeMatchType() {
		// Element has a type that is <some stuff (atom)> <quantifier>
		// And what matters here is the type of quantifier.
		// The type of the atom type also matters a bit for
		// the ALTERNATIVE_MATCH, but I'm not sure that
		// this is the best way to handle that.  For now
		// that's ignored.
		PCREParser.QuantifierContext quantifier = elt.quantifier();

		if (quantifier == null) {
			minRepeats = 1;
			maxRepeats = 1;

			return MatchType.EXACT_MATCH;
		} else {
			String text = quantifier.getText();
			if (text.endsWith("?")) {
				isOptional = true;
				text = text.substring(0, text.length() - 1);
			}
			if (text.startsWith("{")) {
				// Do a size parsing.
				// Actually, you can have something like
				// {n, m}+, but that's unsupported right now.
				assert(!text.endsWith("+"));

				String rangeText = text.substring(1, text.length() - 1);
				String[] rangeElements = rangeText.split(",");

				if (rangeElements.length == 1) {
					// Fixed range.
					minRepeats = Integer.parseInt(rangeElements[0]);
					maxRepeats = minRepeats;
					return MatchType.FIXED_NUMBER;
				} else {
					// Can be either n,m or n,
					minRepeats = Integer.parseInt(rangeElements[0]);

					if (rangeElements[1] == "") {
						// This really should mean infinity.
						maxRepeats = Integer.MAX_VALUE;
					} else {
						maxRepeats = Integer.parseInt(rangeElements[1]);
					}

					return MatchType.MATCH_RANGE;
				}

				// Now, there is one of 
			} else {
				switch(text) {
					case "+":
						minRepeats = 1;
						maxRepeats = Integer.MAX_VALUE;
						return MatchType.PLUS_MATCH;
					case "*":
						minRepeats = 0;
						maxRepeats = Integer.MAX_VALUE;
						return MatchType.STAR_MATCH;
					case "": // This can happen if we get rid of#
						// bits and peices.
						minRepeats = 1;
						maxRepeats = 1;
						return MatchType.EXACT_MATCH;
					default:
						minRepeats = 0;
						maxRepeats = 0;
						return MatchType.UNSUPPORTED;
				}
			}
		}
	}

	public MatchType matchType() {
		return matchType;
	}

	public PCREParser.ElementContext tree() {
		return elt;
	}
}
