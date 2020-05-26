package main;

import main.parser.*;
import main.FSA.FSA;
import main.generator.SingleStateGenerator;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;


public class Main {
	public static void main(String[] args) {
		String convertTo = args[0];
		String convertFrom = args[1];

		// Parse the individual regular expressions.
		ParseTree to = parse(convertTo);
		ParseTree from = parse(convertFrom);

		// Then build a SingleStateMachine conversion.
		SingleStateGenerator generator = new SingleStateGenerator(from, to);

		// Generate the generator.
		FSA fsa = generator.generate();
		if (fsa == null) {
			System.out.println("Failed to synthesize, error message");
			System.out.println(generator.getDiagnostic());
		} else {
			System.out.println("Successfully synthesized!");
			fsa.print();
		}
	}

	public static ParseTree parse(String contents) {
		PCRELexer lexer = new PCRELexer(CharStreams.fromString(contents));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PCREParser parser = new PCREParser(tokens);

		return parser.parse();
	}
}
