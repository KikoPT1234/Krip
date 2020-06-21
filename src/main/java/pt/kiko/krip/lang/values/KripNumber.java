package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.math.BigDecimal;

public class KripNumber extends KripValue<Double> {

	public KripNumber(double number, Context context) {
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
	public String getType() {
		return "number";
	}

	@Override
	public RuntimeResult plus(KripValue<?> other) {
		if (other instanceof KripString)
			return new RuntimeResult().success(new KripString(getValueString() + other.getValueString(), context));
		else if (other instanceof KripNumber) {
			KripNumber numberValue = (KripNumber) other;
			return new RuntimeResult().success(new KripNumber(value + (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult minus(KripValue<?> other) {
		if (other instanceof KripNumber) {
			KripNumber numberValue = (KripNumber) other;
			return new RuntimeResult().success(new KripNumber(value - (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult mul(KripValue<?> other) {
		if (other instanceof KripNumber) {
			KripNumber numberValue = (KripNumber) other;
			return new RuntimeResult().success(new KripNumber(value * (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult div(KripValue<?> other) {
		if (other instanceof KripNumber) {
			KripNumber numberValue = (KripNumber) other;
			return new RuntimeResult().success(new KripNumber(value / (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult pow(KripValue<?> other) {
		if (other instanceof KripNumber) {
			KripNumber numberValue = (KripNumber) other;
			return new RuntimeResult().success(new KripNumber(Math.pow(value, numberValue.getValue()), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult mod(KripValue<?> other) {
		if (other instanceof KripNumber) {
			KripNumber numberValue = (KripNumber) other;
			return new RuntimeResult().success(new KripNumber(value % (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult lessThan(KripValue<?> other) {
		if (other instanceof KripNumber) {
			KripNumber numberValue = (KripNumber) other;
			return new RuntimeResult().success(new KripBoolean(value < (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult lessThanOrEqual(KripValue<?> other) {
		if (other instanceof KripNumber) {
			KripNumber numberValue = (KripNumber) other;
			return new RuntimeResult().success(new KripBoolean(value <= (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult greaterThan(KripValue<?> other) {
		if (other instanceof KripNumber) {
			KripNumber numberValue = (KripNumber) other;
			return new RuntimeResult().success(new KripBoolean(value > (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult greaterThanOrEqual(KripValue<?> other) {
		if (other instanceof KripNumber) {
			KripNumber numberValue = (KripNumber) other;
			return new RuntimeResult().success(new KripBoolean(value >= (double) numberValue.getValue(), context));
		} else return illegalOperation(other);
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof KripNumber) {
			KripNumber numberValue = (KripNumber) other;
			return new RuntimeResult().success(new KripBoolean(value == (double) numberValue.getValue(), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}

	@Override
	public boolean isTrue() {
		return value != 0;
	}

	@Override
	public KripValue<Double> copy() {
		return new KripNumber(value, context).setPosition(startPosition, endPosition);
	}

	@Override
	public void makePrototype() {

	}

	public boolean isWhole() {
		return Math.floor(value) == value;
	}
}
