package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.Interpreter;
import pt.kiko.krip.lang.nodes.Node;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.util.List;

public class FunctionValue extends BaseFunctionValue {

	public Node body;
	public boolean shouldAutoReturn;

	public FunctionValue(String name, Node body, List<String> argNames, boolean shouldAutoReturn, Context context) {
		super(name, argNames, context);
		this.body = body;
		this.shouldAutoReturn = shouldAutoReturn;
	}

	@Override
	public RuntimeResult execute(List<Value> args, Context ctx) {
		RuntimeResult result = new RuntimeResult();
		Context context = generateNewContext();
		context.parent = ctx;

		result.register(populateArgs(argNames, args, context));
		if (result.shouldReturn()) return result;

		Value value = result.register(Interpreter.visit(body, context));
		if (result.shouldReturn() && result.funcReturnValue == null) return result;

		Value returnValue = shouldAutoReturn ? value : result.funcReturnValue;
		if (returnValue == null) returnValue = new NullValue(this.context);
		return result.success(returnValue);
	}

	@Override
	public Value copy() {
		return new FunctionValue(name, body, argNames, shouldAutoReturn, context).setPosition(startPosition, endPosition);
	}

	@Override
	public RuntimeResult plus(Value other) {
		if (other instanceof StringValue) return new RuntimeResult().success(new StringValue(getValue() + other.getValue(), context));
		else return illegalOperation(other);
	}
}
