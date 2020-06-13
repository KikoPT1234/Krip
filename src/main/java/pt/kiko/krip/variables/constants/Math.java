package pt.kiko.krip.variables.constants;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.BuiltInFunctionValue;
import pt.kiko.krip.lang.values.NumberValue;
import pt.kiko.krip.lang.values.ObjectValue;
import pt.kiko.krip.lang.values.Value;

import java.util.Collections;
import java.util.HashMap;

public class Math extends ObjectValue {

	static {
		Krip.registerValue("Math", new Math());
	}

	public Math() {
		super(new HashMap<>(), Krip.context);

		value.put("PI", new NumberValue(3.14159265358979, context));

		value.put("floor", new BuiltInFunctionValue("floor", Collections.singletonList("number"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> value = context.symbolTable.get("number");

				if (!(value instanceof NumberValue)) return invalidType(value, context);

				return result.success(new NumberValue(java.lang.Math.floor(((NumberValue) value).getValue()), context));
			}
		});

		value.put("ceil", new BuiltInFunctionValue("ceil", Collections.singletonList("number"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> value = context.symbolTable.get("number");

				if (!(value instanceof NumberValue)) return invalidType(value, context);

				return result.success(new NumberValue(java.lang.Math.ceil(((NumberValue) value).getValue()), context));
			}
		});

		value.put("round", new BuiltInFunctionValue("round", Collections.singletonList("number"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> value = context.symbolTable.get("number");

				if (!(value instanceof NumberValue)) return invalidType(value, context);

				return result.success(new NumberValue(java.lang.Math.round(((NumberValue) value).getValue()), context));
			}
		});

		value.put("abs", new BuiltInFunctionValue("abs", Collections.singletonList("number"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> value = context.symbolTable.get("number");

				if (!(value instanceof NumberValue)) return invalidType(value, context);

				return result.success(new NumberValue(java.lang.Math.abs(((NumberValue) value).getValue()), context));
			}
		});

		value.put("sqrt", new BuiltInFunctionValue("sqrt", Collections.singletonList("number"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> value = context.symbolTable.get("number");

				if (!(value instanceof NumberValue)) return invalidType(value, context);

				return result.success(new NumberValue(java.lang.Math.sqrt(((NumberValue) value).getValue()), context));
			}
		});

		value.put("cbrt", new BuiltInFunctionValue("cbrt", Collections.singletonList("number"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> value = context.symbolTable.get("number");

				if (!(value instanceof NumberValue)) return invalidType(value, context);

				return result.success(new NumberValue(java.lang.Math.cbrt(((NumberValue) value).getValue()), context));
			}
		});
	}

}
