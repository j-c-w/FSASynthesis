package main.walk;

import java.util.ArrayList;

import main.parser.PCREBaseListener;
import main.parser.PCREParser;

public class RegexCharacterListGenerator extends PCREBaseListener {
	ArrayList<Character> characterList = new ArrayList<Character>();

	public ArrayList<Character> getCharacterList() {
		return characterList;
	}

	public void reset() {
		characterList = new ArrayList<Character>();
	}

	@Override
	public void enterCharacter_class(PCREParser.Character_classContext cts) {
		// TODO
		System.out.println("Found some characters");
	}

	@Override
	public void enterShared_literal(PCREParser.Shared_literalContext ctx) {
		// TODO
		System.out.println("Found some characters");
	}

	@Override
	public void enterOctal_char(PCREParser.Octal_charContext ctx) {
		// TODO
		System.out.println("Found some characters");
	}

	@Override
	public void enterOctal_digit(PCREParser.Octal_digitContext ctx) {
		// TODO
		System.out.println("Found some characters");
	}
}
