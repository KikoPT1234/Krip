package pt.kiko.krip.lang.results;

import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.cases.Cases;
import pt.kiko.krip.lang.errors.SyntaxError;
import pt.kiko.krip.lang.nodes.Node;

/**
 * The result of parsing an if/else if/else block
 */
public class CaseResult {

	/**
	 * The error, if any
	 */
	public SyntaxError error;

	/**
	 * The cases, if any
	 */
	public Cases cases;

	/**
	 * The number of tokens advanced since the creation of this instance
	 */
	public int advanceCount = 0;

	/**
	 * Registers an advancement, incrementing the advanceCount field
	 */
	public void registerAdvancement() {
		advanceCount++;
	}

	/**
	 * Registers another CaseResult instance
	 *
	 * @param result The CaseResult to register
	 * @return The cases field of the result
	 */
	public Cases register(@NotNull CaseResult result) {
		advanceCount += result.advanceCount;
		if (result.error != null) error = result.error;
		return result.cases;
	}

	/**
	 * Registers a ParseResult instance
	 *
	 * @param result The ParseResult to register
	 * @return The node field of the result
	 * @see ParseResult
	 */
	public Node register(@NotNull ParseResult result) {
		advanceCount += result.advanceCount;
		if (result.error != null) error = result.error;
		return result.node;
	}

	/**
	 * Assigns the cases parameter to the cases field
	 *
	 * @param cases The Cases instance to assign
	 * @return The instance
	 */
	public CaseResult success(Cases cases) {
		this.cases = cases;
		return this;
	}

	/**
	 * Assigns the error parameter to the error field
	 *
	 * @param error The value to assign
	 * @return The instance
	 */
	public CaseResult failure(SyntaxError error) {
		this.error = error;
		return this;
	}

}
