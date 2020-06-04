package pt.kiko.krip.lang.results;

import pt.kiko.krip.lang.errors.Error;
import pt.kiko.krip.lang.values.Value;

public class RuntimeResult {

	public Value value;
	public Error error;
	public Value funcReturnValue;
	public boolean loopShouldContinue = false;
	public boolean loopShouldBreak = false;

	public void reset() {
		value = null;
		error = null;
		funcReturnValue = null;
		loopShouldContinue = false;
		loopShouldBreak = false;
	}

	public Value register(RuntimeResult result) {
		if (result.shouldReturn()) error = result.error;
		funcReturnValue = result.funcReturnValue;
		loopShouldContinue = result.loopShouldContinue;
		loopShouldBreak = result.loopShouldBreak;
		return result.value;
	}

	public RuntimeResult success(Value value) {
		reset();
		this.value = value;
		return this;
	}

	public RuntimeResult successReturn(Value value) {
		reset();
		this.funcReturnValue = value;
		return this;
	}

	public RuntimeResult successContinue() {
		reset();
		loopShouldContinue = true;
		return this;
	}

	public RuntimeResult successBreak() {
		reset();
		loopShouldBreak = true;
		return this;
	}

	public RuntimeResult failure(Error error) {
		reset();
		this.error = error;
		return this;
	}

	public boolean shouldReturn() {
		return (this.error != null || this.funcReturnValue != null || this.loopShouldContinue || this.loopShouldBreak);
	}

}
