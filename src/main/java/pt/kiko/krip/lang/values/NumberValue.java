package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

public class NumberValue extends Value {

	protected final double value;

	public NumberValue(double number, Context context) {
		super(context);
		this.value = number;
	}

	@Override
	public String getValue() {
		if (Math.floor(value) == value) return Integer.toString((int) value);
		else return Double.toString(value);
	}

	@Override
	public RuntimeResult plus(Value other) {
		if (other instanceof StringValue) return new RuntimeResult().success(new StringValue(getValue() + other.getValue(), context));
		else if (other instanceof NumberValue) return new RuntimeResult().success(new NumberValue(value + Double.parseDouble(other.getValue()), context));
		else return illegalOperation(other);
	}

	@Override
	public RuntimeResult minus(Value other) {
		if (other instanceof NumberValue) return new RuntimeResult().success(new NumberValue(value - Double.parseDouble(other.getValue()), context));
		else return illegalOperation(other);
	}

	@Override
	public RuntimeResult mul(Value other) {
		if (other instanceof NumberValue) return new RuntimeResult().success(new NumberValue(value * Double.parseDouble(other.getValue()), context));
		else return illegalOperation(other);
	}

	@Override
	public RuntimeResult div(Value other) {
		if (other instanceof NumberValue) return new RuntimeResult().success(new NumberValue(value / Double.parseDouble(other.getValue()), context));
		else return illegalOperation(other);
	}

	@Override
	public RuntimeResult pow(Value other) {
		if (other instanceof NumberValue) return new RuntimeResult().success(new NumberValue(Math.pow(value, Double.parseDouble(other.getValue())), context));
		else return illegalOperation(other);
	}

	@Override
	public RuntimeResult mod(Value other) {
		if (other instanceof NumberValue) return new RuntimeResult().success(new NumberValue(value % Double.parseDouble(other.getValue()), context));
		else return illegalOperation(other);
	}

	@Override
	public RuntimeResult lessThan(Value other) {
		if (other instanceof NumberValue) return new RuntimeResult().success(new BooleanValue(value < Double.parseDouble(other.getValue()), context));
		else return illegalOperation(other);
	}

	@Override
	public RuntimeResult lessThanOrEqual(Value other) {
		if (other instanceof NumberValue) return new RuntimeResult().success(new BooleanValue(value <= Double.parseDouble(other.getValue()), context));
		else return illegalOperation(other);
	}

	@Override
	public RuntimeResult greaterThan(Value other) {
		if (other instanceof NumberValue) return new RuntimeResult().success(new BooleanValue(value > Double.parseDouble(other.getValue()), context));
		else return illegalOperation(other);
	}

	@Override
	public RuntimeResult greaterThanOrEqual(Value other) {
		if (other instanceof NumberValue) return new RuntimeResult().success(new BooleanValue(value >= Double.parseDouble(other.getValue()), context));
		else return illegalOperation(other);
	}

	@Override
	public RuntimeResult equal(Value other) {
		if (other instanceof NumberValue) {
			if (Math.floor(value) == value)
				return new RuntimeResult().success(new BooleanValue(value == Integer.parseInt(other.getValue()), context));
			else
				return new RuntimeResult().success(new BooleanValue(value == Double.parseDouble(other.getValue()), context));
		} else return new RuntimeResult().success(new BooleanValue(false, context));
	}

	@Override
	public RuntimeResult notEquals(Value other) {
		if (other instanceof NumberValue) {
			if (Math.floor(value) == value)
				return new RuntimeResult().success(new BooleanValue(value != Integer.parseInt(other.getValue()), context));
			else
				return new RuntimeResult().success(new BooleanValue(value != Double.parseDouble(other.getValue()), context));
		} else return new RuntimeResult().success(new BooleanValue(true, context));
	}

	@Override
	public boolean isTrue() {
		return value != 0;
	}

	@Override
	public Value copy() {
		return new NumberValue(value, context).setPosition(startPosition, endPosition);
	}
}
