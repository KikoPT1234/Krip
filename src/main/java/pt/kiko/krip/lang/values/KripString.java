package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class KripString extends KripValue<String> {

	public KripString(String value, Context context) {
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
	public KripValue<String> copy() {
		return new KripString(value, context).setPosition(startPosition, endPosition);
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof KripString) {
			return new RuntimeResult().success(new KripBoolean(value == other.getValue() || value.equals(other.getValue()), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}

	@Override
	public void makePrototype() {
		prototype.set("length", new KripNumber(value.length(), context));
		prototype.set("split", new KripJavaFunction("split", Collections.singletonList("regex"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> regex = context.symbolTable.get("regex");

				if (!(regex instanceof KripString)) return invalidType(regex, context);

				return result.success(new KripList(Arrays.stream(KripString.this.getValueString().split(regex.getValueString())).map(string -> new KripString(string, context)).collect(Collectors.toList()), context));
			}
		});
	}

	@Override
	public RuntimeResult plus(KripValue<?> other) {
		if (other instanceof KripNull)
			return new RuntimeResult().success(new KripString(getValueString() + "null", context));
		else return new RuntimeResult().success(new KripString(getValueString() + other.getValueString(), context));
	}
}
