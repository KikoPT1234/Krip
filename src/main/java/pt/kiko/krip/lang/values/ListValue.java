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
	public String getValue() {
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
			return new RuntimeResult().success(new BooleanValue(getValue().equals(other.getValue()), context));
		} else return new RuntimeResult().success(new BooleanValue(false, context));
	}

	@Override
	public RuntimeResult notEquals(Value<?> other) {
		if (other instanceof ListValue) {
			return new RuntimeResult().success(new BooleanValue(!getValue().equals(other.getValue()), context));
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
			if (value instanceof StringValue) string.append("\"").append(value.getValue()).append("\"");
			else string.append(value.getValue());

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

				List<String> list = ListValue.this.value.stream().map(Value::getValue).collect(Collectors.toList());
				return result.success(new StringValue(String.join(string.getValue(), list), context));
			}
		});
	}
}
