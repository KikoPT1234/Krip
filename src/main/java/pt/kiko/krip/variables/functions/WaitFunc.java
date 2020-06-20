package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripNull;
import pt.kiko.krip.lang.values.KripNumber;
import pt.kiko.krip.lang.values.KripValue;

import java.util.Collections;

public class WaitFunc extends KripJavaFunction {

	static {
		Krip.registerValue("wait", new WaitFunc());
	}

	public WaitFunc() {
		super("wait", Collections.singletonList("millis"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> millis = context.symbolTable.get("millis");

		if (!(millis instanceof KripNumber)) return invalidType(millis, context);

		double number = ((KripNumber) millis).getValue();

		if (number < 0 || Math.floor(number) != number)
			return result.failure(new RuntimeError(millis.startPosition, millis.endPosition, "Number must be natural", context));

		try {
			Thread.sleep(((long) number));
		} catch (InterruptedException e) {
			return result.failure(new RuntimeError(startPosition, endPosition, "Error while waiting " + millis + " milliseconds: " + e.getMessage(), context));
		}

		return result.success(new KripNull(context));
	}
}
