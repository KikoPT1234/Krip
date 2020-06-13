package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.BuiltInFunctionValue;
import pt.kiko.krip.lang.values.NullValue;
import pt.kiko.krip.lang.values.NumberValue;
import pt.kiko.krip.lang.values.Value;
import pt.kiko.krip.objects.DateObj;

import java.util.Collections;

public class DateFunc extends BuiltInFunctionValue {

	static {
		Krip.registerValue("Date", new DateFunc());
	}

	public DateFunc() {
		super("Date", Collections.singletonList("time"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		Value<?> time = context.symbolTable.get("time");

		if (!(time instanceof NumberValue || time instanceof NullValue)) return invalidType(time, context);

		if (time instanceof NumberValue)
			return result.success(new DateObj((long) (double) ((NumberValue) time).getValue(), context));
		else return result.success(new DateObj(context));
	}
}
