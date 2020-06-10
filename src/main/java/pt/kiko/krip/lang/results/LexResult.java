package pt.kiko.krip.lang.results;

import pt.kiko.krip.lang.Token;
import pt.kiko.krip.lang.errors.Error;

import java.util.ArrayList;

/**
 * The result of making tokens
 */
public class LexResult {

	/**
	 * The error, if any
	 */
	public Error error;

	/**
	 * The list of tokens, if any
	 */
	public ArrayList<Token> list;

	/**
	 * Assigns the list parameter to the list field
	 *
	 * @param list The list to assign
	 * @return The instance
	 */
	public LexResult success(ArrayList<Token> list) {
		this.list = list;
		return this;
	}

	/**
	 * Assigns the error parameter to the error field
	 *
	 * @param error The value to assign
	 * @return The instance
	 */
	public LexResult failure(Error error) {
		this.error = error;
		return this;
	}

}
