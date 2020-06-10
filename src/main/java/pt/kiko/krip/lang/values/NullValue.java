package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

public class NullValue extends Value<String> {

	public NullValue(Context context) {
		super(context);
	}

	@Override
	public String getValueString() {
		return null;
	}

	@Override
	public void setValue(String value) {
	}

	@Override
	public boolean isTrue() {
		return false;
	}

	@Override
	public Value<String> copy() {
		return new NullValue(context).setPosition(startPosition, endPosition);
	}

	@Override
	public RuntimeResult equal(Value<?> other) {
		return new RuntimeResult().success(new BooleanValue(other instanceof NullValue, context));
	}

	@Override
	public RuntimeResult notEquals(Value<?> other) {
		return new RuntimeResult().success(new BooleanValue(!(other instanceof NullValue), context));
	}

	@Override
	public void makePrototype() {

	}
}
