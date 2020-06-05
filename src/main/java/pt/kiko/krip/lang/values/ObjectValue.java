package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectValue extends Value {

	protected Map<String, Value> value;
	private int tabAmount = 1;

	public ObjectValue(Map<String, Value> object, Context context) {
		super(context);

		this.value = object;
	}

	public void setValue(Map<String, Value> value) {
		this.value = value;
	}

	public ObjectValue setTabAmount(int tabAmount) {
		this.tabAmount = tabAmount;
		return this;
	}

	public Value get(String key) {
		return value.get(key);
	}

	public void set(String key, Value value) {
		this.value.put(key, value);
	}

	@Override
	public String getValue() {
		return toString();
	}

	@Override
	public Value copy() {
		return new ObjectValue(new HashMap<>(value), context).setPosition(startPosition, endPosition);
	}

	@Override
	public String toString() {
		StringBuilder returnString = new StringBuilder(value.size() > 0 ? "{\n" : "{");
		AtomicInteger count = new AtomicInteger();
		value.forEach((key, value) -> {
			count.getAndIncrement();
			returnString.append(String.join("", Collections.nCopies(tabAmount, "  "))).append(key).append(": ").append(value instanceof ObjectValue ? value.equals(this) ? "<circular>" : ((ObjectValue) value).setTabAmount(tabAmount + 1).toString() : value instanceof StringValue ? "\"" + value.toString() + "\"" : value.toString()).append(count.get() == this.value.size() ? "\n" : ",\n");
		});
		returnString.append(String.join("", Collections.nCopies(tabAmount - 1, "  "))).append("}");
		return returnString.toString();
	}

	@Override
	public RuntimeResult equal(Value other) {
		if (other instanceof ObjectValue) {
			return new RuntimeResult().success(new BooleanValue(getValue().equals(other.getValue()), context));
		} else return new RuntimeResult().success(new BooleanValue(false, context));
	}

	@Override
	public RuntimeResult notEquals(Value other) {
		if (other instanceof ObjectValue) {
			return new RuntimeResult().success(new BooleanValue(!getValue().equals(other.getValue()), context));
		} else return new RuntimeResult().success(new BooleanValue(true, context));
	}

	@Override
	public RuntimeResult plus(Value other) {
		if (other instanceof StringValue) return new RuntimeResult().success(new StringValue(getValue() + other.getValue(), context));
		else return illegalOperation(other);
	}
}