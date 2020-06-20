package pt.kiko.krip.lang.values;

import org.jetbrains.annotations.Nullable;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class KripObject extends KripValue<Map<String, KripValue<?>>> {

	private int tabAmount = 1;

	public KripObject(Map<String, KripValue<?>> object, KripValue<?> parent, Context context) {
		super(parent, context);

		this.value = object;
	}

	public KripObject(Map<String, KripValue<?>> object, Context context) {
		super(context);

		this.value = object;
	}

	@Override
	public String getValueString() {
		return toString();
	}

	public void setValue(Map<String, KripValue<?>> value) {
		this.value = value;
	}

	public KripObject setTabAmount(int tabAmount) {
		this.tabAmount = tabAmount;
		return this;
	}

	public @Nullable KripValue<?> get(String key) {
		return value.get(key);
	}

	public void set(String key, KripValue<?> value) {
		this.value.put(key, value);
	}

	@Override
	public KripValue<Map<String, KripValue<?>>> copy() {
		return new KripObject(new HashMap<>(value), context).setPosition(startPosition, endPosition);
	}

	@Override
	public String toString() {
		if (tabAmount >= 3) return "[Object]";

		StringBuilder returnString = new StringBuilder(value.size() > 0 ? "{\n" : "{");
		AtomicInteger count = new AtomicInteger();
		value.forEach((key, value) -> {
			count.getAndIncrement();
			returnString.append(String.join("", Collections.nCopies(tabAmount, "  "))).append(key).append(": ").append(value instanceof KripObject ? value.equals(this) ? "<circular>" : ((KripObject) value).setTabAmount(tabAmount + 1).toString() : value instanceof KripString ? "\"" + value.toString() + "\"" : value.toString()).append(count.get() == this.value.size() ? "\n" : ",\n");
		});
		returnString.append(String.join("", Collections.nCopies(tabAmount - 1, "  "))).append("}");
		return returnString.toString();
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof KripObject) {
			return new RuntimeResult().success(new KripBoolean(value == other.getValue() || value.equals(other.getValue()), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}

	@Override
	public void makePrototype() {

	}

	@Override
	public KripValue<Map<String, KripValue<?>>> setContext(Context context) {
		super.setContext(context);
		value.forEach((name, value) -> value.setContext(context));
		return this;
	}
}
