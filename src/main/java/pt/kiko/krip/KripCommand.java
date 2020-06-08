package pt.kiko.krip;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.kiko.krip.lang.results.RunResult;
import pt.kiko.krip.lang.values.BaseFunctionValue;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class KripCommand implements TabExecutor {

	public KripCommand() {
		PluginCommand command = Objects.requireNonNull(Krip.plugin.getServer().getPluginCommand("krip"));
		command.setAliases(Collections.singletonList("kp"));
		command.setExecutor(this);
		command.setTabCompleter(this);
	}

	@Override
	public synchronized boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
		if (args.length < 1) {
			return true;
		}
		if (args[0].equals("reload") || args[0].equals("rl")) {
			if (args.length < 2) {
				commandSender.sendMessage(ChatColor.RED + "Usage: /krip reload (file | 'all')");
				return true;
			}
			String fileName = args[1];
			if (fileName.equals("all")) {
				reset();
				File[] files = Krip.plugin.scriptFolder.listFiles();
				List<RunResult> results = new ArrayList<>();
				Runnable task = () -> {
					try {
						for (File file : files) {
							results.add(Krip.run(Krip.plugin.loadFile(file), file.getName()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
				Bukkit.getScheduler().scheduleSyncDelayedTask(Krip.plugin, task);
				AtomicInteger nOfErrors = new AtomicInteger();
				for (RunResult result : results.toArray(new RunResult[]{})) {
					if (result.error != null) {
						nOfErrors.getAndIncrement();
						break;
					}
				}
				if (nOfErrors.get() == 0)
					commandSender.sendMessage(ChatColor.GREEN + "Successfully loaded " + files.length + (files.length == 1 ? "script!" : " scripts!"));
				else
					commandSender.sendMessage(ChatColor.RED + "There were " + nOfErrors.get() + " errors while loading " + files.length + " files");
				return true;
			} else {
				if (fileName.split("\\.").length == 1) fileName += ".kp";
				File file = new File(Krip.plugin.scriptFolder, fileName);
				if (!file.exists()) {
					commandSender.sendMessage(ChatColor.RED + "File not found: " + file.getName());
					return true;
				}
				reset(file);
				Runnable task = () -> {
					try {
						RunResult result = Krip.run(Krip.plugin.loadFile(file), file.getName());
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
		}
		return false;
	}

	private void reset() {
		Krip.events.forEach((name, info) -> info.functions.clear());
		Krip.events.clear();
		Krip.commandNames.forEach((name, fName) -> {
			Command plCmd = Krip.commandMap.getCommand(name.toLowerCase());
			assert plCmd != null;
			plCmd.getAliases().forEach(alias -> {
				Krip.knownCommands.remove(alias);
				Krip.knownCommands.remove("krip:" + alias.toLowerCase());
			});
			plCmd.unregister(Krip.commandMap);
			Krip.knownCommands.remove(name.toLowerCase());
			Krip.knownCommands.remove("krip:" + name.toLowerCase());
		});
		Krip.commandNames.clear();
		List<String> namesToRemove = new ArrayList<>();
		Krip.context.symbolTable.symbols.forEach((key, value) -> {
			if (!Krip.registeredNames.contains(key)) namesToRemove.add(key);
		});
		namesToRemove.forEach(name -> Krip.context.symbolTable.remove(name));
		Krip.commandNames.clear();
		Krip.tasks.forEach((fileName, tasks) -> tasks.forEach(BukkitTask::cancel));
		Krip.tasks.clear();
	}

	private void reset(File file) {
		Krip.events.forEach((name, event) -> {
			List<BaseFunctionValue> functionsToRemove = new ArrayList<>();
			event.functions.forEach(function -> {
				if (function.startPosition.fileName.equals(file.getName())) functionsToRemove.add(function);
			});
			functionsToRemove.forEach(function -> event.functions.remove(function));
		});
		List<String> namesToRemove = new ArrayList<>();
		Krip.context.symbolTable.symbols.forEach((key, value) -> {
			if (!Krip.registeredNames.contains(key) && value.startPosition.fileName.equals(file.getName()))
				namesToRemove.add(key);
		});
		namesToRemove.forEach(name -> Krip.context.symbolTable.remove(name));
		List<String> commandsToRemove = new ArrayList<>();
		Krip.commandNames.forEach((name, fName) -> {
			if (fName.equals(file.getName())) commandsToRemove.add(name.toLowerCase());
		});
		commandsToRemove.forEach(name -> {
			Command plCmd = Krip.commandMap.getCommand(name.toLowerCase());
			assert plCmd != null;
			plCmd.getAliases().forEach(alias -> {
				Krip.knownCommands.remove(alias);
				Krip.knownCommands.remove("krip:" + alias);
			});
			plCmd.unregister(Krip.commandMap);
			Krip.knownCommands.remove(name.toLowerCase());
			Krip.knownCommands.remove("krip:" + name.toLowerCase());
			Krip.commandNames.remove(name.toLowerCase());
		});
		AtomicReference<List<BukkitTask>> tasksToRemove = new AtomicReference<>();
		Krip.tasks.forEach((fileName, tasks) -> {
			if (fileName.equals(file.getName())) tasksToRemove.set(tasks);
		});
		if (tasksToRemove.get() != null && tasksToRemove.get().size() > 0)
			tasksToRemove.get().forEach(BukkitTask::cancel);
		Krip.tasks.remove(file.getName());
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length == 1) return Arrays.asList("reload", "rl");
		else if (args.length == 2 && (args[0].equals("reload") || args[0].equals("rl"))) {
			List<String> names = Arrays.stream(Krip.plugin.scriptFolder.listFiles()).map(File::getName).collect(Collectors.toList());
			names.add("all");
			return names;
		}
		return Collections.emptyList();
	}
}
