package pt.kiko.krip.objects;

import org.bukkit.command.ConsoleCommandSender;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.util.Collections;
import java.util.HashMap;

public class ConsoleCommandSenderObj extends KripObject {

	public ConsoleCommandSender sender;

	public ConsoleCommandSenderObj(ConsoleCommandSender sender, Context context) {
		super(new HashMap<>(), context);

		this.sender = sender;

		value.put("send", new KripJavaFunction("send", Collections.singletonList("message"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> message = context.symbolTable.get("message");

				if (!(message instanceof KripString)) return invalidType(message, context);

				sender.sendMessage(message.getValueString());
				return result.success(new KripNull(context));
			}
		});
		value.put("hasPermission", new KripJavaFunction("hasPermission", Collections.singletonList("message"), context) {
			@Override
			public RuntimeResult run(Context context) {
				RuntimeResult result = new RuntimeResult();
				KripValue<?> message = context.symbolTable.get("message");

				if (!(message instanceof KripString)) return invalidType(message, context);

				return result.success(new KripBoolean(sender.hasPermission(message.getValueString()), context));
			}
		});
	}

	@Override
	public RuntimeResult equal(KripValue<?> other) {
		if (other instanceof ConsoleCommandSenderObj) {
			return new RuntimeResult().success(new KripBoolean(sender == ((ConsoleCommandSenderObj) other).sender || sender.equals(((ConsoleCommandSenderObj) other).sender), context));
		} else return new RuntimeResult().success(new KripBoolean(false, context));
	}
}
