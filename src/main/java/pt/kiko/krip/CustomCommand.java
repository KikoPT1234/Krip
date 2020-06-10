package pt.kiko.krip;

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
import pt.kiko.krip.objects.ConsoleCommandSenderObj;
import pt.kiko.krip.objects.OnlinePlayerObj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CustomCommand implements TabExecutor {

	private final Value<?> function;
	private final ListValue args;
	private final Context context;
	private final List<String> optionalArgs = new ArrayList<>();
	public Command command;

	public CustomCommand(Value<?> function, ListValue args, Command command, Context context) {
		this.function = function;
		if (args != null) this.args = args;
		else this.args = new ListValue(new ArrayList<>(), context);
		this.command = command;
		this.context = context;
	}

	public void addOptionalArg(String argName) {
		optionalArgs.add(argName);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(function instanceof BaseFunctionValue)) return true;

		List<Value<?>> requiredArgs = this.args.value.stream().filter(arg -> !optionalArgs.contains(arg.getValueString())).collect(Collectors.toList());
		if (args.length < requiredArgs.size()) return false;
		List<Value<?>> argValues = Arrays.stream(args).map(arg -> new StringValue(arg, context)).collect(Collectors.toList());

		ObjectValue sender = commandSender instanceof Player ? new OnlinePlayerObj((Player) commandSender, context) : new ConsoleCommandSenderObj((ConsoleCommandSender) commandSender, context);
		sender.set("isPlayer", new BooleanValue(commandSender instanceof Player, context));

		RuntimeResult result = ((BaseFunctionValue) function).execute(Arrays.asList(sender, new ListValue(argValues, context)), context);
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
