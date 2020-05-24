{ pkgs ? import<nixpkgs> {} }:

with pkgs;

mkShell {
	buildInputs = [ python38 python38Packages.antlr4-python3-runtime antlr4 jdk gradle ];
	SHELL_NAME = "RegexSynthesis";
}
