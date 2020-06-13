package pt.kiko.krip.objects;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class DateObj extends ObjectValue {

	public Date date;

	public DateObj(Context context) {
		super(new HashMap<>(), context);
		date = new Date();

		value.put("time", new NumberValue(date.getTime(), context));

		value.put("setTime", new BuiltInFunctionValue("setTime", Collections.singletonList("time"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> time = context.symbolTable.get("time");

				if (!(time instanceof NumberValue)) return invalidType(time, context);

				date.setTime((long) (double) ((NumberValue) time).getValue());
				DateObj.this.value.put("time", new NumberValue(date.getTime(), context));

				return result.success(new NullValue(context));
			}
		});


	}

	public DateObj(long time, Context context) {
		super(new HashMap<>(), context);
		date = new Date(time);

		value.put("time", new NumberValue(date.getTime(), context));

		value.put("setTime", new BuiltInFunctionValue("setTime", Collections.singletonList("time"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> time = context.symbolTable.get("time");

				if (!(time instanceof NumberValue)) return invalidType(time, context);

				date.setTime((long) (double) ((NumberValue) time).getValue());
				DateObj.this.value.put("time", new NumberValue(date.getTime(), context));

				return result.success(new NullValue(context));
			}
		});
	}

	@Override
	public String toString() {
		return date.toString();
	}
}
