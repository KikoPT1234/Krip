package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ListValue extends Value<List<Value<?>>> {

	public List<Value<?>> value;

	public ListValue(List<Value<?>> value, Context context) {
		super(context);
		this.value = value;
	}

	@Override
	public String getValueString() {
		return toString();
	}

	@Override
	public void setValue(List<Value<?>> value) {
		this.value = value;
	}

	@Override
	public Value<List<Value<?>>> copy() {
		return new ListValue(new ArrayList<>(value), context).setPosition(startPosition, endPosition);
	}

	@Override
	public RuntimeResult equal(Value<?> other) {
		if (other instanceof ListValue) {
			return new RuntimeResult().success(new BooleanValue(getValueString().equals(other.getValueString()), context));
		} else return new RuntimeResult().success(new BooleanValue(false, context));
	}

	@Override
	public RuntimeResult notEquals(Value<?> other) {
		if (other instanceof ListValue) {
			return new RuntimeResult().success(new BooleanValue(!getValueString().equals(other.getValueString()), context));
		} else return new RuntimeResult().success(new BooleanValue(true, context));
	}

	@Override
	public String toString() {
		if (value.size() == 0) return "[]";

		AtomicInteger count = new AtomicInteger();
		StringBuilder string = new StringBuilder();
		string.append("[");

		value.forEach(value -> {
			count.getAndIncrement();
			if (value instanceof StringValue) string.append("\"").append(value.getValueString()).append("\"");
			else string.append(value.getValueString());

			if (count.get() != this.value.size()) string.append(", ");
		});

		string.append("]");

		return string.toString();
	}

	@Override
	public void makePrototype() {
		prototype.set("length", new NumberValue(value.size(), context));

		prototype.set("join", new BuiltInFunctionValue("join", Collections.singletonList("string"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> string = context.symbolTable.get("string");

				if (!(string instanceof StringValue)) return invalidType(string, context);

				List<String> list = ListValue.this.value.stream().map(Value::getValueString).collect(Collectors.toList());
				return result.success(new StringValue(String.join(string.getValueString(), list), context));
			}
		});

		prototype.set("forEach", new BuiltInFunctionValue("forEach", Collections.singletonList("function"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> function = context.symbolTable.get("function");

				if (!(function instanceof BaseFunctionValue)) return invalidType(function, context);

				ListValue.this.value.forEach(value -> ((BaseFunctionValue) function).execute(Collections.singletonList(value), context));

				return result.success(new NullValue(context));
			}
		});

		prototype.set("map", new BuiltInFunctionValue("map", Collections.singletonList("function"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> function = context.symbolTable.get("function");

				if (!(function instanceof BaseFunctionValue)) return invalidType(function, context);

				List<Value<?>> returnValue = new ArrayList<>();
				Value<?>[] list = ListValue.this.value.toArray(new Value[]{});

				for (Value<?> listValue : list) {
					returnValue.add(result.register(((BaseFunctionValue) function).execute(Collections.singletonList(listValue), context)));
					if (result.shouldReturn()) return result;
				}

				return result.success(new ListValue(returnValue, context));
			}
		});

		prototype.set("filter", new BuiltInFunctionValue("filter", Collections.singletonList("function"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> function = context.symbolTable.get("function");

				if (!(function instanceof BaseFunctionValue)) return invalidType(function, context);

				List<Value<?>> returnValue = new ArrayList<>();
				Value<?>[] list = ListValue.this.value.toArray(new Value[]{});

				for (Value<?> listValue : list) {
					Value<?> returnedValue = result.register(((BaseFunctionValue) function).execute(Collections.singletonList(listValue), context));
					if (result.shouldReturn()) return result;

					if (returnedValue.isTrue()) returnValue.add(returnedValue);
				}

				return result.success(new ListValue(returnValue, context));
			}
		});

		prototype.set("find", new BuiltInFunctionValue("find", Collections.singletonList("function"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> function = context.symbolTable.get("function");

				if (!(function instanceof BaseFunctionValue)) return invalidType(function, context);

				Value<?>[] list = ListValue.this.value.toArray(new Value[]{});

				for (Value<?> value : list) {
					Value<?> returnedValue = result.register(((BaseFunctionValue) function).execute(Collections.singletonList(value), context));
					if (result.shouldReturn()) return result;

					if (returnedValue.isTrue()) return result.success(returnedValue);
				}

				return result.success(new NullValue(context));
			}
		});
	}
}
