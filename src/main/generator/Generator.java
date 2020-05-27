package main.generator;

import main.FSA.FSA;

public interface Generator {
	public FSA generate();
	public String getDiagnostic();
}
