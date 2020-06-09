package pt.kiko.krip.variables.functions;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterCommandFunc extends BuiltInFunctionValue {

	static {
		Krip.registerValue("registerCommand", new RegisterCommandFunc());
	}

	public RegisterCommandFunc() {
		super("registerCommand", Arrays.asList("name", "info", "function"), Krip.context);
	}

	@Override
	public synchronized RuntimeResult run(@NotNull Context context) {
		RuntimeResult result = new RuntimeResult();

		Value<?> name = context.symbolTable.get("name");
		Value<?> info = context.symbolTable.get("info");
		Value<?> function = context.symbolTable.get("function");

		if (!(name instanceof StringValue)) return invalidType(name, context);
		if (!(info instanceof ObjectValue || info instanceof NullValue)) return invalidType(info, context);
		if (!(function instanceof BaseFunctionValue || function instanceof NullValue))
			return invalidType(function, context);

		if (name.getValue().equals(""))
			return result.failure(new RuntimeError(name.startPosition, name.endPosition, "Command name must not be empty", context));

		Value<?> description = null;
		Value<?> permission = null;
		Value<?> permissionMessage = null;
		Value<?> usage = null;
		Value<?> args = null;
		Value<?> aliases = null;

		if (info instanceof ObjectValue) {
			ObjectValue infoObj = (ObjectValue) info;

			description = infoObj.get("description");
			permission = infoObj.get("permission");
			permissionMessage = infoObj.get("permissionMessage");
			usage = infoObj.get("usage");
			args = infoObj.get("args");
			aliases = infoObj.get("aliases");
		}

		if (description != null && !(description instanceof StringValue)) return invalidType(description, context);
		else if (description != null && description.getValue().equals(""))
			return result.failure(new RuntimeError(description.startPosition, description.endPosition, "Description must not be empty", context));

		if (permission != null && !(permission instanceof StringValue)) return invalidType(permission, context);
		else if (permission != null && permission.getValue().equals(""))
			return result.failure(new RuntimeError(permission.startPosition, permission.endPosition, "Permission must not be empty", context));

		if (permissionMessage != null && !(permissionMessage instanceof StringValue))
			return invalidType(permissionMessage, context);
		else if (permissionMessage != null && permissionMessage.getValue().equals(""))
			return result.failure(new RuntimeError(permissionMessage.startPosition, permissionMessage.endPosition, "Description must not be empty", context));

		if (usage != null && !(usage instanceof StringValue)) return invalidType(usage, context);
		else if (usage != null && usage.getValue().equals(""))
			return result.failure(new RuntimeError(usage.startPosition, usage.endPosition, "Description must not be empty", context));

		if (args != null && !(args instanceof ListValue)) return invalidType(args, context);

		if (aliases != null && !(aliases instanceof ListValue)) return invalidType(aliases, context);

		PluginCommand command = null;
		try {
			final Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			c.setAccessible(true);
			command = c.newInstance(name.getValue(), Krip.plugin);
			CustomCommand customCommand = new CustomCommand(function, (ListValue) args, command, context);
			if (description != null) command.setDescription(description.getValue());
			else command.setDescription("A Krip command");

			if (permission != null) command.setPermission(permission.getValue());

			if (permissionMessage != null) command.setPermissionMessage(permissionMessage.getValue());

			if (usage != null) command.setUsage(usage.getValue());
			else if (args != null) {
				StringBuilder builder = new StringBuilder();
				builder.append("/").append(command.getName().toLowerCase()).append(" ");
				Value<?>[] list = ((ListValue) args).value.toArray(new Value[]{});
				boolean isOptional = false;

				for (Value<?> arg : list) {
					if (!(arg instanceof StringValue)) return invalidType(arg, context);
					else if (arg.getValue().equals(""))
						return result.failure(new RuntimeError(arg.startPosition, arg.endPosition, "Argument name must not be empty", context));

					String value = arg.getValue();
					if (value.endsWith("?")) {
						isOptional = true;
						((StringValue) arg).setValue(value.substring(0, value.length() - 1));
						value = arg.getValue();
						customCommand.addOptionalArg(value);
						builder.append("[").append(value).append("] ");
					} else if (isOptional)
						return result.failure(new RuntimeError(arg.startPosition, arg.endPosition, "Required argument must not follow optional argument", context));
					else builder.append("<").append(value).append("> ");
				}

				command.setUsage(builder.toString());
			}

			if (aliases != null) {
				List<String> aliasStrings = new ArrayList<>();
				Value<?>[] list = ((ListValue) aliases).value.toArray(new Value[]{});

				for (Value<?> alias : list) {
					if (!(alias instanceof StringValue)) return invalidType(alias, context);
					else if (alias.getValue().equals(""))
						return result.failure(new RuntimeError(alias.startPosition, alias.endPosition, "Alias must not be empty", context));
					aliasStrings.add(alias.getValue());
				}
				command.setAliases(aliasStrings);
			}

			command.setLabel(name.getValue());
			command.setExecutor(customCommand);
			command.setTabCompleter(customCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assert command != null;

		Krip.commandNames.put(name.getValue().toLowerCase(), name.startPosition.fileName);
		Command existingCmd = Krip.commandMap.getCommand(name.getValue().toLowerCase());
		if (existingCmd != null) {
			Krip.knownCommands.remove(existingCmd.getName());
			Krip.knownCommands.remove("krip:" + existingCmd.getName());
			existingCmd.unregister(Krip.commandMap);
		}

		HelpMap help = Bukkit.getHelpMap();
		HelpTopic t = new GenericCommandHelpTopic(command);
		help.addTopic(t);

		Krip.commandMap.register("krip", command);

		try {
			Krip.syncCommandsMethod.invoke(Bukkit.getServer());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return result.success(new NullValue(context.parent));
	}
}
