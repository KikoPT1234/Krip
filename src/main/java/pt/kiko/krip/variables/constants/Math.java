package pt.kiko.krip.variables.constants;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripNumber;
import pt.kiko.krip.lang.values.KripObject;
import pt.kiko.krip.lang.values.KripValue;

import java.util.Collections;
import java.util.HashMap;

public class Math extends KripObject {

	static {
		Krip.registerValue("Math", new Math());
	}

	public Math() {
		super(new HashMap<>(), Krip.context);

		value.put("PI", new KripNumber(3.14159265358979, context));

		value.put("floor", new KripJavaFunction("floor", Collections.singletonList("number"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> value = context.symbolTable.get("number");

				if (!(value instanceof KripNumber)) return invalidType(value, context);

				return result.success(new KripNumber(java.lang.Math.floor(((KripNumber) value).getValue()), context));
			}
		});

		value.put("ceil", new KripJavaFunction("ceil", Collections.singletonList("number"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> value = context.symbolTable.get("number");

				if (!(value instanceof KripNumber)) return invalidType(value, context);

				return result.success(new KripNumber(java.lang.Math.ceil(((KripNumber) value).getValue()), context));
			}
		});

		value.put("round", new KripJavaFunction("round", Collections.singletonList("number"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> value = context.symbolTable.get("number");

				if (!(value instanceof KripNumber)) return invalidType(value, context);

				return result.success(new KripNumber(java.lang.Math.round(((KripNumber) value).getValue()), context));
			}
		});

		value.put("abs", new KripJavaFunction("abs", Collections.singletonList("number"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> value = context.symbolTable.get("number");

				if (!(value instanceof KripNumber)) return invalidType(value, context);

				return result.success(new KripNumber(java.lang.Math.abs(((KripNumber) value).getValue()), context));
			}
		});

		value.put("sqrt", new KripJavaFunction("sqrt", Collections.singletonList("number"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> value = context.symbolTable.get("number");

				if (!(value instanceof KripNumber)) return invalidType(value, context);

				return result.success(new KripNumber(java.lang.Math.sqrt(((KripNumber) value).getValue()), context));
			}
		});

		value.put("cbrt", new KripJavaFunction("cbrt", Collections.singletonList("number"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> value = context.symbolTable.get("number");

				if (!(value instanceof KripNumber)) return invalidType(value, context);

				return result.success(new KripNumber(java.lang.Math.cbrt(((KripNumber) value).getValue()), context));
			}
		});
	}

}
