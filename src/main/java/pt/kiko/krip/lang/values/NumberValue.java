package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.math.BigDecimal;

public class NumberValue extends Value<Double> {

	public NumberValue(double number, Context context) {
		super(context);
		this.value = number;
	}

	@Override
	public String getValueString() {
		return new BigDecimal(value.toString()).stripTrailingZeros().toPlainString();
	}

	@Override
	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public RuntimeResult plus(Value<?> other) {
		if (other instanceof StringValue)
			return new RuntimeResult().success(new StringValue(getValueString() + other.getValueString(), context));
		else if (other instanceof NumberValue) {
			NumberValue numberValue = (NumberValue) other;
			return new RuntimeResult().success(new NumberValue(value + (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult minus(Value<?> other) {
		if (other instanceof NumberValue) {
			NumberValue numberValue = (NumberValue) other;
			return new RuntimeResult().success(new NumberValue(value - (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult mul(Value<?> other) {
		if (other instanceof NumberValue) {
			NumberValue numberValue = (NumberValue) other;
			return new RuntimeResult().success(new NumberValue(value * (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult div(Value<?> other) {
		if (other instanceof NumberValue) {
			NumberValue numberValue = (NumberValue) other;
			return new RuntimeResult().success(new NumberValue(value / (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult pow(Value<?> other) {
		if (other instanceof NumberValue) {
			NumberValue numberValue = (NumberValue) other;
			return new RuntimeResult().success(new NumberValue(Math.pow(value, numberValue.getValue()), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult mod(Value<?> other) {
		if (other instanceof NumberValue) {
			NumberValue numberValue = (NumberValue) other;
			return new RuntimeResult().success(new NumberValue(value % (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult lessThan(Value<?> other) {
		if (other instanceof NumberValue) {
			NumberValue numberValue = (NumberValue) other;
			return new RuntimeResult().success(new BooleanValue(value < (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult lessThanOrEqual(Value<?> other) {
		if (other instanceof NumberValue) {
			NumberValue numberValue = (NumberValue) other;
			return new RuntimeResult().success(new BooleanValue(value <= (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult greaterThan(Value<?> other) {
		if (other instanceof NumberValue) {
			NumberValue numberValue = (NumberValue) other;
			return new RuntimeResult().success(new BooleanValue(value > (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult greaterThanOrEqual(Value<?> other) {
		if (other instanceof NumberValue) {
			NumberValue numberValue = (NumberValue) other;
			return new RuntimeResult().success(new BooleanValue(value >= (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult equal(Value<?> other) {
		if (other instanceof NumberValue) {
			NumberValue numberValue = (NumberValue) other;
			return new RuntimeResult().success(new BooleanValue(value == (double) numberValue.getValue(), context));
		} else return new RuntimeResult().success(new BooleanValue(false, context));
	}

	@Override
	public RuntimeResult notEquals(Value<?> other) {
		if (other instanceof NumberValue) {
			NumberValue numberValue = (NumberValue) other;
			return new RuntimeResult().success(new BooleanValue(value != (double) numberValue.getValue(), context));
		} else return new RuntimeResult().success(new BooleanValue(true, context));
	}

	@Override
	public boolean isTrue() {
		return value != 0;
	}

	@Override
	public Value<Double> copy() {
		return new NumberValue(value, context).setPosition(startPosition, endPosition);
	}

	@Override
	public void makePrototype() {

	}

	public boolean isWhole() {
		return Math.floor(value) == value;
	}
}
