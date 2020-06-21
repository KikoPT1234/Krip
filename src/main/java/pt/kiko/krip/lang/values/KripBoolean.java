package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

public class KripBoolean extends KripValue<Boolean> {

	public KripBoolean(boolean value, Context context) {
		super(context);
		this.value = value;
	}

	@Override
	public String getValueString() {
		return Boolean.toString(value);
	}

	@Override
	public void setValue(Boolean value) {
		this.value = value;
	}

	@Override
	public KripValue<Boolean> copy() {
		return new KripBoolean(value, context).setPosition(startPosition, endPosition);
	}

	@Override
	public boolean isTrue() {
		return value;
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof KripBoolean) {
			return new RuntimeResult().success(new KripBoolean(value == other.getValue() || value.equals(other.getValue()), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}

	@Override
	public void makePrototype() {

	}

	@Override
	public String getType() {
		return "boolean";
	}
}
