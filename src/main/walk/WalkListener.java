package main.walk;

import org.antlr.v4.runtime.ParserRuleContext;

import main.parser.PCREBaseListener;

public class WalkListener extends PCREBaseListener {
	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
		System.out.println("Entered rule " + ctx.getText());
	}
}
