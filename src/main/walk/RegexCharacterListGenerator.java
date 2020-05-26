package main.walk;

import java.util.ArrayList;

import main.parser.PCREBaseListener;
import main.parser.PCREParser;
import main.parser.RangeParser;

import org.antlr.v4.runtime.tree.TerminalNode;

public class RegexCharacterListGenerator extends PCREBaseListener {
	ArrayList<char[]> characterList = new ArrayList<char[]>();
	// This is for keeping track of tokens we encounter
	// in character class contexts.
	ArrayList<Character> characterClassChars;

	private PCREParser.Character_classContext inCharClass;

	public int getLength() {
		return characterList.size();
	}

	public char[] getValidCharactersAtIndex(int index) {
		return characterList.get(index);
	}

	public void reset() {
		characterList = new ArrayList<char[]>();
	}

	@Override
	public void enterCharacter_class(PCREParser.Character_classContext cts) {
		if (inCharClass == null) {
			inCharClass = cts;
			characterClassChars = new ArrayList<Character>();
		} else {
			throw new RuntimeException("Unexpected nested character class");
		}
	}

	@Override
	public void exitCharacter_class(PCREParser.Character_classContext cts) {
		if (inCharClass == cts) {
			inCharClass = null;
			char[] chrs = RangeParser.parse(characterClassChars);
			characterList.add(chrs);
		}
	}

	@Override
	public void visitTerminal(TerminalNode node) {
		// If we have entered shared_literal and not added this
		if (inCharClass == null) {
			// throw new RuntimeException("Unexpected Terminal node " + node);
			// Hmm..  not sure what to do here right now.  Maybe add
			// to list.  Probably we shouldn't ignore it...
		} else {
			String contents = node.getText();
			assert(contents.length() == 0);
			char character = contents.charAt(0);
			if (character == '-') {
				characterClassChars.add(character);
			}
		}
	}

	@Override
	public void enterShared_literal(PCREParser.Shared_literalContext ctx) {
		String contents = ctx.getText();
		assert(contents.length() == 1); // If this is more than one
		// char, not actually sure what it is.  Maybe an escp code?
		// Need to handle if so.

		if (inCharClass != null) {
			// We are in a character class.  So, we should define
			// the list as such.
			characterClassChars.add(contents.charAt(0));
		} else {
			// This is a char on its own.
			char[] chrs = new char[] {contents.charAt(0)};

			characterList.add(chrs);
		}
	}

	@Override
	public void enterOctal_char(PCREParser.Octal_charContext ctx) {
		// TODO
		System.out.println("Found octal characters");
		assert(false); // Not implemented
	}

	@Override
	public void enterOctal_digit(PCREParser.Octal_digitContext ctx) {
		// TODO
		System.out.println("Found digit characters");
		assert(false); // Not implemented
	}
}
