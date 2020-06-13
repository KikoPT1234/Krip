package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.BuiltInFunctionValue;
import pt.kiko.krip.lang.values.StringValue;
import pt.kiko.krip.lang.values.Value;

import java.util.Collections;

public class ErrorFunc extends BuiltInFunctionValue {

	static {
		Krip.registerValue("Error", new ErrorFunc());
	}

	public ErrorFunc() {
		super("Error", Collections.singletonList("details"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		Value<?> details = context.symbolTable.get("details");

		if (!(details instanceof StringValue)) return invalidType(details, context);

		return result.failure(new RuntimeError(startPosition, endPosition, details.getValueString(), context));
	}
}
