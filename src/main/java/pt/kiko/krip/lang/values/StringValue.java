package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

public class StringValue extends Value {

	protected final String value;

	public StringValue(String value, Context context) {
		super(context);
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public Value copy() {
		return new StringValue(value, context).setPosition(startPosition, endPosition);
	}

	@Override
	public RuntimeResult equal(Value other) {
		return new RuntimeResult().success(new BooleanValue(getValue().equals(other.getValue()), context));
	}

	@Override
	public RuntimeResult notEquals(Value other) {
		return new RuntimeResult().success(new BooleanValue(!getValue().equals(other.getValue()), context));
	}

	@Override
	public RuntimeResult plus(Value other) {
		return new RuntimeResult().success(new StringValue(getValue() + other.getValue(), context));
	}
}
