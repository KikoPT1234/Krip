package pt.kiko.krip.lang.values;

import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.SymbolTable;
import pt.kiko.krip.lang.results.RuntimeResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

abstract public class BaseFunctionValue extends Value {

	public String name;
	public List<String> argNames;

	public BaseFunctionValue(String name, List<String> argNames, Context context) {
		super(context);
		this.name = name;
		this.argNames = argNames;
	}

	public void setThis(ObjectValue object) {
		Context thisContext = generateNewContext();
		thisContext.symbolTable.set("this", object, true);
		context = thisContext;
	}

	public Context generateNewContext() {
		Context newContext = new Context("<function " + name + ">", context, startPosition);
		newContext.symbolTable = new SymbolTable(newContext.symbolTable.parent != null ? newContext.symbolTable.parent : newContext.parent.symbolTable);
		return newContext;
	}

	public RuntimeResult populateArgs(@NotNull List<String> argNames, List<Value> args, Context context) {
		argNames.forEach((argName) -> {
			int index = argNames.indexOf(argName);
			Value argValue = args.size() - 1 >= index ? args.get(index) : new NullValue(context);
			context.symbolTable.set(argName, argValue, false);
		});
		return new RuntimeResult().success(new NullValue(context));
	}

	@Override
	public String getValue() {
		return "<function " + name + ">";
	}

	@Override
	public RuntimeResult equal(Value other) {
		if (other instanceof BaseFunctionValue) return new RuntimeResult().success(new BooleanValue(name.equals(other.getValue()), context));
		else return new RuntimeResult().success(new BooleanValue(false, context));
	}

	@Override
	public RuntimeResult notEquals(Value other) {
		if (other instanceof BaseFunctionValue) return new RuntimeResult().success(new BooleanValue(!name.equals(other.getValue()), context));
		else return new RuntimeResult().success(new BooleanValue(true, context));
	}

	abstract public RuntimeResult execute(List<Value> args);
}
