package pt.kiko.krip.lang.values;

import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.Position;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.util.Collections;
import java.util.HashMap;

abstract public class Value<T> {
	public Context context;

	public Position startPosition;
	public Position endPosition;

	public ObjectValue prototype;
	public Value<?> parent;
	protected T value;

	public abstract String getValue();

	public Value(Value<?> parent, Context context) {
		this.parent = parent;
		this.context = context;
	}

	public Value(Context context) {
		this.context = context;
		prototype = new ObjectValue(new HashMap<>(), this, context);
		prototype.set("toString", new BuiltInFunctionValue("toString", Collections.emptyList(), this, context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new StringValue(Value.this.getValue(), context.parent));
			}
		});
	}

	public abstract void setValue(T value);

	public abstract void makePrototype();

	public abstract Value<T> copy();

	public Value<T> setPosition(Position startPosition, Position endPosition) {
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		return this;
	}

	public Value<T> setContext(Context context) {
		this.context = context;
		return this;
	}

	@Override
	public String toString() {
		return getValue();
	}

	public RuntimeResult illegalOperation(@NotNull Value<?> other) {
		return new RuntimeResult().failure(new RuntimeError(startPosition, other.endPosition, "Illegal operation", context));
	}

	public RuntimeResult plus(Value<?> other) {
		if (other instanceof StringValue)
			return new RuntimeResult().success(new StringValue(getValue() + other.getValue(), context));
		return illegalOperation(other);
	}

	public RuntimeResult minus(Value<?> other) {
		return illegalOperation(other);
	}

	public RuntimeResult mul(Value<?> other) {
		return illegalOperation(other);
	}

	public RuntimeResult div(Value<?> other) {
		return illegalOperation(other);
	}

	public RuntimeResult pow(Value<?> other) {
		return illegalOperation(other);
	}

	public RuntimeResult mod(Value<?> other) {
		return illegalOperation(other);
	}

	abstract public RuntimeResult equal(Value<?> other);

	abstract public RuntimeResult notEquals(Value<?> other);

	public RuntimeResult lessThan(Value<?> other) {
		return illegalOperation(other);
	}

	public RuntimeResult lessThanOrEqual(Value<?> other) {
		return illegalOperation(other);
	}

	public RuntimeResult greaterThan(Value<?> other) {
		return illegalOperation(other);
	}

	public RuntimeResult greaterThanOrEqual(Value<?> other) {
		return illegalOperation(other);
	}

	public boolean isTrue() {
		return !getValue().equals("") || getValue() != null;
	}

	public RuntimeResult and(Value<?> other) {
		return new RuntimeResult().success(new BooleanValue(isTrue() && other.isTrue(), context));
	}

	public RuntimeResult or(Value<?> other) {
		return new RuntimeResult().success(new BooleanValue(isTrue() || other.isTrue(), context));
	}

	protected RuntimeResult invalidType(@NotNull Value<?> value, Context context) {
		return new RuntimeResult().failure(new RuntimeError(value.startPosition != null ? value.startPosition : startPosition, value.endPosition != null ? value.endPosition : endPosition, "Invalid type", context));
	}

}
