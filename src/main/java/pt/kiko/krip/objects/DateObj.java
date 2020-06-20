package pt.kiko.krip.objects;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class DateObj extends KripObject {

	public Date date;

	public DateObj(Context context) {
		super(new HashMap<>(), context);
		date = new Date();

		value.put("time", new KripNumber(date.getTime(), context));

		value.put("setTime", new KripJavaFunction("setTime", Collections.singletonList("time"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> time = context.symbolTable.get("time");

				if (!(time instanceof KripNumber)) return invalidType(time, context);

				date.setTime((long) (double) ((KripNumber) time).getValue());
				DateObj.this.value.put("time", new KripNumber(date.getTime(), context));

				return result.success(new KripNull(context));
			}
		});


	}

	public DateObj(long time, Context context) {
		super(new HashMap<>(), context);
		date = new Date(time);

		value.put("time", new KripNumber(date.getTime(), context));

		value.put("setTime", new KripJavaFunction("setTime", Collections.singletonList("time"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> time = context.symbolTable.get("time");

				if (!(time instanceof KripNumber)) return invalidType(time, context);

				date.setTime((long) (double) ((KripNumber) time).getValue());
				DateObj.this.value.put("time", new KripNumber(date.getTime(), context));

				return result.success(new KripNull(context));
			}
		});
	}

	@Override
	public String toString() {
		return date.toString();
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof DateObj) {
			return new RuntimeResult().success(new KripBoolean(date == ((DateObj) other).date || date.equals(((DateObj) other).date), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}
}
