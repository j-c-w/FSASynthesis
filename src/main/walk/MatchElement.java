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
		// TODO --- Properly compute this shit.
		return MatchType.EXACT_MATCH;
	}

	public MatchType matchType() {
		return matchType;
	}

	public PCREParser.ElementContext tree() {
		return elt;
	}
}
