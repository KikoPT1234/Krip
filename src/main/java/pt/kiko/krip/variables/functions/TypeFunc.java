package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.lang.values.KripValue;

import java.util.Collections;

public class TypeFunc extends KripJavaFunction {

	static {
		Krip.registerValue("type", new TypeFunc());
	}

	public TypeFunc() {
		super("type", Collections.singletonList("value"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> value = context.symbolTable.get("value");

		return result.success(new KripString(value.getType(), context));
	}
}
