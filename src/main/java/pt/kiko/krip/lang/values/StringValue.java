package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class StringValue extends Value<String> {

	public StringValue(String value, Context context) {
		super(context);
		if (value == null) this.value = "null";
		else this.value = value;
	}

	@Override
	public String getValueString() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public Value<String> copy() {
		return new StringValue(value, context).setPosition(startPosition, endPosition);
	}

	@Override
	public RuntimeResult equal(Value<?> other) {
		return new RuntimeResult().success(new BooleanValue(getValueString().equals(other.getValueString()), context));
	}

	@Override
	public RuntimeResult notEquals(Value<?> other) {
		return new RuntimeResult().success(new BooleanValue(!getValueString().equals(other.getValueString()), context));
	}

	@Override
	public void makePrototype() {
		prototype.set("length", new NumberValue(value.length(), context));
		prototype.set("split", new BuiltInFunctionValue("split", Collections.singletonList("regex"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				Value<?> regex = context.symbolTable.get("regex");

				if (!(regex instanceof StringValue)) return invalidType(regex, context);

				return result.success(new ListValue(Arrays.stream(StringValue.this.getValueString().split(regex.getValueString())).map(string -> new StringValue(string, context)).collect(Collectors.toList()), context));
			}
		});
	}

	@Override
	public RuntimeResult plus(Value<?> other) {
		if (other instanceof NullValue)
			return new RuntimeResult().success(new StringValue(getValueString() + "null", context));
		else return new RuntimeResult().success(new StringValue(getValueString() + other.getValueString(), context));
	}
}
