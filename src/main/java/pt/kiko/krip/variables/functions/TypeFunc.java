package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Collections;

public class TypeFunc extends BuiltInFunctionValue {

	static {
		Krip.registerValue("type", new TypeFunc());
	}

	public TypeFunc() {
		super("type", Collections.singletonList("value"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		Value<?> value = context.symbolTable.get("value");

		if (value instanceof NumberValue) return result.success(new StringValue("NUMBER", context));
		else if (value instanceof StringValue) return result.success(new StringValue("STRING", context));
		else if (value instanceof BooleanValue) return result.success(new StringValue("BOOLEAN", context));
		else if (value instanceof NullValue) return result.success(new StringValue("NULL", context));
		else if (value instanceof ListValue) return result.success(new StringValue("LIST", context));
		else if (value instanceof ObjectValue) return result.success(new StringValue("OBJECT", context));
		else if (value instanceof BaseFunctionValue) return result.success(new StringValue("FUNCTION", context));
		else return result.success(new StringValue("UNKNOWN", context));
	}
}
