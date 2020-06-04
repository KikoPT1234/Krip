package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

public class BooleanValue extends Value {

	protected final boolean value;

	public BooleanValue(boolean value, Context context) {
		super(context);
		this.value = value;
	}

	@Override
	public String getValue() {
		return Boolean.toString(value);
	}

	@Override
	public Value copy() {
		return new BooleanValue(value, context).setPosition(startPosition, endPosition);
	}

	@Override
	public boolean isTrue() {
		return value;
	}

	@Override
	public RuntimeResult equal(Value other) {
		if (other instanceof ListValue) {
			return new RuntimeResult().success(new BooleanValue(getValue().equals(other.getValue()), context));
		} else return new RuntimeResult().success(new BooleanValue(false, context));
	}

	@Override
	public RuntimeResult notEquals(Value other) {
		if (other instanceof ListValue) {
			return new RuntimeResult().success(new BooleanValue(!getValue().equals(other.getValue()), context));
		} else return new RuntimeResult().success(new BooleanValue(true, context));
	}

	@Override
	public RuntimeResult plus(Value other) {
		if (other instanceof StringValue) return new RuntimeResult().success(new StringValue(getValue() + other.getValue(), context));
		else return illegalOperation(other);
	}
}
