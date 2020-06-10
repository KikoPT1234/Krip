package pt.kiko.krip.lang.results;

import pt.kiko.krip.lang.errors.Error;
import pt.kiko.krip.lang.values.Value;

/**
 * Represents the result of the executed code
 */

public class RunResult {

	/**
	 * The error, if any
	 */
	public Error error;

	/**
	 * The value, if any
	 */
	public Value<?> value;

	/**
	 * Assigns the value parameter to the value field
	 *
	 * @param value The value to assign
	 * @return The instance
	 */
	public RunResult success(Value<?> value) {
		this.value = value;
		return this;
	}

	/**
	 * Assigns the error parameter to the error field
	 *
	 * @param error The error to assign
	 * @return The instance
	 */
	public RunResult failure(Error error) {
		this.error = error;
		return this;
	}
}
