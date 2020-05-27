package main;

import main.parser.*;
import main.FSA.FSA;
import main.generator.SingleStateGenerator;
import main.generator.Generator;
import main.utils.FileUtils;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.io.File;


public class Main {
	// Changed via arguments.
	static String output_folder = "output";

	public static void main(String[] args) {
		// Parse the arguments.
		Queue<String> argsQueue = new LinkedList<String>();
		for (String arg : args) {
			argsQueue.add(arg);

			if (arg == "-h" || arg == "--help") {
				System.out.println("Usage: Main (-f file ) ( -e <expr1> <expr2>) [-o output folder]");
				return;
			}
		}

		String filename = null, to = null, from = null;
		boolean from_filename = false, from_string = false;

		while (argsQueue.size() > 0) {
			String top = argsQueue.remove();
			switch(top) {
				case "-f":
					from_filename = true;
					filename = argsQueue.remove();
					break;
				case "-e":
					from_string = true;
					to = argsQueue.remove();
					from = argsQueue.remove();
					break;
				case "-o":
					output_folder = argsQueue.remove();
					break;
				default:
					System.out.println("Unknown flag " + top);
					return;
			}
		}

		File outputFolderFile = new File(output_folder);
		// Clear the output folder.
		FileUtils.deleteDir(outputFolderFile);
		outputFolderFile.mkdirs();

		if (from_filename) {
			compileFilename(filename);
		}

		if (from_string) {
			convertStrings(to, from);
		}
	}

	public static void compileFilename(String filename) {
		System.out.println("Running from filename" + filename);
		// We assume that the filename is of the format
		// in ANMLZoo -- that is, each line is a different
		// regex.  There are characters denoting the start
		// and end of each regex ('/').

		ArrayList<String> lines = FileUtils.readLines(filename);
		// Convert out of ANMLZoo format.
		for (int i = 0; i < lines.size(); i ++) {
			String regex = lines.get(i);
			lines.set(i, regex.substring(1, regex.length() - 1));
			System.out.println(regex.substring(1, regex.length() - 1));
		}

		// Compile between each pair of strings.
		for (int i = 0; i < lines.size(); i ++) {
			String[] messages = new String[lines.size()];
			FSA[] machines = new FSA[lines.size()];

			String compileTo = lines.get(i);
			System.out.println("Compiling to " + Integer.toString(i) + "(/" + Integer.toString(lines.size()) + ")");
			for (int j = 0; j < lines.size(); j ++) {
				if (i == j)
					continue;

				String compileFrom = lines.get(j);

				// Get the generator:
				Generator generator = compile(compileTo, compileFrom);

				// And get the FSA if it exists.
				FSA fsa = generator.generate();
				if (fsa == null) {
					messages[j] = generator.getDiagnostic();
				} else {
					messages[j] = "Success!";
				}
				machines[j] = fsa;
			}

			StringBuilder contents = new StringBuilder();
			for (int j = 0; j < messages.length; j ++) {
				contents.append("Compiling to pattern " + Integer.toString(i + 1) + " from pattern " + Integer.toString(j + 1) + " resulted in: " + messages[j] + "\n");
			}

			// Now, write everything out to some files.
			File contentsFile = new File(output_folder + "/RESULTS_REGEX_" + Integer.toString(i + 1));
			FileUtils.write(contentsFile, contents.toString());
		}

		// TODO -- Write the FSAs out to a file.
	}

	public static Generator compile(String toStr, String fromStr) {
		return compileSingleState(toStr, fromStr);
	}

	public static Generator compileSingleState(String toStr, String fromStr) {
		// Parse the individual regular expressions.
		ParseTree to = parse(toStr);
		ParseTree from = parse(fromStr);

		// Then build a SingleStateMachine conversion.
		SingleStateGenerator generator = new SingleStateGenerator(from, to);
		return generator;
	}

	public static void convertStrings(String convertTo, String convertFrom) {
		Generator generator = compile(convertTo, convertFrom);

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
