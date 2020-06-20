package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.Interpreter;
import pt.kiko.krip.lang.nodes.Node;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.util.List;

public class KripFunction extends KripBaseFunction {

	public Node body;
	public boolean shouldAutoReturn;

	public KripFunction(String name, Node body, List<String> argNames, boolean shouldAutoReturn, Context context) {
		super(name, argNames, context);
		this.body = body;
		this.shouldAutoReturn = shouldAutoReturn;
	}

	@Override
	public RuntimeResult execute(List<KripValue<?>> args, Context ctx) {
		RuntimeResult result = new RuntimeResult();
		Context context = generateNewContext();
		context.parent = ctx;

		result.register(populateArgs(argNames, args, context));
		if (result.shouldReturn()) return result;

		KripValue<?> value = result.register(Interpreter.visit(body, context));
		if (result.shouldReturn() && result.funcReturnValue == null) return result;

		KripValue<?> returnValue = shouldAutoReturn ? value : result.funcReturnValue;
		if (returnValue == null) returnValue = new KripNull(this.context);
		return result.success(returnValue);
	}

	@Override
	public KripValue<String> copy() {
		return new KripFunction(value, body, argNames, shouldAutoReturn, context).setPosition(startPosition, endPosition);
	}
}
