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

public class RegisterCommandFunc extends KripJavaFunction {

	static {
		Krip.registerValue("registerCommand", new RegisterCommandFunc());
	}

	public RegisterCommandFunc() {
		super("registerCommand", Arrays.asList("name", "info", "function"), Krip.context);
	}

	@Override
	public synchronized RuntimeResult run(@NotNull Context context) {
		RuntimeResult result = new RuntimeResult();

		KripValue<?> name = context.symbolTable.get("name");
		KripValue<?> info = context.symbolTable.get("info");
		KripValue<?> function = context.symbolTable.get("function");

		if (!(name instanceof KripString)) return invalidType(name, context);
		if (!(info instanceof KripObject || info instanceof KripNull)) return invalidType(info, context);
		if (!(function instanceof KripBaseFunction || function instanceof KripNull))
			return invalidType(function, context);

		if (name.getValueString().equals(""))
			return result.failure(new RuntimeError(name.startPosition, name.endPosition, "Command name must not be empty", context));

		KripValue<?> description = null;
		KripValue<?> permission = null;
		KripValue<?> permissionMessage = null;
		KripValue<?> usage = null;
		KripValue<?> args = null;
		KripValue<?> aliases = null;

		if (info instanceof KripObject) {
			KripObject infoObj = (KripObject) info;

			description = infoObj.get("description");
			permission = infoObj.get("permission");
			permissionMessage = infoObj.get("permissionMessage");
			usage = infoObj.get("usage");
			args = infoObj.get("args");
			aliases = infoObj.get("aliases");
		}

		if (description != null && !(description instanceof KripString)) return invalidType(description, context);
		else if (description != null && description.getValueString().equals(""))
			return result.failure(new RuntimeError(description.startPosition, description.endPosition, "Description must not be empty", context));

		if (permission != null && !(permission instanceof KripString)) return invalidType(permission, context);
		else if (permission != null && permission.getValueString().equals(""))
			return result.failure(new RuntimeError(permission.startPosition, permission.endPosition, "Permission must not be empty", context));

		if (permissionMessage != null && !(permissionMessage instanceof KripString))
			return invalidType(permissionMessage, context);
		else if (permissionMessage != null && permissionMessage.getValueString().equals(""))
			return result.failure(new RuntimeError(permissionMessage.startPosition, permissionMessage.endPosition, "Description must not be empty", context));

		if (usage != null && !(usage instanceof KripString)) return invalidType(usage, context);
		else if (usage != null && usage.getValueString().equals(""))
			return result.failure(new RuntimeError(usage.startPosition, usage.endPosition, "Description must not be empty", context));

		if (args != null && !(args instanceof KripList)) return invalidType(args, context);

		if (aliases != null && !(aliases instanceof KripList)) return invalidType(aliases, context);

		PluginCommand command = null;
		try {
			final Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			c.setAccessible(true);
			command = c.newInstance(name.getValueString(), Krip.plugin);
			CustomCommand customCommand = new CustomCommand(function, (KripList) args, command, context);
			if (description != null) command.setDescription(description.getValueString());
			else command.setDescription("A Krip command");

			if (permission != null) command.setPermission(permission.getValueString());

			if (permissionMessage != null) command.setPermissionMessage(permissionMessage.getValueString());

			if (usage != null) command.setUsage(usage.getValueString());
			else if (args != null) {
				StringBuilder builder = new StringBuilder();
				builder.append("/").append(command.getName().toLowerCase()).append(" ");
				KripValue<?>[] list = ((KripList) args).value.toArray(new KripValue[]{});
				boolean isOptional = false;

				for (KripValue<?> arg : list) {
					if (!(arg instanceof KripString)) return invalidType(arg, context);
					else if (arg.getValueString().equals(""))
						return result.failure(new RuntimeError(arg.startPosition, arg.endPosition, "Argument name must not be empty", context));

					String value = arg.getValueString();
					if (value.endsWith("?")) {
						isOptional = true;
						((KripString) arg).setValue(value.substring(0, value.length() - 1));
						value = arg.getValueString();
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
				KripValue<?>[] list = ((KripList) aliases).value.toArray(new KripValue[]{});

				for (KripValue<?> alias : list) {
					if (!(alias instanceof KripString)) return invalidType(alias, context);
					else if (alias.getValueString().equals(""))
						return result.failure(new RuntimeError(alias.startPosition, alias.endPosition, "Alias must not be empty", context));
					aliasStrings.add(alias.getValueString());
				}
				command.setAliases(aliasStrings);
			}

			command.setLabel(name.getValueString());
			command.setExecutor(customCommand);
			command.setTabCompleter(customCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assert command != null;

		Krip.commandNames.put(name.getValueString().toLowerCase(), name.startPosition.fileName);
		Command existingCmd = Krip.commandMap.getCommand(name.getValueString().toLowerCase());
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

		return result.success(new KripNull(context));
	}
}
