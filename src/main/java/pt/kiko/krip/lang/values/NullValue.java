package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

public class NullValue extends Value {

	public NullValue(Context context) {
		super(context);
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public boolean isTrue() {
		return false;
	}

	@Override
	public Value copy() {
		return new NullValue(context).setPosition(startPosition, endPosition);
	}

	@Override
	public RuntimeResult equal(Value other) {
		return new RuntimeResult().success(new BooleanValue(other instanceof NullValue, context));
	}

	@Override
	public RuntimeResult notEquals(Value other) {
		return new RuntimeResult().success(new BooleanValue(!(other instanceof NullValue), context));
	}

	@Override
	public RuntimeResult plus(Value other) {
		if (other instanceof StringValue) return new RuntimeResult().success(new StringValue(getValue() + other.getValue(), context));
		else return illegalOperation(other);
	}
}
