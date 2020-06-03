package main.test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import main.FSA.FSA;
import main.Main;
import main.generator.SingleStateGenerator;

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

	@Test
	public void CheckBracketRanges() {
		FSA result;
		// TODO -- Fix these tests.
		// result = Main.compileSingleState("xx", "[abc]{2}").generate();
		// assertNotEquals("Compilation must succeed", result, null);

		// result = Main.compileSingleState("[abc]{1,10}", "x[xy]{1,9}").generate();
		// assertNotEquals("Compilation must succeed", result, null);

		// result = Main.compileSingleState("[abc][def]", "[123]{2}").generate();
		// assertNotEquals("Compilation must succeed", result, null);

		result = Main.compileSingleState("[abc]{1}?", "[123]?").generate();
		assertNotEquals("Compilation must succeed", result, null);

		// And some that should fail.
	}

	@Test
	public void DotTests() {
		FSA result;
		result = Main.compileSingleState(".", "a").generate();
		assertEquals("Compilation should fail", result, null);

		result = Main.compileSingleState("a", ".").generate();
		assertNotEquals("Compilation should succeed", result, null);

		result = Main.compileSingleState("[.]", "a").generate();
		assertNotEquals("Compilation should succeed", result, null);
	}

	@Test
	public void NegationTests() {
		FSA result;
		result = Main.compileSingleState("[^AB]", "A").generate();
		assertNotEquals("Compilation should succeed", result, null);

		result = Main.compileSingleState("xy", "[^a][bc]").generate();
		assertEquals("Compilation should fail", result, null);
	}
}
