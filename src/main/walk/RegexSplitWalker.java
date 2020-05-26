package main.walk;

import org.antlr.v4.runtime.ParserRuleContext;
import java.util.ArrayList;

import main.parser.PCREBaseListener;
import main.parser.PCREParser;

public class RegexSplitWalker extends PCREBaseListener {
	// Used for tracking elements to ensure that we are only
	// putting top-level elements in the list.
	PCREParser.ElementContext currentElement = null;
	// Used for storing elements.
	ArrayList<MatchElement> elementList = new ArrayList<MatchElement>();

	public ArrayList<MatchElement> getElementList() {
		return elementList;
	}

	public void reset() {
		elementList = new ArrayList<MatchElement>();
	}

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		System.out.println("Entered rule " + ctx.getText());
	}

	@Override
	public void enterElement(PCREParser.ElementContext ctx) {
		// We expect this to be an in-order walk, so
		// put this elemnt on the list provided that it
		// is a top level element.

		if (currentElement == null) {
			elementList.add(new MatchElement(ctx));
			currentElement = ctx;
		} else {
			// TODO -- Some debug information would be nice.
			// Maybe this is an error?  Not sure.
		}
	}

	@Override
	public void exitElement(PCREParser.ElementContext ctx) {
		// Enable adding to the list again if we are
		// exiting the top-level element.
		if (ctx == currentElement) {
			currentElement = null;
		}
	}
}
