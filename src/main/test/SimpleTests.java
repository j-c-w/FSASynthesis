package main.test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import main.FSA.FSA;
import main.Main;

public class SimpleTests {
	@Test
	public void CheckCompileEquality() {
		FSA result;
		result = Main.compileSingleState("a", "b").generate();
		assertNotEquals("Compilation must succeed", result, null);

		result = Main.compileSingleState("a+", "[a-z]+").generate();
		assertNotEquals("Compilation must succeed", result, null);

		result = Main.compileSingleState("a*", "[a-z]+").generate();
		assertEquals("Compilation must not succeed", result, null);

		result = Main.compileSingleState("a*", "[a-z]+").generate();
		assertEquals("Compilation must not succeed", result, null);

		result = Main.compileSingleState("ab", "[a-z]").generate();
		assertEquals("Compilation must not succeed", result, null);

		result = Main.compileSingleState("ab", "[a-z]").generate();
		assertEquals("Compilation must not succeed", result, null);
	}

	@Test
	public void CheckNegatedRanges() {
		FSA result;
		result = Main.compileSingleState("xy", "[^a][b-z]").generate();
		assertEquals("Compilation must not succeed", result, null);
	}
}
