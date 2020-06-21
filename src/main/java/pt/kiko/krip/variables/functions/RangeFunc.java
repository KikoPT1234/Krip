package pt.kiko.krip.variables.functions;

import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RangeFunc extends KripJavaFunction {

	static {
		Krip.registerValue("range", new RangeFunc());
	}

	public RangeFunc() {
		super("range", Arrays.asList("start", "end", "step"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> start = context.symbolTable.get("start");
		KripValue<?> end = context.symbolTable.get("end");
		KripValue<?> step = context.symbolTable.get("step");

		if (!(start instanceof KripNumber)) return invalidType(start, context);
		if (!(end instanceof KripNumber)) return invalidType(end, context);
		if (!(step instanceof KripNumber || step instanceof KripNull)) return invalidType(step, context);

		List<KripValue<?>> list = new ArrayList<>();
		double stepNumber = step instanceof KripNull ? 1 : (double) ((KripNumber) step).getValue();

		for (double i = ((KripNumber) start).getValue(); i <= ((KripNumber) end).getValue(); i += stepNumber) {
			list.add(new KripNumber(i, context));
		}

		return result.success(new KripList(list, context));
	}
}
