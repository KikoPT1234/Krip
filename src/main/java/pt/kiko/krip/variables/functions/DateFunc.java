package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripNull;
import pt.kiko.krip.lang.values.KripNumber;
import pt.kiko.krip.lang.values.KripValue;
import pt.kiko.krip.objects.DateObj;

import java.util.Collections;

public class DateFunc extends KripJavaFunction {

	static {
		Krip.registerValue("Date", new DateFunc());
	}

	public DateFunc() {
		super("Date", Collections.singletonList("time"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> time = context.symbolTable.get("time");

		if (!(time instanceof KripNumber || time instanceof KripNull)) return invalidType(time, context);

		if (time instanceof KripNumber)
			return result.success(new DateObj((long) (double) ((KripNumber) time).getValue(), context));
		else return result.success(new DateObj(context));
	}
}
