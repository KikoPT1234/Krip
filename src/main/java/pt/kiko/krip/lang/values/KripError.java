package pt.kiko.krip.lang.values;

import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.Position;
import pt.kiko.krip.lang.errors.RuntimeError;

import java.util.HashMap;

public class KripError extends KripObject {

	public RuntimeError error;

	public KripError(@NotNull RuntimeError error, Position startPosition, Position endPosition, Context context) {
		super(new HashMap<>(), context);
		setPosition(startPosition, endPosition);
		this.error = error;

		value.put("details", new KripString(error.details, context));
	}

	@Override
	public String toString() {
		return error.toString();
	}
}
