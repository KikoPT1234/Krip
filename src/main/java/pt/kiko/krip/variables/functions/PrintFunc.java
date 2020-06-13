package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.BuiltInFunctionValue;
import pt.kiko.krip.lang.values.NullValue;

import java.util.Collections;

public class PrintFunc extends BuiltInFunctionValue {

	static {
		Krip.registerValue("print", new PrintFunc());
	}

	public PrintFunc() {
		super("print", Collections.singletonList("value"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		System.out.println(context.symbolTable.get("value").toString());
		return new RuntimeResult().success(new NullValue(context));
	}
}
