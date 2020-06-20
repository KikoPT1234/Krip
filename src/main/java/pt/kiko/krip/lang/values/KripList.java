package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class KripList extends KripValue<List<KripValue<?>>> {

	public KripList(List<KripValue<?>> value, Context context) {
		super(context);
		this.value = value;
	}

	@Override
	public String getValueString() {
		return toString();
	}

	@Override
	public void setValue(List<KripValue<?>> value) {
		this.value = value;
	}

	@Override
	public KripValue<List<KripValue<?>>> copy() {
		return new KripList(new ArrayList<>(value), context).setPosition(startPosition, endPosition);
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof KripList) {
			return new RuntimeResult().success(new KripBoolean(value == other.getValue() || value.equals(other.getValue()), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}

	@Override
	public String toString() {
		if (value.size() == 0) return "[]";

		AtomicInteger count = new AtomicInteger();
		StringBuilder string = new StringBuilder();
		string.append("[");

		value.forEach(value -> {
			count.getAndIncrement();
			if (value instanceof KripString) string.append("\"").append(value.getValueString()).append("\"");
			else string.append(value.getValueString());

			if (count.get() != this.value.size()) string.append(", ");
		});

		string.append("]");

		return string.toString();
	}

	@Override
	public void makePrototype() {
		prototype.set("length", new KripNumber(value.size(), context));

		prototype.set("join", new KripJavaFunction("join", Collections.singletonList("string"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> string = context.symbolTable.get("string");

				if (!(string instanceof KripString)) return invalidType(string, context);

				List<String> list = KripList.this.value.stream().map(KripValue::getValueString).collect(Collectors.toList());
				return result.success(new KripString(String.join(string.getValueString(), list), context));
			}
		});

		prototype.set("forEach", new KripJavaFunction("forEach", Collections.singletonList("function"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> function = context.symbolTable.get("function");

				if (!(function instanceof KripBaseFunction)) return invalidType(function, context);

				KripList.this.value.forEach(value -> ((KripBaseFunction) function).execute(Collections.singletonList(value), context));

				return result.success(new KripNull(context));
			}
		});

		prototype.set("map", new KripJavaFunction("map", Collections.singletonList("function"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> function = context.symbolTable.get("function");

				if (!(function instanceof KripBaseFunction)) return invalidType(function, context);

				List<KripValue<?>> returnValue = new ArrayList<>();
				KripValue<?>[] list = KripList.this.value.toArray(new KripValue[]{});

				for (KripValue<?> listValue : list) {
					returnValue.add(result.register(((KripBaseFunction) function).execute(Collections.singletonList(listValue), context)));
					if (result.shouldReturn()) return result;
				}

				return result.success(new KripList(returnValue, context));
			}
		});

		prototype.set("filter", new KripJavaFunction("filter", Collections.singletonList("function"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> function = context.symbolTable.get("function");

				if (!(function instanceof KripBaseFunction)) return invalidType(function, context);

				List<KripValue<?>> returnValue = new ArrayList<>();
				KripValue<?>[] list = KripList.this.value.toArray(new KripValue[]{});

				for (KripValue<?> listValue : list) {
					KripValue<?> returnedValue = result.register(((KripBaseFunction) function).execute(Collections.singletonList(listValue), context));
					if (result.shouldReturn()) return result;

					if (returnedValue.isTrue()) returnValue.add(returnedValue);
				}

				return result.success(new KripList(returnValue, context));
			}
		});

		prototype.set("find", new KripJavaFunction("find", Collections.singletonList("function"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> function = context.symbolTable.get("function");

				if (!(function instanceof KripBaseFunction)) return invalidType(function, context);

				KripValue<?>[] list = KripList.this.value.toArray(new KripValue[]{});

				for (KripValue<?> value : list) {
					KripValue<?> returnedValue = result.register(((KripBaseFunction) function).execute(Collections.singletonList(value), context));
					if (result.shouldReturn()) return result;

					if (returnedValue.isTrue()) return result.success(returnedValue);
				}

				return result.success(new KripNull(context));
			}
		});
	}
}
