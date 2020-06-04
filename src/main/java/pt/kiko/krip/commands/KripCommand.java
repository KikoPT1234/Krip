package pt.kiko.krip.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pt.kiko.krip.Krip;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KripCommand implements CommandExecutor {

	public KripCommand() {
		Objects.requireNonNull(Krip.plugin.getServer().getPluginCommand("krip")).setExecutor(this);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
		if (args.length < 1) {
			return true;
		}
		if (args[0].equals("reload")) {
			File file = new File(Krip.plugin.scriptFolder, args[1]);
			if (!file.exists()) {
				commandSender.sendMessage(ChatColor.RED + "File not found");
				return true;
			}
			Krip.events.forEach((name, event) -> event.functions.forEach(function -> {
				if (function.startPosition.fileName.equals(args[1])) event.functions.remove(function);
			}));
			List<String> namesToRemove = new ArrayList<>();
			Krip.context.symbolTable.symbols.forEach((key, value) -> {
				if (!Krip.registeredNames.contains(key)) namesToRemove.add(key);
			});
			namesToRemove.forEach(name -> Krip.context.symbolTable.remove(name));
			new Thread(() -> Krip.run(Krip.plugin.loadFile(file), file.getName())).start();
		}
		return false;
	}
}
