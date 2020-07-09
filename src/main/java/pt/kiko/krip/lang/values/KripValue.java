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
abstract public class KripValue<T> implements Serializable {

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
	 * The KripObject representing this value's prototype, if any
	 */
	public KripObject prototype;

	/**
	 * The parent value if this value is a prototype
	 */
	public KripValue<?> parent;

	/**
	 * The actual value to use
	 */
	public T value;

	/**
	 * Constructor for a prototype value
	 *
	 * @param parent  The parent value
	 * @param context The context this value is in
	 */
	public KripValue(KripValue<?> parent, Context context) {
		this.parent = parent;
		this.context = context;
	}

	/**
	 * Constructor for a normal value
	 *
	 * @param context The context this value is in
	 */
	public KripValue(Context context) {
		this.context = context;
		prototype = new KripObject(new HashMap<>(), this, context);
		prototype.set("toString", new KripJavaFunction("toString", Collections.emptyList(), this, context) {
			@Override
			public RuntimeResult run(Context context) {
				return new RuntimeResult().success(new KripString(KripValue.this.getValueString(), context));
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
	 * Makes a new KripObject representing this value's prototype
	 */
	public abstract void makePrototype();

	/**
	 * Copies this value
	 *
	 * @return A copy of this value
	 */
	public abstract KripValue<T> copy();

	/**
	 * Sets this value's start and end positions
	 *
	 * @param startPosition The start position
	 * @param endPosition   The end position
	 * @return The instance
	 */
	public KripValue<T> setPosition(Position startPosition, Position endPosition) {
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
	public KripValue<T> setContext(Context context) {
		this.context = context;
		return this;
	}

	/**
	 * Alias for the getValueString method
	 *
	 * @return The value as a string
	 * @see KripValue#getValueString()
	 */
	@Override
	public String toString() {
		return getValueString();
	}

	public abstract String getType();

	/**
	 * Shortcut for returning an illegal operation error
	 *
	 * @param other The other value
	 * @return The error
	 */
	public RuntimeResult illegalOperation(@NotNull KripValue<?> other) {
		return new RuntimeResult().failure(new RuntimeError(startPosition, other.endPosition, "Illegal operation", context));
	}

	/**
	 * Shortcut for returning an invalid type error
	 *
	 * @param value   The other value
	 * @param context The context
	 * @return The error
	 */
	protected RuntimeResult invalidType(@NotNull KripValue<?> value, Context context) {
		return new RuntimeResult().failure(new RuntimeError(value.startPosition != null ? value.startPosition : startPosition, value.endPosition != null ? value.endPosition : endPosition, "Invalid type", context));
	}

	/**
	 * Plus binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult plus(KripValue<?> other) {
		if (other instanceof KripString)
			return new RuntimeResult().success(new KripString(getValueString() + other.getValueString(), context));
		return illegalOperation(other);
	}

	/**
	 * Minus binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult minus(KripValue<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Multiply binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult mul(KripValue<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Divide binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult div(KripValue<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Power binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult pow(KripValue<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Modulo binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult mod(KripValue<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Equal binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	abstract public RuntimeResult equal(KripValue<?> other);

	/**
	 * Less than binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult lessThan(KripValue<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Less than or equal to binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult lessThanOrEqual(KripValue<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Greater than binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult greaterThan(KripValue<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Greater than or equal to binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult greaterThanOrEqual(KripValue<?> other) {
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
	public RuntimeResult and(KripValue<?> other) {
		return new RuntimeResult().success(new KripBoolean(isTrue() && other.isTrue(), context));
	}

	/**
	 * Bitwise and binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult bitwiseAnd(KripValue<?> other) {
		return illegalOperation(other);
	}

	/**
	 * Or binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult or(KripValue<?> other) {
		return new RuntimeResult().success(new KripBoolean(isTrue() || other.isTrue(), context));
	}

	public RuntimeResult notEquals(KripValue<?> other) {
		RuntimeResult result = equal(other);

		return result.success(new KripBoolean(!((KripBoolean) result.value).getValue(), context));
	}

	/**
	 * Bitwise or binary operation
	 *
	 * @param other The other value
	 * @return The result
	 */
	public RuntimeResult bitwiseOr(KripValue<?> other) {
		return illegalOperation(other);
	}

}
