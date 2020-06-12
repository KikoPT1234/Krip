package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.Position;
import pt.kiko.krip.lang.errors.RuntimeError;

import java.util.HashMap;

public class ErrorValue extends ObjectValue {

	public RuntimeError error;

	public ErrorValue(RuntimeError error, Position startPosition, Position endPosition, Context context) {
		super(new HashMap<>(), context);
		setPosition(startPosition, endPosition);
		this.error = error;

		value.put("details", new StringValue(error.details, context));
	}

	@Override
	public String toString() {
		return error.toString();
	}
}
