package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.BuiltInFunctionValue;
import pt.kiko.krip.lang.values.NullValue;
import pt.kiko.krip.lang.values.NumberValue;
import pt.kiko.krip.lang.values.Value;

import java.util.Collections;

public class WaitFunc extends BuiltInFunctionValue {

	static {
		Krip.registerValue("wait", new WaitFunc());
	}

	public WaitFunc() {
		super("wait", Collections.singletonList("millis"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		Value<?> millis = context.symbolTable.get("millis");

		if (!(millis instanceof NumberValue)) return invalidType(millis, context);

		double number = ((NumberValue) millis).getValue();

		if (number < 0 || Math.floor(number) != number)
			return result.failure(new RuntimeError(millis.startPosition, millis.endPosition, "Number must be natural", context));

		try {
			Thread.sleep(((long) number));
		} catch (InterruptedException e) {
			return result.failure(new RuntimeError(startPosition, endPosition, "Error while waiting " + millis + " milliseconds: " + e.getMessage(), context));
		}

		return result.success(new NullValue(context.parent));
	}
}
