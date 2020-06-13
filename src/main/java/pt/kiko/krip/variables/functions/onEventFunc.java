package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Arrays;

public class onEventFunc extends BuiltInFunctionValue {

	static {
		Krip.registerValue("onEvent", new onEventFunc());
	}

	public onEventFunc() {
		super("onEvent", Arrays.asList("eventName", "function"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		Value<?> name = context.symbolTable.get("eventName");
		Value<?> func = context.symbolTable.get("function");

		if (!(name instanceof StringValue)) return invalidType(name, context);
		if (!(func instanceof BaseFunctionValue)) return invalidType(func, context);

		if (!Krip.events.containsKey(name.getValueString()))
			return result.failure(new RuntimeError(name.startPosition, name.endPosition, "Invalid event", context));
		Krip.events.get(name.getValueString()).addFunction((BaseFunctionValue) func);

		return result.success(new NullValue(context));
	}
}
