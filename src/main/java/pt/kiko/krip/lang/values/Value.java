package pt.kiko.krip.lang.values;

import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.Position;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;

/**
 * Represents any value (e.g. a string, number, list, function, etc)
 *
 * @param <T> The type of the value field
 */
abstract public class Value<T> implements Serializable {

	/**
	 * The context this value is in
	 */
	public transient Context context;

	/**
	 * The start position of this value, if any
	 */
	public transient Position startPosition;

	/**
	 * The end position of this value, if any
	 */
	public transient Position endPosition;

	/**
	 * The ObjectValue representing this value's prototype, if any
	 */
	public ObjectValue prototype;

	/**
	 * The parent value if this value is a prototype
	 */
	public Value<?> parent;

	/**
	 * The actual value to use
	 */
	protected T value;

	/**
	 * Constructor for a prototype value
	 *
	 * @param parent  The parent value
	 * @param context The context this value is in
	 */
	public Value(Value<?> parent, Context context) {
		this.parent = parent;
		this.context = context;
	}

	/**
	 * Constructor for a normal value
	 *
	 * @param context The context this value is in
	 */
	public Value(Context context) {
		this.context = context;
		prototype = new ObjectValue(new HashMap<>(), this, context);
		prototype.set("toString", new BuiltInFunctionValue("toString", Collections.emptyList(), this, context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new StringValue(Value.this.getValueString(), context.parent));
			}
		});
	}

	/**
	 * @return The value as a string
	 */
	public abstract String getValueString();

	/**
	 * @return The value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Sets the value field to the value parameter
	 *
	 * @param value The value to set
	 */
	public abstract void setValue(T value);

	/**
	 * Makes a new ObjectValue representing this value's prototype
	 */
	public abstract void makePrototype();

	/**
	 * Copies this value
	 *
	 * @return A copy of this value
	 */
	public abstract Value<T> copy();

	/**
	 * Sets this value's start and end positions
	 *
	 * @param startPosition The start position
	 * @param endPosition   The end position
	 * @return The instance
	 */
	public Value<T> setPosition(Position startPosition, Position endPosition) {
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		return this;
	}

	/**
	 * Sets this value's context
	 *
	 * @param context The context
	 * @return The instance
	 */
	public Value<T> setContext(Context context) {
		this.context = context;
		return this;
	}

	/**
	 * Alias for the getValueString method
	 *
	 * @return The value as a string
	 * @see Value#getValueString()
	 */
	@Override
	public String toString() {
		return getValueString();
	}

	/**
	 * Shortcut for returning an illegal operation error
	 *
	 * @param other The other value
	 * @return The error
	 */
	public RuntimeResult illegalOperation(@NotNull Value<?> other) {
		return new RuntimeResult().failure(new RuntimeError(startPosition, other.endPosition, "Illegal operation", context));
	}

	/**
	 * Shortcut for returning an invalid type error
	 *
	 * @param value   The other value
	 * @param context The context
	 * @return The error
	 */
	protected RuntimeResult invalidType(@NotNull Value<?> value, Context context) {
		return new RuntimeResult().failure(new RuntimeError(value.startPosition != null ? value.startPosition : startPosition, value.endPosition != null ? value.endPosition : endPosition, "Invalid type", context));
	}

	/**
	 * Plus binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult plus(Value<?> other) {
		if (other instanceof StringValue)
			return new RuntimeResult().success(new StringValue(getValueString() + other.getValueString(), context));
		return illegalOperation(other);
	}

	/**
	 * Minus binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult minus(Value<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Multiply binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult mul(Value<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Divide binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult div(Value<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Power binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult pow(Value<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Modulo binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult mod(Value<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Equal binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	abstract public RuntimeResult equal(Value<?> other);

	/**
	 * Not equal binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	abstract public RuntimeResult notEquals(Value<?> other);

	/**
	 * Less than binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult lessThan(Value<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Less than or equal to binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult lessThanOrEqual(Value<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Greater than binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult greaterThan(Value<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Greater than or equal to binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult greaterThanOrEqual(Value<?> other) {
		return illegalOperation(other);
	}

	/**
	 * @return A boolean of whether the value is truthy or not
	 */
	public boolean isTrue() {
		return !getValueString().equals("") || getValueString() != null;
	}

	/**
	 * And binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult and(Value<?> other) {
		return new RuntimeResult().success(new BooleanValue(isTrue() && other.isTrue(), context));
	}

	/**
	 * Or binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult or(Value<?> other) {
		return new RuntimeResult().success(new BooleanValue(isTrue() || other.isTrue(), context));
	}

}
