package pt.kiko.krip.lang.results;

import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.errors.Error;
import pt.kiko.krip.lang.nodes.Node;

/**
 * Represents the result of the parser
 *
 * @see pt.kiko.krip.lang.Parser
 */
public class ParseResult {

	/**
	 * The node, if any
	 */
	public Node node;

	/**
	 * The error, if any
	 */
	public Error error;

	/**
	 * The number of tokens advanced since the creation of the instance
	 */
	public int advanceCount = 0;

	/**
	 * The number of tokens to reverse
	 */
	public int toReverseCount = 0;

	/**
	 * Registers an advancement, incrementing the advanceCount field
	 */
	public void registerAdvancement() {
		advanceCount++;
	}

	/**
	 * Registers another ParseResult instance
	 *
	 * @param result The ParseResult to register
	 * @return The node field of the ParseResult instance
	 */
	public Node register(@NotNull ParseResult result) {
		advanceCount += result.advanceCount;
		if (result.error != null) error = result.error;
		return result.node;
	}

	/**
	 * Tries to register another ParseResult instance, changing the toReverseCount field if there's an error
	 *
	 * @param result The ParseResult to register
	 * @return null if there's an error, the node field of the ParseResult instance otherwise
	 * @see ParseResult#register(ParseResult)
	 */
	public Node tryRegister(@NotNull ParseResult result) {
		if (result.error != null) {
			toReverseCount = result.advanceCount;
			return null;
		}
		return register(result);
	}

	/**
	 * Assigns the node parameter to the node field
	 *
	 * @param node The node to assign
	 * @return The instance
	 */
	public ParseResult success(Node node) {
		this.node = node;
		return this;
	}

	/**
	 * Assigns the error parameter to the error field
	 *
	 * @param error The error to assign
	 * @return The instance
	 */
	public ParseResult failure(Error error) {
		this.error = error;
		return this;
	}

}
