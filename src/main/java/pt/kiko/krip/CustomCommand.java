package pt.kiko.krip;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pt.kiko.krip.lang.Context;
import pt.kiko.krip.lang.results.RuntimeResult;
import pt.kiko.krip.lang.values.*;
import pt.kiko.krip.objects.player.ConsoleCommandSenderObj;
import pt.kiko.krip.objects.player.OnlinePlayerObj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CustomCommand implements TabExecutor {

	private final KripValue<?> function;
	private final KripList args;
	private final Context context;
	private final List<String> optionalArgs = new ArrayList<>();
	private final String executor;
	private String executorMessage;
	public Command command;

	public CustomCommand(KripValue<?> function, KripList args, Command command, Context context, @Nullable String executor, @Nullable String executorMessage) {
		this.function = function;
		if (args != null) this.args = args;
		else this.args = new KripList(new ArrayList<>(), context);
		this.command = command;
		this.context = context;
		this.executor = executor;
		if (executor != null)
			this.executorMessage = executorMessage == null ? ChatColor.RED + "Only " + (executor.equals("console") ? "the console" : "a player") + " can execute this command!" : executorMessage;
	}

	public void addOptionalArg(String argName) {
		optionalArgs.add(argName);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(function instanceof KripBaseFunction)) return true;

		if (executor != null && ((commandSender instanceof ConsoleCommandSender && !executor.equals("console")) || (commandSender instanceof Player && !executor.equals("player")))) {
			commandSender.sendMessage(executorMessage);
			return true;
		}

		List<KripValue<?>> requiredArgs = this.args.value.stream().filter(arg -> !optionalArgs.contains(arg.getValueString())).collect(Collectors.toList());
		if (args.length < requiredArgs.size()) return false;
		List<KripValue<?>> argValues = Arrays.stream(args).map(arg -> new KripString(arg, context)).collect(Collectors.toList());

		KripObject sender = commandSender instanceof Player ? new OnlinePlayerObj((Player) commandSender, context) : new ConsoleCommandSenderObj((ConsoleCommandSender) commandSender, context);
		sender.set("isPlayer", new KripBoolean(commandSender instanceof Player, context));

		RuntimeResult result = ((KripBaseFunction) function).execute(Arrays.asList(sender, new KripList(argValues, context)), context);
		if (result.error != null) {
			System.out.println(result.error.toString());
		}
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
		return Collections.emptyList();
	}
}
