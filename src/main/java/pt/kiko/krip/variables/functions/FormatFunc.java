package pt.kiko.krip.variables.functions;

import org.bukkit.ChatColor;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.KripJavaFunction;
import pt.kiko.krip.lang.values.KripString;
import pt.kiko.krip.lang.values.KripValue;

import java.util.Collections;

public class FormatFunc extends KripJavaFunction {

	static {
		Krip.registerValue("format", new FormatFunc());
	}

	public FormatFunc() {
		super("format", Collections.singletonList("message"), Krip.context);
	}

	@Override
	public RuntimeResult run(Context context) {
		RuntimeResult result = new RuntimeResult();
		KripValue<?> message = context.symbolTable.get("message");

		if (!(message instanceof KripString)) return invalidType(message, context);

		return result.success(new KripString(ChatColor.translateAlternateColorCodes('&', message.getValueString()), context));
	}
}
