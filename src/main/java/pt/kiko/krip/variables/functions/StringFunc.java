package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.BuiltInFunctionValue;
import pt.kiko.krip.lang.values.StringValue;

import java.util.Collections;

public class StringFunc extends BuiltInFunctionValue {

	static {
		Krip.registerValue("string", new StringFunc());
	}

	public StringFunc() {
		super("string", Collections.singletonList("value"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		return new RuntimeResult().success(new StringValue(context.symbolTable.get("value").getValue(), context.parent));
	}
}
