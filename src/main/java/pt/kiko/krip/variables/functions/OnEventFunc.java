package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Arrays;

public class OnEventFunc extends KripJavaFunction {

	static {
		Krip.registerValue("onEvent", new OnEventFunc());
	}

	public OnEventFunc() {
		super("onEvent", Arrays.asList("eventName", "function"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> name = context.symbolTable.get("eventName");
		KripValue<?> func = context.symbolTable.get("function");

		if (!(name instanceof KripString)) return invalidType(name, context);
		if (!(func instanceof KripBaseFunction)) return invalidType(func, context);

		if (!Krip.events.containsKey(name.getValueString()))
			return result.failure(new RuntimeError(name.startPosition, name.endPosition, "Invalid event", context));
		Krip.events.get(name.getValueString()).addFunction((KripBaseFunction) func);

		return result.success(new KripNull(context));
	}
}
