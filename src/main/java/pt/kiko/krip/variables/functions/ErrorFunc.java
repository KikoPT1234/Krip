package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.lang.values.KripValue;

import java.util.Collections;

public class ErrorFunc extends KripJavaFunction {

	static {
		Krip.registerValue("Error", new ErrorFunc());
	}

	public ErrorFunc() {
		super("Error", Collections.singletonList("details"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> details = context.symbolTable.get("details");

		if (!(details instanceof KripString)) return invalidType(details, context);

		return result.failure(new RuntimeError(startPosition, endPosition, details.getValueString(), context));
	}
}
