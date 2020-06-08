package pt.kiko.krip.variables.functions;

import org.bukkit.ChatColor;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.BuiltInFunctionValue;
import pt.kiko.krip.lang.values.StringValue;
import pt.kiko.krip.lang.values.Value;

import java.util.Collections;

public class FormatFunc extends BuiltInFunctionValue {

	static {
		Krip.registerValue("format", new FormatFunc());
	}

	public FormatFunc() {
		super("format", Collections.singletonList("message"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		Value<?> message = context.symbolTable.get("message");

		if (!(message instanceof StringValue)) return invalidType(message, context);

		return result.success(new StringValue(ChatColor.translateAlternateColorCodes('&', message.getValue()), context.parent));
	}
}
