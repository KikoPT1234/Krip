package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

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

		if (value instanceof KripNumber) return result.success(new KripString("NUMBER", context));
		else if (value instanceof KripString) return result.success(new KripString("STRING", context));
		else if (value instanceof KripBoolean) return result.success(new KripString("BOOLEAN", context));
		else if (value instanceof KripNull) return result.success(new KripString("NULL", context));
		else if (value instanceof KripList) return result.success(new KripString("LIST", context));
		else if (value instanceof KripObject) return result.success(new KripString("OBJECT", context));
		else if (value instanceof KripBaseFunction) return result.success(new KripString("FUNCTION", context));
		else return result.success(new KripString("UNKNOWN", context));
	}
}
