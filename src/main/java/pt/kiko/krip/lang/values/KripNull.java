package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

public class KripNull extends KripValue<String> {

	public KripNull(Context context) {
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
	public KripValue<String> copy() {
		return new KripNull(context).setPosition(startPosition, endPosition);
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		return new RuntimeResult().success(new KripBoolean(other instanceof KripNull, context));
	}

	@Override
	public void makePrototype() {

	}

	@Override
	public String getType() {
		return "null";
	}
}
