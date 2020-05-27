package main.walk;

import main.parser.PCREParser;

public class MatchElement {
	MatchType matchType;
	PCREParser.ElementContext elt;

	public MatchElement(PCREParser.ElementContext elt) {
		this.elt = elt;
		matchType = computeMatchType();
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
			return MatchType.EXACT_MATCH;
		} else {
			String text = quantifier.getText();
			switch(text) {
				case "+":
					return MatchType.PLUS_MATCH;
				case "*":
					return MatchType.STAR_MATCH;
				default:
					return MatchType.UNSUPPORTED;
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
