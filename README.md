This is a tool designed to enable the generation of conversion machines
capable of converting between different regular expressions.


To build, get Nix, and run nix-shell in this directory, then run gradle build.
If you don't want to get nix, look in the shell.nix file and extract
the examples needed.

#Running

After entering a nix-shell (using `nix-shell` in the TLD),
or installing all the prereqs in the shell.nix file, run:

> gradle run --args "<RegexToCompileTo> <RegexToCompileFrom> [other flags]"

#Testing

After installing and entering a nix-shell (as above),
do

> gradle test
