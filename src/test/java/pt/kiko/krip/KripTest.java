package pt.kiko.krip;

import org.junit.jupiter.api.Test;
import pt.kiko.krip.lang.results.RunResult;
import pt.kiko.krip.lang.values.ListValue;

import static org.junit.jupiter.api.Assertions.*;

class KripTest {

	@Test
	void run() {
		RunResult result = Krip.run("const b = {}; function helloWorld(a) {b.a = a; b.b = [a, 5]; b.c = b.a + b.b[1]; return b}; helloWorld(5)", "test");
		assertNull(result.error);
		assertNotNull(result.value);

		assertEquals(result.value.toString(), "[{\n" +
				"  a: 5,\n" +
				"  b: [5, 5],\n" +
				"  c: 10\n" +
				"}, <function helloWorld>, {\n" +
				"  a: 5,\n" +
				"  b: [5, 5],\n" +
				"  c: 10\n" +
				"}]");
		System.out.println(result.value.toString());
	}
}