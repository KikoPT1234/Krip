package pt.kiko.krip.lang.values;

import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.SymbolTable;
import pt.kiko.krip.lang.results.RuntimeResult;

import java.util.List;

abstract public class KripBaseFunction extends KripValue<String> {

	public List<String> argNames;

	public KripBaseFunction(String name, List<String> argNames, KripValue<?> parent, Context context) {
		super(parent, context);
		this.value = name;
		this.argNames = argNames;
	}

	public KripBaseFunction(String name, List<String> argNames, Context context) {
		super(context);
		this.value = name;
		this.argNames = argNames;
	}

	public void setThis(KripObject object) {
		Context thisContext = generateNewContext();
		thisContext.symbolTable.set("this", object, true);
		context = thisContext;
	}

	public Context generateNewContext() {
		Context newContext = new Context("<function " + (value == null ? "anonymous" : value) + ">", context, startPosition);
		newContext.symbolTable = new SymbolTable(newContext.symbolTable.parent != null ? newContext.symbolTable.parent : newContext.parent.symbolTable);
		return newContext;
	}

	public RuntimeResult populateArgs(@NotNull List<String> argNames, List<KripValue<?>> args, Context context) {
		argNames.forEach((argName) -> {
			int index = argNames.indexOf(argName);
			KripValue<?> argValue = args.size() - 1 >= index ? args.get(index) : new KripNull(context);
			context.symbolTable.set(argName, argValue, false);
		});
		return new RuntimeResult().success(new KripNull(context));
	}

	@Override
	public String getValueString() {
		return "function " + (value == null ? "anonymous" : value) + "(" + String.join(", ", argNames) + ")";
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof KripBaseFunction) {
			return new RuntimeResult().success(new KripBoolean(this == other || this.equals(other), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}

	@Override
	public RuntimeResult plus(KripValue<?> other) {
		return illegalOperation(other);
	}

	@Override
	public void makePrototype() {

	}

	abstract public RuntimeResult execute(List<KripValue<?>> args, Context ctx);
}
