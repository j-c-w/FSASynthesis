package main.utils;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.nio.file.Files;

public class FileUtils {
	public static ArrayList<String> readLines(String filename) {
		BufferedReader reader;
		ArrayList<String> contents = new ArrayList<String>();

		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			while (line != null) {
				contents.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return contents;
	}

	public static void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				if (!Files.isSymbolicLink(f.toPath())) {
					deleteDir(f);
				}
			}
		}
		file.delete();
	}

	public static void write(File file, String contents) {
		try {
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.write(contents);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
