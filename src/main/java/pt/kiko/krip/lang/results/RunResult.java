package pt.kiko.krip.lang.results;

import pt.kiko.krip.lang.errors.Error;
import pt.kiko.krip.lang.values.Value;

public class RunResult {

	public Error error;
	public Value<?> value;

	public RunResult success(Value<?> value) {
		this.value = value;
		return this;
	}

	public RunResult failure(Error error) {
		this.error = error;
		return this;
	}
}
