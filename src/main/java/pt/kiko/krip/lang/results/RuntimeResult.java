package pt.kiko.krip.lang.results;

import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.values.Value;

/**
 * Represents the result of the Interpreter
 *
 * @see pt.kiko.krip.lang.Interpreter
 */
public class RuntimeResult {

	/**
	 * The value, if any
	 */
	public Value<?> value;

	/**
	 * The error, if any
	 */
	public RuntimeError error;

	/**
	 * The value for a function to return, if any
	 */
	public Value<?> funcReturnValue;

	/**
	 * Whether a loop (if any) should continue
	 */
	public boolean loopShouldContinue = false;

	/**
	 * Whether a loop (if any) should break
	 */
	public boolean loopShouldBreak = false;

	/**
	 * Resets the variables
	 */
	public void reset() {
		value = null;
		error = null;
		funcReturnValue = null;
		loopShouldContinue = false;
		loopShouldBreak = false;
	}

	/**
	 * Registers another RuntimeResult instance
	 *
	 * @param result The RuntimeResult to register
	 * @return The value field of the RuntimeResult instance
	 */
	public Value<?> register(@NotNull RuntimeResult result) {
		if (result.shouldReturn()) error = result.error;
		funcReturnValue = result.funcReturnValue;
		loopShouldContinue = result.loopShouldContinue;
		loopShouldBreak = result.loopShouldBreak;
		return result.value;
	}

	/**
	 * Assigns the value parameter to the value field
	 *
	 * @param value The value to assign
	 * @return The instance
	 */
	public RuntimeResult success(Value<?> value) {
		reset();
		this.value = value;
		return this;
	}

	/**
	 * Assigns the funcReturnValue parameter to the funcReturnValue field
	 *
	 * @param value The value to assign
	 * @return The instance
	 */
	public RuntimeResult successReturn(Value<?> value) {
		reset();
		this.funcReturnValue = value;
		return this;
	}

	/**
	 * Sets the loopShouldContinue field to true
	 *
	 * @return The instance
	 */
	public RuntimeResult successContinue() {
		reset();
		loopShouldContinue = true;
		return this;
	}

	/**
	 * Sets the loopShouldBreak field to true
	 *
	 * @return The instance
	 */
	public RuntimeResult successBreak() {
		reset();
		loopShouldBreak = true;
		return this;
	}

	/**
	 * Assigns the error parameter to the error field
	 *
	 * @param error The error to assign
	 * @return The instance
	 */
	public RuntimeResult failure(RuntimeError error) {
		reset();
		this.error = error;
		return this;
	}

	/**
	 * Checks whether the result should be returned
	 *
	 * @return A boolean of whether the result should be returned or not
	 */
	public boolean shouldReturn() {
		return (this.error != null || this.funcReturnValue != null || this.loopShouldContinue || this.loopShouldBreak);
	}

}
