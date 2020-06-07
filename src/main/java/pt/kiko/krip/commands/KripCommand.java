package pt.kiko.krip.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.Krip;
import pt.kiko.krip.lang.results.RunResult;
import pt.kiko.krip.lang.values.BaseFunctionValue;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class KripCommand implements CommandExecutor {

	private static Method syncCommandsMethod;

	static {
		try {
			Class<?> craftServer;
			String revision = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
			craftServer = Class.forName("org.bukkit.craftbukkit." + revision + ".CraftServer");

			syncCommandsMethod = craftServer.getDeclaredMethod("syncCommands");
			syncCommandsMethod.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	BlockingQueue<Runnable> loadQueue = new ArrayBlockingQueue<>(20, true);

	public KripCommand() {
		Objects.requireNonNull(Krip.plugin.getServer().getPluginCommand("krip")).setExecutor(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
		if (args.length < 1) {
			return true;
		}
		if (args[0].equals("reload")) {
			if (args.length < 2) {
				commandSender.sendMessage(ChatColor.RED + "Usage: /krip reload (file)");
				return true;
			}
			File file = new File(Krip.plugin.scriptFolder, args[1]);
			if (!file.exists()) {
				commandSender.sendMessage(ChatColor.RED + "File not found: " + args[1]);
				return true;
			}
			Krip.events.forEach((name, event) -> {
				List<BaseFunctionValue> functionsToRemove = new ArrayList<>();
				event.functions.forEach(function -> {
					if (function.startPosition.fileName.equals(args[1])) functionsToRemove.add(function);
				});
				functionsToRemove.forEach(function -> event.functions.remove(function));
			});
			List<String> namesToRemove = new ArrayList<>();
			Krip.context.symbolTable.symbols.forEach((key, value) -> {
				if (!Krip.registeredNames.contains(key)) namesToRemove.add(key);
			});
			namesToRemove.forEach(name -> Krip.context.symbolTable.remove(name));
			List<String> commandsToRemove = new ArrayList<>();
			Krip.commandNames.forEach((name, fileName) -> {
				if (fileName.equals(args[1])) commandsToRemove.add(name);
			});
			try {
				final Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
				commandMapField.setAccessible(true);
				final Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
				knownCommandsField.setAccessible(true);
				final SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getPluginManager());
				final Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);

				commandsToRemove.forEach(name -> {
					Command plCmd = commandMap.getCommand(name);
					if (plCmd != null) plCmd.unregister(commandMap);
					knownCommands.remove(name);
					knownCommands.remove("krip:" + name);
					Krip.commandNames.remove(name);
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
			Runnable task = () -> {
				try {
					RunResult result = Krip.run(Krip.plugin.loadFile(file), file.getName());
					syncCommandsMethod.invoke(Bukkit.getServer());
					if (result.error != null) {
						commandSender.sendMessage(ChatColor.RED + "Error while loading " + file.getName() + ": " + ChatColor.DARK_RED + result.error.details);
						commandSender.sendMessage(ChatColor.RED + "Check the logs for more info");
					} else commandSender.sendMessage(ChatColor.GREEN + file.getName() + " loaded successfully!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
			Bukkit.getScheduler().scheduleSyncDelayedTask(Krip.plugin, task);
		}
		return false;
	}
}
