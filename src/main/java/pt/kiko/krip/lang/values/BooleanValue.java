package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

public class BooleanValue extends Value<Boolean> {

	public BooleanValue(boolean value, Context context) {
		super(context);
		this.value = value;
	}

	@Override
	public String getValue() {
		return Boolean.toString(value);
	}

	@Override
	public void setValue(Boolean value) {
		this.value = value;
	}

	@Override
	public Value<Boolean> copy() {
		return new BooleanValue(value, context).setPosition(startPosition, endPosition);
	}

	@Override
	public boolean isTrue() {
		return value;
	}

	@Override
	public RuntimeResult equal(Value<?> other) {
		if (other instanceof ListValue) {
			return new RuntimeResult().success(new BooleanValue(getValue().equals(other.getValue()), context));
		} else return new RuntimeResult().success(new BooleanValue(false, context));
	}

	@Override
	public RuntimeResult notEquals(Value<?> other) {
		if (other instanceof ListValue) {
			return new RuntimeResult().success(new BooleanValue(!getValue().equals(other.getValue()), context));
		} else return new RuntimeResult().success(new BooleanValue(true, context));
	}

	@Override
	public void makePrototype() {

	}
}
