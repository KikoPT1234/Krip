package pt.kiko.krip.variables.functions;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.CustomCommand;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.errors.RuntimeError;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class RegisterCommandFunc extends BuiltInFunctionValue {

	static {
		Krip.registerValue("registerCommand", new RegisterCommandFunc());
	}

	public RegisterCommandFunc() {
		super("registerCommand", Arrays.asList("name", "function"), Krip.context);
	}

	@Override
	public synchronized RuntimeResult run(@NotNull Context context) {
		RuntimeResult result = new RuntimeResult();

		Value name = context.symbolTable.get("name");
		Value function = context.symbolTable.get("function");

		if (!(name instanceof StringValue))
			return result.failure(new RuntimeError(name.startPosition, name.endPosition, "Invalid type", context));
		if (!(function instanceof BaseFunctionValue))
			return result.failure(new RuntimeError(function.startPosition, function.endPosition, "Invalid type", context));

		PluginCommand command = null;
		try {
			final Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			c.setAccessible(true);
			command = c.newInstance(name.getValue(), Krip.plugin);
			CustomCommand customCommand = new CustomCommand((BaseFunctionValue) function, command, context);
			command.setDescription("test");
			command.setPermission("test");
			command.setUsage("test");
			command.setLabel(name.getValue());
			command.setExecutor(customCommand);
			command.setTabCompleter(customCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assert command != null;

		Krip.commandNames.put(name.getValue(), function.startPosition.fileName);
		Krip.commandMap.register("krip", command);

		HelpMap help = Bukkit.getHelpMap();
		HelpTopic t = new GenericCommandHelpTopic(command);
		help.addTopic(t);


		return result.success(new NullValue(context.parent));
	}
}
